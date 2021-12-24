package com.example.news.UserSpace;

        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
        import android.widget.TextView;
        import android.widget.Toast;

import com.example.news.Adapter.MyAppreciationAdapter;
import com.example.news.FunctionalPage.Main_News_ContentActivity;
import com.example.news.R;
import com.example.news.bean.MyAppreciationBean;
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

public class MyAppreciationActivity extends AppCompatActivity implements View.OnClickListener {
    private String token;
    private RecyclerView recyclerView;
    private SmartRefreshLayout smartRefreshLayout;
    private Button back; private TextView my_appreciation_num;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appreciation);

        back = findViewById(R.id.back);
        recyclerView = findViewById(R.id.recyclerView);
        my_appreciation_num = findViewById(R.id.my_appreciation_num);
        smartRefreshLayout = findViewById(R.id.smart_refresh);
        back.setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        smartRefreshLayout.setEnableLoadMore(true);
        preferences = getSharedPreferences("LoginData", MODE_PRIVATE);
        token = preferences.getString("token","Null");
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
        find_like_news();
    }

    private void find_like_news() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                Request request = new Request.Builder()
                        .url("http://39.106.195.109/itnews/api/self/like-news-ids")
                        .method("GET", null)
                        .addHeader("Authorization", token)
                        .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                        .addHeader("Content-Type", "application/json")
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.i("onFailure", e.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(MyAppreciationActivity.this, "连接失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String result = response.body().string();
                        Log.d("result", result);
                        Gson gson = new Gson();
                        MyAppreciationBean myAppreciationBean = gson.fromJson(result, MyAppreciationBean.class);
                        if (!myAppreciationBean.equals("")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(myAppreciationBean.getData().getNews().size()==0)
                                        my_appreciation_num.setText("暂无点赞新闻！");
                                    //setadapter
                                    MyAppreciationAdapter myAppreciationAdapter = new MyAppreciationAdapter(
                                            MyAppreciationActivity.this, myAppreciationBean, new MyAppreciationAdapter.OnItemClickListener() {
                                        @Override
                                        public void onClick(int pos) {
                                            Intent intent = new Intent(MyAppreciationActivity.this, Main_News_ContentActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putInt("id", myAppreciationBean.getData().getNews().get(pos).getId());
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        }
                                    });
                                    recyclerView.setAdapter(myAppreciationAdapter);
                                }

                            });

                        }
                    }

                });

            }

        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }
}