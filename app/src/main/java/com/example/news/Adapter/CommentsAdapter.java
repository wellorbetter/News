package com.example.news.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.news.R;
import com.example.news.bean.CommentsBean;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {

    public Context mContext;private CommentsBean commentsBean;
    public CommentsAdapter(Context context,  CommentsBean commentsBean){
        this.mContext = context;
        this.commentsBean = commentsBean;
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new CommentsViewHolder(LayoutInflater.from(mContext).inflate(R.layout.comments_items, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {
        if(commentsBean.getData().getComments().size()==0){
            holder.comments_num.setText("还没有评论哦！");
        }
        else {
            Log.d("??", String.valueOf(commentsBean.getData().getComments().size()));
            //记住 int类型的不能直接settext
            holder.textview_author_name.setText("作者名字："+commentsBean.getData().getComments().get(position).getUsername());//还有个nickname没弄
            holder.textview_content.setText("内容："+commentsBean.getData().getComments().get(position).getContent());
            holder.textview_issue_time.setText("发表时间："+commentsBean.getData().getComments().get(position).getCreate_time());
            Glide.with(holder.itemView).load(commentsBean.getData().getComments().get(position)
                    .getAvatar()).into(holder.imageview_author);
        }
    }



    @Override
    public int getItemCount() {
        if(commentsBean != null && commentsBean.getData().getComments().size()!=0)
        return commentsBean.getData().getComments().size();
        else return 1;
    }
    static class CommentsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView comments_num; private TextView textview_author_name;
        private TextView textview_content; private TextView textview_issue_time;
        private ImageView imageview_author;
        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);
            imageview_author = itemView.findViewById(R.id.imageview_author);
            comments_num = itemView.findViewById(R.id.comments_num);
            textview_author_name = itemView.findViewById(R.id.textview_author_name);
            textview_content = itemView.findViewById(R.id.textview_content);
            textview_issue_time = itemView.findViewById(R.id.textview_issue_time);
        }

        @Override
        public void onClick(View v) {

            }
        }
    }
