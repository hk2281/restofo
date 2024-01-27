package com.example.restofo.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import com.example.restofo.Core.Login.LoginContract;
import com.example.restofo.Core.Registration.RegistrationContract;
import com.example.restofo.Core.Registration.RegistrationPresenter;
import com.example.restofo.R;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener, RegistrationContract.View {
    TextView tvLogin;
    Button btnRegistration;
    EditText edtEmail, edtPassword;
    private RegistrationPresenter mRegisterPresenter;
    ProgressDialog mPrgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initViews();
    }
    private void initViews() {
        btnRegistration = (Button)findViewById(R.id.btn_register);
        btnRegistration.setOnClickListener(this);
        tvLogin = (TextView)findViewById(R.id.tv_login);
        tvLogin.setOnClickListener(this);
        edtEmail = (EditText)findViewById(R.id.email_register);
        edtPassword = (EditText)findViewById(R.id.password_register);

        mRegisterPresenter = new RegistrationPresenter(this);

        mPrgressDialog = new ProgressDialog(this);
        mPrgressDialog.setMessage("Please wait, Adding profile to database.");
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_register){
            checkRegistrationDetails();
        }
        else if (view.getId() == R.id.tv_login) {
            moveToLoginActivity();
        }

    }

    private void moveToLoginActivity() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    private void checkRegistrationDetails() {
        if(!TextUtils.isEmpty(edtEmail.getText().toString()) && !TextUtils.isEmpty(edtPassword.getText().toString())){
            initLogin(edtEmail.getText().toString(), edtPassword.getText().toString());
        }else{
            if(TextUtils.isEmpty(edtEmail.getText().toString())){
                edtEmail.setError("Please enter a valid email");
            }if(TextUtils.isEmpty(edtPassword.getText().toString())){
                edtPassword.setError("Please enter password");
            }
        }
    }

    private void initLogin(String email, String password) {
        mPrgressDialog.show();
        mRegisterPresenter.register(this, email, password);
    }

    @Override
    public void onRegistrationSuccess(FirebaseUser firebaseUser) {
        mPrgressDialog.dismiss();
        Toast.makeText(getApplicationContext(), "Successfully Registered" , Toast.LENGTH_SHORT).show();
        moveToLoginActivity();
    }

    @Override
    public void onRegistrationFailure(String message) {
        mPrgressDialog.dismiss();
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
