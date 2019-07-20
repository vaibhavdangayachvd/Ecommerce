package com.example.ecommerce;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Created by trending design on 15/3/19.
 */
public class SmartPhoneAdapter extends RecyclerView.Adapter<SmartPhoneAdapter.MyViewHolder> {

    Context context;
    private List<Favourite> OfferList;
    boolean showingfirst = true;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView image,like;
        TextView title,price;

        public MyViewHolder(View view) {
            super(view);

            image = (ImageView)view.findViewById(R.id.image);
            title = (TextView) view.findViewById(R.id.title);
            price = (TextView) view.findViewById(R.id.price);
        }
    }

    public SmartPhoneAdapter(Context context, List<Favourite> offerList) {
        this.OfferList = offerList;
        this.context = context;
    }

    @Override
    public SmartPhoneAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home, parent, false);
        return new SmartPhoneAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Favourite lists = OfferList.get(position);
        holder.image.setImageResource(lists.getImage());
        holder.title.setText(lists.getTitle());
        holder.price.setText(lists.getPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return OfferList.size();
    }
}