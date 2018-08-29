package io.github.koocci.maknesecretnote;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends RootActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final ImageView iv = (ImageView)findViewById(R.id.imageView1);
        final Animation anim = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.set1);
        final Animation anim2 = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.set2);
        final Animation anim3 = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.set3);
        final Animation anim4 = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.set4);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* 메뉴액티비티를 실행하고 로딩화면을 죽인다.*/
                Intent mainIntent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, 2000);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.e("start", "1111");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                iv.startAnimation(anim2);
                Log.e("end", "1111");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        anim2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                iv.startAnimation(anim3);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        anim3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                iv.startAnimation(anim4);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        iv.startAnimation(anim);
    }
}
