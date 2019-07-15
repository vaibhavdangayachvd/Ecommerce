package com.example.ecommerce;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.ecommerce.helper.URLContract;
import com.example.ecommerce.helper.UserLogin;
import com.github.mzule.fantasyslide.SideBar;
import com.github.mzule.fantasyslide.Transformer;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private UserLogin userLogin;
    private TextView displayName;
    private CircleImageView displayPic;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragment_call(new Home());
        displayName = findViewById(R.id.displayName);
        displayPic = findViewById(R.id.displayPic);
        preferences = getSharedPreferences("user", MODE_PRIVATE);
        userLogin = ViewModelProviders.of(MainActivity.this).get(UserLogin.class);
        LiveData<Boolean> loginObserver = userLogin.getLoginObserver();
        //Keep Track Of Login Status
        loginObserver.observe(MainActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    updateUIForLoggedIn();
                } else {
                    updateUIForGuest();
                }
            }
        });

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        final DrawerArrowDrawable indicator = new DrawerArrowDrawable(this);
        indicator.setColor(Color.WHITE);
        getSupportActionBar().setHomeAsUpIndicator(indicator);

        setTransformer();
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (((ViewGroup) drawerView).getChildAt(1).getId() == R.id.leftSideBar) {
                    indicator.setProgress(slideOffset);
                }
            }
        });
    }

    public void fragment_call(Fragment f) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction tr = manager.beginTransaction();
        tr.replace(R.id.mainactivity_frame, f);
        tr.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        userLogin.checkLoginStatus(MainActivity.this);
    }

    private void setTransformer() {
        final float spacing = getResources().getDimensionPixelSize(R.dimen.spacing);
        SideBar rightSideBar = findViewById(R.id.rightSideBar);
        rightSideBar.setTransformer(new Transformer() {
            private View lastHoverView;

            @Override
            public void apply(ViewGroup sideBar, View itemView, float touchY, float slideOffset, boolean isLeft) {
                boolean hovered = itemView.isPressed();
                if (hovered && lastHoverView != itemView) {
                    animateIn(itemView);
                    animateOut(lastHoverView);
                    lastHoverView = itemView;
                }
            }

            private void animateOut(View view) {
                if (view == null) {
                    return;
                }
                ObjectAnimator translationX = ObjectAnimator.ofFloat(view, "translationX", -spacing, 0);
                translationX.setDuration(200);
                translationX.start();
            }

            private void animateIn(View view) {
                ObjectAnimator translationX = ObjectAnimator.ofFloat(view, "translationX", 0, -spacing);
                translationX.setDuration(200);
                translationX.start();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //When menu is clicked
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        }
        return true;
    }

    private void updateUIForLoggedIn() {
        String name = preferences.getString("name", "User");
        displayName.setText(String.format("Logout %s", name));
        if(preferences.getBoolean("hasDp", false))
            Picasso.get().load(URLContract.BASE_URL+"/api/images/" + preferences.getString("username", "user") + ".jpeg").placeholder(R.drawable.loading).into(displayPic);
    }

    private void updateUIForGuest() {
        displayName.setText(getString(R.string.login_register));
        displayPic.setImageResource(R.drawable.d_user);
    }

    public void onClick(View view) {
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);
        int id = view.getId();
        switch (id) {
            case R.id.account_setting:
                if (isLoggedIn)
                    startActivity(new Intent(MainActivity.this, AccountSettings.class));
                else
                    startActivity(new Intent(MainActivity.this, Login.class));
                break;
            case R.id.cart:
                if (isLoggedIn)
                    Toast.makeText(this, "cart", Toast.LENGTH_SHORT).show();
                else
                    startActivity(new Intent(MainActivity.this, Login.class));
                break;
            case R.id.order_history:
                if (isLoggedIn)
                    Toast.makeText(this, "order history", Toast.LENGTH_SHORT).show();
                else
                    startActivity(new Intent(MainActivity.this, Login.class));
                break;
            case R.id.userInfo:
                if (isLoggedIn) {
                    userLogin.logout(MainActivity.this);
                    Toast.makeText(MainActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
                } else
                    startActivity(new Intent(MainActivity.this, Login.class));
                break;
            case R.id.category:
                Toast.makeText(this, "category", Toast.LENGTH_SHORT).show();
                break;
            case R.id.notifications:
                Toast.makeText(this, "notifications", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rate_app:
                Toast.makeText(this, "5 star", Toast.LENGTH_SHORT).show();
                break;
            case R.id.home:
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                break;
            case R.id.formen:
                Toast.makeText(this, "for men", Toast.LENGTH_SHORT).show();
                break;
            case R.id.forwomen:
                Toast.makeText(this, "for women", Toast.LENGTH_SHORT).show();
                break;
            case R.id.forkids:
                Toast.makeText(this, "for kids", Toast.LENGTH_SHORT).show();
                break;
            case R.id.offers:
                Toast.makeText(this, "Offers", Toast.LENGTH_SHORT).show();
                break;
            case R.id.wishlist:
                Toast.makeText(this, "wishlist", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}