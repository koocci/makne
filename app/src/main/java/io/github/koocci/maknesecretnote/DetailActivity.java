package io.github.koocci.maknesecretnote;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.github.koocci.maknesecretnote.Adapter.PrefSpinnerAdapter;
import io.github.koocci.maknesecretnote.DO.FoodMarketItem;
import io.github.koocci.maknesecretnote.DO.PrefItem;
import io.github.koocci.maknesecretnote.Handler.DBHelper;

public class DetailActivity extends RootActivity {

    DBHelper db;
    private static final int CHINESE = 1;
    private static final int KOREAN = 2;
    private static final int JAPANESE = 3;
    private static final int CHICKEN = 4;
    private static final int MEAT = 5;
    private static final int EXTRA = 6;

    private static final int DEFAULT = 0;

    RatingBar marketPref;
    ArrayList<FoodMarketItem> item;
    ArrayList<PrefItem> prefItem;

    ImageView marketImage;
    TextView marketName;
    TextView marketLoc;
    TextView marketTel;
    TextView marketType;
    TextView marketCnt;
    TextView marketComment;

    List<String> prefList;

    PrefSpinnerAdapter spinAdapter;

    int market_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        marketImage = findViewById(R.id.market_image);
        marketName = findViewById(R.id.market_name);
        marketLoc = findViewById(R.id.market_loc);
        marketTel = findViewById(R.id.market_tel);
        marketType = findViewById(R.id.market_type);
        marketCnt = findViewById(R.id.market_cnt);
        marketPref = findViewById(R.id.market_pref);
        marketComment = findViewById(R.id.market_coment);
        Button update = findViewById(R.id.update);
        Button delete = findViewById(R.id.delete);

        Intent intent = getIntent();
        market_id = intent.getIntExtra("market_id", 0);

        db = new DBHelper(this);
        item = db.selectDetail(market_id);
        prefItem = db.selectMarketPref(market_id);

        Spinner spinner = findViewById(R.id.pref_cate);
        prefList = new ArrayList<>();
        prefList.add("공통");
        for(int i = 0; i < prefItem.size(); i++){
            prefList.add(prefItem.get(i).getName());
        }

        spinAdapter = new PrefSpinnerAdapter(this, prefList);
        spinner.setAdapter(spinAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == DEFAULT){
                    marketPref.setRating(item.get(0).getPref());
                }
                else{
                    marketPref.setRating(prefItem.get(position-1).getScore());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        getData();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, UpdateActivity.class);
                intent.putExtra("market_id", market_id);
                startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMarket();
            }
        });
    }

    public void getData() {
        item.clear();
        item = db.selectDetail(market_id);
        if (item.size() < 1) {
            Toast.makeText(getApplicationContext(), "삭제된 음식점입니다", Toast.LENGTH_LONG).show();
            finish();
        } else if (item.size() > 1) {
            Toast.makeText(getApplicationContext(), "잘못된 접근입니다", Toast.LENGTH_LONG).show();
            finish();
        } else {

            if (item.get(0).getImagePath() == null || "".equals(item.get(0).getImagePath())) {
                marketImage.setImageResource(R.drawable.defaultimage);
                // 임시 사진 보이기
            } else {
                Bitmap bmImg = BitmapFactory.decodeFile(item.get(0).getImagePath());
                marketImage.setImageBitmap(bmImg);
            }
            marketName.setText(item.get(0).getName());
            marketLoc.setText(item.get(0).getAddress());

            if (item.get(0).getPhone() == null || "".equals(item.get(0).getPhone())) {
                marketTel.setText("미등록");
            } else {
                marketTel.setText(item.get(0).getPhone());
            }

            switch ((item.get(0).getCategory())) {
                case CHINESE:
                    marketType.setText("중식");
                    break;
                case KOREAN:
                    marketType.setText("한식");
                    break;
                case JAPANESE:
                    marketType.setText("일식");
                    break;
                case CHICKEN:
                    marketType.setText("치킨");
                    break;
                case MEAT:
                    marketType.setText("고기");
                    break;
                case EXTRA:
                    marketType.setText("기타");
                    break;
            }

            marketCnt.setText(item.get(0).getVisitCount() + "");

            marketPref.setRating(item.get(0).getPref());

            marketComment.setText(item.get(0).getComment());

            prefItem.clear();
            prefItem = db.selectMarketPref(market_id);
            prefList = new ArrayList<>();
            prefList.add("공통");
            for(int i = 0; i < prefItem.size(); i++){
                prefList.add(prefItem.get(i).getName());
            }

            spinAdapter.addAll(prefList);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    public void deleteMarket(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(DetailActivity.this);
        builder1.setMessage("정말로 삭제하시겠습니까?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        db.deleteMarket(market_id);
                        dialog.cancel();
                        Toast.makeText(getApplicationContext(), "삭제되었습니다", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
