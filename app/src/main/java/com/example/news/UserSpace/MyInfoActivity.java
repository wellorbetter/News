package com.example.news.UserSpace;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.news.R;
import com.example.news.bean.ChangeInfoBean;
import com.example.news.bean.MyInfoBean;
import com.google.firebase.database.annotations.NotNull;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences preferences;
    private String token;
    private EditText info;
    private EditText gender;
    private EditText nickname;
    private ImageView avatar;
    private Button back;
    private MyInfoBean myInfoBean;
    private Button change_confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        info = findViewById(R.id.info);
        avatar = findViewById(R.id.avatar);
        change_confirm = findViewById(R.id.change_confirm);
        nickname = findViewById(R.id.nickname);
        gender = findViewById(R.id.gender);
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        change_confirm.setOnClickListener(this);
        preferences = getSharedPreferences("LoginData", MODE_PRIVATE);
        token = preferences.getString("token", "null");
        nickname.setFilters(new MyInfoActivity.Filter[]{new MyInfoActivity.Filter()});
        gender.setFilters(new MyInfoActivity.Filter[]{new MyInfoActivity.Filter()});
        info.setFilters(new MyInfoActivity.Filter[]{new MyInfoActivity.Filter()});
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
                        if(!myInfoBean.equals("") && myInfoBean.getCode() == 1000)
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("??",myInfoBean.getData().getNickname());
                                    info.setText(myInfoBean.getData().getInfo());
                                    nickname.setText(myInfoBean.getData().getNickname());
                                    gender.setText(myInfoBean.getData().getGender());
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

    private void change_info(JSONObject jsonObject)
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, String.valueOf(jsonObject));
                Request request = new Request.Builder()
                        .url("http://39.106.195.109/itnews/api/self/info-refresh")
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
                                Toast.makeText(MyInfoActivity.this, "连接失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String result = response.body().string();
                        Log.d("result",result);
                        Gson gson = new Gson();
                        ChangeInfoBean changeInfoBean = gson.fromJson(result,ChangeInfoBean.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(!changeInfoBean.equals("") ) {
                                        if(changeInfoBean.getCode() == 1000) {
                                            Toast.makeText(MyInfoActivity.this, "更改信息成功！", Toast.LENGTH_SHORT).show();
                                        }
                                        else if(changeInfoBean.getCode() == 993)
                                        {
                                            Toast.makeText(MyInfoActivity.this, "昵称不符合规则?再检查一下吧！", Toast.LENGTH_SHORT).show();
                                        }
                                        else if(changeInfoBean.getCode() == 991)
                                        {
                                            Toast.makeText(MyInfoActivity.this, "性别不符合规则?再检查一下吧！", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                    }

                });

            }

        });
        thread.start();
        try { thread.join(); } catch (InterruptedException e) { e.printStackTrace(); }
    }
    public class Filter implements InputFilter {
        @Override

        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source.equals(" ") || source.toString().contentEquals("\n")) {
                return "";
            } else {
                return null;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("info",String.valueOf( info.getText()));
                    jsonObject.put("nickname",String.valueOf( nickname.getText()));
                    jsonObject.put("gender",String.valueOf( gender.getText()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                change_info(jsonObject);
                finish();
            case R.id.change_confirm:
                JSONObject jsonObject_ = new JSONObject();
                try {
                    jsonObject_.put("info",String.valueOf( info.getText()));
                    jsonObject_.put("nickname",String.valueOf( nickname.getText()));
                    jsonObject_.put("gender",String.valueOf( gender.getText()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                change_info(jsonObject_);
        }
    }

}