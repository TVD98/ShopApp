package com.example.tvdapp.Users;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tvdapp.R;

public class LogInActivity extends AppCompatActivity {

    private EditText login_account_edit;
    private EditText login_pass_edit;
    private Button login_button;
    private Button login_singUp;
    private TextView login_changePassword;

    private LogInViewEntity logInViewEntity = new LogInViewEntity(
            "",
            ""
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        initUI();
    }

    private  void initUI(){
        login_account_edit = findViewById(R.id.login_account);
        login_pass_edit = findViewById(R.id.login_pass);
        login_button = findViewById(R.id.login_button);
        login_singUp = findViewById(R.id.login_SingUp_button);
        login_changePassword = findViewById(R.id.login_changePassword);

        login_pass_edit.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
}