package com.example.tvdapp.product;

import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.tvdapp.order.ProductOrderViewEntity;
import com.example.tvdapp.order.model.ProductResponse;
import com.example.tvdapp.utilities.DispatchGroup;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
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
import java.util.stream.IntStream;

public class ProductModel {
    private List<ProductResponse> productResponses = new ArrayList<>();
    private DatabaseReference mDatabase;
    private ProductModelEvent event;
    private ChildEventListener childEventListener;

    public void setEvent(ProductModelEvent event) {
        this.event = event;
    }

    interface ProductModelEvent {
        void productAdded(ProductViewEntity productViewEntity);

        void productChanged(ProductViewEntity productViewEntity);

        void productRemoved(String id);

        void fetchProductImageSuccess(String id, String imageLink);

        void deleteProductsSuccess();
    }

    public ProductModel() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void fetchData() {
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ProductResponse productResponse = snapshot.getValue(ProductResponse.class);
                productResponses.add(productResponse);
                ProductViewEntity productViewEntity = new ProductViewEntity(
                        productResponse.id,
                        productResponse.name,
                        productResponse.imageURL,
                        productResponse.amount,
                        String.format("%,d", productResponse.price));
                if (event != null) {
                    event.productAdded(productViewEntity);
                }

                fetchProductImage(productResponse);
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ProductResponse productResponse = snapshot.getValue(ProductResponse.class);
                int index = IntStream.range(0, productResponses.size())
                        .filter(i -> productResponses.get(i).id.compareTo(productResponse.id) == 0)
                        .findAny()
                        .orElse(-1);
                if (index != -1) {
                    productResponses.set(index, productResponse);
                }

                ProductViewEntity productViewEntity = new ProductViewEntity(
                        productResponse.id,
                        productResponse.name,
                        productResponse.imageURL,
                        productResponse.amount,
                        String.format("%,d", productResponse.price));
                if (event != null) {
                    event.productChanged(productViewEntity);
                }

                if (isProductImageChanged(productResponse)) {
                    fetchProductImage(productResponse);
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                ProductResponse productResponse = snapshot.getValue(ProductResponse.class);
                int index = IntStream.range(0, productResponses.size())
                        .filter(i -> productResponses.get(i).id.compareTo(productResponse.id) == 0)
                        .findAny()
                        .orElse(-1);
                if (index != -1) {
                    productResponses.remove(index);
                    if (event != null) {
                        event.productRemoved(productResponse.id);
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabase.child("products").addChildEventListener(childEventListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean isProductImageChanged(ProductResponse response) {
        ProductResponse oldResponse = productResponses.stream()
                .filter(product -> product.id.compareTo(response.id) == 0)
                .findAny()
                .orElse(null);
        if (oldResponse != null) {
            return oldResponse.imageURL.compareTo(response.imageURL) != 0;
        } else {
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteProducts(List<ProductViewEntity> selectedProducts) {
        DispatchGroup dispatchGroup = new DispatchGroup();
        for (int i = 0; i < selectedProducts.size(); i++) {
            dispatchGroup.enter();
            mDatabase.child(String.format("products/%s", selectedProducts.get(i).id)).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    dispatchGroup.leave();
                }
            });
        }

        dispatchGroup.notify(new Runnable() {
            @Override
            public void run() {
                if (event != null) {
                    event.deleteProductsSuccess();
                }
            }
        });
    }

    private void fetchProductImage(ProductResponse response) {
        StorageReference sReference = FirebaseStorage.getInstance().getReference();
        StorageReference storageReference = sReference.child(response.imageURL);
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageLink = uri.toString();
                if (event != null) {
                    event.fetchProductImageSuccess(response.id, imageLink);
                }
            }
        });
    }
}
