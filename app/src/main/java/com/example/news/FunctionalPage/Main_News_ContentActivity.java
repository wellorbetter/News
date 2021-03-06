package com.example.news.FunctionalPage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.news.R;
import com.example.news.bean.IslikeBean;
import com.example.news.bean.NewsDetailBean;
import com.example.news.bean.StarBean;
import com.google.firebase.database.annotations.NotNull;
import com.google.gson.Gson;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Main_News_ContentActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button_back;private Button button_comments;private int id;
    private TextView content;private TextView title;private ImageView imageView;
    private String token;  SharedPreferences preferences;
    private Button like_detail;private Button star_detail;
    private Button btn_browser;
    private int page;
    private int size;
    private NewsDetailBean newsDetailBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_news_content);
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        id = bundle.getInt("id");
        page = bundle.getInt("page");
        size = bundle.getInt("size");
        Log.d("id", String.valueOf(id));
        Log.d("page", String.valueOf(page));
        Log.d("size", String.valueOf(size));
        preferences = getSharedPreferences("LoginData", MODE_PRIVATE);
        token = preferences.getString("token","Null");
        Log.d("id", token);
        content = findViewById(R.id.content);
        title = findViewById(R.id.title);
        imageView = findViewById(R.id.image);
        button_back = findViewById(R.id.back);
        like_detail = findViewById(R.id.like_detail);
        star_detail = findViewById(R.id.star_detail);
        btn_browser = findViewById(R.id.btn_browser);
        button_back.setOnClickListener(this);
        button_comments = findViewById(R.id.btn_comments);
        button_comments.setOnClickListener(this);
        like_detail.setOnClickListener(this);
        star_detail.setOnClickListener(this);
        GetNews_info();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();break;
            case R.id.btn_comments:
                Bundle bundle = new Bundle();
                bundle.putInt("id",id);
                bundle.putInt("page",page);
                bundle.putInt("size",size);
                Intent intent = new Intent(Main_News_ContentActivity.this, CommentsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.like_detail:
                islike(id);break;
            case R.id.star_detail:
                star(id);break;
        }
    }
    public void GetNews_info(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                Request request = new Request.Builder()
                        .url("http://39.106.195.109/itnews/api/news/info/" + id +"/info-full")
                        .method("GET", null)
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
                        Log.d("id", result);
                        newsDetailBean = gson.fromJson(result,NewsDetailBean.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("5555", String.valueOf(newsDetailBean.getData().getLike_num()));
                                Log.d("5555", String.valueOf(newsDetailBean.getData().getComment_num()));
                                Log.d("5555", String.valueOf(newsDetailBean.getData().getStar_num()));
                                content.setText(newsDetailBean.getData().getContent());
                                like_detail.setText(String.valueOf(newsDetailBean.getData().getLike_num()));
                                title.setText(newsDetailBean.getData().getTitle());
                                button_comments.setText(String.valueOf(newsDetailBean.getData().getComment_num()));
                                star_detail.setText(String.valueOf(newsDetailBean.getData().getStar_num()));
//                                btn_browser.setText();
                                Glide.with(imageView).load(newsDetailBean.getData().getPics().get(0)).into(imageView);
                            }
                        });
                    }
                });
            }
        });
        thread.start(); try { thread.join(); } catch (InterruptedException e) { e.printStackTrace(); }
    }
    public void islike(int id){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("text/plain");
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
                                        Toast.makeText(Main_News_ContentActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                                        //+1
                                        like_detail.setText(String.valueOf(newsDetailBean.getData().getLike_num() + 1));
                                    }
                                    else {
                                        Toast.makeText(Main_News_ContentActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                                        //-1
                                        like_detail.setText(String.valueOf(newsDetailBean.getData().getLike_num()));
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
    public void star(int id){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
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
                                        Toast.makeText(Main_News_ContentActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                                    //+1
                                        star_detail.setText(String.valueOf(newsDetailBean.getData().getStar_num() + 1));
                                    }
                                    else {
                                        Toast.makeText(Main_News_ContentActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                                        //-1
                                        star_detail.setText(String.valueOf(newsDetailBean.getData().getStar_num()));
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