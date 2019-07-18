package com.example.ecommerce.product;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ecommerce.helper.RequestHelper;
import com.example.ecommerce.helper.URLContract;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public final class ProductLoader extends ViewModel{

    private final int OFFSET = 5;
    private int start = 0;
    private String category;

    private ArrayList<ProductItem> productList = new ArrayList<>();
    private MutableLiveData<Boolean> hasChanges = new MutableLiveData<>();
    private Context context;

    public ProductLoader(Context context) {
        this.context = context.getApplicationContext();
        hasChanges.setValue(false);
    }
    public void setCategory(String category) {
        this.category = category;
        productList.clear();
        start=0;
    }

    public ArrayList<ProductItem> getProducts()
    {
        return productList;
    }
    public LiveData<Boolean> getProductObserver()
    {
        return hasChanges;
    }
    public void loadProducts() {
        final String URL = URLContract.GET_PRODUCTS_BY_CATEGORY_URL +"?category="+category+"&start="+start+"&offset="+OFFSET;
        StringRequest productRequest = new StringRequest(StringRequest.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hasChanges.setValue(parseJSON(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
                hasChanges.setValue(false);
            }
        });
        RequestHelper.getInstance(context).addToRequestQueue(productRequest);
    }
    private boolean parseJSON(String response)
    {
        boolean hasChanged=false;
        try {
            JSONObject obj = new JSONObject(response);
            String status = obj.getString("status");
            if(status.equals("DATA_FOUND"))
            {
                hasChanged=true;
                JSONArray names=obj.getJSONArray("name");
                JSONArray images=obj.getJSONArray("image");
                JSONArray prices=obj.getJSONArray("price");
                JSONArray descriptions=obj.getJSONArray("description");
                JSONArray ids=obj.getJSONArray("id");
                for(int i=0;i<images.length();++i)
                    productList.add(new ProductItem(ids.getString(i),names.getString(i),descriptions.getString(i),prices.getString(i),images.getString(i)));
                start+=images.length();
            }
        }
        catch (Exception e){
            hasChanged=false;
        }
        return hasChanged;
    }
}
