package com.example.news.FunctionalPage;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.news.Fragment.ContainerActivity;
import com.example.news.R;

import androidx.appcompat.app.AppCompatActivity;

public class AnimationActivity extends AppCompatActivity {

    private Intent intent;
    private ImageView imageView;
    SharedPreferences preferences;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        preferences = getSharedPreferences("LoginData", MODE_PRIVATE);
        token = preferences.getString("token","null");
        imageView = findViewById(R.id.image);

        Thread myThread = new Thread() {
            @Override
            public void run() {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(imageView, "scaleX", 1f, 1.5f, 1f);
//沿y轴放大
                            ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(imageView, "scaleY", 1f, 1.5f, 1f);
                            ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "alpha", 1f, 0f);
                            scaleYAnimator.setDuration(2200).start();
                            scaleXAnimator.setDuration(2200).start();
                            animator.setDuration(2200);//时间1s
                            animator.start();
                        }
                    });
                    sleep(2000);
                    //透明度起始为1，结束时为0
                    if(!token.equals("null"))
                    {
                        intent = new Intent(AnimationActivity.this, ContainerActivity.class);
                        //这样的intent后面不能加toast
                    }
                    else {
                        intent = new Intent(AnimationActivity.this,LoginActivity.class);
                    }
                    Log.d("token",token);
                    startActivity(intent);
                    finish();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }
}