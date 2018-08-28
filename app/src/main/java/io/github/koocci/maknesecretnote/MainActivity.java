package io.github.koocci.maknesecretnote;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.security.MessageDigest;
import java.util.ArrayList;

import io.github.koocci.maknesecretnote.Adapter.FoodMarketAdapter;
import io.github.koocci.maknesecretnote.DO.FoodMarketItem;

public class MainActivity extends AppCompatActivity
    implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "mainActivity~";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegistActivity.class));
            }
        });

        ListView listView = findViewById(R.id.list_view);
        ArrayList<FoodMarketItem> items = new ArrayList<>();

//        private int thumbnail;
//        private String name;
//        private String loc;
//        private int pref;
//        private int count;

        for(int i = 1; i < 100; i++){
            items.add(new FoodMarketItem(
                    R.drawable.ic_launcher_background,
                    "Test",
                    "우리집앞ㅁㄴㅇ러미ㅏㅇ너라ㅣ먼ㅇ리ㅏ머이ㅏ러미ㅏㅇㄴ러",
                    5,
                    10000));
        }

        FoodMarketAdapter listAdapter = new FoodMarketAdapter(this, R.layout.food_market_item, items);

        listView.setAdapter(listAdapter);

        Spinner spinner = findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(this);

        String[] array = {
                "과장님 선호도 순",
                "팀장님 선호도 순",
                "상무님 선호도 순"

        };

        ArrayAdapter adapter = new ArrayAdapter(
                getApplicationContext(), // 현재화면의 제어권자
                android.R.layout.simple_spinner_item, // 레이아웃
                array); // 데이터

        spinner.setAdapter(adapter);

        //getAppKeyHash();
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)

        Log.e(TAG, "pos : " + pos);
        Log.e(TAG, "id : " + id);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void getAppKeyHash() {
        //1vPV5o7VbVdsRAZNc1Aacdq2zTU=
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("Hash key", something);
            }
        } catch (Exception e) {
            Log.e("name not found", e.toString());
        }
    }
}
