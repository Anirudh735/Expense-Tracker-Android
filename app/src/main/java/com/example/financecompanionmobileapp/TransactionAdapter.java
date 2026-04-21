package com.example.financecompanionmobileapp;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    private List<Transaction> transactions;
    private List<Transaction> fullList;

    public TransactionAdapter(List<Transaction> transactions) {
        this.transactions = transactions;
        this.fullList = new ArrayList<>(transactions);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction t = transactions.get(position);
        holder.text1.setText(t.getTitle());
        holder.text1.setTextColor(Color.WHITE);
        
        holder.text2.setText(String.format("₹%.2f (%s)", t.getAmount(), t.getType()));
        holder.text2.setTextColor(t.getType().equals("Income") ? Color.GREEN : Color.parseColor("#FF6B6B"));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), AddTransactionActivity.class);
            intent.putExtra("id", t.getId());
            intent.putExtra("title", t.getTitle());
            intent.putExtra("amount", t.getAmount());
            intent.putExtra("type", t.getType());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public void filter(String query) {
        transactions.clear();
        if (query.isEmpty()) {
            transactions.addAll(fullList);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (Transaction item : fullList) {
                if (item.getTitle().toLowerCase().contains(lowerCaseQuery)) {
                    transactions.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void updateList(List<Transaction> newList) {
        fullList = new ArrayList<>(newList);
        transactions = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text1, text2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text1 = itemView.findViewById(android.R.id.text1);
            text2 = itemView.findViewById(android.R.id.text2);
        }
    }
}
