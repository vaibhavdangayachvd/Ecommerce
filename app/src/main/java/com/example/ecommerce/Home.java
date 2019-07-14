package com.example.ecommerce;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

public class Home extends Fragment {
    GridView gridView;
    ArrayList<Integer> product_images;
    ArrayList<String>product_name;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.activity_home,null);
        gridView=v.findViewById(R.id.popular_grid);
        product_images=new ArrayList<>();
        product_name=new ArrayList<>();
        product_name.add("name and price");
        product_images.add(R.drawable.d_user);
        product_name.add("name and price");
        product_images.add(R.drawable.d_user);
        product_name.add("name and price");
        product_images.add(R.drawable.d_user);
        product_name.add("name and price");
        product_images.add(R.drawable.d_user);
        product_name.add("name and price");
        GetImageLoader obj=new GetImageLoader(getActivity(),product_images,product_name);
        gridView.setAdapter(obj);
        return v;
    }
}