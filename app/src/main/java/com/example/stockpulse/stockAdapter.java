package com.example.stockpulse;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stockpulse.network.StockObject;

import java.util.List;

public class stockAdapter extends RecyclerView.Adapter<stockAdapter.StockViewHolder> {

    private List<StockObject> stockItemList;
    private final RecyclerViewInterface recyclerViewInterface;

    public stockAdapter(List<StockObject> stockItemList,
                        RecyclerViewInterface recyclerViewInterface) {
        this. recyclerViewInterface = recyclerViewInterface;
        this.stockItemList = stockItemList;
    }

    public static class StockViewHolder extends RecyclerView.ViewHolder {
        TextView stockNameUI;
        TextView stockValueUI;
        TextView stockPercentUpUI;
        TextView stockPercentDownUI;

        ImageView upArrow;
        ImageView downArrow;

        public StockViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            stockNameUI = itemView.findViewById(R.id.stockNameDisplayLayout);
            stockValueUI = itemView.findViewById(R.id.stockValueDisplayLayout);
            stockPercentUpUI = itemView.findViewById(R.id.stockPercentUpDisplayLayout);
            stockPercentDownUI = itemView.findViewById(R.id.stockPercentDownDisplayLayout);
            upArrow = itemView.findViewById(R.id.arrowUpLayout);
            downArrow =itemView.findViewById(R.id.arrowDownLayout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerViewInterface != null){
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(recyclerViewInterface != null){
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemLongClick(pos);
                        }
                    }
                    return true;
                }
            });
        }
    }

    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new StockViewHolder(itemView, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull stockAdapter.StockViewHolder holder, int position) {
        StockObject stockObject = stockItemList.get(position);
        holder.stockNameUI.setText(stockObject.getStockSymbol());
        holder.stockValueUI.setText(String.valueOf(stockObject.getC()));
        if(stockObject.getD() >= 0) {
            holder.stockPercentUpUI.setText(String.valueOf(stockObject.getD()));
            holder.stockPercentUpUI.setVisibility(View.VISIBLE);
            holder.upArrow.setVisibility(View.VISIBLE);
        }
        else{
            holder.stockPercentDownUI.setText(String.valueOf(stockObject.getD()));
            holder.stockPercentDownUI.setVisibility(View.VISIBLE);
            holder.downArrow.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return stockItemList.size();
    }
}
