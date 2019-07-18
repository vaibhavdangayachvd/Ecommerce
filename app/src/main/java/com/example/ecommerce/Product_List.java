package com.example.ecommerce;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.ecommerce.product.ProductAdaptor;
import com.example.ecommerce.product.ProductLoader;


public class Product_List extends Fragment {
    private final int PRICE=0;
    private final int NAME=1;
    private final int ASCENDING=2;
    private final int DESCENDING=3;

    private ProductLoader loader;
    private String category;
    private ListView productList;
    private ProductAdaptor adaptor;
    private ProgressBar progressBar;
    private LiveData<Boolean> observer;
    private Button sort,filter;
    private int type=NAME;
    private int prevId = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_product_list, container, false);
        initComponents(view);
        setLoader();
        setObserver();
        setListScrollListener();
        setSortListener();
        setFilterListener();
        return view;
    }
    private void setFilterListener()
    {
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                builder.setTitle("Sort");
                builder.setMessage("How you want to filter?");
                builder.setCancelable(true);
                builder.setPositiveButton("PRICE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        type=PRICE;
                    }
                });
                builder.setNegativeButton("NAME", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        type=NAME;
                    }
                });
                builder.show();
            }
        });
    }
    private void setSortListener()
    {
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                builder.setTitle("Sort");
                builder.setMessage("How you want to sort?");
                builder.setCancelable(true);
                if(type==PRICE) {
                    builder.setPositiveButton("Low-High", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getActivity(), "Low to High", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("High-Low", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getActivity(), "High To Low", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    builder.setPositiveButton("A-Z", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getActivity(), "A to Z", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("Z-A", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getActivity(), "Z to A", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                builder.show();
            }
        });
    }
    private void setObserver() {
        observer = loader.getProductObserver();
        observer.observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean)
                    updateUI();
                else
                    progressBar.setVisibility(View.GONE);
            }
        });
    }
    private void setListScrollListener()
    {
        productList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastId = firstVisibleItem + visibleItemCount;
                if (lastId == totalItemCount && lastId != prevId) {
                    prevId = lastId;
                    progressBar.setVisibility(View.VISIBLE);
                    loader.loadProducts();
                }
            }
        });
    }
    private void setLoader() {
        loader = ViewModelProviders.of(getActivity(), new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new ProductLoader(getActivity());
            }
        }).get(ProductLoader.class);
        loader.setCategory(category);
    }

    private void initComponents(View view) {
        Bundle bundle = getArguments();
        category = bundle.getString("category");
        sort=view.findViewById(R.id.sort);
        filter=view.findViewById(R.id.filter);
        productList = view.findViewById(R.id.productList);
        progressBar = view.findViewById(R.id.progressBar);

        RelativeLayout placeholder = view.findViewById(R.id.placeholder);
        productList.setEmptyView(placeholder);
    }

    private void updateUI() {
        progressBar.setVisibility(View.GONE);
        if (productList.getAdapter() == null) {
            adaptor = new ProductAdaptor(getActivity(), loader.getProducts(), category);
            productList.setAdapter(adaptor);
        } else
            adaptor.notifyDataSetChanged();
    }
}