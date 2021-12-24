package com.example.news.FunctionalPage;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.news.R;
import com.example.news.bean.LoginBean;
import com.google.firebase.database.annotations.NotNull;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mailbox;
    private EditText verification_code;
    private EditText username;
    private EditText password;
    private LoginBean loginBean; private Bundle bundle;
    private Button confirm;private Button back;private Button postemail;
    private Map<String, Object> map_register = new HashMap<String, Object>();
    private String verify_code;private String userName;private String passWord;private String mailBox;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        back = findViewById(R.id.back);
        confirm = findViewById(R.id.register_btn_confirm);
        verification_code = (EditText) findViewById(R.id.register_edtext_verification_code);
        mailbox = (EditText)findViewById(R.id.register_edtext_mailbox);
        username = (EditText)findViewById(R.id.register_username);
        password = (EditText)findViewById(R.id.register_password);
        postemail = findViewById(R.id.post_email);
        mailBox = mailbox.getText().toString();
        verify_code = verification_code.getText().toString();
        userName = username.getText().toString();
        passWord = password.getText().toString();
        Log.d("userName",userName);
        back.setOnClickListener(this);
        postemail.setOnClickListener(this);
        confirm.setOnClickListener(this);
        mailbox.setFilters(new RegisterActivity.Filter[]{new RegisterActivity.Filter()});
        verification_code.setFilters(new RegisterActivity.Filter[]{new RegisterActivity.Filter()});
        username.setFilters(new RegisterActivity.Filter[]{new RegisterActivity.Filter()});
        password.setFilters(new RegisterActivity.Filter[]{new RegisterActivity.Filter()});
    }

    private void register_postemail(JSONObject jsonObject)
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client_mailbox = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, String.valueOf(jsonObject));
                Request request = new Request.Builder()
                        .url("http://39.106.195.109/itnews/api/reglog/code-reg")
                        .method("POST", body)
                        .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                        .addHeader("Content-Type", "application/json")
                        .build();
                Call call = client_mailbox.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.i("onFailure",e.getMessage());
                        Toast.makeText(RegisterActivity.this, "连接失败！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String result = response.body().string();
                        Gson gson = new Gson();
                        LoginBean loginBean = gson.fromJson(result,LoginBean.class);
                        Log.i("result",loginBean.toString());
                        Log.i("result",result);
                    }
                });
            }
        });
        thread.start(); try { thread.join(); } catch (InterruptedException e) { e.printStackTrace(); }

    }
    private void register_confirm(JSONObject jsonObject)
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
                Log.i("55",jsonObject.toString());
                Request request = new Request.Builder()
                        .url("http://39.106.195.109/itnews/api/reglog/all-reg")
                        .method("POST", body)
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
                        loginBean = gson.fromJson(result,LoginBean.class);
                    }
                });
            }
        });
        thread.start(); try { thread.join(); } catch (InterruptedException e) { e.printStackTrace(); }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();break;
            case R.id.post_email:
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("email",mailbox.getText().toString());
                    jsonObject.put("usage", (int) 1);
                    Log.d("json",jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                register_postemail(jsonObject);
                break;
            case R.id.register_btn_confirm:
                if( !username.getText().toString().equals("")  && !password.getText().toString().equals("")
                        && !mailbox.getText().toString().equals("") && !verification_code.getText().toString().equals(""))
                {

                    JSONObject jsonObject_post = new JSONObject();
                    try {
                        jsonObject_post.put("username", username.getText().toString());
                        jsonObject_post.put("password",password.getText().toString());
                        jsonObject_post.put("email", mailbox.getText().toString());
                        jsonObject_post.put("verify", verification_code.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    register_confirm(jsonObject_post);
//                    finish();//待改进
                }
                else { Toast.makeText(this, "请输入完整的信息", Toast.LENGTH_SHORT).show(); break;}
                if(!loginBean.getData().equals("") && loginBean.getCode() == (int) 1000)//exist
                {
                    String token = loginBean.getData().getToken();
                    bundle.putString("token", token);
                    Log.d("token",token);
                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                    Toast.makeText(RegisterActivity.this,"注册成功!",Toast.LENGTH_SHORT).show();
                    intent.putExtras(bundle);
                    startActivity(intent);
                    if(loginBean.getCode() == (int) 1109){
                        Toast.makeText(RegisterActivity.this,"该邮箱已被注册",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(RegisterActivity.this,"注册未成功！请重试！",Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
    public class Filter implements InputFilter {
        @Override

        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                String speChat = "[`~!#$%^&*()+=|{}':;',\\[\\]<>/?~！#￥%……&*（）——+|{}【】‘；：”“’。，、？\n]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if (matcher.find()) {
                    Toast.makeText(RegisterActivity.this, "不能输入空格等非法字符！", Toast.LENGTH_SHORT).show();
                    return "";
                }
                if (source.equals(" ")) {
                    Toast.makeText(RegisterActivity.this, "不能输入空格等非法字符！", Toast.LENGTH_SHORT).show();
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