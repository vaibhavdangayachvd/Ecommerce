package com.example.ecommerce.cart;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ecommerce.helper.RequestHelper;
import com.example.ecommerce.helper.URLContract;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public final class CartLoader extends ViewModel {
    private Context context;
    private MutableLiveData<Boolean> hasAdded;

    public CartLoader(Context context) {
        this.context = context.getApplicationContext();
        hasAdded = new MutableLiveData<>();
        hasAdded.setValue(false);
    }

    public LiveData<Boolean> getCartAddObserver() {
        return hasAdded;
    }
    public void addToCart(final String productId) {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        final String username = preferences.getString("username", null);
        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, URLContract.ADD_PRODUCT_TO_CART, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hasAdded.setValue(parseAddResponse(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("productId", productId);
                return params;
            }
        };
        RequestHelper.getInstance(context).addToRequestQueue(stringRequest);
    }

    private boolean parseAddResponse(String response) {
        boolean flag;
        try {
            JSONObject obj = new JSONObject(response);
            if (obj.getString("status").equals("PRODUCT_ADDED")) {
                flag = true;
                Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show();
            }
            else {
                if(obj.getString("status").equals("PRODUCT_ALREADY_ADDED"))
                    Toast.makeText(context, "Already in cart", Toast.LENGTH_SHORT).show();
                    flag = false;
            }
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
}
