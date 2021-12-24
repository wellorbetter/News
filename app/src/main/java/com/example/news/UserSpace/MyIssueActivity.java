package com.example.news.UserSpace;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.news.Adapter.MyIssueAdapter;
import com.example.news.FunctionalPage.Main_News_ContentActivity;
import com.example.news.R;
import com.example.news.bean.MyIssueBean;
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

public class MyIssueActivity extends AppCompatActivity implements View.OnClickListener {
    private String token;private RecyclerView recyclerView;private SmartRefreshLayout smartRefreshLayout;
    private Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_issue);
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        if(bundle!=null)
        { token = bundle.getString("token", token); }
        Log.d("token",token);
        back = findViewById(R.id.back);
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
                smartRefreshLayout.finishLoadMore(1500);
            }
        });
        find_issue_news();
    }
    private void find_issue_news()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                Request request = new Request.Builder()
                        .url("http://39.106.195.109/itnews/api/self/news-ids")
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

                                Toast.makeText(MyIssueActivity.this, "连接失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String result = response.body().string();
                        Log.d("result",result);
                        Gson gson = new Gson();
                        MyIssueBean myIssueBean = gson.fromJson(result,MyIssueBean.class);
                        if(!myIssueBean.equals(""))
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //setadapter
                                    MyIssueAdapter myIssueAdapter = new MyIssueAdapter(
                                            MyIssueActivity.this, myIssueBean, new MyIssueAdapter.OnItemClickListener() {
                                        @Override
                                        public void onClick(int pos) {
                                            Intent intent = new Intent(MyIssueActivity.this, Main_News_ContentActivity.class);
                                            Bundle bundle =new Bundle();
                                            bundle.putInt("id",myIssueBean.getData().getNews().get(pos).getId());
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        }
                                    });
                                    recyclerView.setAdapter(myIssueAdapter);
                                }

                            });

                        }

                        Log.i("result",myIssueBean.toString());
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