package com.example.ecommerce.address;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
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

public final class AddressLoader extends ViewModel {
    private MutableLiveData<Boolean> hasChanges;
    private Context context;
    private ArrayList<AddressItem> addressList;
    private ArrayList<String> addressNameList;

    public AddressLoader(Context context) {
        this.context = context.getApplicationContext();
        hasChanges = new MutableLiveData<>();
        addressList = new ArrayList<>();
        addressNameList = new ArrayList<>();
        hasChanges.setValue(false);
    }

    public LiveData<Boolean> getAddressobserver() {
        return hasChanges;
    }

    public ArrayList<String> getLoadedAddressNames() {
        return addressNameList;
    }

    public void clearPreLoadedData() {
        addressList.clear();
        addressNameList.clear();
        hasChanges.setValue(false);
    }

    public void addNewAddress(Activity activity,final String address ) {
        final ProgressDialog loading=new ProgressDialog(activity);
        loading.setMessage("Adding address..");
        loading.setCancelable(true);
        loading.show();
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        final String username = preferences.getString("username", null);
        if (username != null) {
            StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, URLContract.ADD_ADDRESS, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (parseAddAddressResponse(response))
                    {
                        loading.hide();
                        getAddresses();
                        Toast.makeText(context,"Address Added",Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    loading.hide();
                    Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("username", username);
                    params.put("address", address);
                    return params;
                }
            };
            RequestHelper.getInstance(context).addToRequestQueue(stringRequest);
        }
    }

    private boolean parseAddAddressResponse(String response) {
        boolean flag;
        try {
            JSONObject obj = new JSONObject(response);
            String status = obj.getString("status");
            flag = status.equals("DATA_ADDED");
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public void getAddresses() {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String username = preferences.getString("username", null);
        if (username != null) {
            final String URL = URLContract.GET_ADDRESSES + "?username=" + username;
            StringRequest stringRequest = new StringRequest(StringRequest.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    hasChanges.setValue(parseGetAddressResponse(response));
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show();
                }
            });
            RequestHelper.getInstance(context).addToRequestQueue(stringRequest);
        }
    }

    private boolean parseGetAddressResponse(String response) {
        boolean flag = true;
        try {
            JSONObject obj = new JSONObject(response);
            String status = obj.getString("status");
            if (status.equals("DATA_FOUND")) {
                JSONArray id = obj.getJSONArray("id");
                JSONArray address = obj.getJSONArray("address");
                for (int i = addressList.size(); i < address.length(); ++i) {
                    addressList.add(new AddressItem(id.getString(i), address.getString(i)));
                    addressNameList.add(address.getString(i));
                }
            } else {
                addressNameList.clear();
                addressList.clear();
            }
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public void removeAddress(final int position) {
        final String id = addressList.get(position).getId();
        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, URLContract.REMOVE_ADDRESS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (parseRemoveAddressResponse(response)) {
                    addressNameList.remove(position);
                    addressList.remove(position);
                    hasChanges.setValue(true);
                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                }
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
                params.put("id", id);
                return params;
            }
        };
        RequestHelper.getInstance(context).addToRequestQueue(stringRequest);
    }

    private boolean parseRemoveAddressResponse(String response) {
        boolean flag;
        try {
            JSONObject obj = new JSONObject(response);
            String status = obj.getString("status");
            flag = status.equals("DATA_REMOVED");

        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
}
