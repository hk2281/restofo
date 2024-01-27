package com.example.restofo.Core.Login;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class TokenManager {

    private Context context;

    public TokenManager(Context context) {
        this.context = context;
    }

    public void saveToken(String token) {
        Context activity = this.context;
        SharedPreferences sharedPreferences = activity.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Log.d("ke", token);
        // Сохранение токена
        editor.putString("AuthToken", token);
        editor.apply();
    }

    public String getToken() {
        Context activity = this.context;
        SharedPreferences sharedPreferences = activity.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);

        // Получение токена
        String token = sharedPreferences.getString("AuthToken", null);

        // Проверка наличия токена и возврат его значения
        if (token != null && !token.isEmpty()) {
            Log.d("ke", "Retrieved token: " + token);
            return token;
        } else {
            Log.d("ke", "No token found");
            return null; // или вы можете вернуть другое значение по умолчанию
        }
    }

    // Методы для работы с Keystore и SharedPreferences
    // ...
}