package com.example.ecommerce.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ecommerce.R;
import com.example.ecommerce.helper.URLContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public final class ProductAdaptor extends ArrayAdapter<ProductItem> {
    private String category;
    public ProductAdaptor(@NonNull Context context,@NonNull ArrayList<ProductItem> objects,String category) {
        super(context, 0, objects);
        this.category=category;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null)
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.product_list_item,parent,false);
        ProductItem item=getItem(position);

        ImageView image=convertView.findViewById(R.id.product_image);
        Picasso.get().load(URLContract.PRODUCT_PIC_URL+"/"+category+"/"+item.getImage()).placeholder(R.drawable.loading).error(R.drawable.error).into(image);

        TextView name=convertView.findViewById(R.id.product_name);
        name.setText(item.getName());

        TextView price=convertView.findViewById(R.id.product_price);
        price.setText("Rs."+item.getPrice()+" /-");

        return convertView;
    }
}
