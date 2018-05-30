package com.example.rrty6.vcardapp.ui.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rrty6.vcardapp.R;
import com.example.rrty6.vcardapp.data.MainOperations;
import com.example.rrty6.vcardapp.utils.App;
import com.example.rrty6.vcardapp.utils.PreferenceKeys;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "LoginActivity";

    private Button mLogin , mRegister;
    private EditText mEmailET, mPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean check = true;
        if (getIntent().hasExtra("logout")) {
            check = getIntent().getBooleanExtra("logout",true);
        }
        if (check && MainOperations.isAuthorized()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(TAG, "onCreate: started...");
        mLogin = findViewById(R.id.btn_login);
        mRegister = findViewById(R.id.btn_registration);
        mEmailET = findViewById(R.id.email_enter_for_login);
        mPasswordEditText = findViewById(R.id.password_enter_for_login);

        mLogin.setOnClickListener(this);
        mRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_registration:
                view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                Log.d(TAG, "onClick: registration clicked....");
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                break;
            case R.id.btn_login:
                try {
                    view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    Log.d(TAG, "onCreate: Checking if this a first login....");
                    MainOperations.login(mEmailET.getText().toString(), mPasswordEditText.getText().toString());
                    Toast toast = Toast.makeText(getApplicationContext(),"You are succesfully authorized!", Toast.LENGTH_LONG);
                    toast.show();
                    System.out.println(mEmailET.getText().toString() + " " + mPasswordEditText.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "onClick: clicked...");
                intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                break;

        }
    }



}
//intent = new Intent(LoginActivity.this, MainActivity.class);
//        startActivity(intent);
//        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//        finish();