package com.example.ecommerce.cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ecommerce.R;
import com.example.ecommerce.helper.URLContract;
import com.squareup.picasso.Picasso;

public final class CartAdaptor extends ArrayAdapter<CartItem> {
    private CartLoader cartLoader;

    public CartAdaptor(@NonNull final Context context, CartLoader cartLoader) {
        super(context, 0, cartLoader.getCartItems());
        this.cartLoader = cartLoader;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_cartlist_item, parent, false);
        final CartItem item = getItem(position);

        ImageView image = convertView.findViewById(R.id.product_image);
        Picasso.get().load(URLContract.PRODUCT_PIC_URL + "/" + item.getCategory() + "/" + item.getImage()).placeholder(R.drawable.loading).error(R.drawable.error).into(image);

        TextView name = convertView.findViewById(R.id.product_name);
        name.setText(item.getName());

        TextView price = convertView.findViewById(R.id.product_price);
        price.setText("Rs." + item.getPrice() + "/-");

        final TextView quantity = convertView.findViewById(R.id.quantity);
        quantity.setText("Qty: " + item.getQuantity());

        ImageView positive, negative;

        positive = convertView.findViewById(R.id.quantity_plus);
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartLoader.updateCart(item.getId(),"1");
            }
        });

        negative = convertView.findViewById(R.id.quantity_minus);
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartLoader.updateCart(item.getId(),"-1");
            }
        });

        Button remove = convertView.findViewById(R.id.remove);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartLoader.removeFromCart(item.getId());
            }
        });

        return convertView;
    }
}