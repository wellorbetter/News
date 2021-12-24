package com.example.news.Adapter;

        import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.news.R;
import com.example.news.bean.MyCollectionBean;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyCollectionAdapter extends RecyclerView.Adapter<MyCollectionAdapter.MyCollectionViewHolder> {
    public Context mContext;private MyCollectionBean myCollectionBean;
    public MyCollectionAdapter(Context context, MyCollectionBean myCollectionBean){
        this.mContext = context;
        this.myCollectionBean = myCollectionBean;
    }

    @NonNull
    @Override
    public MyCollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new MyCollectionViewHolder(LayoutInflater.from(mContext).inflate(R.layout.my_collection_item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull MyCollectionViewHolder holder, int position) {
        if(myCollectionBean!=null && myCollectionBean.getCode() == 1000 && myCollectionBean.getData() != null
                && myCollectionBean.getData().getNews().size()!=0)
        {
            Glide.with(holder.itemView).load(myCollectionBean.getData().getNews().get(position)
                    .getNews_pics_set().get(0)).into(holder.imageview_my_collection);
            holder.time_my_collection.setText(myCollectionBean.getData().getNews().get(position).getCreate_time());
            holder.bb_num_tv.setText(String.valueOf(myCollectionBean.getData().getNews().get(position).getBb_num()));
            holder.content_my_collection.setText(myCollectionBean.getData().getNews().get(position).getContent());
            holder.brow_num_tv.setText(String.valueOf(myCollectionBean.getData().getNews().get(position).getBrow_num()));
            holder.like_num_tv.setText(String.valueOf(myCollectionBean.getData().getNews().get(position).getLike_num()));
            holder.star_num_tv.setText(String.valueOf(myCollectionBean.getData().getNews().get(position).getStar_num()));
            holder.title_my_collection.setText(myCollectionBean.getData().getNews().get(position).getTitle());
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
        private TextView title_my_collection;private TextView content_my_collection;
        private TextView time_my_collection;private TextView brow_num_tv;
        private TextView like_num_tv;private TextView star_num_tv;
        private TextView bb_num_tv;private ImageView imageview_my_collection;
        public MyCollectionViewHolder(@NonNull View itemView) {
            super(itemView);
            title_my_collection = itemView.findViewById(R.id.title_my_collection);
            content_my_collection = itemView.findViewById(R.id.content_my_collection);
            time_my_collection = itemView.findViewById(R.id.time_my_collection);
            brow_num_tv = itemView.findViewById(R.id.brow_num_tv);
            like_num_tv = itemView.findViewById(R.id.like_num_tv);
            star_num_tv = itemView.findViewById(R.id.star_num_tv);
            bb_num_tv = itemView.findViewById(R.id.bb_num_tv);
            imageview_my_collection = itemView.findViewById(R.id.imageview_my_collection);

        }

        @Override
        public void onClick(View v) {

        }
    }
}