package com.example.ecommerce;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
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
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Login Provider
        userLogin = ViewModelProviders.of(MainActivity.this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new UserLogin(MainActivity.this);
            }
        }).get(UserLogin.class);
        //Observer
        LiveData<Boolean> loginObserver = userLogin.getLoginObserver();
        loginObserver.observe(MainActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean)
                    updateUIForLoggedIn();
                else
                    updateUIForGuest();
            }
        });
        //Keeps track of login status
        manager=getSupportFragmentManager();
        manager.registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentPreAttached(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull Context context) {
                super.onFragmentPreAttached(fm, f, context);
                userLogin.checkLoginStatus();
            }
        },true);
        //Call Fragment
        initial_load(new Home());

        displayName = findViewById(R.id.displayName);
        displayPic = findViewById(R.id.displayPic);
        preferences = getSharedPreferences("user", MODE_PRIVATE);
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
    private void initial_load(Fragment f){
        FragmentTransaction tr = manager.beginTransaction();
        tr.replace(R.id.mainactivity_frame,f);
        tr.commit();
    }
    private void fragment_call(Fragment f) {
        FragmentTransaction tr = manager.beginTransaction();
        tr.replace(R.id.mainactivity_frame, f).addToBackStack("main");
        tr.commit();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
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
        if (preferences.getBoolean("hasDp", false))
            Picasso.get().load(URLContract.PROFILE_PIC_URL + "/" + preferences.getString("username", "user") + ".jpeg").placeholder(R.drawable.loading).error(R.drawable.d_user).into(displayPic);
    }

    @SuppressLint("SetTextI18n")
    private void updateUIForGuest() {
        displayName.setText("Login");
        displayPic.setImageResource(R.drawable.d_user);
    }

    public void onClick(View view) {
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);
        int id = view.getId();
        switch (id) {
            case R.id.account_setting:
                if (isLoggedIn)
                    fragment_call(new AccountSettings());
                else
                    fragment_call(new Login());
                break;
            case R.id.cart:
                if (isLoggedIn)
                    startActivity(new Intent(MainActivity.this, Cart_List.class));
                else
                    fragment_call(new Login());
                break;
            case R.id.order_history:
                if (isLoggedIn)
                    Toast.makeText(this, "order history", Toast.LENGTH_SHORT).show();
                else
                    fragment_call(new Login());
                break;
            case R.id.userInfo:
                if (isLoggedIn) {
                    userLogin.logout();
                    fragment_call(new Home());
                    Toast.makeText(MainActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
                } else
                    fragment_call(new Login());
                break;
            case R.id.category:
                fragment_call(new Category());
                break;
            case R.id.homeNav:
                fragment_call(new Home());
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