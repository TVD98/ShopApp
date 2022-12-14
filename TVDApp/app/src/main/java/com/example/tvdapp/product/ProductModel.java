package com.example.tvdapp.product;

import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.tvdapp.order.ProductOrderViewEntity;
import com.example.tvdapp.order.model.ProductResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductModel {
    private List<ProductResponse> productResponses = new ArrayList<>();
    private List<ProductViewEntity> productViewEntities = new ArrayList<>();
    private DatabaseReference mDatabase;
    private ProductModelEvent event;

    public void setEvent(ProductModelEvent event) {
        this.event = event;
    }

    interface ProductModelEvent {
        void fetchDataSuccess(List<ProductViewEntity> productViewEntityList);
        void fetchProductImageSuccess(int position);
    }

    public ProductModel() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void fetchData() {
        ValueEventListener postListener = new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productResponses.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ProductResponse productResponse = snapshot.getValue(ProductResponse.class);
                    productResponses.add(productResponse);
                }

                convertResponse();

                if (event != null) {
                    event.fetchDataSuccess(productViewEntities);
                }

                fetchProductImages();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.child("products").addValueEventListener(postListener);
    }

    private void fetchProductImages() {
        StorageReference sReference = FirebaseStorage.getInstance().getReference();
        for (int i = 0; i < productResponses.size(); i++) {
            ProductResponse response = productResponses.get(i);
            StorageReference storageReference = sReference.child(response.imageURL);
            int finalI = i;
            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    productViewEntities.get(finalI).imageLink = uri.toString();
                    if (event != null) {
                        event.fetchProductImageSuccess(finalI);
                    }
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void convertResponse() {
        productViewEntities = productResponses.stream()
                .map(product -> new ProductViewEntity(
                        product.id,
                        product.name,
                        product.imageURL,
                        0,
                        String.format("%,d", product.price)))
                .collect(Collectors.toList());
    }
}
