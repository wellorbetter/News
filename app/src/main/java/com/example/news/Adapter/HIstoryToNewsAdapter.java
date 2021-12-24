package com.example.news.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.news.R;
import com.example.news.bean.History_News_Bean;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HIstoryToNewsAdapter extends RecyclerView.Adapter<HIstoryToNewsAdapter.HIstoryToNewsViewHolder> {
    public Context mContext;private History_News_Bean history_news_bean;
    private OnItemClickListener mListener;
    public HIstoryToNewsAdapter(Context context, History_News_Bean history_news_bean,OnItemClickListener mListener){
        this.mContext = context;
        this.history_news_bean = history_news_bean;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public HIstoryToNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new HIstoryToNewsViewHolder(LayoutInflater.from(mContext).inflate(R.layout.history_to_news_items, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull HIstoryToNewsViewHolder holder, int position) {
        holder.my_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(position);
            }
        });
        if(history_news_bean.getData().getNews().size()==0){
            holder.textview_nickname.setText("还没有浏览记录哦！");
        }
        else {
            holder.textview_nickname.setText(history_news_bean.getData().getNews().get(position).getNickname());
            holder.textview_issue_time.setText(history_news_bean.getData().getNews().get(position).getTime());
            holder.textview_title.setText(history_news_bean.getData().getNews().get(position).getTitle());
            Glide.with(holder.itemView).load(history_news_bean.getData().getNews().get(position)
                    .getAvatar()).into(holder.imageview_author);
        }
    }

    @Override
    public int getItemCount() {
        if(history_news_bean != null && history_news_bean.getData().getNews().size()!=0)
            return history_news_bean.getData().getNews().size();
        else return 1;
    }
    static class HIstoryToNewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textview_nickname;private TextView textview_title;private TextView textview_issue_time;
        private ImageView imageview_author;private LinearLayout my_history;
        public HIstoryToNewsViewHolder(@NonNull View itemView) {
            super(itemView);
            textview_nickname = itemView.findViewById(R.id.textview_nickname);
            textview_title = itemView.findViewById(R.id.textview_title);
            textview_issue_time = itemView.findViewById(R.id.textview_issue_time);
            imageview_author = itemView.findViewById(R.id.imageview_author);
            my_history = itemView.findViewById(R.id.my_history);
        }

        @Override
        public void onClick(View v) {

        }
    }
    public interface OnItemClickListener{
        void onClick(int pos);
    }
}