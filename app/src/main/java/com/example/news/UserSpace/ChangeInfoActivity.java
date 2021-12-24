package com.example.news.UserSpace;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.news.R;
import com.example.news.bean.ChangeInfoBean;
import com.google.firebase.database.annotations.NotNull;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChangeInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences preferences;private Button change_confirm;
    private Button back;
    private String token;String nickname;String gender; String info;
    private EditText nickname_ed;private EditText gender_ed;private EditText info_ed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        nickname = bundle.getString("nickname");
        gender = bundle.getString("gender");
        info = bundle.getString("info");
        Log.d("info",info);
        preferences = getSharedPreferences("LoginData", MODE_PRIVATE);
        token = preferences.getString("token", "null");
        nickname_ed = findViewById(R.id.nickname);
        gender_ed = findViewById(R.id.gender);
        info_ed = findViewById(R.id.info);
        change_confirm = findViewById(R.id.change_confirm);
        back = findViewById(R.id.back);
        change_confirm.setOnClickListener(this);
        back.setOnClickListener(this);
        nickname_ed.setText("昵称：" + nickname);
        gender_ed.setText("性别：" + gender);
        info_ed.setText("签名：" + info);
        nickname_ed.setFilters(new ChangeInfoActivity.Filter[]{new ChangeInfoActivity.Filter()});
        gender_ed.setFilters(new ChangeInfoActivity.Filter[]{new ChangeInfoActivity.Filter()});
        info_ed.setFilters(new ChangeInfoActivity.Filter[]{new ChangeInfoActivity.Filter()});
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
                                Toast.makeText(ChangeInfoActivity.this, "连接失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String result = response.body().string();
                        Log.d("result",result);
                        Gson gson = new Gson();
                        ChangeInfoBean changeInfoBean = gson.fromJson(result,ChangeInfoBean.class);
                        if(!changeInfoBean.equals("") && changeInfoBean.getCode() == 1000)
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ChangeInfoActivity.this, "更改信息成功！", Toast.LENGTH_SHORT).show();
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
            case R.id.change_confirm:
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("info",String.valueOf( info_ed.getText()));
                    jsonObject.put("nickname",String.valueOf( nickname_ed.getText()));
                    jsonObject.put("gender",String.valueOf( gender_ed.getText()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                change_info(jsonObject);
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    public class Filter implements InputFilter {
        @Override

        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                String speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\]<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\n]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if (matcher.find()) {
                    Toast.makeText(ChangeInfoActivity.this, "不能输入空格等非法字符！", Toast.LENGTH_SHORT).show();
                    return "";
                }
                if (source.equals(" ")) {
                    Toast.makeText(ChangeInfoActivity.this, "不能输入空格等非法字符！", Toast.LENGTH_SHORT).show();
                    return "";
                }
//限制大小
            } catch (Exception e) {
                e.printStackTrace();
            }
            return source;
        }
    }
}