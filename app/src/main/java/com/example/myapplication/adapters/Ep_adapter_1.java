package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class Ep_adapter_1 extends RecyclerView.Adapter<Ep_adapter_1.viewHolder> {
    private ArrayList<String> messages;
    private ArrayList<String> senders;
    private ArrayList<Integer> negative;
    private ArrayList<Integer> positive;
    private ArrayList<Integer> neutral;
    Ep_adapter_1.ItemClickListener itemClickListener;
    //adapter constructor
    public Ep_adapter_1(ArrayList<Integer> negative, ArrayList<Integer> positive, ArrayList<Integer> neutral,  ArrayList<String> messages, ArrayList<String> senders, Ep_adapter_1.ItemClickListener itemClickListener)
    {
        this.itemClickListener = itemClickListener;
        this.messages= messages;
        this.senders = senders;
        this.negative = negative;
        this.positive = positive;
        this.neutral = neutral;
    }


    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ItemClickListener itemClickListener;
        TextView messageTV,textButton,senderName,_negative,_positive,_neutral;
        public viewHolder(@NonNull View itemView, ItemClickListener itemClickListener)
        {
            super(itemView);
            itemView.findViewById(R.id.item);
            this.itemClickListener = itemClickListener;
            messageTV = itemView.findViewById(R.id.messageTv);
            textButton = itemView.findViewById(R.id.assertion);
            senderName = itemView.findViewById(R.id.senderName);
            _positive = itemView.findViewById(R.id.positivePolarity);
            _neutral = itemView.findViewById(R.id.neutralPolarity);
            _negative = itemView.findViewById(R.id.negativePolarity);

            textButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) itemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
    @NonNull
    @Override
    public Ep_adapter_1.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.emotinal_profile_view,parent,false);
        return new viewHolder(view,itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull Ep_adapter_1.viewHolder holder, int i) {

        holder.messageTV.setText(messages.get(i));
        holder.senderName.setText(senders.get(i));
        holder._negative.setText(String.valueOf(negative.get(i)));
        holder._neutral.setText(String.valueOf(neutral.get(i)));
        holder._positive.setText(String.valueOf(positive.get(i)));
    }

    @Override
    public int getItemCount() {
        return messages.size()+1;
    }

    public interface ItemClickListener
    {
        void onItemClick(View view, int position);
    }

}
