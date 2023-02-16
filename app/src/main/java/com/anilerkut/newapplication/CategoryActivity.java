package com.anilerkut.newapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;


import com.anilerkut.newapplication.model.NewsModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class CategoryActivity extends AppCompatActivity implements SelectListener {

    List<NewsModel> categoryNews;
    CustomAdapter adapter;
    RecyclerView categoryRecyclerView;
    TextView categoryText;
    String category;
    FirebaseAuth mAuth;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category);

        mAuth = FirebaseAuth.getInstance();
        categoryNews = (List<NewsModel>) getIntent().getSerializableExtra("list");
        category = (String) getIntent().getSerializableExtra("category");
        category = category.substring(0, 1).toUpperCase() + category.substring(1);
        categoryText = findViewById(R.id.categroyTextView);
        categoryRecyclerView = findViewById(R.id.categoryRecylerView);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setLogo(R.drawable.dailyplanet);
        setSupportActionBar(toolbar);
        categoryText.setText(category);
        categoryRecyclerView.setHasFixedSize(true);
        categoryRecyclerView.setLayoutManager(new GridLayoutManager(this,1));
        adapter = new CustomAdapter(this,categoryNews, this);
        categoryRecyclerView.setAdapter(adapter);

        if (Build.VERSION.SDK_INT >= 21)
        {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.app_red3)); //status bar or the time bar at the top (see example image1)
        }
    }

    @Override
    public void OnNewsClicked(NewsModel newsModel) {
        startActivity(new Intent(CategoryActivity.this,DetailsActivity.class).putExtra("data",newsModel));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.exitButton)
        {
            mAuth.signOut();
            startActivity(new Intent(CategoryActivity.this,LoginActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}