package com.anilerkut.newapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anilerkut.newapplication.model.NewsModel;
import com.squareup.picasso.Picasso;

import java.util.List;


public class CustomAdapter  extends RecyclerView.Adapter<CustomViewHolder> {
    private Context context;
    private List<NewsModel> headlines;
    private SelectListener listener;
    //buradaki listener onItemClick için yani haber sayfasına gitmek için, main activityde implement edip this diyince , this artık listenırı da kapsıyor.

    public CustomAdapter(Context context, List<NewsModel> headlines,SelectListener listener) {
        this.context = context;
        this.headlines = headlines;
        this.listener=listener;
    }

    @NonNull

    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.news_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.news_title.setText(headlines.get(position).getTitle());
        holder.news_source.setText(headlines.get(position).getSource().getName());
        if(headlines.get(position).getUrlToImage()!=null)
        {
            Picasso.get().load(headlines.get(position).getUrlToImage()).into(holder.news_image);
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnNewsClicked(headlines.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return headlines.size();
    }
}
