package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ecommerce.category.CategoryAdaptor;
import com.example.ecommerce.category.CategoryLoader;
import com.example.ecommerce.helper.ViewHelper;

public class Category extends Fragment {
    private CategoryLoader loader;
    private ListView categoryList;
    private ProgressBar progressBar;
    private CategoryAdaptor adaptor;
    private LiveData<Boolean> observer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_category, container, false);

        initComponents(view);
        setLoaders();
        setObservers();

        categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView t = view.findViewById(R.id.name);
                String category = t.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("category", category);
                Product_List product_list = new Product_List();
                product_list.setArguments(bundle);
                gotoProductList(product_list);
            }
        });
        loader.loadCategories();
        return view;
    }

    private void initComponents(View view) {
        categoryList = view.findViewById(R.id.categoryList);
        progressBar = view.findViewById(R.id.progress);
        categoryList.setEmptyView(progressBar);
    }

    private void setLoaders() {
        loader = ViewModelProviders.of(getActivity(), new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new CategoryLoader(getActivity());
            }
        }).get(CategoryLoader.class);
}

    private void setObservers() {
        observer = loader.getCategoryObserver();
        observer.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean)
                    updateUI();
            }
        });
    }

    private void updateUI() {
        if (adaptor == null)
            adaptor = new CategoryAdaptor(getActivity(), loader.getCategories());
        if(categoryList.getAdapter()==null)
            categoryList.setAdapter(adaptor);
        else
            adaptor.notifyDataSetChanged();
    }

    private void gotoProductList(Fragment fragment) {
        ViewModelProviders.of(getActivity()).get(ViewHelper.class).loadView(fragment);
    }
}
