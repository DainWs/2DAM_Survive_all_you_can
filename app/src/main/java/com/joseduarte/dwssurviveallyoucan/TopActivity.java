package com.joseduarte.dwssurviveallyoucan;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.joseduarte.dwssurviveallyoucan.adapters.TopListViewAdapter;
import com.joseduarte.dwssurviveallyoucan.models.User;
import com.joseduarte.dwssurviveallyoucan.util.GlobalInformation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TopActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TopListViewAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        GlobalInformation.context = this;
        setContentView(R.layout.activity_top);

        ArrayList<User> list = new ArrayList<>(GlobalInformation.TOP_LIST);

        List<User> top = new ArrayList<>();
        if(list.size() == 1) top.add(list.get(0));
        else if (list.size() > 1) {
            int maxUsers = (list.size() > 50) ? 50 : list.size();
            Collections.sort(list);

            for (int i = 0; i < maxUsers; i++) {
                top.add(list.get(i));
            }
        }

        adapter = new TopListViewAdapter(top);

        recyclerView = findViewById(R.id.tops_recyclerview);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        GlobalInformation.topActivity = this;
    }

    public void update() {
        ArrayList<User> list = new ArrayList<>(GlobalInformation.TOP_LIST);
        List<User> top = list.subList(
                0,
                (list.size() > 50) ? 50 : list.size() - 1
        );
        Collections.sort(top);

        adapter.update(top);
    }
}
