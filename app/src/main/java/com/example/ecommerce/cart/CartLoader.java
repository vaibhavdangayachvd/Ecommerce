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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class CartLoader extends ViewModel {
    private Context context;
    private MutableLiveData<Boolean> hasAdded;
    private MutableLiveData<Boolean> hasLoaded;
    private ArrayList<CartItem> cartList=new ArrayList<>();

    public CartLoader(Context context) {
        this.context = context.getApplicationContext();
        hasAdded = new MutableLiveData<>();
        hasLoaded=new MutableLiveData<>();
        hasAdded.setValue(false);
        hasLoaded.setValue(false);
    }
    public void clearPreLoadedData()
    {
        cartList.clear();
        hasLoaded.setValue(true);
    }
    public ArrayList<CartItem> getCartItems(){return cartList;}
    public LiveData<Boolean> getCartAddObserver() {
        return hasAdded;
    }
    public LiveData<Boolean> getCartLoadObserver(){return hasLoaded;}

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
                hasLoaded.setValue(false);
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

    public void loadCart()
    {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        final String username = preferences.getString("username", null);
        final String URL=URLContract.GET_PRODUCTS_FROM_CART+"?username="+username;
        StringRequest stringRequest=new StringRequest(StringRequest.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hasLoaded.setValue(parseLoadResponse(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Network error",Toast.LENGTH_SHORT).show();
            }
        });
        RequestHelper.getInstance(context).addToRequestQueue(stringRequest);
    }

    private boolean parseLoadResponse(String response) {
        boolean flag;
        try {
            JSONObject obj = new JSONObject(response);
            if (obj.getString("status").equals("DATA_FOUND")) {
                flag = true;
                JSONArray category=obj.getJSONArray("category");
                JSONArray id=obj.getJSONArray("productId");
                JSONArray name=obj.getJSONArray("name");
                JSONArray price=obj.getJSONArray("price");
                JSONArray quantity=obj.getJSONArray("quantity");
                JSONArray image=obj.getJSONArray("image");
                for(int i=cartList.size();i<name.length();++i)
                {
                    CartItem item=new CartItem(id.getString(i),category.getString(i),name.getString(i),price.getString(i),quantity.getString(i),image.getString(i));
                    cartList.add(item);
                }
            }
            else
                flag = false;
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
    public void removeFromCart(final String productId)
    {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        final String username = preferences.getString("username", null);
        StringRequest stringRequest=new StringRequest(StringRequest.Method.POST, URLContract.REMOVE_PRODUCT_FROM_CART, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(parseRemoveResponse(response)) {
                    int index = findItemWithId(productId);
                    cartList.remove(index);
                    hasLoaded.setValue(true);
                }
                else
                    Toast.makeText(context,"Already Removed",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Network Error",Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String>params=new HashMap<>();
                params.put("productId",productId);
                params.put("username",username);
                return params;
            }
        };
        RequestHelper.getInstance(context).addToRequestQueue(stringRequest);
    }
    private int findItemWithId(String id)
    {
        for(int i=0;i<cartList.size();++i)
            if(cartList.get(i).getId().equals(id))
                return i;
        return -1;
    }
    private boolean parseRemoveResponse(String response)
    {
        boolean flag;
        try {
            JSONObject obj = new JSONObject(response);
            if (obj.getString("status").equals("PRODUCT_REMOVED")) {
                flag = true;
            }
            else
                flag=false;
        }
        catch (Exception e)
        {
            flag=false;
        }
        return flag;
    }
    public int getCartTotol()
    {
        int sum=0;
        for(int i=0;i<cartList.size();++i)
        {
            CartItem item=cartList.get(i);
            sum+=Integer.parseInt(item.getPrice())*Integer.parseInt(item.getQuantity());
        }
        return sum;
    }

    public void updateCart(final String productId, final String quantity)
    {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        final String username = preferences.getString("username", null);
        StringRequest stringRequest=new StringRequest(StringRequest.Method.POST, URLContract.UPDATE_PRODUCT_IN_CART, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (parseUpdateResponse(response))
                {
                    updateItemWithId(productId, Integer.parseInt(quantity));
                    hasLoaded.setValue(true);
                }
                else
                    Toast.makeText(context,"You can remove product",Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Network Error",Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("username",username);
                params.put("productId",productId);
                params.put("quantity",quantity);
                return params;
            }
        };
        RequestHelper.getInstance(context).addToRequestQueue(stringRequest);
    }
    private void updateItemWithId(String id,int quantity)
    {
        for(int i=0;i<cartList.size();++i)
            if(cartList.get(i).getId().equals(id))
            {
                cartList.get(i).updateQuantity(quantity);
                return;
            }
    }
    private boolean parseUpdateResponse(String response)
    {
        boolean flag;
        try
        {
            JSONObject obj = new JSONObject(response);
            if (obj.getString("status").equals("UPDATE_SUCCESS"))
                flag = true;
            else
                flag=false;
        }
        catch (Exception e)
        {
            flag=false;
        }
        return flag;
    }
}
