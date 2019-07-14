package com.example.ecommerce.helper;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public final class UserLogin extends ViewModel {
    private MutableLiveData<Boolean> isLoggedIn;

    public UserLogin() {
        isLoggedIn = new MutableLiveData<>();
    }

    //Try Login From Server
    private void tryLogin(final Context context, final String username, final String password) {
        String LOGIN_URL = "https://trustbuy.ml/api/login.php";
        StringRequest request = new StringRequest(Request.Method.POST, LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String[]output = parseJSON(response);
                if (output[0].equals("ACCESS_GRANTED")) {
                    SharedPreferences preferences=context.getSharedPreferences("user",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("name",output[1]);
                    editor.apply();
                    isLoggedIn.setValue(true);
                }
                else {
                    logout(context);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isLoggedIn.setValue(false);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };
        RequestHelper.getInstance(context).addToRequestQueue(request);
    }
    public void logout(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        isLoggedIn.setValue(false);
    }
    //Hit database for consistency
    public void checkLoginStatus(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        if (preferences.getBoolean("isLoggedIn", false)) {
            validateFromServer(context);
        }
        else
            isLoggedIn.setValue(false);
    }

    //Provider Observer
    public LiveData<Boolean> getLoginObserver() {
        return isLoggedIn;
    }

    private void validateFromServer(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String username = preferences.getString("username", null);
        String password = preferences.getString("password", null);
        tryLogin(context, username, password);
    }

    private String[] parseJSON(String json) {
        String[] response = new String[2];
        try {
            JSONObject obj = new JSONObject(json);
            response[0] = obj.getString("status");
            response[1]=obj.getString("firstName");
        } catch (Exception e) {
        } finally {
            return response;
        }
    }
}
