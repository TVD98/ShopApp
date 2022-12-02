package com.example.tvdapp.order;

import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.tvdapp.order.model.ProductResponse;
import com.example.tvdapp.order.model.ProductResponseList;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OrderProductModel {
    private List<ProductOrderViewEntity> productOrderViewEntityList = new ArrayList<>();
    private List<ProductOrderViewEntity> cart = new ArrayList<>();
    private List<ProductResponse> productResponses = new ArrayList<>();
    private ProductResponseList response;
    private OrderProductModelEvent event;
    private DatabaseReference mDatabase;

    public void setEvent(OrderProductModelEvent event) {
        this.event = event;
    }

    interface OrderProductModelEvent {
        void fetchProductListSuccess(List<ProductOrderViewEntity> entityList);

        void cartDidChange(int productCount, String total);

        void fetchProductImageSuccess(int position);
    }

    public OrderProductModel() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void fetchData() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ProductResponse productResponse = snapshot.getValue(ProductResponse.class);
                    productResponses.add(productResponse);
                }

                productOrderViewEntityList = productResponses.stream()
                        .map(product -> new ProductOrderViewEntity(product.name, String.format("%,d", product.price), 0))
                        .collect(Collectors.toList());

                if (event != null) {
                    event.fetchProductListSuccess(productOrderViewEntityList);
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
                    productOrderViewEntityList.get(finalI).uri = uri;
                    if (event != null) {
                        event.fetchProductImageSuccess(finalI);
                    }
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void changeProductCurrentCount(int position, int currentCount) {
        ProductOrderViewEntity entity = productOrderViewEntityList.get(position);
        int lastCount = entity.count;
        entity.count = currentCount;
        if (lastCount == 0 && currentCount > 0) {
            cartDidChange(entity, currentCount, true);
        } else {
            cartDidChange(entity, currentCount, false);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private ProductResponse getProductInfoByName(String name) {
        ProductResponse productInfo = productResponses.stream()
                .filter(product -> product.name.compareTo(name) == 0)
                .findAny()
                .orElse(null);
        return productInfo;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private String getProductTotalMoney() {
        int totalMoney = cart.stream()
                .reduce(0, (total, product) -> total + (getProductInfoByName(product.name).price * product.count), Integer::sum);
        return String.format("%,d", totalMoney);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private int getProductCount() {
        int productCount = cart.stream()
                .reduce(0, (total, product) -> total + product.count, Integer::sum);
        return productCount;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void cartDidChange(ProductOrderViewEntity entity, int currentCount, boolean isAdd) {
        if (currentCount == 0) {
            removeProductFromCart(entity);
        } else if (isAdd) {
            addProductToCart(entity);
        }

        if (event != null) {
            event.cartDidChange(getProductCount(), getProductTotalMoney());
        }
    }

    private void removeProductFromCart(ProductOrderViewEntity entity) {
        cart.remove(entity);
    }

    private void addProductToCart(ProductOrderViewEntity entity) {
        cart.add(entity);
    }
}
