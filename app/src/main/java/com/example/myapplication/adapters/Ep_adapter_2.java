package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class Ep_adapter_2 extends RecyclerView.Adapter<Ep_adapter_2.viewHolder> {
    ArrayList<String> assertions = new ArrayList<>();
    public Ep_adapter_2(ArrayList<String> assertions)
    {
        this.assertions = assertions;
    }


    public class viewHolder extends RecyclerView.ViewHolder
    {
        TextView count,assertion;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            count = itemView.findViewById(R.id.assertionNumber);
            assertion = itemView.findViewById(R.id.assertionContent);
        }
    }
    @NonNull
    @Override
    public Ep_adapter_2.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.assertion_list,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Ep_adapter_2.viewHolder holder, int position)
    {
       // holder.count.setText(assertions.get(position));
        holder.assertion.setText(assertions.get(position));
    }

    @Override
    public int getItemCount() {
        return assertions.size();
    }
}
