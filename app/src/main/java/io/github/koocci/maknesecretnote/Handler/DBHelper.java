package io.github.koocci.maknesecretnote.Handler;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.koocci.maknesecretnote.DO.FoodMarketItem;

public class DBHelper extends SQLiteOpenHelper {
    private final String dbName = "secretnote.db";
    public DBHelper(Context context) {
        super(context, "secretnote.db", null, 1);

        if (!isExistDB(context)) { // 앱 처음 실행 (DB 데이터 없음)
            copyDB(context); // DB 데이터 생성 (복사)
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public boolean isExistDB(Context context) {
        String packageName = context.getPackageName();

        String filePath = "/data/data/" + packageName + "/databases/" + dbName;

        File file = new File(filePath);
        if (file.exists()) {
            return true;
        }

        return false;

    }

    public void copyDB(Context context) {
        AssetManager manager = context.getAssets();

        String packageName = context.getPackageName();

        String folderPath = "/data/data/" + packageName + "/databases";
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        InputStream is = null;
        BufferedInputStream bis = null;

        OutputStream fos = null;
        BufferedOutputStream bos = null;

        try {
            is = manager.open(dbName);
            bis = new BufferedInputStream(is);

            fos = new FileOutputStream(folderPath + "/" + dbName);
            bos = new BufferedOutputStream(fos);

            int data = 0;
            while ((data = bis.read()) != -1) {
                bos.write(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {}
            }
            if(is != null) {
                try {
                    is.close();
                } catch (IOException e) {}
            }
            if(bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {}
            }
            if(fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {}
            }
        }
    }


    // =========================================================


//    CREATE TABLE `preference` (
//            `pref_id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
//            `market_id`	INTEGER NOT NULL,
//            `name`	TEXT NOT NULL,
//            `score`	INTEGER NOT NULL,
//    FOREIGN KEY(`market_id`) REFERENCES `market`(`id`)
//            );

    public void insertPref(int market_id, String name, int score){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues recordValues = new ContentValues();
        recordValues.put("market_id", market_id);
        recordValues.put("name", name);
        recordValues.put("score", score);

        int pk = (int) db.insert("preference", null, recordValues);

        db.close();
    }

    public int insertMarket(String name, String phone, String address, String officehours, int visitcount, String category, String imagepath ) {
        Log.e("helper", "insert");
        Log.e("after", imagepath);
        SQLiteDatabase db = getWritableDatabase();

        ContentValues recordValues = new ContentValues();

        recordValues.put("name", name);
        recordValues.put("phone", phone);
        recordValues.put("address", address);
        recordValues.put("officehours", officehours);
        recordValues.put("visitcount", visitcount);
        recordValues.put("category", category);
        recordValues.put("imagepath", imagepath);

        int newId = (int) db.insert("market", null, recordValues);
        Log.i("rowPosition", newId + "");
//        db.execSQL("INSERT INTO store (name, phone, address, officehours, preference, visitedcount) VALUES " +
//                " (NULL, ?, ?, datetime('now','localtime'))", new Object[] {name, phone, address, officehours, preference, visitcount});
        db.close();

        return newId;
    }

    public List<Map<String, Object>> selectDetail(int market_id) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT a.* FROM market WHERE market_id = " + market_id + ";", null);

        while(cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", cursor.getInt(0));
            map.put("name", cursor.getString(1));
            map.put("phone", cursor.getString(2));
            map.put("address", cursor.getString(3));
            map.put("officehours", cursor.getString(4));
            map.put("visitcount", cursor.getInt(5));
            map.put("category", cursor.getInt(6));
            map.put("imagepath", cursor.getString(7));

            list.add(map);
        }

        db.close();

        return list;
    }

    public ArrayList<FoodMarketItem> selectList(String name) {
        ArrayList<FoodMarketItem> data = new ArrayList<FoodMarketItem>();
        String Where = "";
        if(name != null){
            Where = "WHERE b.name = \"" + name + "\" ";
        }


        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT a.* FROM market as a " +
                "LEFT JOIN (SELECT b.market_id, SUM(b.score) as score_sum, COUNT(b.pref_id) as score_cnt " +
                "FROM preference as b " +
                Where +
                "GROUP BY b.market_id) as c " +
                "ON a.id = c.market_id " +
                "ORDER BY c.score_sum / c.score_cnt DESC;", null);

        while(cursor.moveToNext()) {
            FoodMarketItem item = new FoodMarketItem();
            map.put("id", cursor.getInt(0));
            map.put("name", cursor.getString(1));
            map.put("phone", cursor.getString(2));
            map.put("address", cursor.getString(3));
            map.put("officehours", cursor.getString(4));
            map.put("visitcount", cursor.getInt(5));
            map.put("category", cursor.getInt(6));
            map.put("imagepath", cursor.getString(7));

            list.add(map);
        }

        db.close();

        return list;
    }
}


/*
*
* DO item 고치기 DB랑 맞춰서
* SELECT LIST에 평균 선호도도 뽑기
* SELECT PREF 뽑기
*
*
*
* */