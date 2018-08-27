package io.github.koocci.maknesecretnote;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

import io.github.koocci.maknesecretnote.Adapter.FoodMarketAdapter;
import io.github.koocci.maknesecretnote.DO.FoodMarketItem;

public class MainActivity extends AppCompatActivity {

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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

        FoodMarketAdapter adapter = new FoodMarketAdapter(this, R.layout.food_market_item, items);

        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
