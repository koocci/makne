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
import io.github.koocci.maknesecretnote.DO.PrefItem;

import static android.content.ContentValues.TAG;

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

    public void insertPref(int market_id, String name, int score){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues recordValues = new ContentValues();
        recordValues.put("market_id", market_id);
        recordValues.put("name", name);
        recordValues.put("score", score);

        int pk = (int) db.insert("preference", null, recordValues);

        db.close();
    }

    public void updateMarket(int market_id, String name, String phone, String address, String officehours, int visitcount, int category, String imagepath, String comment){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues recordValues = new ContentValues();

        recordValues.put("name", name);
        recordValues.put("phone", phone);
        recordValues.put("address", address);
        recordValues.put("officehours", officehours);
        recordValues.put("visitcount", visitcount);
        recordValues.put("category", category);
        recordValues.put("imagepath", imagepath);
        recordValues.put("comment", comment);

        String[] whereArgs = {String.valueOf(market_id)};
        int rowAffected = (int) db.update("market", recordValues, "id = ?", whereArgs);

        db.close();

    }

    public void deletePref(int market_id){
        SQLiteDatabase db = getReadableDatabase();

        String[] whereArgs = {String.valueOf(market_id)};
        int rowAffected = (int) db.delete("preference", "market_id = ?", whereArgs);
        Log.i("Delete rowAffected", rowAffected + "");
        db.close();
    }

    public void deleteMarket(int market_id){
        deletePref(market_id);
        SQLiteDatabase db = getReadableDatabase();
        String[] whereArgs = {String.valueOf(market_id)};
        int rowAffected = (int) db.delete("market", "id = ?", whereArgs);
        Log.i("Delete rowAffected", rowAffected + "");

        db.close();

        return;
    }

    public int insertMarket(String name, String phone, String address, String officehours, int visitcount, int category, String imagepath, String comment ) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues recordValues = new ContentValues();

        recordValues.put("name", name);
        recordValues.put("phone", phone);
        recordValues.put("address", address);
        recordValues.put("officehours", officehours);
        recordValues.put("visitcount", visitcount);
        recordValues.put("category", category);
        recordValues.put("imagepath", imagepath);
        recordValues.put("comment", comment);

        int newId = (int) db.insert("market", null, recordValues);
        db.close();

        return newId;
    }

    public ArrayList<FoodMarketItem> selectDetail(int market_id) {
        ArrayList<FoodMarketItem> data = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, name, phone, address, " +
                "officehours, visitcount, category, imagepath, " +
                "(SELECT SUM(score) / COUNT(*) FROM preference WHERE market_id = " + market_id + ") as pref, " +
                "comment " +
                "FROM market WHERE id = " + market_id + ";", null);

        while(cursor.moveToNext()) {
            FoodMarketItem item = new FoodMarketItem(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getInt(5),
                    cursor.getInt(6),
                    cursor.getString(7),
                    cursor.getInt(8),
                    cursor.getString(9)
            );

            data.add(item);
        }

        db.close();

        return data;
    }

    public ArrayList<PrefItem> selectMarketPref(int market_id) {
        ArrayList<PrefItem> data = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT pref_id, name, score FROM preference WHERE market_id = " + market_id + ";", null);

        while(cursor.moveToNext()) {
            PrefItem item = new PrefItem(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2)
            );

            data.add(item);
        }

        db.close();

        return data;
    }

    public List<String> selectListPref() {
        List<String> data = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Log.e(TAG, "db.getPath() : " + db.getPath());

        Cursor cursor = db.rawQuery("SELECT DISTINCT name FROM preference;", null);
        data.add("종합");

        while(cursor.moveToNext()) {
            data.add(cursor.getString(0));
        }

        db.close();

        return data;
    }

    public ArrayList<FoodMarketItem> selectList(String name) {
        ArrayList<FoodMarketItem> data = new ArrayList<>();
        String Where = "";
        if(name != null){
            Where = "WHERE b.name = \"" + name + "\" ";
        }

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT a.id, a.name, a.phone, a.address, " +
                "a.officehours, a.visitcount, a.category, " +
                "a.imagepath, (c.score_sum / c.score_cnt), a.comment FROM market as a " +
                "LEFT JOIN (SELECT b.market_id, SUM(b.score) as score_sum, COUNT(b.pref_id) as score_cnt " +
                "FROM preference as b " +
                Where +
                "GROUP BY b.market_id) as c " +
                "ON a.id = c.market_id " +
                "ORDER BY c.score_sum / c.score_cnt DESC;", null);

        while(cursor.moveToNext()) {
            FoodMarketItem item = new FoodMarketItem(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getInt(5),
                    cursor.getInt(6),
                    cursor.getString(7),
                    cursor.getInt(8),
                    cursor.getString(9)
            );
            data.add(item);
        }

        db.close();

        return data;
    }
}