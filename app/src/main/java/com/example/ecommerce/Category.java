package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.Toast;

import com.example.ecommerce.category.CategoryAdaptor;
import com.example.ecommerce.category.CategoryLoader;

public class Category extends Fragment {
    private CategoryLoader loader;
    private ListView categoryList;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_category,container,false);
        categoryList=view.findViewById(R.id.categoryList);
        progressBar=view.findViewById(R.id.progress);
        loader= ViewModelProviders.of(getActivity(), new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T)new CategoryLoader(getActivity());
            }
        }).get(CategoryLoader.class);

        LiveData<Boolean> observer=loader.getCategoryObserver();
        observer.observe(Category.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean)
                    updateUI();
            }
        });

        categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView t=view.findViewById(R.id.name);
                String category=t.getText().toString();
                Toast.makeText(getActivity(),category,Toast.LENGTH_SHORT).show();
            }
        });
        loader.loadCategories();
        return view;
    }
    private void updateUI()
    {
        CategoryAdaptor adaptor = new CategoryAdaptor(getActivity(), loader.getCategories());
        categoryList.setAdapter(adaptor);
        progressBar.setVisibility(View.GONE);
    }
}
