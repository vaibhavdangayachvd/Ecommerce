package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.cart.CartLoader;
import com.example.ecommerce.helper.URLContract;
import com.example.ecommerce.helper.UserLogin;
import com.example.ecommerce.helper.ViewHelper;
import com.example.ecommerce.product.ProductItem;
import com.squareup.picasso.Picasso;

public class Product_View extends Fragment {
    private ImageView image;
    private TextView name, price, description;
    private String category;
    private ProductItem item;
    private Button buy,cart;
    private UserLogin userLoader;
    private CartLoader cartLoader;
    private LiveData<Boolean> userObserver;
    private LiveData<Boolean> cartAddObserver;
    private ProgressBar progressBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_product_view, container, false);
        initComponents(view);
        setLoader();
        setObserver();
        progressBar.setVisibility(View.GONE);
        return view;
    }

    private void initComponents(View view) {
        image = view.findViewById(R.id.product_image);
        name = view.findViewById(R.id.product_name);
        price = view.findViewById(R.id.product_price);
        description = view.findViewById(R.id.product_description);
        buy=view.findViewById(R.id.buyNow);
        cart=view.findViewById(R.id.addCart);
        progressBar=view.findViewById(R.id.progress);
        Bundle bundle = getArguments();
        item = (ProductItem) bundle.getSerializable("item");
        category=bundle.getString("category");
        Picasso.get().load(URLContract.PRODUCT_PIC_URL + "/" + category + "/" + item.getImage()).placeholder(R.drawable.loading).error(R.drawable.error).into(image);
        name.setText(item.getName());
        price.setText("Rs." + item.getPrice() + " /-");
        description.setText(item.getDescription());
    }
    private void setLoader() {

        userLoader = ViewModelProviders.of(getActivity(), new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new UserLogin(getActivity());
            }
        }).get(UserLogin.class);
        userLoader.checkLoginStatus();

        cartLoader = ViewModelProviders.of(getActivity(), new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new CartLoader(getActivity());
            }
        }).get(CartLoader.class);
    }

    private void setObserver() {
        //Login Observer
        userObserver = userLoader.getLoginObserver();
        userObserver.observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean)
                    updateUIForLogin();
                else
                    updateUIForGuest();
            }
        });

        //Cart Observer
        cartAddObserver=cartLoader.getCartAddObserver();
        cartAddObserver.observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }
    private void updateUIForLogin() {
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Bought",Toast.LENGTH_SHORT).show();
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                cartLoader.addToCart(item.getId());
            }
        });
    }

    private void updateUIForGuest() {
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoLogin();
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoLogin();
            }
        });
    }
    private void gotoLogin()
    {
        ViewModelProviders.of(getActivity()).get(ViewHelper.class).loadView(new Login());
    }
}
