package io.github.koocci.maknesecretnote;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.github.koocci.maknesecretnote.Adapter.CateSpinnerAdapter;
import io.github.koocci.maknesecretnote.Adapter.MainSpinnerAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView marketImage = findViewById(R.id.market_image);
        TextView marketName = findViewById(R.id.market_name);
        TextView marketLoc = findViewById(R.id.market_loc);
        TextView marketTel = findViewById(R.id.market_tel);
        TextView marketType = findViewById(R.id.market_type);
        TextView marketCnt = findViewById(R.id.market_cnt);
        marketPref = findViewById(R.id.market_pref);
        TextView marketComment = findViewById(R.id.market_coment);

        Intent intent = getIntent();
        int market_id = intent.getIntExtra("market_id", 0);

        db = new DBHelper(this);
        item = db.selectDetail(market_id);
        prefItem = db.selectMarketPref(market_id);

        Spinner spinner = findViewById(R.id.pref_cate);
        List<String> prefList = new ArrayList<>();
        prefList.add("공통");
        for(int i = 0; i < prefItem.size(); i++){
            prefList.add(prefItem.get(i).getName());
        }

        CateSpinnerAdapter spinAdapter = new CateSpinnerAdapter(this, prefList);
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


        if(item.size() < 1){
            Toast.makeText(getApplicationContext(), "삭제된 음식점입니다", Toast.LENGTH_LONG).show();
            finish();
        }
        else if(item.size() > 1){
            Toast.makeText(getApplicationContext(), "잘못된 접근입니다", Toast.LENGTH_LONG).show();
            finish();
        }
        else{

            if(item.get(0).getImagePath() == null || "".equals(item.get(0).getImagePath())){
                marketImage.setImageResource(R.drawable.chinese);
                // 임시 사진 보이기
            }
            else{
                Bitmap bmImg = BitmapFactory.decodeFile(item.get(0).getImagePath());
                marketImage.setImageBitmap(bmImg);
            }
            marketName.setText(item.get(0).getName());
            marketLoc.setText(item.get(0).getAddress());

            if(item.get(0).getPhone() == null || "".equals(item.get(0).getPhone())){
                marketTel.setText("미등록");
            }
            else{
                marketTel.setText(item.get(0).getPhone());
            }

            switch((item.get(0).getCategory())){
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

        }

    }

    public void deleteMarket(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(DetailActivity.this);
        builder1.setMessage("정말로 삭제하시겠습니까?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        //

                        dialog.cancel();
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
