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
import java.util.Map;

import io.github.koocci.maknesecretnote.Adapter.FoodMarketAdapter;
import io.github.koocci.maknesecretnote.Adapter.MainSpinnerAdapter;
import io.github.koocci.maknesecretnote.DO.FoodMarketItem;
import io.github.koocci.maknesecretnote.DO.PrefItem;
import io.github.koocci.maknesecretnote.Handler.DBHelper;

public class MainActivity extends RootActivity
    implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "mainActivity~";
    private static final int CHINESE = 1;
    private static final int KOREAN = 2;
    private static final int JAPANESE = 3;
    private static final int CHICKEN = 4;
    private static final int MEAT = 5;
    private static final int EXTRA = 6;

    private static final int DEFAULT = 0;

    FoodMarketAdapter listAdapter;
    List<String> prefList;
    DBHelper db;
    ArrayList<FoodMarketItem> items;

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
        db = new DBHelper(this);
        items = db.selectList(null);
        Log.e(TAG, "main items : " + items.toString());

        listAdapter = new FoodMarketAdapter(this, R.layout.food_market_item, items);

        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FoodMarketItem data = items.get(position);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("market_id", data.getId());
                startActivity(intent);
            }
        });

        Spinner spinner = findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(this);

        prefList = db.selectListPref();

        MainSpinnerAdapter spinAdapter = new MainSpinnerAdapter(this, prefList);
        spinner.setAdapter(spinAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                items.clear();
                if(position == DEFAULT){
                    items = db.selectList(null);
                }
                else{
                    items = db.selectList(prefList.get(position));
                }
                listAdapter.addAll(items);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
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
