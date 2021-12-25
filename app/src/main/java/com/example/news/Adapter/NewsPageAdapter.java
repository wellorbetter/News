package com.example.news.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
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
import com.example.news.bean.IslikeBean;
import com.example.news.bean.News_brief_Bean;
import com.example.news.bean.StarBean;
import com.google.firebase.database.annotations.NotNull;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.google.android.gms.internal.zzir.runOnUiThread;

public class NewsPageAdapter extends RecyclerView.Adapter<NewsPageAdapter.NewPageViewHolder>  {
    public Context mContext;private News_brief_Bean news_brief_bean;
    private ArrayList<News_brief_Bean>list;
    private int page = 0;private int count;
    private OnItemClickListener mListener;
    private int tag = 0;
    private SharedPreferences preferences;
    private int pos;
    private String token;

    public NewsPageAdapter(Context context,ArrayList<News_brief_Bean>list,OnItemClickListener mListener){
        this.mContext = context;
        this.list = list;
        this.mListener = mListener;
    }
    @NonNull
    @Override
    public NewPageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new NewPageViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recyclerview_newspage, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewPageViewHolder holder, int position) {
        Log.d("position", String.valueOf(position));
        page = (position) / 5;
        count = position % 5;
        if(list!=null && list.size()!=0 && list.get(page).getData().getNews().size() != 5)
        {
            page = list.size()-1;
            if(list.get(page).getData().getNews().size()!=list.get(page-1).getData().getNews().size()){
                int cnt = 0;
                for(int i = 0; i < list.size() - 1; i++)
                {
                    cnt += list.get(i).getData().getNews().size();
                }
                Log.d("cnt", String.valueOf(cnt));
                count = position - cnt;
                Log.d("count", String.valueOf(count));
            }
        }
        holder.setIsRecyclable(false);
        if(list != null && list.size()!=0)
        news_brief_bean = list.get(page);
        holder.news_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(position);
            }
        });
//        holder.btn_like.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                preferences = mContext.getSharedPreferences("LoginData", MODE_PRIVATE);
//                token = preferences.getString("token", "error");
//                islike(holder,position);
//            }
//        });
//        holder.btn_star.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                preferences = mContext.getSharedPreferences("LoginData", MODE_PRIVATE);
//                token = preferences.getString("token", "error");
//                star(holder,position);
//            }
//        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(position);
            }
        });
        if(list != null && list.size()!=0 && news_brief_bean != null && news_brief_bean.getCode() == 1000 && news_brief_bean.getData().getNews().size()!=0)
        {
            holder.content.setText(news_brief_bean.getData().getNews().get(count).getContent());
            holder.title.setText(news_brief_bean.getData().getNews().get(count).getTitle());
//            holder.time.setText(news_brief_bean.getData().getNews().get(count).getCreate_time());
//            holder.btn_browser.setText(String.valueOf(news_brief_bean.getData().getNews().get(count).getBrow_num()));
//            holder.btn_like.setText(String.valueOf(news_brief_bean.getData().getNews().get(count).getLike_num()));
//            holder.btn_star.setText(String.valueOf(news_brief_bean.getData().getNews().get(count).getStar_num()));
//            holder.bb_num.setText(String.valueOf(news_brief_bean.getData().getNews().get(count).getBb_num()));
            //这个bbnum貌似不是评论的个数？ = =
            if(news_brief_bean.getData().getNews().get(count)
                    .getNews_pics_set().size()!=0)
            Glide.with(holder.itemView).load(news_brief_bean.getData().getNews().get(count)
                    .getNews_pics_set().get(0)).into(holder.imageView);
        }
        else{
            Toast.makeText(mContext, "出错了？！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        int cnt = 0;
        for(int i = 0; i < list.size(); i++)
        {
            cnt += list.get(i).getData().getNews().size();
        }

        return cnt;
    }



    static class NewPageViewHolder extends RecyclerView.ViewHolder {
        private TextView time;private Button button_comments;
        private TextView title;private TextView content;
        private ImageView imageView;private LinearLayout news_detail;
        private Button btn_browser;private Button btn_like;
        private Button btn_star;private Button bb_num;
        public NewPageViewHolder(@NonNull View itemView) {
            super(itemView);
            news_detail = itemView.findViewById(R.id.news_detail);
//            time = itemView.findViewById(R.id.time);
//            btn_browser = itemView.findViewById(R.id.btn_browser);
            title = itemView.findViewById(R.id.title);
//            btn_like = itemView.findViewById(R.id.btn_like);
//            btn_star = itemView.findViewById(R.id.btn_star);
//            bb_num = itemView.findViewById(R.id.bb_num);
            content = itemView.findViewById(R.id.content);
            imageView = itemView.findViewById(R.id.content_image);
        }
    }

    public void add(ArrayList<News_brief_Bean> mlist) {
        //增加数据
        this.list.addAll(list.size(),mlist);
        notifyItemInserted(list.size()*5-5);
    }
    public void refresh(ArrayList<News_brief_Bean> mlist) {
        //增加数据
        this.list.removeAll(list);
        this.list.addAll(list.size(),mlist);
        Log.d("list", String.valueOf(list.size()));
        notifyItemChanged(0,5);
    }
    public interface OnItemClickListener{
        void onClick(int pos);
    }
    public void islike(@NonNull NewPageViewHolder holder, int pos){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("text/plain");
                int id = list.get(pos/5).getData().getNews().get(pos%5).getId();
                RequestBody body = RequestBody.create(mediaType, "");
                Request request = new Request.Builder()
                        .url("http://39.106.195.109/itnews/api/news/operator/"+id+"/like")
                        .method("POST", body)
                        .addHeader("Authorization",token)
                        .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.i("onFailure",e.getMessage());
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String result = response.body().string();
                        Gson gson = new Gson();
                        IslikeBean islikeBean = gson.fromJson(result,IslikeBean.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(islikeBean!=null)
                                    if(islikeBean.getData().getIsLike()==1) {
                                        Toast.makeText(mContext, "点赞成功", Toast.LENGTH_SHORT).show();
                                        holder.btn_like.setText(String.valueOf(list.get(pos / 5).getData().getNews().get(pos % 5).getLike_num() + 1));
                                    }
                                    else {
                                        Toast.makeText(mContext, "取消点赞成功", Toast.LENGTH_SHORT).show();
                                        holder.btn_like.setText(String.valueOf(list.get(pos / 5).getData().getNews().get(pos % 5).getLike_num()));
                                    }
                            }
                        });

                        Log.i("result",result);
                    }
                });
            }
        });
        thread.start(); try { thread.join(); } catch (InterruptedException e) { e.printStackTrace(); }
    }
    public void star(@NonNull NewPageViewHolder holder, int pos){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int id = list.get(pos/5).getData().getNews().get(pos%5).getId();
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("text/plain");
                RequestBody body = RequestBody.create(mediaType, "");
                Request request = new Request.Builder()
                        .url("http://39.106.195.109/itnews/api/news/operator/"+id+"/star")
                        .method("POST", body)
                        .addHeader("Authorization", token)
                        .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.i("onFailure",e.getMessage());
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String result = response.body().string();
                        Gson gson = new Gson();
                        StarBean starBean = gson.fromJson(result,StarBean.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(starBean!=null)
                                    if(starBean.getData().getIsStar()==1) {
                                        Toast.makeText(mContext, "收藏成功", Toast.LENGTH_SHORT).show();
                                        holder.btn_star.setText(String.valueOf(list.get(pos / 5).getData().getNews().get(pos % 5).getStar_num() + 1));
                                    }
                                    else {
                                        Toast.makeText(mContext, "取消收藏成功", Toast.LENGTH_SHORT).show();
                                        holder.btn_star.setText(String.valueOf(list.get(pos / 5).getData().getNews().get(pos % 5).getStar_num()));
                                    }
                            }
                        });

                        Log.i("result",result);
                    }
                });
            }
        });
        thread.start(); try { thread.join(); } catch (InterruptedException e) { e.printStackTrace(); }
    }

}
