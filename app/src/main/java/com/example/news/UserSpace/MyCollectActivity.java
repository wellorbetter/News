package com.example.news.UserSpace;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.news.Adapter.MyCollectionAdapter;
import com.example.news.FunctionalPage.Main_News_ContentActivity;
import com.example.news.R;
import com.example.news.bean.MyCollectionBean;
import com.google.firebase.database.annotations.NotNull;
import com.google.gson.Gson;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyCollectActivity extends AppCompatActivity {
    private String token;private ArrayList<MyCollectionBean>list;
    SharedPreferences preferences;
    private RecyclerView recyclerView;
    private SmartRefreshLayout smartRefreshLayout;
    private MyCollectionAdapter myCollectionAdapter;
    private TextView collection_num;
    private Button back;
    //我的收藏
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collect);
        collection_num = findViewById(R.id.collection_num);
        recyclerView = findViewById(R.id.recyclerView);
        back = findViewById(R.id.back);
        smartRefreshLayout = findViewById(R.id.smart_refresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        smartRefreshLayout.setEnableLoadMore(true);
        preferences = getSharedPreferences("LoginData", MODE_PRIVATE);
        token = preferences.getString("token", "null");
        find_collection();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                find_collection();
                smartRefreshLayout.finishRefresh(1500);
            }
        });

        //监听上滑动作（也就是加载更多）的监听器
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//                    find_collection();
                smartRefreshLayout.finishLoadMore(2000);
            }
        });
    }
    private void find_collection()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                Request request = new Request.Builder()
                        .url("http://39.106.195.109/itnews/api/self/star-news-ids")
                        .method("GET", null)
                        .addHeader("Authorization", token)
                        .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.i("onFailure",e.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MyCollectActivity.this, "连接失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String result = response.body().string();
                        Gson gson = new Gson();
                        MyCollectionBean myCollectionBean = gson.fromJson(result,MyCollectionBean.class);
                        if(!myCollectionBean.equals(""))
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("result",result);
                                    if(myCollectionBean.getData().getNews().size()==0)
                                        collection_num.setText("暂无收藏哦！");
                                    myCollectionAdapter  = new MyCollectionAdapter(MyCollectActivity.this,myCollectionBean,
                                    new MyCollectionAdapter.OnItemClickListener() {
                                        @Override
                                        public void onClick(int pos) {
                                            Intent intent = new Intent(MyCollectActivity.this, Main_News_ContentActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putInt("id", myCollectionBean.getData().getNews().get(pos).getId());
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        }
                                    });
                                    recyclerView.setAdapter(myCollectionAdapter);
                                }
                            });
                        }



                    }
                });
            }
        });
        thread.start();

        try { thread.join();} catch (InterruptedException e) { e.printStackTrace(); }
    }
}