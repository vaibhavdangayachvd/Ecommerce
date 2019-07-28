package com.example.ecommerce.checkout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ecommerce.R;
import com.example.ecommerce.cart.CartItem;

import java.util.ArrayList;

public final class CheckoutAdaptor extends ArrayAdapter<CartItem> {
    public CheckoutAdaptor(@NonNull Context context, ArrayList<CartItem> cartList) {
        super(context, 0,cartList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null)
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.checkout_item_view,parent,false);
        CartItem item=getItem(position);

        TextView name=convertView.findViewById(R.id.name);
        name.setText(item.getName());
        TextView price=convertView.findViewById(R.id.price);
        price.setText(item.getPrice());
        TextView quantity=convertView.findViewById(R.id.quantity);
        quantity.setText(item.getQuantity());
        TextView amount=convertView.findViewById(R.id.total);
        amount.setText(String.valueOf(Integer.parseInt(item.getQuantity())*Integer.parseInt(item.getPrice())));
        return convertView;
    }
}
