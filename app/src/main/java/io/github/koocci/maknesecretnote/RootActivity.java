package io.github.koocci.maknesecretnote;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.tsengvn.typekit.TypekitContextWrapper;

/**
 * Created by gujinhyeon on 2018. 8. 28..
 */

public class RootActivity extends AppCompatActivity {

    // 폰트 변경

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}