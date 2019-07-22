package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ecommerce.helper.RequestHelper;
import com.example.ecommerce.helper.URLContract;
import com.example.ecommerce.helper.ViewHelper;
import com.example.ecommerce.product.ProductItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Search extends Fragment {
    private EditText search;
    private ListView hint;
    private ArrayList<ProductItem> hintList;
    private ArrayList<String> categoryList;
    private ArrayList<String> nameList;
    private ArrayAdapter<String> adapter;
    private TextView placeholder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_search, container, false);
        initComponents(view);
        setSearchListener();
        setHintClickListener();

        search.requestFocus();
        showKeyboard();
        hint.setAdapter(adapter);
        return view;
    }

    private void setSearchListener() {
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                makeStringRequest(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void resetHint() {
        hintList.clear();
        categoryList.clear();
        nameList.clear();
        adapter.notifyDataSetChanged();
    }

    private void makeStringRequest(String keyword) {
        if (TextUtils.isEmpty(keyword)) {
            resetHint();
            return;
        }
        final String URL = URLContract.SEARCH_PRODUCTS + "?keyword=" + keyword;
        StringRequest request = new StringRequest(StringRequest.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                resetHint();
                parseSearchResponse(response);
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Network error", Toast.LENGTH_SHORT).show();
            }
        });
        RequestHelper.getInstance(getActivity()).addToRequestQueue(request);
    }

    private void parseSearchResponse(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            String status = obj.getString("status");
            if (status.equals("DATA_FOUND")) {
                JSONArray id = obj.getJSONArray("id");
                JSONArray name = obj.getJSONArray("name");
                JSONArray price = obj.getJSONArray("price");
                JSONArray category = obj.getJSONArray("category");
                JSONArray description = obj.getJSONArray("description");
                JSONArray image = obj.getJSONArray("image");
                for (int i = 0; i < id.length(); ++i) {
                    ProductItem item = new ProductItem(id.getString(i), name.getString(i), description.getString(i), price.getString(i), image.getString(i));
                    hintList.add(item);
                    categoryList.add(category.getString(i));
                    nameList.add(name.getString(i));
                }
            }
        } catch (Exception e) {
        }
    }

    private void initComponents(View view) {
        search = view.findViewById(R.id.search);
        hint = view.findViewById(R.id.hint);
        hintList = new ArrayList<>();
        categoryList = new ArrayList<>();
        nameList = new ArrayList<>();
        placeholder = view.findViewById(R.id.placeholder);
        hint.setEmptyView(placeholder);
        adapter = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, nameList);

    }

    private void setHintClickListener() {
        hint.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductItem item = hintList.get(position);
                String category = categoryList.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("item", item);
                bundle.putString("category", category);
                Product_View product_view = new Product_View();
                product_view.setArguments(bundle);
                hideKeyboard();
                gotoProductView(product_view);
            }
        });
    }
    private void showKeyboard()
    {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getActivity().getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(getActivity());
        }
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
    }
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getActivity().getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(getActivity());
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    private void gotoProductView(Fragment fragment) {
        ViewModelProviders.of(getActivity()).get(ViewHelper.class).loadView(fragment);
    }
}
