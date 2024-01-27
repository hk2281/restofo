package com.example.restofo.Core.Login;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

public class LoginInteractor  implements LoginContract.Intractor{
    private  LoginContract.onLoginListener mOnLoginListener;


    public LoginInteractor(LoginContract.onLoginListener onLoginListener){
        this.mOnLoginListener = onLoginListener;
    }
    @Override
    public void performFirebaseLogin(Activity activity, String email, String password) {
        TokenManager tManager = new TokenManager(activity.getApplicationContext());
        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<GetTokenResult> tokenTask) {
                                        if (tokenTask.isSuccessful()) {
                                            // Получение токена аутентификации
                                            String idToken = tokenTask.getResult().getToken();
                                            // Сохранение токена
                                            tManager.saveToken(idToken);
                                            mOnLoginListener.onSuccess("Logged in successfully with token: " + idToken);
                                        } else {
                                            // Обработка ошибки получения токена
                                            mOnLoginListener.onFailure(tokenTask.getException().getMessage());
                                        }
                                    }
                                });
                            }
                        }
                        else {
                            mOnLoginListener.onFailure(task.getException().toString());
                        }
                    }
                });
    }

}
