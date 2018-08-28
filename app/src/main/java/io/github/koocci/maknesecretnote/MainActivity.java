package io.github.koocci.maknesecretnote;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import java.util.List;

import io.github.koocci.maknesecretnote.Adapter.FoodMarketAdapter;
import io.github.koocci.maknesecretnote.Adapter.MainSpinnerAdapter;
import io.github.koocci.maknesecretnote.DO.FoodMarketItem;

public class MainActivity extends AppCompatActivity
    implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "mainActivity~";
    private static final int CHINESE = 1;
    private static final int KOREAN = 2;
    private static final int JAPANESE = 3;
    private static final int CHICKEN = 4;
    private static final int MEAT = 5;
    private static final int EXTRA = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
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

        for(int i = 1; i < 100; i++){
            items.add(new FoodMarketItem(
                    R.drawable.people,
                    "Test",
                    "TestTestTestTestTestTestTestTestTestTestTest",
                    3,
                    10000,
                    KOREAN));
        }

        FoodMarketAdapter listAdapter = new FoodMarketAdapter(this, R.layout.food_market_item, items);

        listView.setAdapter(listAdapter);

        Spinner spinner = findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(this);

        List<String> data = new ArrayList<>();
        data.add("종합 선호도 순");
        data.add("홍길동 전무님 선호도 순");
        data.add("홍길동 상무님 선호도 순");
        data.add("홍길동 팀장님 선호도 순");
        data.add("홍길동 차장님 선호도 순");
        data.add("홍길동 부장님 선호도 순");
        data.add("홍길동 과장님 선호도 순");
        data.add("홍길동 대리님 선호도 순");

        MainSpinnerAdapter spinAdapter = new MainSpinnerAdapter(this, data);
        spinner.setAdapter(spinAdapter);

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
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return false;
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
