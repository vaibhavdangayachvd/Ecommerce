package com.example.ecommerce.category;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ecommerce.helper.RequestHelper;
import com.example.ecommerce.helper.URLContract;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public final class CategoryLoader extends ViewModel {
    private ArrayList<CategoryItem> categoryList=new ArrayList<>();
    private MutableLiveData<Boolean> hasChanges=new MutableLiveData<>();
    private Context context;
    public CategoryLoader(Context context) {
        this.context = context.getApplicationContext();
        hasChanges.setValue(false);
    }
    public void loadCategories()
    {
        if(categoryList.isEmpty()) {
            StringRequest categoryRequest = new StringRequest(StringRequest.Method.GET, URLContract.GET_ALL_CATEGORIES_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    hasChanges.setValue(parseJSON(response));
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
                    hasChanges.setValue(false);
                }
            });
            RequestHelper.getInstance(context).addToRequestQueue(categoryRequest);
        }
    }
    public ArrayList<CategoryItem> getCategories()
    {
        return categoryList;
    }
    public LiveData<Boolean>getCategoryObserver()
    {
        return hasChanges;
    }
    private boolean parseJSON(String response)
    {
        boolean hasChanged=false;
        try
        {
            JSONObject obj=new JSONObject(response);
            String status=obj.getString("status");
            if(status.equals("DATA_FOUND"))
            {
                hasChanged=true;
                JSONArray names=obj.getJSONArray("name");
                JSONArray images=obj.getJSONArray("image");
                for(int i=0;i<images.length();++i)
                    categoryList.add(new CategoryItem(names.getString(i),images.getString(i)));
            }
        }
        catch (Exception e){
            hasChanged=false;
        }
        return hasChanged;
    }
}