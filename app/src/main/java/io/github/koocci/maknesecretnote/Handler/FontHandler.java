package io.github.koocci.maknesecretnote.Handler; /**
 * Created by gujinhyeon on 2018. 8. 28..
 */

import android.app.Application;

import com.tsengvn.typekit.Typekit;

public class FontHandler extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Typekit.getInstance().addNormal(Typekit.createFromAsset(this, "fonts/Typo_CrayonM.ttf"));
        Typekit.getInstance().addBold(Typekit.createFromAsset(this, "fonts/Typo_CrayonM.ttf"));
    }
}
