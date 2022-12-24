package com.example.tvdapp.login;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tvdapp.R;
import com.example.tvdapp.home.HomeActivity;
import com.example.tvdapp.users.RegisterActivity;
import com.example.tvdapp.warehouse.WarehouseActivity;

public class LogInActivity extends AppCompatActivity implements TextWatcher {

    private EditText login_account_edit;
    private EditText login_pass_edit;
    private Button login_button;
    private Button login_singUp;
    private TextView login_changePassword;
    private LoginModel model = new LoginModel();
    private ProgressDialog loadingDialog;
    private AlertDialog alertDialog;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_log_in);
        initUI();
        setupModel();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initUI() {
        login_account_edit = findViewById(R.id.login_account);
        login_pass_edit = findViewById(R.id.login_pass);
        login_button = findViewById(R.id.login_button);
        login_singUp = findViewById(R.id.login_SingUp_button);
        login_changePassword = findViewById(R.id.login_changePassword);

        login_pass_edit.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);
        login_account_edit.addTextChangedListener(this);
        login_pass_edit.addTextChangedListener(this);
        updateStatusLoginButton(model.isValid());

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoadingDialog();
                model.login();
            }
        });

        login_singUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentSingUp = new Intent(LogInActivity.this, RegisterActivity.class);
                startActivity(intentSingUp);
            }
        });
    }

    private void setupModel() {
        model.setContext(this);
        model.setEvent(new LoginModel.LoginModelEvent() {
            @Override
            public void loginSuccess(String userType) {
                loadingDialog.cancel();
                gotoHomeActivity(userType);
            }

            @Override
            public void loginFail() {
                loadingDialog.cancel();
                showAlert();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    public void updateStatusLoginButton(boolean isEnable) {
        if (isEnable) {
            login_button.setBackgroundColor(getColor(R.color.Green));
            login_button.setTextColor(getResources().getColor(R.color.white));
        } else {
            login_button.setBackgroundColor(getColor(R.color.LightGray));
            login_button.setTextColor(getResources().getColor(R.color.black));
        }
    }

    private void showLoadingDialog() {
        loadingDialog = ProgressDialog.show(this, "",
                "Uploading. Please wait...", true);
    }

    private void showAlert() {
        alertDialog = new AlertDialog.Builder(this)
                .setTitle("Đăng nhập thất bại")
                .setMessage("Tài khoản hoặc mật khẩu không chính xác")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.cancel();
                    }
                })
                .show();
    }

    private void gotoHomeActivity(String userType) {
        Intent homeIntent = new Intent(this, HomeActivity.class);
        homeIntent.putExtra("user_type", userType);
        startActivity(homeIntent);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void afterTextChanged(Editable editable) {
        String text = editable.toString();
        if (login_account_edit.getEditableText().equals(editable)) {
            model.logInViewEntity.login_account = text;
        } else {
            model.logInViewEntity.login_pass = text;
        }

        updateStatusLoginButton(model.isValid());
    }
}