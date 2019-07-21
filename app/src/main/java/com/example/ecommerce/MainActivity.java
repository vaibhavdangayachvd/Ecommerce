package com.example.ecommerce;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
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
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.ecommerce.helper.URLContract;
import com.example.ecommerce.helper.UserLogin;
import com.example.ecommerce.helper.ViewHelper;
import com.example.ecommerce.home.Home;
import com.github.mzule.fantasyslide.SideBar;
import com.github.mzule.fantasyslide.Transformer;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private UserLogin userLogin;
    private TextView displayName,swipeLeft;
    private CircleImageView displayPic;
    private SharedPreferences preferences;
    private ViewHelper viewLoader;
    private FragmentManager manager;
    private LiveData<Boolean> loginObserver;
    private LiveData<Fragment> viewObserver;
    private Thread hideSwipeLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setLoaders();
        setObservers();
        initComponents();
        setManagerLifeCycleCallbacks();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        final DrawerArrowDrawable indicator = new DrawerArrowDrawable(this);
        indicator.setColor(Color.WHITE);

        getSupportActionBar().setHomeAsUpIndicator(indicator);

        setTransformer();

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
    private void setManagerLifeCycleCallbacks()
    {
        manager.registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentViewCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull View v, @Nullable Bundle savedInstanceState) {
                super.onFragmentViewCreated(fm, f, v, savedInstanceState);
                userLogin.checkLoginStatus();
                viewLoader.registerView(f);
            }

            @Override
            public void onFragmentPreCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @Nullable Bundle savedInstanceState) {
                super.onFragmentPreCreated(fm, f, savedInstanceState);

            }

        }, true);
    }
    private void initComponents() {
        manager = getSupportFragmentManager();
        displayName = findViewById(R.id.displayName);
        displayPic = findViewById(R.id.displayPic);
        preferences = getSharedPreferences("user", MODE_PRIVATE);
        drawerLayout = findViewById(R.id.drawerLayout);
        swipeLeft=findViewById(R.id.swipe_icon);
        hideSwipeLeft=new Thread(){
            @Override
            public void run() {
                try
                {
                    sleep(10000);
                }
                catch (Exception e){}
                finally {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            swipeLeft.setVisibility(View.GONE);
                        }
                    });
                }
            }
        };
    }

    private void setLoaders() {
        //Login Loader
        userLogin = ViewModelProviders.of(MainActivity.this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new UserLogin(MainActivity.this);
            }
        }).get(UserLogin.class);

        //View Loaders
        viewLoader = ViewModelProviders.of(MainActivity.this).get(ViewHelper.class);
    }

    private void setObservers() {
        //Login Observer
        loginObserver = userLogin.getLoginObserver();
        loginObserver.observe(MainActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean)
                    updateUIForLoggedIn();
                else
                    updateUIForGuest();
            }
        });

        //View Observer
        viewObserver = viewLoader.getObserver();
        viewObserver.observe(MainActivity.this, new Observer<Fragment>() {
            @Override
            public void onChanged(Fragment fragment) {
                if (viewLoader.firstRun) {
                    initial_load(fragment);
                    viewLoader.firstRun = false;
                    hideSwipeLeft.start();
                } else {
                    swipeLeft.setVisibility(View.GONE);
                    if (viewLoader.recreate)
                        viewLoader.recreate = false;
                    else
                        fragment_call(fragment);
                }
            }
        });
    }

    private void initial_load(Fragment f) {
        FragmentTransaction tr = manager.beginTransaction();
        tr.replace(R.id.mainactivity_frame, f);
        tr.commit();
    }

    private void fragment_call(Fragment f) {
        FragmentTransaction tr = manager.beginTransaction();
        tr.replace(R.id.mainactivity_frame, f).addToBackStack(null);
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewLoader.preForRecreate();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //When menu is clicked
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
                break;
            case R.id.cart:
                boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);
                if (isLoggedIn)
                    viewLoader.loadView(new Cart_List());
                else
                    viewLoader.loadView(new Login());
                break;
            case R.id.search:
                viewLoader.loadView(new Search());
                break;
            case R.id.action_notifications:
                Toast.makeText(MainActivity.this,"Under Construction",Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    private void updateUIForLoggedIn() {
        String name = preferences.getString("name", "User");
        displayName.setText(String.format("Logout %s", name));
        displayName.setTextColor(getResources().getColorStateList(R.color.name_login));
        if (preferences.getBoolean("hasDp", false))
            Picasso.get().load(URLContract.PROFILE_PIC_URL + "/" + preferences.getString("username", "user") + ".jpeg").placeholder(R.drawable.loading).error(R.drawable.d_user).into(displayPic);
    }

    @SuppressLint("SetTextI18n")
    private void updateUIForGuest() {
        displayName.setText("Login");
        displayName.setTextColor(getResources().getColorStateList(R.color.not_login));
        displayPic.setImageResource(R.drawable.d_user);
    }

    public void onClick(View view) {
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);
        int id = view.getId();
        switch (id) {
            case R.id.account_setting:
                if (isLoggedIn)
                    viewLoader.loadView(new AccountSettings());
                else
                    viewLoader.loadView(new Login());
                break;
            case R.id.cart:
                if (isLoggedIn)
                    viewLoader.loadView(new Cart_List());
                else
                    viewLoader.loadView(new Login());
                break;
            case R.id.order_history:
                if (isLoggedIn)
                    Toast.makeText(this, "Under Construction", Toast.LENGTH_SHORT).show();
                else
                    viewLoader.loadView(new Login());
                break;
            case R.id.userInfo:
                if (isLoggedIn) {
                    userLogin.logout();
                    Toast.makeText(MainActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
                } else
                    viewLoader.loadView(new Login());
                break;
            case R.id.category:
                viewLoader.loadView(new Category());
                break;
            case R.id.homeNav:
                viewLoader.loadView(new Home());
                break;
            case R.id.settings:
                viewLoader.loadView(new AccountSettings());
                break;
            case R.id.rate_app:
                Toast.makeText(this, "Under Construction", Toast.LENGTH_SHORT).show();
                break;
            case R.id.home:
                viewLoader.loadView(new Home());
                break;
            case R.id.formen:
                viewLoader.loadView(new Category());
                break;
            case R.id.forwomen:
                viewLoader.loadView(new Category());
                break;
            case R.id.forkids:
                viewLoader.loadView(new Category());
                break;
            case R.id.offers:
                viewLoader.loadView(new Category());
                break;
            case R.id.wishlist:
                viewLoader.loadView(new Cart_List());
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (manager.getBackStackEntryCount() == 0)
            moveTaskToBack(true);
        else
            super.onBackPressed();
    }
}