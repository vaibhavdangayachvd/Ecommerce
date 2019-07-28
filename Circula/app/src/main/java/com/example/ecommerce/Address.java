package com.example.ecommerce;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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

import com.example.ecommerce.address.AddressLoader;
import com.example.ecommerce.helper.UserLogin;
import com.example.ecommerce.helper.ViewHelper;

public class Address extends Fragment {
    private ListView addresses;
    private UserLogin loginLoader;
    private AddressLoader addressLoader;
    private LiveData<Boolean> loginObserver;
    private LiveData<Boolean> addressObserver;
    private ArrayAdapter<String> adapter;
    private EditText house,locality,city,pincode;
    private String state;
    private Spinner spinner;
    private Button submit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_address, container, false);
        initComponents(view);
        setLoaders();
        setObservers();
        setListListeners();
        setSubmitListener();
        setSpinnerListener();
        addressLoader.getAddresses();
        return view;
    }
    private void setSubmitListener()
    {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInput())
                {
                    String hn=house.getText().toString();
                    String loc=locality.getText().toString();
                    String cy=city.getText().toString();
                    String pin=pincode.getText().toString();
                    String address=hn+","+loc+","+cy+","+state+","+pin;
                    addressLoader.addNewAddress(getActivity(),address);
                    clearInput();
                }
            }
        });
    }
    private void clearInput()
    {
        house.setText("");
        locality.setText("");
        city.setText("");
        pincode.setText("");
    }
    private boolean validateInput()
    {
        if(TextUtils.isEmpty(house.getText().toString()))
        {
            house.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(locality.getText().toString()))
        {
            locality.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(city.getText().toString()))
        {
            city.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(pincode.getText().toString()))
        {
            pincode.requestFocus();
            return false;
        }
        return true;
    }
    private void setSpinnerListener()
    {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                state=getResources().getStringArray(R.array.states)[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void setListListeners() {
        addresses.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                addressLoader.removeAddress(position);
                return true;
            }
        });

        addresses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle=new Bundle();
                bundle.putSerializable("address",addressLoader.getLoadedAddresses().get(position));
                Fragment checkout=new Checkout();
                checkout.setArguments(bundle);
                ViewModelProviders.of(getActivity()).get(ViewHelper.class).loadView(checkout);
            }
        });
    }

    private void initComponents(View view) {
        addresses = view.findViewById(R.id.address_list);
        TextView placeholder = view.findViewById(R.id.placeholder);
        addresses.setEmptyView(placeholder);

        house=view.findViewById(R.id.houseNo);
        locality=view.findViewById(R.id.locality);
        city=view.findViewById(R.id.city);
        pincode=view.findViewById(R.id.pincode);
        spinner=view.findViewById(R.id.state);
        submit=view.findViewById(R.id.submit);
    }

    private void setLoaders() {
        loginLoader = ViewModelProviders.of(getActivity(), new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new UserLogin(getActivity());
            }
        }).get(UserLogin.class);

        addressLoader = ViewModelProviders.of(getActivity(), new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new AddressLoader(getActivity());
            }
        }).get(AddressLoader.class);
    }

    private void setObservers() {
        loginObserver = loginLoader.getLoginObserver();
        loginObserver.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (!aBoolean) {
                    addressLoader.clearPreLoadedData();
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });

        addressObserver = addressLoader.getAddressobserver();
        addressObserver.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    if (adapter == null)
                        adapter = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, addressLoader.getLoadedAddressNames());
                    if (addresses.getAdapter() == null)
                        addresses.setAdapter(adapter);
                    else
                        adapter.notifyDataSetChanged();
                }
            }
        });
    }
}