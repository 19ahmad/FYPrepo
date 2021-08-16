package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class Ep_adapter extends RecyclerView.Adapter<Ep_adapter.viewHolder>
{
    private ArrayList<String> _data;
    ItemClickListener itemClickListener;
    //adapter constructor
    public Ep_adapter(ArrayList<String> data, ItemClickListener itemClickListener)
    {
        this.itemClickListener = itemClickListener;
        this._data= data;
    }

    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener
    {
        TextView name;
        ImageView pic;
        ItemClickListener itemClickListener;
        public viewHolder(@NonNull View itemView, ItemClickListener itemClickListener) {
            super(itemView);
            itemView.findViewById(R.id.item);
            this.itemClickListener = itemClickListener;
            pic = itemView.findViewById(R.id.profile_pic);
            name = itemView.findViewById(R.id.title);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) itemClickListener.onItemClick(v, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            if (itemClickListener != null)
            {
                itemClickListener.onItemClick(v, getAdapterPosition());
                return true;
            }
            else
            {
                return false;
            }
        }
    }
    @NonNull
    @Override
    public Ep_adapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.em_profile_item,viewGroup,false);
        return new viewHolder(view,itemClickListener);
    }
    @Override
    public void onBindViewHolder(@NonNull Ep_adapter.viewHolder viewHolder, int i) {
        viewHolder.name.setText( _data.get(i));

    }
    @Override
    public int getItemCount()
    {
        return _data.size();
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
