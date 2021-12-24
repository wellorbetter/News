package com.example.news.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.news.R;

import androidx.appcompat.app.AppCompatActivity;

public class ContainerActivity extends AppCompatActivity implements View.OnClickListener {
    private CommitPage commitPage; private MyOwnPage myOwnPage;private NewsPage newsPage;
//    private Button back;
    private Bundle bundle_receive;private Bundle bundle;
    private String username = "游客";private String password;private String token;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        Toast.makeText(ContainerActivity.this, "欢迎回来", Toast.LENGTH_SHORT).show();
        preferences = getSharedPreferences("LoginData", MODE_PRIVATE);
        editor = preferences.edit();
        LinearLayout MyOwnPage = findViewById(R.id.LinearLayout_MyOwnPage);
        LinearLayout NewsPage = findViewById(R.id.LinearLayout_NewsPage);
        LinearLayout CommitPage = findViewById(R.id.LinearLayout_CommitPage);
        bundle_receive = getIntent().getExtras();
        if(bundle_receive != null)
        {
            username = bundle_receive.getString("username");
            password = bundle_receive.getString("password");
            token = bundle_receive.getString("token");
        }
        InitFragment();Log.d("username",username);
        bundle = new Bundle();
        bundle.putString("username",username);bundle.putString("token",token);
        myOwnPage.setArguments(bundle);
//        back = findViewById(R.id.back);
//        back.setOnClickListener(this);
        MyOwnPage.setOnClickListener(this);
        NewsPage.setOnClickListener(this);
        CommitPage.setOnClickListener(this);
    }

    private void InitFragment() {
        myOwnPage = new MyOwnPage();
        newsPage = new NewsPage();
        commitPage = new CommitPage();
        getFragmentManager().beginTransaction().add(R.id.framelayout_container,myOwnPage)
                .add(R.id.framelayout_container,newsPage)
                .add(R.id.framelayout_container,commitPage).commitAllowingStateLoss();
        getFragmentManager().beginTransaction().hide(commitPage).hide(myOwnPage).show(newsPage).commitAllowingStateLoss();
    }

    @Override
    public void onClick(View v) {
        hideFragment();
        switch (v.getId()){
            case R.id.LinearLayout_MyOwnPage: {
                Log.d("5556","show");
                if (myOwnPage == null)
                    myOwnPage = new MyOwnPage();
                if (bundle_receive != null) myOwnPage.setArguments(bundle_receive);
                getFragmentManager().beginTransaction().show(myOwnPage).commitAllowingStateLoss();
            }
//                getFragmentManager().beginTransaction().replace(R.id.framelayout_container,myOwnPage).commitAllowingStateLoss();
                break;
            case R.id.LinearLayout_NewsPage: {
                if (newsPage == null)
                    newsPage = new NewsPage();
                Log.d("5556","show");
                getFragmentManager().beginTransaction().show(newsPage).commitAllowingStateLoss();
            }
//                getFragmentManager().beginTransaction().replace(R.id.framelayout_container,newsPage).commitAllowingStateLoss();
                break;
            case R.id.LinearLayout_CommitPage: {
                Log.d("5556","show");
                if (commitPage == null)
                    commitPage = new CommitPage();
                getFragmentManager().beginTransaction().show(commitPage).commitAllowingStateLoss();
            }
//                getFragmentManager().beginTransaction().replace(R.id.framelayout_container,commitPage).commitAllowingStateLoss();
                break;
//            case R.id.back:
//                finish();
//                break;
        }
    }
    private void hideFragment()
    {
        if(newsPage != null && newsPage.isAdded()) {
            Log.d("hide","hide");
            getFragmentManager().beginTransaction().hide(newsPage).commitAllowingStateLoss();
        }
        if(myOwnPage != null && myOwnPage.isAdded()) {
            Log.d("hide","hide");
            getFragmentManager().beginTransaction().hide(myOwnPage).commitAllowingStateLoss();
        }
        if(commitPage != null && commitPage.isAdded()) {
            Log.d("hide","hide");
            getFragmentManager().beginTransaction().hide(commitPage).commitAllowingStateLoss();
        }
    }
}