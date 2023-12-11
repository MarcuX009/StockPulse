package com.example.stockpulse;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class stockAdapter extends RecyclerView.Adapter<stockAdapter.StockViewHolder> {

    private List<stockItem> stockItemList;
    private  final RecyclerViewInterface recyclerViewInterface;

    public stockAdapter(List<stockItem> stockItemList,
                        RecyclerViewInterface recyclerViewInterface) {
        this. recyclerViewInterface = recyclerViewInterface;
        this.stockItemList = stockItemList;
    }

    public static class StockViewHolder extends RecyclerView.ViewHolder {
        TextView stockNameUI;
        TextView stockValueUI;
        TextView stockPercentUpUI;
        TextView stockPercentDownUI;

        public StockViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            stockNameUI = itemView.findViewById(R.id.stockNameDisplayLayout);
            stockValueUI = itemView.findViewById(R.id.stockValueDisplayLayout);
            stockPercentUpUI = itemView.findViewById(R.id.stockPercentUpDisplayLayout);
            stockPercentDownUI = itemView.findViewById(R.id.stockPercentDownDisplayLayout);

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
        stockItem stockItem = stockItemList.get(position);
        holder.stockNameUI.setText(stockItem.getStockName());
        holder.stockValueUI.setText(String.valueOf(stockItem.getStockValue()));
        if(stockItem.getStockPercent() >= 0) {
            holder.stockPercentUpUI.setText(String.valueOf(stockItem.getStockPercent()));
            holder.stockPercentUpUI.setVisibility(View.VISIBLE);
        }
        else{
            holder.stockPercentDownUI.setText(String.valueOf(stockItem.getStockPercent()));
            holder.stockPercentDownUI.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return stockItemList.size();
    }
}
