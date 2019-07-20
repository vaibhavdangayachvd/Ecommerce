package com.example.ecommerce;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

public class Home extends Fragment {
    GridView gridView;
    private int image1[] = {R.drawable.d_user,R.drawable.d_user,R.drawable.d_user,R.drawable.d_user,R.drawable.d_user,R.drawable.d_user};
    private String title1[] = {"Galaxy M10","I Phone X","Vivo V15","Google Pixel 3","Samsung S7","I Phone 7"};
    private String price1[] = {"Rs 8,990","Rs 69,000","Rs 67,418","Rs 40,000","Rs 43,000","Rs 51,000"};
    private SmartPhoneAdapter bAdapter1;
    RecyclerView category,best;
    private ArrayList<Favourite> favouriteModelClasses1;
    ArrayList<Integer> product_images;
    ArrayAdapter<String> product_name;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.activity_home,null);
        /*gridView=v.findViewById(R.id.popular_grid);
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
        gridView.setAdapter(obj);*/
        /*category=v.findViewById(R.id.category);
        category.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        category.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.HORIZONTAL));
        product_name.add("Mobiles");
        product_name.add("Clothes");
        product_name.add("Furniture");*/
        best=v.findViewById(R.id.smartphone_recyclerview);
        best.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        best.setItemAnimator(new DefaultItemAnimator());
        favouriteModelClasses1 = new ArrayList<>();

        for (int i = 0; i < image1.length; i++) {
            Favourite beanClassForRecyclerView_contacts = new Favourite(image1[i],title1[i],price1[i]);
            favouriteModelClasses1.add(beanClassForRecyclerView_contacts);
        }
        bAdapter1 = new SmartPhoneAdapter(getActivity(),favouriteModelClasses1);
        best.setAdapter(bAdapter1);
        ViewPager mViewPager = v.findViewById(R.id.preview);
        ImageAdapter adapterView = new ImageAdapter(getActivity());
        mViewPager.setAdapter(adapterView);
        return v;
    }
}