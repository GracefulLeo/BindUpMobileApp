package com.bindup.vcard.vcardapp.ui.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bindup.vcard.vcardapp.R;
import com.bindup.vcard.vcardapp.data.MainOperations;
import com.bindup.vcard.vcardapp.utils.UIHandler;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    // constants

    private static final String TAG = "LoginActivity";

    // widgets

    private RelativeLayout mProgressBarContainer;
    private Button mLogin , mRegister;
    private EditText mEmailET, mPasswordEditText;

    // vars

    private MainOperations mainOperations = new MainOperations(new UIHandler(this,null));

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
        mProgressBarContainer = findViewById(R.id.progress_bar_login_container);

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
                    mainOperations.login(mEmailET.getText().toString(), mPasswordEditText.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "onClick: clicked...");
                break;

        }
    }
    public void progressBarChange(boolean change) {
        if (change) {
            mEmailET.setFocusable(false);
            mPasswordEditText.setFocusable(false);
            mProgressBarContainer.bringToFront();
            mProgressBarContainer.setBackgroundColor(getResources().getColor(R.color.mainBackgroundblur));
            mProgressBarContainer.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            mProgressBarContainer.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            mEmailET.setFocusableInTouchMode(true);
            mPasswordEditText.setFocusableInTouchMode(true);
            mEmailET.setFocusable(true);
            mPasswordEditText.setFocusable(true);
        }
    }
}