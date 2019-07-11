package com.example.ecommerce;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.ecommerce.helper.UserLogin;

public class SplashScreen extends AppCompatActivity {
    UserLogin userLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        userLogin=new UserLogin(SplashScreen.this);
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    doPreStartCheck();
                    Intent gotoMainActivity=new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(gotoMainActivity);
                    finish();
                }
            }
            private void doPreStartCheck()
            {
                if(userLogin.isLoggedIn())
                    userLogin.validateFromServer();
            }
        };
        t.start();
    }
}