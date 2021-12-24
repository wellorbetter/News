package com.example.news.UserSpace;

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
import com.example.news.bean.MyInfoBean;
import com.google.firebase.database.annotations.NotNull;
import com.google.gson.Gson;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences preferences;
    private String token;
    private TextView info;private TextView username;
    private TextView gender;private TextView nickname;
    private TextView selfid;private ImageView avatar;
    private TextView like_num;private TextView star_num;
    private TextView follow_num;private TextView fans_num;
    private Button change;private Button back;
    private MyInfoBean myInfoBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        info = findViewById(R.id.info);
        fans_num = findViewById(R.id.fans_num);
        change = findViewById(R.id.change);
        follow_num = findViewById(R.id.follow_num);
        star_num = findViewById(R.id.star_num);
        like_num = findViewById(R.id.like_num);
        avatar = findViewById(R.id.avatar);
        selfid = findViewById(R.id.selfid);
        nickname = findViewById(R.id.nickname);
        gender = findViewById(R.id.gender);
        username = findViewById(R.id.username);
        back = findViewById(R.id.back);
        change.setOnClickListener(this);
        back.setOnClickListener(this);
        preferences = getSharedPreferences("LoginData", MODE_PRIVATE);
        token = preferences.getString("token", "null");
        get_info();
    }
    private void get_info()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                Request request = new Request.Builder()
                        .url("http://39.106.195.109/itnews/api/self/info")
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
                                Toast.makeText(MyInfoActivity.this, "连接失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String result = response.body().string();
                        Log.d("result",result);
                        Gson gson = new Gson();
                        myInfoBean = gson.fromJson(result,MyInfoBean.class);
                        if(!myInfoBean.equals(""))
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    info.setText("签名:"+myInfoBean.getData().getInfo());
                                    like_num.setText("like_num:"+String.valueOf(myInfoBean.getData().getLike_num()));
                                    star_num.setText("star_num:"+String.valueOf(myInfoBean.getData().getStar_num()));
                                    selfid.setText("selfid:"+String.valueOf(myInfoBean.getData().getSelfid()));
                                    fans_num.setText("fans_num:"+String.valueOf(myInfoBean.getData().getFans_num()));
                                    follow_num.setText("follow_num:"+String.valueOf(myInfoBean.getData().getFollow_num()));
                                    username.setText("username:"+myInfoBean.getData().getUsername());
                                    nickname.setText("nickname:"+myInfoBean.getData().getNickname());
                                    gender.setText("gender:"+myInfoBean.getData().getGender());
                                    Glide.with(avatar).load(myInfoBean.getData().getAvatar()).into(avatar);
                                }

                            });

                        }

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
            case R.id.change:
                Bundle bundle = new Bundle();
                Intent intent = new Intent(MyInfoActivity.this,ChangeInfoActivity.class);
                bundle.putString("info",String.valueOf(myInfoBean.getData().getInfo()));
                bundle.putString("nickname",String.valueOf(myInfoBean.getData().getNickname()));
                bundle.putString("gender",String.valueOf(myInfoBean.getData().getGender()));
                Log.d("info",String.valueOf(myInfoBean.getData().getInfo()));
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.back:
                finish();
        }
    }
}