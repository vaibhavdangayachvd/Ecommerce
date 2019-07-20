package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Cart_List extends Fragment {
    ListView cartList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_cart_list,container,false);
        initComponents(view);
        return view;
    }
    private void initComponents(View view)
    {
        cartList=view.findViewById(R.id.cartList);
        View empty=view.findViewById(R.id.layout_cart_empty);
        cartList.setEmptyView(empty);
    }
}
