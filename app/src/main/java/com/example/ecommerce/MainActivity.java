package com.example.ecommerce;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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

import com.example.ecommerce.helper.UserLogin;
import com.github.mzule.fantasyslide.SideBar;
import com.github.mzule.fantasyslide.SimpleFantasyListener;
import com.github.mzule.fantasyslide.Transformer;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private UserLogin userLogin;
    private LiveData<Boolean> loginObserver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragment_call(new Home());
        final SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
        userLogin = ViewModelProviders.of(MainActivity.this).get(UserLogin.class);
        loginObserver = userLogin.getLoginObserver();
        //Keep Track Of Login Status
        loginObserver.observe(MainActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean == true) {
                    Toast.makeText(MainActivity.this, "Hi User", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Hi Guest", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        final DrawerArrowDrawable indicator = new DrawerArrowDrawable(this);
        indicator.setColor(Color.WHITE);
        getSupportActionBar().setHomeAsUpIndicator(indicator);

        setTransformer();
        setListener();
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

    public void fragment_call(Fragment f){
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction tr=manager.beginTransaction();
        tr.replace(R.id.mainactivity_frame,f);
        tr.commit();
    }
    @Override
    protected void onStart() {
        super.onStart();
        userLogin.checkLoginStatus(MainActivity.this);
    }

    private void setListener() {
        final TextView tipView = findViewById(R.id.tipView);
        SideBar leftSideBar = findViewById(R.id.leftSideBar);
        leftSideBar.setFantasyListener(new SimpleFantasyListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public boolean onHover(@Nullable View view, int index) {
                tipView.setVisibility(View.VISIBLE);
                if (view instanceof TextView) {
                    tipView.setText(String.format("%s", ((TextView) view).getText().toString()));
                } else if (view != null && view.getId() == R.id.userInfo) {
                    tipView.setText(R.string.profile);
                } else {
                    tipView.setText(null);
                }
                return false;

            }

            @SuppressLint("DefaultLocale")
            @Override
            public boolean onSelect(View view, int index) {
                tipView.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, String.format("%d selected", index), Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onCancel() {
                tipView.setVisibility(View.INVISIBLE);
            }
        });
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
            Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        }
        return true;
    }

    public void onClick(View view) {
        if (view instanceof TextView) {
            int id = view.getId();
            switch (id) {
                case R.id.account_setting:
                    Toast.makeText(this, "account setting", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.category:
                    Toast.makeText(this, "category", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.cart:
                    Toast.makeText(this, "cart", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.order_history:
                    Toast.makeText(this, "order history", Toast.LENGTH_SHORT).show();
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
                case R.id.userInfo:
                    Toast.makeText(this, "user", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}