package com.example.ecommerce.category;

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

public final class CategoryAdaptor extends ArrayAdapter<CategoryItem> {
    public CategoryAdaptor(@NonNull Context context, @NonNull ArrayList<CategoryItem> objects) {
        super(context,0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null)
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.category_item,parent,false);
        CategoryItem item=getItem(position);
        ImageView image=convertView.findViewById(R.id.image);
        Picasso.get().load(URLContract.CATEGORY_PIC_URL+"/"+item.getImage()).placeholder(R.drawable.loading).error(R.drawable.error).into(image);
        TextView name=convertView.findViewById(R.id.name);
        name.setText(item.getName());
        return convertView;
    }
}
