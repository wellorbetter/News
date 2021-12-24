package com.example.news.UserSpace;

import android.os.Bundle;
import android.widget.Toast;

import com.example.news.R;

import androidx.appcompat.app.AppCompatActivity;

public class MyInterestActivity extends AppCompatActivity {
//我的关注
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_interest);
        Toast.makeText(MyInterestActivity.this, "QAQ" +
                "前方正在施工！", Toast.LENGTH_SHORT).show();
    }
}