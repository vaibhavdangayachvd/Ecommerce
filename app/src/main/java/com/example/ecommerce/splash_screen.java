package com.example.ecommerce;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
public class splash_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Thread t=new Thread(){
            @Override
            public void run() {
                try{
                    sleep(3000);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    startActivity(new Intent(splash_screen.this,MainActivity.class));
                }
            }
        };
        t.start();
    }
}