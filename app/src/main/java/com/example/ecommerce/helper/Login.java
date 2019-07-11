package com.example.ecommerce.helper;

import android.content.Context;

public interface Login {
    public boolean tryLogin(String username,String password);
    public boolean isLoggedIn();
}
