package com.example.ecommerce;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.ecommerce.helper.ViewHelper;
import com.example.ecommerce.product.ProductAdaptor;
import com.example.ecommerce.product.ProductItem;
import com.example.ecommerce.product.ProductLoader;


public class Product_List extends Fragment {
    private final int PRICE = 0;
    private final int NAME = 1;
    private final int ASCENDING = 2;
    private final int DESCENDING = 3;


    private ProductLoader productLoader;
    private String category;
    private ListView productList;
    private ProductAdaptor adaptor;
    private ProgressBar progressBar;
    private LiveData<Boolean> productObserver;
    private Button sort, filter;
    private int type = PRICE;
    private String type_message_ascending = "Low-High";
    private String type_message_descending = "High-Low";
    private int prevId = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_product_list, container, false);
        initComponents(view);
        setLoader();
        setObserver();

        setListScrollListener();
        setListItemSelectListener();
        setSortListener();
        setFilterListener();
        return view;
    }

    private void initComponents(View view) {
        Bundle bundle = getArguments();
        category = bundle.getString("category");
        sort = view.findViewById(R.id.sort);
        filter = view.findViewById(R.id.filter);
        productList = view.findViewById(R.id.productList);
        progressBar = view.findViewById(R.id.progressBar);
        RelativeLayout placeholder = view.findViewById(R.id.placeholder);
        Button home=view.findViewById(R.id.goto_home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewModelProviders.of(getActivity()).get(ViewHelper.class).loadView(new Home());
            }
        });
        productList.setEmptyView(placeholder);
    }

    private void setLoader() {
        productLoader = ViewModelProviders.of(getActivity(), new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new ProductLoader(getActivity());
            }
        }).get(ProductLoader.class);
        productLoader.setCategory(category);
        productLoader.loadProducts();
    }

    private void setObserver() {
        productObserver = productLoader.getProductObserver();
        productObserver.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean)
                    updateUI();
                else
                    progressBar.setVisibility(View.GONE);
            }
        });

    }


    private void updateUI() {
        progressBar.setVisibility(View.GONE);
        if (productList.getAdapter() == null) {
            adaptor = new ProductAdaptor(getActivity(), productLoader.getProducts(), category);
            productList.setAdapter(adaptor);
        } else
            adaptor.notifyDataSetChanged();
    }

    private void setListScrollListener() {
        productList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastId = firstVisibleItem + visibleItemCount;
                if (totalItemCount != 0 && lastId == totalItemCount && lastId != prevId) {
                    prevId = lastId;
                    progressBar.setVisibility(View.VISIBLE);
                    productLoader.loadProducts();
                }
            }
        });
    }

    private void setListItemSelectListener() {
        productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductItem item = productLoader.getProductAt(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("item", item);
                bundle.putString("category", category);
                Product_View product_view = new Product_View();
                product_view.setArguments(bundle);
                gotoProductView(product_view);
            }
        });
    }

    private void setFilterListener() {
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Sort");
                builder.setMessage("How you want to filter?");
                builder.setCancelable(true);
                builder.setPositiveButton("PRICE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        type = PRICE;
                        type_message_ascending = "Low-High";
                        type_message_descending = "High-Low";
                    }
                });
                builder.setNegativeButton("NAME", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        type = NAME;
                        type_message_ascending = "A-Z";
                        type_message_descending = "Z-A";
                    }
                });
                builder.show();
            }
        });
    }

    private void setSortListener() {
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Sort");
                builder.setMessage("How you want to sort?");
                builder.setCancelable(true);
                builder.setPositiveButton(type_message_ascending, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        productLoader.arrangeBy(type, ASCENDING);
                    }
                });
                builder.setNegativeButton(type_message_descending, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        productLoader.arrangeBy(type, DESCENDING);
                    }
                });
                builder.show();
            }
        });
    }

    private void gotoProductView(Fragment fragment) {
        ViewModelProviders.of(getActivity()).get(ViewHelper.class).loadView(fragment);
    }
}