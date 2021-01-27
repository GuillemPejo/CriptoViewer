package me.guillem.criptoviewer.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.guillem.criptoviewer.R;
import me.guillem.criptoviewer.retrofit.Coin;
import me.guillem.criptoviewer.retrofit.Datum;

/**
 * * Created by Guillem on 27/01/21.
 */
public class rvAdapter extends RecyclerView.Adapter<rvAdapter.VH> {

    private List<Datum> mData;
    private ItemClickListener mClickListener;
    Context context;
    private Map<String, Coin> cryptoListIcons = new HashMap<>();

    public Map<String, Coin> getCryptoListIcons() {
        return cryptoListIcons;
    }

    // data is passed into the constructor
    public rvAdapter(List<Datum> data) {
        this.mData = data;
    }

    // Usually involves inflating a layout from XML and returning the holder
    // inflates the row layout from xml when needed
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_coin, parent, false);
        VH viewHolder = new VH(view);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    // binds the data to the TextView in each row
    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(final VH holder, int position) {
        // Get the data model based on position
        final Datum datum = mData.get(position);

        // Set item views based on your views and data model
        holder.symbol.setText((datum.getSymbol()));

        holder.name.setText(datum.getName());

        holder.price.setText("$"+String.format("%.2f", datum.getQuote().getUSD().getPrice()));

        holder.textView1h.setText(String.format("1h: %.2f", datum.getQuote().getUSD().getPercentChange1h()) + "%");
        holder.textView1h.setTextColor(Color(String.format("%,f", datum.getQuote().getUSD().getPercentChange1h())));

        holder.textView24h.setText(String.format("24h: %.2f", datum.getQuote().getUSD().getPercentChange24h()) + "%");
        holder.textView24h.setTextColor(Color(String.format("%,f", datum.getQuote().getUSD().getPercentChange24h())));

        holder.textView7d.setText(String.format("7d: %.2f", datum.getQuote().getUSD().getPercentChange7d()) + "%");
        holder.textView7d.setTextColor(Color(String.format("%,f", datum.getQuote().getUSD().getPercentChange7d())));



        String logoURL = cryptoListIcons.get(datum.getSymbol()).getLogo();
        logoURL = logoURL.replace("64x64", "200x200");

        try {
            Picasso.get().load(logoURL).into(holder.icon);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private int Color(String price){
        if(price.contains("-")){
            return Color.RED;
        }
        return Color.GREEN;
    }


    // Returns the total count of items in the list
    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    // stores and recycles views as they are scrolled off screen
    public class VH extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        TextView symbol;
        TextView name;
        TextView price;
        TextView marketCap;
        TextView volume24h;
        TextView textView1h;
        TextView textView24h;
        TextView textView7d;
        ImageView icon;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        VH(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            itemView.setOnClickListener(this);

            name = itemView.findViewById(R.id.name);
            symbol = itemView.findViewById(R.id.symbol);
            price = itemView.findViewById(R.id.price);
            textView1h = itemView.findViewById(R.id.textView1h);
            textView24h = itemView.findViewById(R.id.textView24h);
            textView7d = itemView.findViewById(R.id.textView7d);
            icon = itemView.findViewById(R.id.icon);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

    }

    // convenience method for getting data at click position
    public Datum getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}