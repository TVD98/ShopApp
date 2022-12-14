package com.example.tvdapp.editProduct;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tvdapp.R;
import com.example.tvdapp.order.model.ProductResponse;
import com.example.tvdapp.utilities.FileUtil;

import java.io.File;
import java.io.IOException;

public class EditProductActivity extends AppCompatActivity implements TextWatcher {
    private ImageView addImageView;
    private ImageView cameraImageView;
    private ImageView productImageView;
    private EditText nameEditText;
    private EditText priceEditText;
    private EditText costPriceEditText;
    private Button leftButton;
    private Button rightButton;
    private ProgressDialog loadingDialog;
    private EditProductModel model = new EditProductModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_edit_product);

        Intent intent = getIntent();
        String productId = intent.getStringExtra("productId");
        model.setProductId(productId);

        initUI();
        initModel();
    }

    private void setupNavigation(boolean isEdit) {
        if (isEdit) {
            setTitle(R.string.edit_product_activity_title);
        } else {
            setTitle(R.string.add_product_activity_title);
        }
    }

    private void setupBottomView() {
        if (model.isEditMode()) {
            leftButton.setText(R.string.edit_product_remove);
            rightButton.setText(R.string.edit_product_update);
        } else {
            leftButton.setText(R.string.edit_product_clear);
            rightButton.setText(R.string.edit_product_done);
        }
    }

    private void initUI() {
        setupNavigation(model.isEditMode());
        addImageView = findViewById(R.id.edit_product_add_image_view);
        cameraImageView = findViewById(R.id.edit_product_camera_image_view);
        nameEditText = findViewById(R.id.edit_product_name_edit_text);
        priceEditText = findViewById(R.id.edit_product_price_edit_text);
        costPriceEditText = findViewById(R.id.edit_product_cost_price_edit_text);
        leftButton = findViewById(R.id.edit_product_left_button);
        rightButton = findViewById(R.id.edit_product_right_button);
        productImageView = findViewById(R.id.edit_product_image_view);

        setupUI();
    }

    private void setupUI() {
        setupBottomView();
        updateStatusRightButton(model.isValidProduct());

        addImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImageFromGallery();
            }
        });

        cameraImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoadingDialog();

                if (model.isEditMode()) {
                    model.removeProduct();
                }
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoadingDialog();

                if (model.isEditMode()) {
                    model.editProduct();
                } else {
                    model.createProduct();
                }
            }
        });

        nameEditText.addTextChangedListener(this);
        priceEditText.addTextChangedListener(this);
        costPriceEditText.addTextChangedListener(this);
    }

    private void initModel() {
        model.setEvent(new EditProductModel.EditProductModelEvent() {
            @Override
            public void productInfoDidChange(boolean isValid) {
                updateStatusRightButton(isValid);
            }

            @Override
            public void fetchDataSuccess(ProductResponse response) {
                bindData(response);
                updateStatusRightButton(true);
            }

            @Override
            public void fetchImageSuccess(String imageLink) {
                showProductImage(imageLink);
            }

            @Override
            public void uploadProductSuccess() {
                loadingDialog.cancel();
                finish();
            }

            @Override
            public void uploadProductFail(String error) {
                loadingDialog.cancel();
            }
        });

        if (model.isEditMode()) {
            model.fetchData();
        }
    }

    private void bindData(ProductResponse response) {
        nameEditText.removeTextChangedListener(this);
        priceEditText.removeTextChangedListener(this);
        costPriceEditText.removeTextChangedListener(this);
        nameEditText.setText(response.name);
        priceEditText.setText(String.format("%,d", response.price));
        costPriceEditText.setText(String.format("%,d", response.costPrice));
        nameEditText.addTextChangedListener(this);
        priceEditText.addTextChangedListener(this);
        costPriceEditText.addTextChangedListener(this);
    }

    private void showProductImage(String imageLink) {
        Glide.with(this)
                .load(imageLink)
                .centerCrop()
                .into(productImageView);
    }

    private void showProductImage(Uri uri) {
        Glide.with(this)
                .load(uri)
                .centerCrop()
                .into(productImageView);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateStatusRightButton(boolean isEnable) {
        if (isEnable) {
            rightButton.setBackgroundColor(getResources().getColor(R.color.Green));
        } else {
            rightButton.setBackgroundColor(getResources().getColor(R.color.LightGray));
        }
    }

    private void chooseImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        someActivityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data == null) {
                            //error
                            return;
                        }
                        Uri uri = data.getData();
                        model.setProductImageFile(uri);
                        showProductImage(uri);
                    }
                }
            });

    private void showLoadingDialog() {
        loadingDialog = ProgressDialog.show(EditProductActivity.this, "",
                "Uploading. Please wait...", true);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String text = editable.toString();

        if (editable.equals(nameEditText.getEditableText())) {
            model.setName(text);
        } else {
            int number = 0;
            if (!text.isEmpty()) {
                String realText = text.replace(",", "").replace(".", "");
                number = Integer.parseInt(realText);
            }
            if (editable.equals(priceEditText.getEditableText())) {
                formatEditText(priceEditText, number);
                model.setPrice(number);
            } else {
                formatEditText(costPriceEditText, number);
                model.setCostPrice(number);
            }
        }
    }

    private void formatEditText(EditText editText, int number) {
        editText.removeTextChangedListener(this);
        editText.setText(String.format("%,d", number));
        editText.setSelection(editText.length());
        editText.addTextChangedListener(this);
    }
}