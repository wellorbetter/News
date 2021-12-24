package com.example.news.FunctionalPage;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.news.Fragment.ContainerActivity;
import com.example.news.R;
import com.example.news.Util.IntentReceiver;
import com.example.news.bean.LoginBean;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_mainapp;private Button btn_register; private EditText editText_username;private EditText editText_password;
    private Button findpassword;private Bundle bundle;private String token;
    private String username = null;
    private String password = null;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private int tag = 1;
    private String token_get_login = "null";
    private BroadcastReceiver receiver = new IntentReceiver();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(receiver, filter);
        btn_mainapp = findViewById(R.id.btn_mainapp);
        btn_register = findViewById(R.id.btn_register);
        findpassword = findViewById(R.id.btn_findpassword);
        editText_username = findViewById(R.id.ed_username);
        editText_password = findViewById(R.id.ed_password);
        editText_username.setFilters(new Filter[]{new Filter()});
        editText_password.setFilters(new Filter[]{new Filter()});
        username = editText_username.getText().toString();
        password = editText_password.getText().toString();
        bundle = getIntent().getExtras();
        preferences = getSharedPreferences("LoginData", MODE_PRIVATE);
        editor = preferences.edit();
        if(!preferences.getString("token","null").equals("null"))
        {
            Intent intent = new Intent(LoginActivity.this, ContainerActivity.class);
            Toast.makeText(LoginActivity.this,"欢迎回来！" + preferences.getString("username","游客"),Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
        if(bundle != null) {
            token = bundle.getString("token");
            if(preferences.getString("token","null")!="null") //first?
            { editor.putString("token", token).commit(); }
        }
        btn_mainapp.setOnClickListener(this);
        findpassword.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_mainapp:
                if(editText_username.getText().toString().equals("") || editText_password.getText().toString().equals(""))
                { Toast.makeText(LoginActivity.this,"未输入完整信息！",Toast.LENGTH_SHORT).show(); break;}
                //我还有弄个验证的东西看他能不能登录
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("username", editText_username.getText().toString());
                    jsonObject.put("password",editText_password.getText().toString());
//                    jsonObject.put("token",token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                login(jsonObject);
                Log.i("555","后执行");
                break;
            case R.id.btn_register:
                Intent intent_register = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent_register);
                break;
            case R.id.btn_findpassword:
                Intent intent_findpassword = new Intent(LoginActivity.this, GetBackPasswordActivity.class);
                startActivity(intent_findpassword);
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
                    Toast.makeText(LoginActivity.this, "不能输入空格等非法字符！", Toast.LENGTH_SHORT).show();
                    return "";
                }
                if (source.equals(" ")) {
                    Toast.makeText(LoginActivity.this, "不能输入空格等非法字符！", Toast.LENGTH_SHORT).show();
                    return "";
                }
//限制大小
            } catch (Exception e) {
                e.printStackTrace();
            }
            return source;
        }
    }
    private void login(JSONObject jsonObject)
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                tag = 0;
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
                Request request = new Request.Builder()
                        .url("http://39.106.195.109/itnews/api/reglog/all-log")
                        .method("POST", body)
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
                                Toast.makeText(LoginActivity.this, "连接失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String result = response.body().string();
                        Gson gson = new Gson();
                        LoginBean loginBean = gson.fromJson(result,LoginBean.class);
                        if(!loginBean.equals(""))
                        {
                            token_get_login = loginBean.getData().getToken();
                            editor.putString("token", token_get_login);
                            editor.putString("username", editText_username.getText().toString());
                            editor.putString("password", editText_password.getText().toString());
                            editor.commit();
                            Log.i("555",token_get_login);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(!token_get_login.equals(""))//主线程和子线程之间的关系
                                    {
                                        Intent intent = new Intent(LoginActivity.this, ContainerActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("username",editText_username.getText().toString());
                                        bundle.putString("password",editText_password.getText().toString());
                                        bundle.putString("token", token_get_login);

                                        Log.i("token",token_get_login);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                    else {Toast.makeText(LoginActivity.this,"未登录成功！",Toast.LENGTH_SHORT).show();}
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