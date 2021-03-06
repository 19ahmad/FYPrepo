package com.example.myapplication.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.gameModule.Contact_model;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.viewHolder> {
    private List<Contact_model> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
   public Adapter(Context context, List<Contact_model> data, ItemClickListener onItemClickListener) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mClickListener = onItemClickListener;
    }

    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //to add more data simply create coresponding variales here !
        TextView name,phoneNumber;
        ImageView pic;
        ItemClickListener itemClickListener;
        public viewHolder(@NonNull View itemView, ItemClickListener itemClickListener)
        {
            super(itemView);
            pic = itemView.findViewById(R.id.avatar);
            name = itemView.findViewById(R.id.name);
            phoneNumber = itemView.findViewById(R.id.mobileNumber);
            this.itemClickListener = itemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    // public com.example.myapplication.gameModule.adapter(Context context, List<profile> profile_list)

    @NonNull
    @Override
    public Adapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.contact_list_item,viewGroup,false);

        return new viewHolder(view,mClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.viewHolder viewHolder, int i) {
        viewHolder.name.setText(mData.get(i).name);
        viewHolder.phoneNumber.setText(mData.get(i).mobileNumber);
    }

    @Override
    public int getItemCount()
    {
        return mData.size();
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
