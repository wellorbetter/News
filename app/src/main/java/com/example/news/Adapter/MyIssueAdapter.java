package com.example.news.Adapter;

        import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.news.R;
import com.example.news.bean.MyIssueBean;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyIssueAdapter extends RecyclerView.Adapter<MyIssueAdapter.MyIssueViewHolder> {
    public Context mContext;private MyIssueBean myIssueBean;
    private OnItemClickListener mListener;
    public MyIssueAdapter(Context context, MyIssueBean myIssueBean, OnItemClickListener mListener){
        this.mContext = context;
        this.myIssueBean = myIssueBean;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public MyIssueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new MyIssueViewHolder(LayoutInflater.from(mContext).inflate(R.layout.my_issue_items, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull MyIssueViewHolder holder, int position) {
        holder.my_issue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(position);
            }
        });
        if(myIssueBean != null && myIssueBean.getCode() == 1000)
        {
            holder.content.setText(myIssueBean.getData().getNews().get(position).getContent());
            holder.title.setText(myIssueBean.getData().getNews().get(position).getTitle());
            holder.time.setText(myIssueBean.getData().getNews().get(position).getCreate_time());
            holder.btn_browser.setText(String.valueOf(myIssueBean.getData().getNews().get(position).getBrow_num()));
            holder.btn_like.setText(String.valueOf(myIssueBean.getData().getNews().get(position).getLike_num()));
            holder.btn_star.setText(String.valueOf(myIssueBean.getData().getNews().get(position).getStar_num()));
            holder.bb_num.setText(String.valueOf(myIssueBean.getData().getNews().get(position).getBb_num()));
            //这个bbnum貌似不是评论的个数？ = =
            Glide.with(holder.itemView).load(myIssueBean.getData().getNews().get(position)
                    .getNews_pics_set().get(0)).into(holder.imageView);
        }
        else{
            Toast.makeText(mContext, "出错了？！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        if(myIssueBean != null && myIssueBean.getData().getNews().size()!=0)
            return myIssueBean.getData().getNews().size();
        else return 1;
    }
    static class MyIssueViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView time;
        private TextView title;private TextView content;
        private ImageView imageView;
        private Button btn_browser;private Button btn_like;
        private Button btn_star;private Button bb_num;
        private LinearLayout my_issue;
        public MyIssueViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            btn_browser = itemView.findViewById(R.id.btn_browser);
            title = itemView.findViewById(R.id.title);
            btn_like = itemView.findViewById(R.id.btn_like);
            btn_star = itemView.findViewById(R.id.btn_star);
            bb_num = itemView.findViewById(R.id.bb_num);
            content = itemView.findViewById(R.id.content);
            imageView = itemView.findViewById(R.id.content_image);
            my_issue = itemView.findViewById(R.id.my_issue);
        }

        @Override
        public void onClick(View v) {

        }
    }
    public interface OnItemClickListener{
        void onClick(int pos);
    }
}