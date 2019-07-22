package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ecommerce.helper.ViewHelper;
import com.example.ecommerce.home.CategoryAdapter;
import com.example.ecommerce.home.Favourite;
import com.example.ecommerce.home.Favourite2;
import com.example.ecommerce.home.ImageAdapter;
import com.example.ecommerce.home.SmartPhoneAdapter;

import java.util.ArrayList;

public class Home extends Fragment {
    private int image1[] = {R.drawable.pocof1, R.drawable.appleiphone8plus, R.drawable.vivoz1pro, R.drawable.oppoa5};
    private String title1[] = {"POCO F1", "Iphone 8 Plus", "Vivo Z1 Pro", "Oppo A5"};
    private String price1[] = {"Rs. 28,999", "Rs. 61,499", "Rs. 17,990", "Rs. 11,990"};
    private String category1[] = {"Mobiles", "Cloths", "Furniture"};
    private SmartPhoneAdapter bAdapter1;
    private CategoryAdapter bAdapter2;
    RecyclerView best, category;
    private ArrayList<Favourite> favouriteModelClasses1;
    private ArrayList<Favourite2> favouriteModelClasses2;
    TextView search;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_home, null);
        initComponents(v);

        //search block
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewModelProviders.of(getActivity()).get(ViewHelper.class).loadView(new Search());
            }
        });


        //best smart phone block
        setXML(best);
        favouriteModelClasses1 = new ArrayList<>();
        for (int i = 0; i < image1.length; i++) {
            Favourite beanClassForRecyclerView_contacts = new Favourite(image1[i], title1[i], price1[i]);
            favouriteModelClasses1.add(beanClassForRecyclerView_contacts);
        }
        bAdapter1 = new SmartPhoneAdapter(getActivity(), favouriteModelClasses1);
        best.setAdapter(bAdapter1);

        //this block is for image slider
        ViewPager mViewPager = v.findViewById(R.id.preview);
        ImageAdapter adapterView = new ImageAdapter(getActivity());
        mViewPager.setAdapter(adapterView);

        //category block
        setXML(category);
        favouriteModelClasses2 = new ArrayList<>();
        for (int i = 0; i < category1.length; i++) {
            Favourite2 beanClassForRecyclerView_contacts1 = new Favourite2(category1[i]);
            favouriteModelClasses2.add(beanClassForRecyclerView_contacts1);
        }
        bAdapter2 = new CategoryAdapter(getActivity(), favouriteModelClasses2);
        category.setAdapter(bAdapter2);

        //returning view
        return v;
    }

    private void setXML(RecyclerView r) {
        r.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        r.setItemAnimator(new DefaultItemAnimator());
    }

    private void initComponents(View v) {
        best = v.findViewById(R.id.smartphone_recyclerview);
        category = v.findViewById(R.id.category);
        search = v.findViewById(R.id.search);
    }
}