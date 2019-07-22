package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ecommerce.helper.RequestHelper;
import com.example.ecommerce.helper.URLContract;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends Fragment {
    private Button register;
    private EditText firstName,lastName,username,password,rePassword,email,mobile;
    private RadioGroup gender;
    private StringRequest registerRequest;
    private ProgressDialog loading;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_register,container,false);
        initViewComponents(view);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateFieldValues()) {
                    loading=new ProgressDialog(getActivity());
                    loading.setMessage("Adding you...");
                    loading.setCancelable(true);
                    loading.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            registerRequest.cancel();
                        }
                    });
                    loading.show();
                    registerNewUser();
                }
            }
        });
        return view;
    }

    private void registerNewUser()
    {
        final String first=firstName.getText().toString();
        final String last=lastName.getText().toString();
        final String user=username.getText().toString();
        final String pass=password.getText().toString();
        final String em=email.getText().toString();
        final String mob=mobile.getText().toString();
        int sel=gender.getCheckedRadioButtonId();
        final String gen=String.valueOf(sel==R.id.male?1:0);

        registerRequest=new StringRequest(StringRequest.Method.POST,URLContract.REGISTER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.cancel();
                handelResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.cancel();
                Toast.makeText(getActivity(),"Server Error",Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String,String> params=new HashMap<>();
                params.put("firstName",first);
                params.put("lastName",last);
                params.put("username",user);
                params.put("password",pass);
                params.put("email",em);
                params.put("mobile",mob);
                params.put("gender",gen);
                return params;
            }
        };
        RequestHelper.getInstance(getActivity()).addToRequestQueue(registerRequest);
    }
    private void handelResponse(String response)
    {
        try
        {
            JSONObject obj=new JSONObject(response);
            String status=obj.getString("status");
            if(status.equals("REGISTER_SUCCESS"))
            {
                Toast.makeText(getActivity(),"Registration Successful",Toast.LENGTH_SHORT).show();
                gotoLogin();
            }
            else
                Toast.makeText(getActivity(),status,Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            Toast.makeText(getActivity(),"Server Error",Toast.LENGTH_SHORT).show();
        }
    }
    private boolean validateFieldValues()
    {
        if(firstName.getText().toString().matches("")){
            Toast.makeText(getActivity(), "Enter First name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(lastName.getText().toString().matches("")){
            Toast.makeText(getActivity(), "Enter Last name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(username.getText().toString().matches("")){
            Toast.makeText(getActivity(), "Enter Username", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(email.getText().toString().matches("")){
            Toast.makeText(getActivity(), "Enter Email ID", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!email.getText().toString().contains("@") && !email.getText().toString().contains(".")){
            Toast.makeText(getActivity(), "Invalid Email ID", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(mobile.getText().toString().matches("")){
            Toast.makeText(getActivity(), "Enter Mobile Number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(mobile.getText().toString().contains("-") || mobile.getText().toString().contains("*") || mobile.getText().toString().contains("/") || mobile.getText().toString().contains(".")){
            Toast.makeText(getActivity(), "Invalid Mobile number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password.getText().toString().matches("")){
            Toast.makeText(getActivity(), "Enter Password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(rePassword.getText().toString().matches("")){
            Toast.makeText(getActivity(), "Enter Confirm Password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(gender.getCheckedRadioButtonId()==-1){
            Toast.makeText(getActivity(), "Select gender", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
           return true;
    }
    }
    private void initViewComponents(View v)
    {
        register=v.findViewById(R.id.register);
        firstName=v.findViewById(R.id.First_name);
        lastName=v.findViewById(R.id.Last_name);
        username=v.findViewById(R.id.username);
        password=v.findViewById(R.id.password);
        rePassword=v.findViewById(R.id.confirm_password);
        email=v.findViewById(R.id.email_id);
        mobile=v.findViewById(R.id.mobile_no);
        gender=v.findViewById(R.id.gender);
    }
    private void gotoLogin()
    {
        FragmentManager manager=getActivity().getSupportFragmentManager();
        manager.popBackStack();
    }
}