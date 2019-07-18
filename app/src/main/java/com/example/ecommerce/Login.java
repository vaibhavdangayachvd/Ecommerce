package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ecommerce.helper.RequestHelper;
import com.example.ecommerce.helper.URLContract;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends Fragment {
    private SharedPreferences preferences;
    private TextView username,password,message;
    StringRequest requestLogin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_login,container,false);
        preferences=getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

        username=view.findViewById(R.id.username);
        password=view.findViewById(R.id.password);
        message=view.findViewById(R.id.show_message);

        Button login = view.findViewById(R.id.login);
        Button register = view.findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoRegister();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                final ProgressDialog loading=new ProgressDialog(getActivity());
                loading.setCancelable(true);
                loading.setMessage("Logging you in...");
                loading.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        requestLogin.cancel();
                    }
                });
                loading.show();
                final String user=username.getText().toString();
                final String pass=password.getText().toString();
                if(TextUtils.isEmpty(user) || TextUtils.isEmpty(pass))
                {
                    message.setText("Fill all fields");
                    loading.cancel();
                }
                else
                {
                    requestLogin=new StringRequest(StringRequest.Method.POST, URLContract.LOGIN_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try
                            {
                                loading.cancel();
                                JSONObject obj = new JSONObject(response);
                                String status = obj.getString("status");
                                if(status.equals("ACCESS_GRANTED")) {
                                    String name= obj.getString("firstName");
                                    String pass=obj.getString("passwordHash");
                                    int hasDp=obj.getInt("hasDp");
                                    SharedPreferences.Editor editor=preferences.edit();
                                    editor.putString("username",user);
                                    editor.putString("password",pass);
                                    editor.putString("name",name);
                                    editor.putBoolean("hasDp", hasDp != 0);
                                    editor.putBoolean("isLoggedIn",true);
                                    editor.apply();
                                    gotoHome();
                                }
                                else
                                    message.setText("Login Failed");
                            }
                            catch (Exception e) {
                                message.setText("Server Response Error");
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            loading.cancel();
                            message.setText("Network Error");
                        }
                    })
                    {
                        @Override
                        public Map<String, String> getParams() {
                            HashMap<String,String>mParams=new HashMap<>();
                            mParams.put("username",user);
                            mParams.put("password",pass);
                            return mParams;
                        }
                    };
                    RequestHelper.getInstance(getActivity()).addToRequestQueue(requestLogin);
                }
            }
        });
        return view;
    }
    private void gotoHome()
    {
        FragmentManager manager=getActivity().getSupportFragmentManager();
        FragmentTransaction tr = manager.beginTransaction();
        tr.replace(R.id.mainactivity_frame,new Home());
        tr.commit();
    }
    private void gotoRegister()
    {
        FragmentManager manager=getActivity().getSupportFragmentManager();
        FragmentTransaction tr = manager.beginTransaction();
        tr.replace(R.id.mainactivity_frame,new Register());
        tr.commit();
    }
}