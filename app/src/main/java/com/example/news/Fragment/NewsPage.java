package com.example.news.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.news.Adapter.NewsPageAdapter;
import com.example.news.FunctionalPage.Main_News_ContentActivity;
import com.example.news.R;
import com.example.news.bean.News_brief_Bean;
import com.google.firebase.database.annotations.NotNull;
import com.google.gson.Gson;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class NewsPage extends Fragment {
    private RecyclerView recyclerView; private SmartRefreshLayout smartRefreshLayout;
    private ArrayList<News_brief_Bean>list = new ArrayList<>();
    private int page = 1;
    private int size = 5;
    private String Authorization = "null";
    private SharedPreferences preferences;
    private News_brief_Bean news_brief_bean;
    private NewsPageAdapter newsPageAdapter;
    private int tag = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newspage, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        smartRefreshLayout = view.findViewById(R.id.smart_refresh);
        preferences = getActivity().getSharedPreferences("LoginData", MODE_PRIVATE);
        Authorization = preferences.getString("token", "error");
        Log.d("preferences", preferences.toString());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        smartRefreshLayout.setEnableLoadMore(true);
        news_brief_bean = new News_brief_Bean();
        page = 1;
        GetNews_id();
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//                int temp = page;
//                page = 1;
                page = 1;
                refresh();
                Log.d("5555", String.valueOf(tag));
//                page = temp;
                smartRefreshLayout.finishRefresh(1500);
            }
        });

        //监听上滑动作（也就是加载更多）的监听器
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page += 1;
                if(news_brief_bean != null && news_brief_bean.getData() != null) {
                    Log.d("refresh", news_brief_bean.getData().getNews().toString());
                    if (page < news_brief_bean.getData().getNews().size()) {
                        refresh_add();
                    } else if (news_brief_bean.getData().getNews().size() != 0) {
                        refresh_add();
                        while (news_brief_bean.getCode() != 1000) {
                            size--;
                            refresh_add();
                            if (news_brief_bean.getCode() == 1000)
                                break;
                        }
                    } else {
                        Toast.makeText(getActivity(), "没有更多了！", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getActivity(), "没有更多了！", Toast.LENGTH_SHORT).show();
                }
                smartRefreshLayout.finishLoadMore(2000);
            }
        });
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    public void GetNews_id(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                Request request = new Request.Builder()
                        .url("http://39.106.195.109/itnews/api/news/recommend/v4?page=" + page + "&size=" + size)
                        .method("GET", null)
                        .addHeader("Authorization", Authorization)
                        .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                        .addHeader("Content-Type", "application/json")
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
                        news_brief_bean = gson.fromJson(result,News_brief_Bean.class);
                        if(news_brief_bean != null && news_brief_bean.getCode() == 1000) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    list.add(news_brief_bean);
                                    newsPageAdapter = new NewsPageAdapter(getActivity(), list, new NewsPageAdapter.OnItemClickListener() {
                                        @Override
                                        public void onClick(int pos) {

                                            Bundle bundle = new Bundle();
                                            int page_tmp = pos / 5;
                                            int cnt = pos % 5;
                                            bundle.putInt("id", list.get(page_tmp).getData().getNews().get(cnt).getId());
                                            bundle.putInt("page", page_tmp + 1);
                                            bundle.putInt("size", cnt + 1);
                                            Intent intent = new Intent(getActivity(), Main_News_ContentActivity.class);
                                            intent.putExtras(bundle);
                                            //出现bundle传值 在另一个activity中接收失败的问题
                                            getActivity().startActivity(intent);
                                        }
                                    });
                                    recyclerView.setAdapter(newsPageAdapter);
                                }
                            });
                        }
                    }
                });
            }
        });
        thread.start(); try { thread.join(); } catch (InterruptedException e) { e.printStackTrace(); }
    }
    public void refresh_add(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                Request request = new Request.Builder()
                        .url("http://39.106.195.109/itnews/api/news/recommend/v4?page=" + page + "&size=" + size)
                        .method("GET", null)
                        .addHeader("Authorization", Authorization)
                        .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                        .addHeader("Content-Type", "application/json")
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
                        news_brief_bean = gson.fromJson(result,News_brief_Bean.class);
                        if(news_brief_bean != null && news_brief_bean.getCode() == 1000) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ArrayList<News_brief_Bean>list = new ArrayList<>();
                                    list.add(news_brief_bean);
                                    newsPageAdapter.add(list);
                                    newsPageAdapter.notifyItemRangeInserted((page-1)*5,5);
                                }
                            });

                        }
                        else {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "没有更多了！", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        Log.i("result",result);
                    }
                });
            }
        });
        thread.start(); try { thread.join(); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    public void refresh(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                Request request = new Request.Builder()
                        .url("http://39.106.195.109/itnews/api/news/recommend/v4?page=" + 1 + "&size=" + 5)
                        .method("GET", null)
                        .addHeader("Authorization", Authorization)
                        .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                        .addHeader("Content-Type", "application/json")
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
                        news_brief_bean = gson.fromJson(result,News_brief_Bean.class);
                        if(news_brief_bean != null && news_brief_bean.getCode() == 1000) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ArrayList<News_brief_Bean>list = new ArrayList<>();
                                    list.add(news_brief_bean);
                                    newsPageAdapter.refresh(list);
                                    newsPageAdapter.notifyItemChanged(0,5);
                                }
                            });

                        }
                        else {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "没有更多了！", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        Log.i("result",result);
                    }
                });
            }
        });
        thread.start(); try { thread.join(); } catch (InterruptedException e) { e.printStackTrace(); }

    }


}