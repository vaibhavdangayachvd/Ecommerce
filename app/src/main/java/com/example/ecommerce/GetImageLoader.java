package com.example.ecommerce;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class GetImageLoader extends BaseAdapter {

    LayoutInflater inf;
    Context cnt;
    ArrayList<Integer>img;
    ArrayList<String>name;
    public GetImageLoader(Context context, ArrayList<Integer> images, ArrayList<String>name) {
        this.cnt=context;
        this.img=images;
        this.name=name;
        this.inf=LayoutInflater.from(cnt);

    }

    @Override
    public int getCount() {
        return img.size();
    }

    @Override
    public Object getItem(int i) {
        return img.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view=inf.inflate(R.layout.getimageloader,viewGroup,false);
        ImageView imageView=(ImageView)view.findViewById(R.id.imageView);
        TextView nm=view.findViewById(R.id.textView);
        imageView.setImageResource(img.get(i));
        nm.setText(name.get(i));
        return  view;
    }
}