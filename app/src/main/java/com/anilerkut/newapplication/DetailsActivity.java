package com.anilerkut.newapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.anilerkut.newapplication.model.NewsModel;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity{

    NewsModel headlines;
    TextView txt_title,txt_author,txt_summary,txt_date,txt_content;
    ImageView news_image;
    Button goToUrl;
    FirebaseAuth mAuth;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        headlines = (NewsModel) getIntent().getSerializableExtra("data");
        txt_title = (TextView)findViewById(R.id.detail_title);
        txt_author = findViewById(R.id.detail_author);
        txt_summary =  findViewById(R.id.detail_summary);
        txt_date = findViewById(R.id.detail_date);
        txt_content = findViewById(R.id.detail_content);
        news_image =  findViewById(R.id.detail_image);
        goToUrl = findViewById(R.id.goToUrlButton);
        toolbar = findViewById(R.id.toolbar);
        mAuth = FirebaseAuth.getInstance();

        txt_title.setText(headlines.getTitle());
        txt_author.setText(headlines.getAuthor());
        txt_summary.setText(headlines.getDescription());
        if(headlines.getPublishedAt()!=null)
        {
            String newNewsDate= headlines.getPublishedAt().substring(0,10);
            txt_date.setText(newNewsDate);
        }
        else
        {
            txt_date.setText("");
        }
        if(headlines.getContent()!=null)
        {
            String newNewsContent= headlines.getContent().substring(0,headlines.getContent().length()-13);
            txt_content.setText(newNewsContent);
        }
        else
        {
            txt_content.setText("");
        }
        Picasso.get().load(headlines.getUrlToImage()).into(news_image);
        toolbar.setTitle("");
        toolbar.setLogo(R.drawable.dailyplanet);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= 21)
        {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.app_red3)); //status bar or the time bar at the top (see example image1)
        }

        goToUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUrl(headlines.getUrl());
            }
        });
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
            startActivity(new Intent(DetailsActivity.this,LoginActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public void goToUrl(String url)
    {
        Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse(url));
        startActivity(browse);
    }





}