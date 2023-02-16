package com.anilerkut.newapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anilerkut.newapplication.model.NewsApiResponse;
import com.anilerkut.newapplication.model.NewsModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements SelectListener,View.OnClickListener {

    ArrayList<NewsModel> mainPageNews;
    private String baseURL = "https://newsapi.org/v2/";
    Retrofit retrofit;
    RecyclerView recyclerView;
    CustomAdapter adapter;
    LinearLayout b1,b2,b3,b4,b5,b6;
    SearchView searchView;
    ProgressDialog dialog;
    ImageButton mainPageButton;
    FirebaseAuth mAuth;
    Toolbar toolbar;

    BottomNavigationView bottomBar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() { //Burası search işlemi içindir. RequestManagera search için girdiğimiz query yollanır.
            @Override
            public boolean onQueryTextSubmit(String query) {

                dialog.setTitle("Fetching News Articles of "+ query );
                dialog.show();
                RequestManager requestManager = new RequestManager(MainActivity.this);
                requestManager.getMainNews(listener,"general",query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        b1 = findViewById(R.id.button_sports);
        b1.setOnClickListener(this);
        b2 = findViewById(R.id.button_health);
        b2.setOnClickListener(this);
        b3 = findViewById(R.id.button_science);
        b3.setOnClickListener(this);
        b4 = findViewById(R.id.button_entertainment);
        b4.setOnClickListener(this);
        b5 = findViewById(R.id.button_business);
        b5.setOnClickListener(this);
        b6 = findViewById(R.id.button_technology);
        b6.setOnClickListener(this);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setLogo(R.drawable.dailyplanet);
        setSupportActionBar(toolbar);

        mainPageButton = findViewById(R.id.mainPageButton);
        mainPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestManager requestManager = new RequestManager(MainActivity.this);
                requestManager.getMainNews(listener,"general",null);
            }
        });

        RequestManager requestManager = new RequestManager(this);
        requestManager.getMainNews(listener,"general",null);

        if (Build.VERSION.SDK_INT >= 21) //status barın rengini değiştirmek için olan kod
        {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.app_red3)); //status bar or the time bar at the top (see example image1)
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user==null)
        {
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
        }
    }

    private final OnFetchDataListener<NewsApiResponse> listener = new OnFetchDataListener<NewsApiResponse>() //general newslerin çekilmesi içiin olan listener.Burda showNews methodunu çağırıyoruz.
    {
        @Override
        public void onFetchData(List<NewsModel> list, String message)
        {
            if(list.isEmpty())
            {
                Toast.makeText(MainActivity.this,"No Data Found",Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
            else
            {
                showNews(list);
                dialog.dismiss();
            }
        }

        @Override
        public void onError(String message) {
            Toast.makeText(MainActivity.this, "An Error Occured",Toast.LENGTH_LONG).show();
        }
    };

    private final onCategoryListener<NewsApiResponse> categoryListener = new onCategoryListener<NewsApiResponse>() { //kategori tuşuna basınca çağırılan listener. toCategoryNewsPage e yönlendirdik.
        @Override
        public void onGategoryData(List<NewsModel> list, String message,String category)
        {
            toCategoryNewsPage(list,category);

        }

        @Override
        public void onError(String message) {

        }
    };

    private void showNews(List<NewsModel> list) { //ana ekranda haberlerin listelenmesini sağlayan method içinde adapter ve recylerview tanımlamaları yapılıyor.
       recyclerView = findViewById(R.id.news_recylerView);
       recyclerView.setHasFixedSize(true);
       recyclerView.setLayoutManager(new GridLayoutManager(this,1));
       adapter = new CustomAdapter(this,list,this);
       recyclerView.setAdapter(adapter);

    }

    @Override
    public void OnNewsClicked(NewsModel newsModel)  //habere tıkladığımızda haber ile birlikte detay sayfasına gidiyor.
    {
        startActivity(new Intent(MainActivity.this,DetailsActivity.class).putExtra("data",newsModel));
    }

    @Override
    public void onClick(View view) { //kategori butonuna tıklayınca olacaklar burada yer alıyor. Request manager ile apıdan yeni bir sorgu yapılıyor.
        LinearLayout layout = (LinearLayout) view;
        TextView categoryText = (TextView) layout.getChildAt(1);
        String category = categoryText.getText().toString().toLowerCase();
        RequestManager manager = new RequestManager(this);
        manager.getNewsFromCategory(categoryListener,category,null);
    }

    private void toCategoryNewsPage(List<NewsModel> list,String category) //kullanıcnının tıkladığı kategoriye gidiyor.
    {
        startActivity(new Intent(MainActivity.this,CategoryActivity.class).putExtra("list", (Serializable) list).putExtra("category",category));
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
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}