package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ecommerce.helper.RequestHelper;
import com.example.ecommerce.helper.URLContract;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class AccountSettings extends Fragment {
    private Button save, upload;
    private EditText firstName, lastName, oldPassword, newPassword, email, mobile;
    private TextView username;
    private RadioGroup gender;
    private StringRequest request;
    private String currentUser;
    private SharedPreferences preferences;
    private ImageView image;
    private Bitmap bitmap;
    private ProgressDialog loading;
    private final int REQUEST_CODE = 101;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_account_settings,container,false);
        initViewComponents(view);
        loadExistingUserData();
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getDp = new Intent(Intent.ACTION_GET_CONTENT);
                getDp.setType("image/*");
                startActivityForResult(getDp, REQUEST_CODE);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFieldValues()) {
                    loading = new ProgressDialog(getActivity());
                    loading.setMessage("Updating profile...");
                    loading.setCancelable(true);
                    loading.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            request.cancel();
                        }
                    });
                    loading.show();
                    updateUser();
                }
            }
        });
        return view;
    }
    private boolean validateFieldValues() {
        if (firstName.getText().toString().matches("")){
            Toast.makeText(getActivity(), "Enter first name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (lastName.getText().toString().matches("")){
            Toast.makeText(getActivity(), "Enter last name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (gender.getCheckedRadioButtonId()==-1){
            Toast.makeText(getActivity(), "Select gender", Toast.LENGTH_SHORT).show();
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
        if(email.getText().toString().matches("")){
            Toast.makeText(getActivity(), "Enter Email ID", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!email.getText().toString().contains("@") && !email.getText().toString().contains(".")){
            Toast.makeText(getActivity(), "Invalid Email ID", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(username.getText().toString().matches("")){
            Toast.makeText(getActivity(), "Enter Username", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(oldPassword.getText().toString().matches("") && !newPassword.getText().toString().matches("")){
            Toast.makeText(getActivity(), "Enter old Password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(newPassword.getText().toString().matches("") && !oldPassword.getText().toString().matches("")){
            Toast.makeText(getActivity(), "Enter new Password", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }
    }

    private void updateUser() {
        request = new StringRequest(StringRequest.Method.POST, URLContract.UPDATE_USER_DATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.cancel();
                parseUpdateResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.cancel();
                dieWithServerError();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("username", currentUser);
                params.put("firstName", firstName.getText().toString());
                params.put("lastName", lastName.getText().toString());
                params.put("email", email.getText().toString());
                params.put("mobile", mobile.getText().toString());
                int res = R.id.male == gender.getCheckedRadioButtonId() ? 1 : 2;
                params.put("gender", String.valueOf(res));
                if (bitmap != null)
                    params.put("image", imageToString(bitmap));
                if (!TextUtils.isEmpty(oldPassword.getText().toString())) {
                    params.put("oldPassword", oldPassword.getText().toString());
                    params.put("newPassword", newPassword.getText().toString());
                }
                return params;
            }
        };
        RequestHelper.getInstance(getActivity()).addToRequestQueue(request);
    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == getActivity().RESULT_OK && data != null) {
            Uri result = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), result);
                image.setImageBitmap(bitmap);
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Image Load Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void initViewComponents(View v) {
        save = v.findViewById(R.id.save);
        upload = v.findViewById(R.id.upload);
        firstName = v.findViewById(R.id.firstName);
        lastName = v.findViewById(R.id.lastName);
        username = v.findViewById(R.id.username);
        oldPassword = v.findViewById(R.id.oldPassword);
        newPassword = v.findViewById(R.id.newPassword);
        email = v.findViewById(R.id.email);
        mobile = v.findViewById(R.id.mobile);
        gender = v.findViewById(R.id.gender);
        image = v.findViewById(R.id.image);
        preferences = v.getContext().getSharedPreferences("user", Context.MODE_PRIVATE);

        currentUser = preferences.getString("username", null);
    }

    private void loadExistingUserData() {
        username.setText(currentUser);
        loading = new ProgressDialog(getActivity());
        loading.setCancelable(false);
        loading.setMessage("Fetching Your Data...");
        loading.show();
        final String URL = URLContract.GET_USER_DATA_URL+"?username=" + currentUser;
        request = new StringRequest(StringRequest.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.cancel();
                parseLoadResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.cancel();
                dieWithServerError();
            }
        });
        RequestHelper.getInstance(getActivity()).addToRequestQueue(request);
    }

    private void parseUpdateResponse(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            String status = obj.getString("status");
            if (status.equals("UPDATE_FAILED"))
                dieWithServerError();
            else {
                Toast.makeText(getActivity(), status, Toast.LENGTH_SHORT).show();
                updateSharedPreferences(obj);
            }
        } catch (Exception e) {
            dieWithServerError();
        }
    }

    private void updateSharedPreferences(JSONObject obj) {
        SharedPreferences.Editor editor = preferences.edit();
        try {
            String name = obj.getString("firstName");
            editor.putString("name", name);
        } catch (Exception e){
            Log.i("VD","No New Name");
        }
        try {
            String password = obj.getString("password");
            editor.putString("password", password);
        } catch (Exception e) {
            Log.i("VD","No New Password");
        }
        try {
            int hasDp = obj.getInt("hasDp");
            editor.putBoolean("hasDp", hasDp != 0);
        } catch (Exception e) {
            Log.i("VD","No New Dp State");
        }
        editor.apply();
    }

    private void parseLoadResponse(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            String status = obj.getString("status");
            if (status.equals("USER_NOT_FOUND"))
                dieWithServerError();
            else {
                String first = obj.getString("firstName");
                String last = obj.getString("lastName");
                String em = obj.getString("email");
                String mob = obj.getString("mobile");
                int gen = obj.getInt("gender");
                int hasDp = obj.getInt("hasDp");

                updateUI(first, last, em, mob, gen, hasDp);
            }
        } catch (Exception e) {
            dieWithServerError();
        }
    }

    private void updateUI(String first, String last, String em, String mob, int gen, int hasDp) {
        firstName.setText(first);
        lastName.setText(last);
        mobile.setText(mob);
        email.setText(em);
        if (gen == 1)
            gender.check(R.id.male);
        else
            gender.check(R.id.female);
        if (hasDp == 1) {
            Picasso.get().load(URLContract.PROFILE_PIC_URL+"/" + currentUser + ".jpeg").placeholder(R.drawable.loading).into(image);
        }
    }

    private void dieWithServerError() {
        Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
        FragmentManager manager=getActivity().getSupportFragmentManager();
        FragmentTransaction tr = manager.beginTransaction();
        tr.replace(R.id.mainactivity_frame,new Home());
        tr.commit();
    }
}