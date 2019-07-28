package com.example.ecommerce;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ecommerce.address.AddressItem;
import com.example.ecommerce.cart.CartLoader;
import com.example.ecommerce.checkout.CheckoutAdaptor;
import com.example.ecommerce.helper.RequestHelper;
import com.example.ecommerce.helper.URLContract;
import com.example.ecommerce.helper.ViewHelper;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

public final class Checkout extends Fragment {
    private ListView finalProducts;
    private Button pay;
    private TextView amount, address;
    private CartLoader cartLoader;
    private LiveData<Boolean> cartObserver;
    private ProgressBar progress;
    private AddressItem addressItem;
    private CheckoutAdaptor adaptor;
    private String checksumHash;
    private String username;
    private String orderid;
    private final String mid="aACUqs69617563936504";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.checkout, container, false);
        initComponents(view);
        setLoaders();
        setObservers();
        cartLoader.loadCart();
        setAddress();
        setPayListener();
        return view;
    }

    private void setPayListener() {
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateOrderId();
            }
        });
    }

    private void generateOrderId() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, URLContract.GENERATE_ORDERID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.cancel();
                if (parseGenerateResponse(response))
                    generateChecksum();
                else
                    Toast.makeText(getActivity(), "Technical Error", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();
                Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("amount", String.valueOf(cartLoader.getCartTotol()));
                params.put("address",addressItem.getId());
                return params;
            }
        };
        RequestHelper.getInstance(getActivity()).addToRequestQueue(request);
    }
    private void createOrder()
    {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        StringRequest request=new StringRequest(StringRequest.Method.POST, URLContract.CREATE_ORDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.cancel();
                if(response.equals("FAILED"))
                    Toast.makeText(getContext(),"Technical Error - Contact Support", Toast.LENGTH_LONG).show();
                else
                {
                    cartLoader.emptyCart();
                    Toast.makeText(getContext(),"Order Placed", Toast.LENGTH_SHORT).show();
                    ViewModelProviders.of(getActivity()).get(ViewHelper.class).loadView(new Home());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();
                Toast.makeText(getContext(),"Network Error - Contact Support", Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                params.put("username",username);
                params.put("orderid",orderid);
                return params;
            }
        };
        RequestHelper.getInstance(getActivity()).addToRequestQueue(request);
    }
    private void startPaytmTransaction()
    {
        PaytmPGService service=PaytmPGService.getStagingService();
        HashMap<String,String>params=new HashMap<>();
        params.put("MID",mid);
        params.put("ORDER_ID",orderid);
        params.put("CUST_ID",username);
        params.put("TXN_AMOUNT",String.valueOf(cartLoader.getCartTotol()));
        params.put("CHANNEL_ID","WAP");
        params.put("WEBSITE","WEBSTAGING");
        params.put("CALLBACK_URL",URLContract.PAYTM_VERIFY_CALLBACK_URL);
        params.put("CHECKSUMHASH",checksumHash);
        params.put("INDUSTRY_TYPE_ID","Retail");

        PaytmOrder order=new PaytmOrder(params);
        service.initialize(order,null);
        service.startPaymentTransaction(getActivity(), true, true, new PaytmPaymentTransactionCallback() {
            @Override
            public void onTransactionResponse(Bundle inResponse) {

                if(inResponse.toString().contains("TXN_SUCCESS"))
                    createOrder();
                else
                    Toast.makeText(getActivity(),"Payment Failed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void networkNotAvailable() {
                Toast.makeText(getActivity(),"Network Error",Toast.LENGTH_SHORT).show();
                ViewModelProviders.of(getActivity()).get(ViewHelper.class).loadView(new Home());
            }

            @Override
            public void clientAuthenticationFailed(String inErrorMessage) {
                Toast.makeText(getActivity(),"Login Failed",Toast.LENGTH_SHORT).show();
                ViewModelProviders.of(getActivity()).get(ViewHelper.class).loadView(new Home());
            }

            @Override
            public void someUIErrorOccurred(String inErrorMessage) {
                Toast.makeText(getActivity(),"UI Error",Toast.LENGTH_SHORT).show();
                ViewModelProviders.of(getActivity()).get(ViewHelper.class).loadView(new Home());
            }

            @Override
            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                Toast.makeText(getActivity(),"Error in loading page",Toast.LENGTH_SHORT).show();
                ViewModelProviders.of(getActivity()).get(ViewHelper.class).loadView(new Home());
            }

            @Override
            public void onBackPressedCancelTransaction() {
                Toast.makeText(getActivity(),"Cancelled",Toast.LENGTH_SHORT).show();
                ViewModelProviders.of(getActivity()).get(ViewHelper.class).loadView(new Home());
            }

            @Override
            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                Toast.makeText(getActivity(),"Cancelled",Toast.LENGTH_SHORT).show();
                ViewModelProviders.of(getActivity()).get(ViewHelper.class).loadView(new Home());
            }
        });
    }
    private void generateChecksum()
    {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        StringRequest request=new StringRequest(StringRequest.Method.POST, URLContract.PAYTM_GENERATE_CHECKSUM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.cancel();
                if(parseChecksumResponse(response))
                    startPaytmTransaction();
                else
                    Toast.makeText(getActivity(),"Technical error",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();
                Toast.makeText(getActivity(),"Network Error",Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String>params=new HashMap<>();
                params.put("MID",mid);
                params.put("ORDER_ID",orderid);
                params.put("CUST_ID",username);
                params.put("TXN_AMOUNT",String.valueOf(cartLoader.getCartTotol()));
                params.put("CHANNEL_ID","WAP");
                params.put("WEBSITE","WEBSTAGING");
                params.put("CALLBACK_URL",URLContract.PAYTM_VERIFY_CALLBACK_URL);
                params.put("INDUSTRY_TYPE_ID","Retail");
                return params;
            }
        };
        RequestHelper.getInstance(getActivity()).addToRequestQueue(request);
    }
    private boolean parseChecksumResponse(String response)
    {
        boolean flag;
        try
        {
            JSONObject obj=new JSONObject(response);
            checksumHash=obj.getString("CHECKSUMHASH");
            flag=true;
        }
        catch (Exception e){
            flag=false;
        }
        return flag;
    }
    private boolean parseGenerateResponse(String response) {
        boolean flag;
        try {
            JSONObject obj = new JSONObject(response);
            String status = obj.getString("status");
            if (status.equals("ID_GENERATED")) {
                flag = true;
                orderid = obj.getString("orderid");
            } else
                flag = false;
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    private void setObservers() {
        cartObserver = cartLoader.getCartLoadObserver();
        cartObserver.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean)
                    updateUI();
            }
        });
    }

    private void updateUI() {
        adaptor = new CheckoutAdaptor(getActivity(), cartLoader.getCartItems());
        amount.setText("Rs." + String.valueOf(cartLoader.getCartTotol()) + "/-");
        finalProducts.setAdapter(adaptor);
    }

    private void setLoaders() {
        cartLoader = ViewModelProviders.of(getActivity(), new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new CartLoader(getActivity());
            }
        }).get(CartLoader.class);
    }

    private void setAddress() {
        Bundle bundle = getArguments();
        AddressItem item = (AddressItem) bundle.getSerializable("address");
        address.setText(addressItem.getAddress());
    }

    private void initComponents(View view) {
        finalProducts = view.findViewById(R.id.finalProducts);
        pay = view.findViewById(R.id.pay);
        amount = view.findViewById(R.id.amount);
        progress = view.findViewById(R.id.progress);
        finalProducts.setEmptyView(progress);
        address = view.findViewById(R.id.address);

        Bundle bundle = getArguments();
        addressItem = (AddressItem) bundle.getSerializable("address");

        SharedPreferences preferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        username = preferences.getString("username", null);
    }
}
