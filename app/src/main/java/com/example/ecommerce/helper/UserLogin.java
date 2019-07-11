package com.example.ecommerce.helper;

import android.content.Context;
import android.content.SharedPreferences;

public final class UserLogin implements Login {
    private Context context;
    public UserLogin(Context context)
    {
        this.context=context;
    }
    @Override
    public boolean tryLogin(String username, String password) {
        return false;
    }

    @Override
    public boolean isLoggedIn() {
        SharedPreferences preferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        return preferences.getBoolean("isLoggedIn",false);
    }
    public void validateFromServer()
    {
        SharedPreferences preferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        String username=preferences.getString("username",null);
        String password=preferences.getString("password",null);
        if(!tryLogin(username,password))
        {
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();
        }
    }
}
