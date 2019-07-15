package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ecommerce.helper.RequestHelper;
import com.example.ecommerce.helper.URLContract;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    private SharedPreferences preferences;
    private final String URL= URLContract.BASE_URL+"/api/login.php";
    private TextView username,password,message;
    StringRequest requestLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences=getSharedPreferences("user",MODE_PRIVATE);

        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        message=findViewById(R.id.show_message);

        Button login = findViewById(R.id.login);
        Button register = findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Register.class));
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog loading=new ProgressDialog(Login.this);
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
                    message.setText("Fill all fields");
                else
                {
                    requestLogin=new StringRequest(StringRequest.Method.POST, URL, new Response.Listener<String>() {
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
                                    finish();
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
                    RequestHelper.getInstance(Login.this).addToRequestQueue(requestLogin);
                }
            }
        });
    }
}