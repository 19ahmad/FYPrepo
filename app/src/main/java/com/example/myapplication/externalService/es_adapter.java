package com.example.myapplication.externalService;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class es_adapter extends RecyclerView.Adapter<es_adapter.viewHolder>{
    private ArrayList<String> _data;

    public es_adapter(ArrayList<String> data) {
        this._data= data;
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        //to add more data simpy create coresponding variales here !
        TextView name;
        ImageView pic;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.profile_pic);
            name = itemView.findViewById(R.id.title);
        }
    }

    // public adapter(Context context, List<profile> profile_list)

    @NonNull
    @Override
    public es_adapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.em_profile_item,viewGroup,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull es_adapter.viewHolder viewHolder, int i) {
        viewHolder.name.setText( _data.get(i));
    }

    @Override
    public int getItemCount() {
        return _data.size();
    }

//    @Override
//    public int getItemCount()
//    {
//        return _data.length;
//    }


}
