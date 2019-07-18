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

    private MutableLiveData<Boolean>isLoggedIn;
    private Context context;
    private SharedPreferences preferences;
    public UserLogin(Context context)
    {
        this.context=context.getApplicationContext();
        preferences=this.context.getSharedPreferences("user",Context.MODE_PRIVATE);
        isLoggedIn=new MutableLiveData<>();
    }
    //Try Login From Server
    private void Login(final String username, final String password) {
        String LOGIN_URL = URLContract.LOGIN_URL+"?forSessionCheck=true";
        StringRequest request = new StringRequest(Request.Method.POST, LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String[]output = parseJSON(response);
                if (output[0].equals("ACCESS_GRANTED")) {
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("name",output[1]);
                    editor.putBoolean("hasDp",Integer.parseInt(output[2])!=0);
                    editor.apply();
                    isLoggedIn.setValue(true);
                }
                else {
                    logout();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                logout();
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
    public LiveData<Boolean> getLoginObserver()
    {
        return isLoggedIn;
    }
    public void logout()
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        isLoggedIn.setValue(false);
    }
    //Hit database for consistency
    public void checkLoginStatus() {
        if (preferences.getBoolean("isLoggedIn", false))
            validateFromServer();
        else
            isLoggedIn.setValue(false);
    }
    private void validateFromServer() {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String username = preferences.getString("username", null);
        String password = preferences.getString("password", null);
        Login(username, password);
    }

    private String[] parseJSON(String json) {
        String[] response = new String[3];
        try {
            JSONObject obj = new JSONObject(json);
            response[0] = obj.getString("status");
            response[1]=obj.getString("firstName");
            response[2]=String.valueOf(obj.getInt("hasDp"));
        } catch (Exception e) {
            response[0]="ACCESS_DENIED";
        }
        return response;
    }
}
