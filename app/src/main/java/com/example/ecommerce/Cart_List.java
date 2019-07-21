package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
import android.widget.ListView;
import android.widget.TextView;

import com.example.ecommerce.cart.CartAdaptor;
import com.example.ecommerce.cart.CartLoader;
import com.example.ecommerce.helper.UserLogin;
import com.example.ecommerce.helper.ViewHelper;

public class Cart_List extends Fragment {
    private ListView cartList;
    TextView amount;
    LiveData<Boolean> cartLoadObserver;
    LiveData<Boolean> loginObserver;
    CartLoader cartLoader;
    UserLogin userLoader;
    View footer;
    CartAdaptor cartAdaptor;
    Button shopNow;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_cart_list, container, false);
        initComponents(view);
        setLoaders();
        setObservers();
        cartLoader.loadCart();
        shopNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoCategories();
            }
        });
        return view;
    }
    private void gotoCategories()
    {
        ViewModelProviders.of(getActivity()).get(ViewHelper.class).loadView(new Category());
    }
    private void initComponents(View view) {
        cartList = view.findViewById(R.id.cartList);
        footer=view.findViewById(R.id.footer);
        View empty = view.findViewById(R.id.layout_cart_empty);
        cartList.setEmptyView(empty);
        amount=view.findViewById(R.id.amount);
        shopNow=view.findViewById(R.id.shop_now);
    }
    private void setLoaders() {
        cartLoader = ViewModelProviders.of(getActivity(), new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new CartLoader(getActivity());
            }
        }).get(CartLoader.class);

        userLoader= ViewModelProviders.of(getActivity(), new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new UserLogin(getActivity());
            }
        }).get(UserLogin.class);
    }

    private void setObservers() {

        loginObserver=userLoader.getLoginObserver();
        loginObserver.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(!aBoolean)
                {
                    cartLoader.clearPreLoadedData();
                    FragmentManager manager=getActivity().getSupportFragmentManager();
                    manager.popBackStack();
                }
            }
        });

        cartLoadObserver = cartLoader.getCartLoadObserver();
        cartLoadObserver.observe(getViewLifecycleOwner(),new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean)
                    updateUI();
            }
        });
    }
    private void updateUI()
    {

        if(cartAdaptor==null)
            cartAdaptor=new CartAdaptor(getActivity(),cartLoader);
        if(cartList.getAdapter()==null)
            cartList.setAdapter(cartAdaptor);
        else
            cartAdaptor.notifyDataSetChanged();
        int total=cartLoader.getCartTotol();
        if(total!=0)
        {
            footer.setVisibility(View.VISIBLE);
            amount.setText("Rs."+cartLoader.getCartTotol()+"/-");
        }
        else
            footer.setVisibility(View.INVISIBLE);
    }
}
