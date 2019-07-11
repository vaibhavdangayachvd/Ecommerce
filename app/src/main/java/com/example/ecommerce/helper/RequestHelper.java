package com.example.ecommerce.helper;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public final class RequestHelper {
    private Context context;
    private static RequestHelper instance;
    private RequestQueue requestQueue;
    private RequestHelper(Context context)
    {
        this.context=context;
        requestQueue=getRequestQueue();
    }
    public static synchronized RequestHelper getInstance(Context context) {
        if (instance == null) {
            instance = new RequestHelper(context);
        }
        return instance;
    }
    public RequestQueue getRequestQueue()
    {
        if(requestQueue==null)
            requestQueue= Volley.newRequestQueue(context.getApplicationContext());
        return requestQueue;
    }
    public <T>void addToRequestQueue(Request<T> req)
    {
        getRequestQueue().add(req);
    }
}
