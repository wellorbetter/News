package com.example.news.Adapter;

        import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.news.R;
import com.example.news.bean.MyCollectionBean;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyCollectionAdapter extends RecyclerView.Adapter<MyCollectionAdapter.MyCollectionViewHolder> {
    public Context mContext;private MyCollectionBean myCollectionBean;
    private OnItemClickListener mListener;
    public MyCollectionAdapter(Context context, MyCollectionBean myCollectionBean,OnItemClickListener mListener){
        this.mContext = context;
        this.myCollectionBean = myCollectionBean;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public MyCollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new MyCollectionViewHolder(LayoutInflater.from(mContext).inflate(R.layout.my_collection_item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull MyCollectionViewHolder holder, int position) {
        holder.my_issue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(position);
            }
        });
        if(myCollectionBean != null && myCollectionBean.getCode() == 1000)
        {
            holder.content.setText(myCollectionBean.getData().getNews().get(position).getContent());
            holder.title.setText(myCollectionBean.getData().getNews().get(position).getTitle());
            holder.time.setText(myCollectionBean.getData().getNews().get(position).getCreate_time());
            holder.btn_browser.setText(String.valueOf(myCollectionBean.getData().getNews().get(position).getBrow_num()));
            holder.btn_like.setText(String.valueOf(myCollectionBean.getData().getNews().get(position).getLike_num()));
            holder.btn_star.setText(String.valueOf(myCollectionBean.getData().getNews().get(position).getStar_num()));
            holder.bb_num.setText(String.valueOf(myCollectionBean.getData().getNews().get(position).getBb_num()));
            //这个bbnum貌似不是评论的个数？ = =
            Glide.with(holder.itemView).load(myCollectionBean.getData().getNews().get(position)
                    .getNews_pics_set().get(0)).into(holder.imageView);
        }

    }

    @Override
    public int getItemCount() {
        if(myCollectionBean != null && myCollectionBean.getCode() ==1000 && myCollectionBean.getData().getNews().size()!= 0)
        return myCollectionBean.getData().getNews().size();
        else
            return 0;
    }
    static class MyCollectionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView time;
        private TextView title;private TextView content;
        private ImageView imageView;
        private Button btn_browser;private Button btn_like;
        private Button btn_star;private Button bb_num;
        private LinearLayout my_issue;
        public MyCollectionViewHolder(@NonNull View itemView) {
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