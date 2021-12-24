package com.example.news.FunctionalPage;

import android.content.Context;
import android.content.Intent;
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
import com.example.news.bean.GetPassWordBack_Bean;
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

public class GetBackPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mailbox;
    private EditText verification_code;
    private EditText password;
    private Button confirm;private Button back;private Button postemail;
    private String verify_code;private String passWord;private String mailBox;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_back_password);
        back = findViewById(R.id.back);
        confirm = findViewById(R.id.register_btn_confirm);
        verification_code = (EditText) findViewById(R.id.register_edtext_verification_code);
        mailbox = (EditText)findViewById(R.id.register_edtext_mailbox);
        password = (EditText)findViewById(R.id.new_password);
        postemail = findViewById(R.id.post_email);
        mailBox = mailbox.getText().toString();
        verify_code = verification_code.getText().toString();
        passWord = password.getText().toString();
        Log.d("userName", passWord);
        back.setOnClickListener(this);
        postemail.setOnClickListener(this);
        confirm.setOnClickListener(this);
        preferences = getSharedPreferences("LoginData",
                Context.MODE_ENABLE_WRITE_AHEAD_LOGGING | MODE_WORLD_WRITEABLE | MODE_MULTI_PROCESS);
        editor = preferences.edit();
        mailbox.setFilters(new GetBackPasswordActivity.Filter[]{new GetBackPasswordActivity.Filter()});
        verification_code.setFilters(new GetBackPasswordActivity.Filter[]{new GetBackPasswordActivity.Filter()});
        password.setFilters(new GetBackPasswordActivity.Filter[]{new GetBackPasswordActivity.Filter()});
    }

    private void get_password_back(JSONObject jsonObject)
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, String.valueOf(jsonObject));
                Request request = new Request.Builder()
                        .url("http://39.106.195.109/itnews/api/reglog/pwd-recall")
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

                        Log.i("result",result);
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
                if( !password.getText().toString().equals("")
                        && !mailbox.getText().toString().equals(""))
                {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("email",mailbox.getText().toString());
//                        jsonObject.put("password", password.getText().toString());
                        jsonObject.put("usage", (int) 2);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("225",jsonObject.toString());
                    back_password_postemail(jsonObject);
                }
                else { Toast.makeText(this, "请输入完整的信息", Toast.LENGTH_SHORT).show(); }
                break;
            case R.id.register_btn_confirm:
                if( !password.getText().toString().equals("")
                        && !mailbox.getText().toString().equals("") && !verification_code.getText().toString().equals(""))
                {

                    JSONObject jsonObject_post = new JSONObject();
                    try {
                        jsonObject_post.put("password", password.getText().toString());
                        jsonObject_post.put("email", mailbox.getText().toString());
                        jsonObject_post.put("verify", verification_code.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("225",jsonObject_post.toString());
                    get_password_back(jsonObject_post);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GetBackPasswordActivity.this,"密码找回成功！",Toast.LENGTH_SHORT).show();
                        }
                    });
                    Intent intent = new Intent(GetBackPasswordActivity.this, LoginActivity.class);
                    startActivity(intent);
//                    finish();//待改进
                }
                else { Toast.makeText(this, "请输入完整的信息", Toast.LENGTH_SHORT).show(); }
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
                    Toast.makeText(GetBackPasswordActivity.this, "不能输入空格等非法字符！", Toast.LENGTH_SHORT).show();
                    return "";
                }
                if (source.equals(" ")) {
                    Toast.makeText(GetBackPasswordActivity.this, "不能输入空格等非法字符！", Toast.LENGTH_SHORT).show();
                    return "";
                }
//限制大小
            } catch (Exception e) {
                e.printStackTrace();
            }
            return source;
        }
    }
    private void back_password_postemail(JSONObject jsonObject)
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, String.valueOf(jsonObject));
                Request request = new Request.Builder()
                        .url("http://39.106.195.109/itnews/api/reglog/code-reg")
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
                                Toast.makeText(GetBackPasswordActivity.this, "连接失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String result = response.body().string();
                        Gson gson = new Gson();
                        GetPassWordBack_Bean getPassWordBack_bean = gson.fromJson(result,GetPassWordBack_Bean.class);
                        if(getPassWordBack_bean.getCode() == 1000)
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(GetBackPasswordActivity.this,"发送成功，请注意邮件！",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }
        });
        thread.start(); try { thread.join(); } catch (InterruptedException e) { e.printStackTrace(); }

    }
}