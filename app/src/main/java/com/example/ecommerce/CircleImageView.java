package com.example.ecommerce;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.AttributeSet;
import com.github.mzule.fantasyslide.R;

public class CircleImageView extends de.hdodenhof.circleimageview.CircleImageView {
    private SharedPreferences preferences;
    public CircleImageView(Context context) {
        super(context);
    }
    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        preferences=getContext().getSharedPreferences("user",Context.MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);
        if (pressed) {
            if(isLoggedIn){
                setBorderColor(getResources().getColor(R.color.green));
            }
            else{
                setBorderColor(getResources().getColor(R.color.colorAccent));
            }
        } else {
            setBorderColor(Color.WHITE);
        }
    }
}