package io.github.koocci.maknesecretnote.Handler;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import io.github.koocci.maknesecretnote.MainActivity;

/**
 * Created by gujinhyeon on 2018. 8. 29..
 */

public class BackPressHandler {
    private long backKeyPressedTime = 0;
    private Toast toast;

    private Activity activity;

    public BackPressHandler(Activity context) {
        this.activity = context;
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            toast.cancel();

            Intent t = new Intent(activity, MainActivity.class);
            activity.startActivity(t);

            activity.moveTaskToBack(true);
            activity.finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    public void showGuide() {
        toast = Toast.makeText(activity, "한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
        toast.show();
    }

}
