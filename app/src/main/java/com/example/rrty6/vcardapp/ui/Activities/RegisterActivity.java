package com.example.rrty6.vcardapp.ui.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rrty6.vcardapp.R;
import com.example.rrty6.vcardapp.data.MainOperations;
import com.example.rrty6.vcardapp.utils.UIHandler;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private MainOperations mainOperations;
    private Button mButtonRegister;
    private EditText mEmailET, mPasswordET, mPasswordRepetitionET;
    private ProgressBar mProgressBar;
    private TextView mProgressBarMessage;
    private RelativeLayout mProgressBarContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mainOperations = new MainOperations(new UIHandler(this,null));

        mButtonRegister = findViewById(R.id.btn_register_for_registration);
        mProgressBar = findViewById(R.id.progress_bar_registration);
        mProgressBarContainer = findViewById(R.id.progress_bar_registration_container);
        mProgressBarMessage = findViewById(R.id.progress_bar_registration_text_view);

        mEmailET = findViewById(R.id.email_enter_for_register);
        mPasswordET = findViewById(R.id.password_enter_for_register);
        mPasswordRepetitionET = findViewById(R.id.password_repeat_enter_for_registration);

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//@TODO make it smoother. Code is very ugly....
                if (Objects.equals(mPasswordET.getText().toString(), mPasswordRepetitionET.getText().toString())) {
                    if (isEmailValid(mEmailET.getText().toString())) {
                        if (isPasswordValid(mPasswordET.getText().toString())) {

                            mainOperations.register(mEmailET.getText().toString(), mPasswordET.getText().toString());
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), "Password must be not shorter then 4 characters", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Invalid email . Please check if entered data is correct", Toast.LENGTH_LONG);
                        toast.show();
                    }
                } else {
                    Snackbar.make(v, "Password doesn't match", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });
    }

    public void progressBarChange(boolean change,String message) {
        if (change) {
            mEmailET.setFocusable(false);
            mPasswordET.setFocusable(false);
            mPasswordRepetitionET.setFocusable(false);
            mProgressBarMessage.setText(message);
            mProgressBarContainer.bringToFront();
            mProgressBarContainer.setBackgroundColor(getResources().getColor(R.color.mainBackgroundblur));
            mProgressBarContainer.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            mProgressBarContainer.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
}
