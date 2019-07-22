package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

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
import com.example.ecommerce.helper.ViewHelper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends Fragment {
    private SharedPreferences preferences;
    private TextView username, password, message;
    private Button login;
    private Button register;
    private StringRequest requestLogin;
    private ProgressDialog loading;
    private String user, pass;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login, container, false);
        initComponents(view);
        setRegisterListener();
        setLoginListener();
        return view;
    }

    private void setLoginListener() {
        login.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                loading.show();
                user = username.getText().toString();
                pass = password.getText().toString();
                if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pass)) {
                    message.setText("Fill all fields");
                    loading.hide();
                } else
                    makeLoginRequest();
            }
        });
    }

    private void makeLoginRequest() {
        requestLogin = new StringRequest(StringRequest.Method.POST, URLContract.LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseLoginResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.hide();
                message.setText("Network Error");
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                HashMap<String, String> mParams = new HashMap<>();
                mParams.put("username", user);
                mParams.put("password", pass);
                return mParams;
            }
        };
        RequestHelper.getInstance(getActivity()).addToRequestQueue(requestLogin);
    }

    private void parseLoginResponse(String response) {
        try {
            loading.hide();
            JSONObject obj = new JSONObject(response);
            String status = obj.getString("status");
            if (status.equals("ACCESS_GRANTED")) {
                String name = obj.getString("firstName");
                String pass = obj.getString("passwordHash");
                int hasDp = obj.getInt("hasDp");
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("username", user);
                editor.putString("password", pass);
                editor.putString("name", name);
                editor.putBoolean("hasDp", hasDp != 0);
                editor.putBoolean("isLoggedIn", true);
                editor.apply();
                gotoBack();
            } else
                message.setText("Login Failed");
        } catch (Exception e) {
            message.setText("Server Response Error");
        }
    }

    private void setRegisterListener() {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoRegister();
            }
        });
    }

    private void initComponents(View view) {
        preferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);
        message = view.findViewById(R.id.show_message);

        login = view.findViewById(R.id.login);
        register = view.findViewById(R.id.register);

        loading = new ProgressDialog(getActivity());
        loading.setCancelable(true);
        loading.setMessage("Logging you in...");
        loading.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (requestLogin != null)
                    requestLogin.cancel();
            }
        });
    }

    private void gotoBack() {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.popBackStack();
    }

    private void gotoRegister() {
        ViewModelProviders.of(getActivity()).get(ViewHelper.class).loadView(new Register());
    }
}