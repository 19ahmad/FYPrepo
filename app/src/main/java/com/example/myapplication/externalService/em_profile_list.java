package com.example.myapplication.externalService;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.myapplication.R;

public class em_profile_list extends AppCompatActivity {

    RecyclerView em_profile_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_em_profile_list);
        this.setTitle("Profiles List");
        em_profile_list = findViewById(R.id.rec_prof_list);

       em_profile_list.setLayoutManager(new LinearLayoutManager(this));

        String _array[] = { "Anees","Raheel","Nadeem","Tabib","Kaleem","Faizan","Talah","Ahmad","Ehtisham","Hamza","Manan","Umer","Talah","Ahmad","Ehtisham","Hamza","Manan","Umer","Talah","Ahmad","Ehtisham","Hamza","Manan","Umer"};
        em_profile_list.setAdapter(new es_adapter(_array));
    }
}