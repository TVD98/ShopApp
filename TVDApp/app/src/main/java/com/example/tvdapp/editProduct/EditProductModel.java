package com.example.tvdapp.editProduct;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.tvdapp.order.model.ProductResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.File;

public class EditProductModel {
    private String productId;
    private EditProductModelEvent event;
    private DatabaseReference mDatabase;
    private StorageReference storageRef;
    private ValueEventListener productInfoListener;


    public EditProductModel() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storageRef = FirebaseStorage.getInstance().getReference();
    }

    public void setEvent(EditProductModelEvent event) {
        this.event = event;
    }

    private ProductResponse productResponse = new ProductResponse("", "", "", 0, 0);
    private Uri productImageFile;

    public void setProductImageFile(Uri productImageFile) {
        this.productImageFile = productImageFile;

        if (event != null) {
            event.productInfoDidChange(isValidProduct());
        }
    }

    public void setProductId(String productId) {
        this.productId = productId;

        if (productId.isEmpty()) {
            productResponse.id = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
        } else {
            productResponse.id = productId;
        }
    }

    public void clearValueEventListeners() {
        if (productInfoListener != null) {
            mDatabase.child(String.format("products/%s", productId)).removeEventListener(productInfoListener);
        }
    }

    public void fetchData() {
        productInfoListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productResponse = dataSnapshot.getValue(ProductResponse.class);

                if (event != null) {
                    event.fetchDataSuccess(productResponse);
                }

                fetchProductImages();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.child(String.format("products/%s", productId)).addValueEventListener(productInfoListener);
    }

    private void fetchProductImages() {
        StorageReference sReference = FirebaseStorage.getInstance().getReference();
        StorageReference storageReference = sReference.child(productResponse.imageURL);
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (event != null) {
                    event.fetchImageSuccess(uri.toString());
                }
            }
        });
    }

    public void editProduct() {
        if (productImageFile == null) {
            clearValueEventListeners();
            uploadProduct();
        } else {
            uploadImage();
        }
    }

    public void createProduct() {
        uploadImage();
    }

    public void removeProduct() {
        clearValueEventListeners();
        mDatabase.child(String.format("products/%s", productResponse.id)).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        if (event != null) {
                            event.uploadProductSuccess();
                        }
                    }
                });
    }

    private void uploadImage() {
        String imageURL = String.format("product/%s", productResponse.id);
        StorageReference storageReference = storageRef.child(imageURL);
        UploadTask uploadTask = storageReference.putFile(productImageFile);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                productResponse.imageURL = imageURL;
                clearValueEventListeners();
                uploadProduct();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (event != null) {
                    event.uploadProductFail(e.toString());
                }
            }
        });
    }

    private void uploadProduct() {
        mDatabase.child(String.format("products/%s", productResponse.id)).setValue(productResponse)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        if (event != null) {
                            event.uploadProductSuccess();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (event != null) {
                            event.uploadProductFail(e.toString());
                        }
                    }
                });
    }

    public boolean isEditMode() {
        return !productId.isEmpty();
    }

    public boolean isValidProduct() {
        if (productResponse.name.isEmpty() || (productResponse.imageURL.isEmpty() && productImageFile == null)
                || productResponse.price == 0 || productResponse.costPrice == 0) {
            return false;
        } else {
            return true;
        }
    }

    public void setName(String name) {
        productResponse.name = name;
        if (event != null) {
            event.productInfoDidChange(isValidProduct());
        }
    }

    public void setPrice(int price) {
        productResponse.price = price;
        if (event != null) {
            event.productInfoDidChange(isValidProduct());
        }
    }

    public void setCostPrice(int costPrice) {
        productResponse.costPrice = costPrice;
        if (event != null) {
            event.productInfoDidChange(isValidProduct());
        }
    }

    interface EditProductModelEvent {
        void productInfoDidChange(boolean isValid);

        void fetchDataSuccess(ProductResponse response);

        void fetchImageSuccess(String imageLink);

        void uploadProductSuccess();

        void uploadProductFail(String error);
    }
}
