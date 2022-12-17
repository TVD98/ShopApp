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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderProductModel {
    private List<ProductOrderViewEntity> productOrderViewEntityList = new ArrayList<>();
    private List<ProductOrderViewEntity> cart = new ArrayList<>();
    private List<ProductResponse> productResponses = new ArrayList<>();
    private OrderProductModelEvent event;
    private DatabaseReference mDatabase;

    public OrderActivityType type = OrderActivityType.order;
    public String confirmOrderInfoJson = "";

    public List<ProductOrderViewEntity> getProductOrderViewEntityList() {
        return productOrderViewEntityList;
    }

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

                if (type == OrderActivityType.order) {
                    productOrderViewEntityList = productResponses.stream()
                            .map(product -> new ProductOrderViewEntity(
                                    product.id,
                                    product.name,
                                    product.price,
                                    0,
                                    product.amount))
                            .collect(Collectors.toList());
                } else {
                    productOrderViewEntityList = productResponses.stream()
                            .map(product -> new ProductOrderViewEntity(
                                    product.id,
                                    product.name,
                                    product.costPrice,
                                    0,
                                    product.amount))
                            .collect(Collectors.toList());
                }

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
                    productOrderViewEntityList.get(finalI).imageLink = uri.toString();
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
    private String getProductTotalMoney() {
        int totalMoney = cart.stream()
                .reduce(0, (total, product) -> total + (product.price * product.count), Integer::sum);
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

    public String getCartJson() {
        Gson gson = new Gson();
        return gson.toJson(cart);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void receiveCartFromConfirmOrder(String cartJson, String confirmOrderInfoJson) {
        this.confirmOrderInfoJson = confirmOrderInfoJson;

        Gson gson = new Gson();
        Type listType = new TypeToken<List<ProductOrderViewEntity>>() {}.getType();
        List<ProductOrderViewEntity> newCart = gson.fromJson(cartJson, listType);

        for (ProductOrderViewEntity entity : productOrderViewEntityList) {
            ProductOrderViewEntity result = newCart.stream()
                    .filter(product -> product.id.compareTo(entity.id) == 0)
                    .findAny()
                    .orElse(null);

            if (result == null) {
                entity.count = 0;
            } else {
                entity.count = result.count;
            }
        }

        if (event != null) {
            event.fetchProductListSuccess(productOrderViewEntityList);
        }

        recreateCart();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void recreateCart() {
        cart = new ArrayList<>();
        for (ProductOrderViewEntity entity : productOrderViewEntityList) {
            if (entity.count > 0) {
                cart.add(entity);
            }
        }

        if (event != null) {
            event.cartDidChange(getProductCount(), getProductTotalMoney());
        }
    }
}
