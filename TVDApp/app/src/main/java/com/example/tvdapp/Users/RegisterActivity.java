package com.example.tvdapp.Users;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.tvdapp.R;
import com.example.tvdapp.home.HomeActivity;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements TextWatcher {

    private EditText register_account_edit;
    private EditText register_pass_edit;
    private EditText register_pass_again_edit;
    private EditText register_name_edit;
    private Spinner register_gender_spinner;
    private Spinner register_position_spinner;
    private EditText register_area_edit;
    private EditText register_cccd_edit;
    private EditText register_phone_edit;
    private EditText register_address_edit;
    private Button register_signUp_button;
    private Button register_cancel_button;
    private int selected;
    private int selectedGender;

    private RegisterViewEntity registerViewEntity = new RegisterViewEntity(
            "",
            "",
            "",
            "",
            0,
            0,
            "",
            "",
            "",
            ""
    );

    String arr[] = {
            "Quản lý",
            "Nhân viên"};

    String arrGender[] = {
            "Nam",
            "Nu"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initUI();


    }

    private void initUI() {
        register_account_edit = findViewById(R.id.register_account_name_text);
        register_pass_edit = findViewById(R.id.register_pass_edit_text);
        register_pass_again_edit = findViewById(R.id.register_pass_again_edit_text);
        register_name_edit = findViewById(R.id.register_name_employee_edit_text);
        register_gender_spinner = findViewById(R.id.spinner2);
        register_position_spinner = findViewById(R.id.spinner1);
        register_area_edit = findViewById(R.id.register_area_employee_edit_text);
        register_cccd_edit = findViewById(R.id.register_CCCD_employee_edit_text);
        register_phone_edit = findViewById(R.id.register_phone_employee_edit_text);
        register_address_edit = findViewById(R.id.register_address_employee_edit_text);
        register_cancel_button = findViewById(R.id.register_cancel_button);
        register_signUp_button = findViewById(R.id.register_register_button);

        register_pass_edit.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);
        register_pass_again_edit.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);

        register_account_edit.addTextChangedListener(this);
        register_pass_edit.addTextChangedListener(this);
        register_pass_again_edit.addTextChangedListener(this);
        register_name_edit.addTextChangedListener(this);
        register_area_edit.addTextChangedListener(this);
        register_cccd_edit.addTextChangedListener(this);
        register_phone_edit.addTextChangedListener(this);
        register_address_edit.addTextChangedListener(this);


        RegisterAdapter adapter = new RegisterAdapter(this, arr);
        register_position_spinner.setAdapter(adapter);

        register_position_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected = i;
                registerViewEntity.position_employee = i;
                updateStatusSignUpButton(isValid());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        RegisterAdapter adapter2 = new RegisterAdapter(this, arrGender);
        register_gender_spinner.setAdapter(adapter2);

        register_gender_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedGender = i;
                registerViewEntity.gender = i;
                updateStatusSignUpButton(isValid());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        register_signUp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
//                startActivity(intent);
                FirebaseDatabase.getInstance().getReference().child("account").setValue(registerViewEntity);
            }
        });

        register_cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register_account_edit.getText().clear();
                register_pass_edit.getText().clear();
                register_pass_again_edit.getText().clear();
                register_name_edit.getText().clear();
                register_area_edit.getText().clear();
                register_cccd_edit.getText().clear();
                register_phone_edit.getText().clear();
                register_address_edit.getText().clear();
            }
        });

    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        if (charSequence.length() == 0) {
            register_account_edit.setError("Bạn bắt buộc phải nhập Tên đăng nhập");
        } else {
            register_account_edit.setError(null);
        }

        if (charSequence.length() == 0) {
            register_name_edit.setError("Bạn bắt buộc phải nhập Họ tên nhân viên");
        } else {
            register_name_edit.setError(null);
        }
        //checkPassword();


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void afterTextChanged(Editable editable) {
        String text = editable.toString();
        if (editable.equals(register_account_edit.getEditableText())) {
            registerViewEntity.account_name = text;

        } else if (editable.equals(register_pass_edit.getEditableText())) {
            registerViewEntity.pass = text;

        } else if (editable.equals(register_pass_again_edit.getEditableText())) {
            registerViewEntity.pass_again = text;

        } else if (editable.equals(register_name_edit.getEditableText())) {
            registerViewEntity.name_employee = text;


        } else if (editable.equals(register_area_edit.getEditableText())) {
            registerViewEntity.area = text;

        } else if (editable.equals(register_cccd_edit.getEditableText())) {
            registerViewEntity.cccd = text;

        } else if (editable.equals(register_phone_edit.getEditableText())) {
            registerViewEntity.phone = text;

        } else if (editable.equals(register_address_edit.getEditableText())) {
            registerViewEntity.address = text;
        }

        updateStatusSignUpButton(isValid());
        checkPassword();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    public void updateStatusSignUpButton(boolean isEnable) {
        if (isEnable) {
            register_signUp_button.setBackgroundColor(getColor(R.color.Green));
            ;
            register_signUp_button.setTextColor(getResources().getColor(R.color.white));
        } else {
            register_signUp_button.setBackgroundColor(getColor(R.color.LightGray));
            ;
            register_signUp_button.setTextColor(getResources().getColor(R.color.black));
        }
    }

    private boolean isValid() {
        if (registerViewEntity.account_name.isEmpty()) {
            return false;
        }


        if (registerViewEntity.name_employee.isEmpty()) {
            return false;

        }
        if (registerViewEntity.pass.compareTo(registerViewEntity.pass_again) != 0) {
            return false;
        }

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPassword() {
        if (registerViewEntity.pass.equals(registerViewEntity.pass_again)) {
            updateStatusSignUpButton(isValid());

        }else {
            register_account_edit.setError("Mật khẩu nhập lại phải khớp với mật khẩu ");
        }
    }
}