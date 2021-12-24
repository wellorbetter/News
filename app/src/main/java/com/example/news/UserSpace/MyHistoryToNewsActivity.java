package com.example.news.UserSpace;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.news.Adapter.HIstoryToNewsAdapter;
import com.example.news.FunctionalPage.Main_News_ContentActivity;
import com.example.news.R;
import com.example.news.bean.History_News_Bean;
import com.google.firebase.database.annotations.NotNull;
import com.google.gson.Gson;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyHistoryToNewsActivity extends AppCompatActivity implements View.OnClickListener {
    private String token;private RecyclerView recyclerView;private SmartRefreshLayout smartRefreshLayout;
    private Button back;private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_history);
//        Bundle bundle = new Bundle();
//        bundle = getIntent().getExtras();
//        if(bundle!=null)
//        { token = bundle.getString("token", token); }
//        Log.d("token",token);

        back = findViewById(R.id.back);
        preferences = getSharedPreferences("LoginData", MODE_PRIVATE);
        token = preferences.getString("token", "null");
        recyclerView = findViewById(R.id.recyclerView);
        smartRefreshLayout = findViewById(R.id.smart_refresh);
        back.setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        smartRefreshLayout.setEnableLoadMore(true);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishRefresh(1500);
            }
        });

        //监听上滑动作（也就是加载更多）的监听器
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                Toast.makeText(MyHistoryToNewsActivity.this, "没有更多了！", Toast.LENGTH_SHORT).show();
                smartRefreshLayout.finishLoadMore(1500);
            }
        });
        find_history_news();
    }
    private void find_history_news()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                Request request = new Request.Builder()
                        .url("http://39.106.195.109/itnews/api/self/history-news")
                        .method("GET", null)
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

                                Toast.makeText(MyHistoryToNewsActivity.this, "连接失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String result = response.body().string();
                        Log.d("result",result);
                        Gson gson = new Gson();
                        History_News_Bean history_news_bean = gson.fromJson(result,History_News_Bean.class);
                        if(!history_news_bean.equals(""))
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                               //setadapter
                                    HIstoryToNewsAdapter hIstoryToNewsAdapter = new HIstoryToNewsAdapter(
                                            MyHistoryToNewsActivity.this, history_news_bean, new HIstoryToNewsAdapter.OnItemClickListener() {
                                        @Override
                                        public void onClick(int pos) {
                                            Intent intent = new Intent(MyHistoryToNewsActivity.this, Main_News_ContentActivity.class);
                                            Bundle bundle =new Bundle();
                                            bundle.putInt("id",history_news_bean.getData().getNews().get(pos).getNews_id());
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        }
                                    });
                                    recyclerView.setAdapter(hIstoryToNewsAdapter);
                                }

                            });

                        }

                        Log.i("result",history_news_bean.toString());
                        Log.i("result",result);
                    }

                });

            }

        });
        thread.start();
        try { thread.join(); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:finish();break;
        }
    }

}