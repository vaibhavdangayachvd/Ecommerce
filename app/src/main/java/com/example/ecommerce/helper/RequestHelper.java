package com.example.ecommerce.helper;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

public final class RequestHelper {
    private Context context;
    private Cache cache;
    private Network network;
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
        if(requestQueue==null) {
            cache=new DiskBasedCache(context.getCacheDir(),20 * 1024 * 1024);
            network=new BasicNetwork(new HurlStack());
            requestQueue= new RequestQueue(cache,network);
            requestQueue.start();
        }
        return requestQueue;
    }
    public <T>void addToRequestQueue(Request<T> req)
    {
        getRequestQueue().add(req);
    }
}
