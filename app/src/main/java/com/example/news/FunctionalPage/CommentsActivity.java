package com.example.news.FunctionalPage;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.news.Adapter.CommentsAdapter;
import com.example.news.R;
import com.example.news.bean.CommentsBean;
import com.example.news.bean.CommentsCallbackBean;
import com.google.firebase.database.annotations.NotNull;
import com.google.gson.Gson;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CommentsActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;private SmartRefreshLayout smartRefreshLayout;
    private ArrayList<Map<String, Object>>list;
    private Button button_back;
    private SharedPreferences preferences;
    private int id;private int page;private String token;
    private int size;
    private TextView comments_num;
    private EditText comments;
    private CommentsAdapter commentsAdapter;
    private Button comments_issue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        comments = findViewById(R.id.comments);
        comments_issue = findViewById(R.id.comments_issue);
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        page = 1;
        size = 5;
        id = bundle.getInt("id",-1);
        int test = bundle.getInt("test",-1);
        Log.d("??", String.valueOf(test));
        preferences = getSharedPreferences("LoginData", MODE_PRIVATE);
        token = preferences.getString("token", "null");
        button_back = findViewById(R.id.back);
        recyclerView = findViewById(R.id.recyclerView_comments);
        smartRefreshLayout = findViewById(R.id.smart_refresh_comments);
        comments_num = findViewById(R.id.comments_num);
        button_back.setOnClickListener(this);
        comments.setOnClickListener(this);
        comments_issue.setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        smartRefreshLayout.setEnableLoadMore(true);
        get_comments();
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                commentsAdapter.notifyDataSetChanged();
                smartRefreshLayout.finishRefresh(1500);
            }
        });

        //监听上滑动作（也就是加载更多）的监听器
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//                newsPageAdapter.notifyItemInserted(list.size());
//                newsPageAdapter.notifyDataSetChanged();
                smartRefreshLayout.finishLoadMore(1500);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();break;
            case R.id.comments_issue:
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("content",comments.getText());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                comments(jsonObject);
        }
    }
    private void get_comments()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                Request request = new Request.Builder()
                        .url("http://39.106.195.109/itnews/api/news/info/" + id +"/comment?page=" + page +"&size=" + size)
                        .method("GET", null)
                        .addHeader("Authorization", token)
                        .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                        .addHeader("Content-Type", "application/json")
                        .build();
                Log.d("mdg", String.valueOf(id));
                Log.d("mdg", String.valueOf(page));
                Log.d("mdg", String.valueOf(size));
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.i("onFailure",e.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(CommentsActivity.this, "连接失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String result = response.body().string();
                        Log.d("result",result);
                        Gson gson = new Gson();
                        CommentsBean commentsBean = gson.fromJson(result,CommentsBean.class);
                        if(!commentsBean.equals(""))
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    comments_num.setText("评论总数：" + commentsBean.getData().getComments().size());
                                    commentsAdapter = new CommentsAdapter(CommentsActivity.this,commentsBean);
                                    recyclerView.setAdapter(commentsAdapter);
                                }
                            });

                        }
                        Log.i("result",commentsBean.toString());
                        Log.i("result",result);
                    }
                });
            }
        });
        thread.start();

        try { thread.join();} catch (InterruptedException e) { e.printStackTrace(); }
    }
    private void comments(JSONObject jsonObject)
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, String.valueOf(jsonObject));
                Request request = new Request.Builder()
                        .url("http://39.106.195.109/itnews/api/news/operator/"+id+"/comment")
                        .method("POST", body)
                        .addHeader("Authorization", token)
                        .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                        .addHeader("Content-Type", "application/json")
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.i("onFailure",e.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(CommentsActivity.this, "连接失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String result = response.body().string();
                        Log.d("result",result);
                        Gson gson = new Gson();
                        CommentsCallbackBean commentsCallbackBean = gson.fromJson(result,CommentsCallbackBean.class);
                        if(!commentsCallbackBean.equals("") && commentsCallbackBean.getCode() == 1000)
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                   Toast.makeText(CommentsActivity.this,"评论成功！",Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                        Log.i("result",result);
                    }
                });
            }
        });
        thread.start();

        try { thread.join();} catch (InterruptedException e) { e.printStackTrace(); }
    }
}