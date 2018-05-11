package com.example.rrty6.vcardapp.ui.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rrty6.vcardapp.R;
import com.example.rrty6.vcardapp.data.MainOperations;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private Button mButtonRegister;
    private EditText mEmailET, mPasswordET, mPasswordRepetitionET;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mButtonRegister = findViewById(R.id.btn_register_for_registration);


        mEmailET = findViewById(R.id.email_enter_for_register);
        mPasswordET = findViewById(R.id.password_enter_for_register);
        mPasswordRepetitionET = findViewById(R.id.password_repeat_enter_for_registration);


        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//@TODO make it smoother. Code is very ugly....

                if (Objects.equals(mPasswordET.getText().toString(), mPasswordRepetitionET.getText().toString())) {
                    try {
                        if(isEmailValid(mEmailET.getText().toString())) {
                            if (isPasswordValid(mPasswordET.getText().toString())){
                            MainOperations.register(mEmailET.getText().toString(), mPasswordET.getText().toString());
                            Toast toast = Toast.makeText(getApplicationContext(), "Registration succesfully completed!", Toast.LENGTH_LONG);
                            toast.show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                            }else {
                                Toast toast = Toast.makeText(getApplicationContext(), "Password must be not shorter then 4 characters", Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }else{
                            Toast toast = Toast.makeText(getApplicationContext(), "Invalid email . Please check if entered data is correct", Toast.LENGTH_LONG);
                            toast.show();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Snackbar.make(v, "Password doesn't match", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });


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
