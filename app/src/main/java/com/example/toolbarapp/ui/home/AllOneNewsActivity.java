package com.example.toolbarapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.toolbarapp.MainActivity;
import com.example.toolbarapp.R;

import java.util.ArrayList;


public class AllOneNewsActivity extends AppCompatActivity {

    public static RecyclerNewsItem recyclerNewsItem;
    RecyclerView recyclerView;
    ArrayList<RecyclerNewsItem> recyclerNews = new ArrayList<RecyclerNewsItem>();
    AllOneAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_one_news);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewNewsRec);

        recyclerView.setLayoutManager(new LinearLayoutManager(AllOneNewsActivity.this));
        recyclerView.setHasFixedSize(true);
        recyclerNews = new ArrayList<>();


        recyclerNews.add(recyclerNewsItem);
        System.out.println(recyclerNews);
        adapter = new AllOneAdapter(recyclerNews, AllOneNewsActivity.this, 1);
        recyclerView.setAdapter(adapter);
    }
    public static void upItem(RecyclerNewsItem recycler){
        recyclerNewsItem = recycler;
    }

    public void back(View view) {
        Intent intent = new Intent(AllOneNewsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
