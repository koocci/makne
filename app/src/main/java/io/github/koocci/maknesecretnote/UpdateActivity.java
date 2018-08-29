package io.github.koocci.maknesecretnote;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.github.koocci.maknesecretnote.Adapter.CateSpinnerAdapter;
import io.github.koocci.maknesecretnote.DO.FoodMarketItem;
import io.github.koocci.maknesecretnote.DO.PrefItem;
import io.github.koocci.maknesecretnote.Handler.DBHelper;

public class UpdateActivity extends RootActivity {

    private static final int CHINESE = 1;
    private static final int KOREAN = 2;
    private static final int JAPANESE = 3;
    private static final int CHICKEN = 4;
    private static final int MEAT = 5;
    private static final int EXTRA = 6;

    private static final int REQ_CODE_SELECT_IMAGE=100;
    private static final int REQUEST_IMAGE_CAPTURE = 672;

    private String imagepath;

    private Uri photoUri;

    int id = 0;

    LinearLayout layout;
    EditText prefCate;
    RatingBar marketPref;
    LayoutInflater inflater;
    ArrayList<PrefHolder> holder;
    DBHelper db;

    ImageView marketImage;
    Button camera;
    Button album;
    EditText marketName;
    EditText marketLoc;
    EditText marketTel;
    Spinner marketType;
    Button complete;
    Button exit;
    TextView marketCnt;


    ArrayList<FoodMarketItem> item;
    ArrayList<PrefItem> prefItem;
    EditText marketComent;
    int market_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        Intent intent = getIntent();
        market_id = intent.getIntExtra("market_id", 0);

        checkPermissions();

        marketImage = findViewById(R.id.market_image);
        camera = findViewById(R.id.camera);
        album = findViewById(R.id.album);
        marketName = findViewById(R.id.market_name);
        marketLoc = findViewById(R.id.market_loc);
        marketTel = findViewById(R.id.market_tel);
        marketType = findViewById(R.id.market_type);
        complete = findViewById(R.id.complete);
        exit = findViewById(R.id.exit);


        marketCnt = findViewById(R.id.market_cnt);
        Button up = findViewById(R.id.up);
        Button down = findViewById(R.id.down);
        marketComent = findViewById(R.id.market_coment);

        db = new DBHelper(this);
        item = db.selectDetail(market_id);
        prefItem = db.selectMarketPref(market_id);

        if(item.size() < 1){
            Toast.makeText(getApplicationContext(), "삭제된 음식점입니다", Toast.LENGTH_LONG).show();
            finish();
        }
        else if(item.size() > 1){
            Toast.makeText(getApplicationContext(), "잘못된 접근입니다", Toast.LENGTH_LONG).show();
            finish();
        }
        else{
            imagepath = item.get(0).getImagePath();

            if(item.get(0).getImagePath() == null || "".equals(item.get(0).getImagePath())){
                marketImage.setImageResource(R.drawable.defaultimage);
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

            List<Number> cateData = new ArrayList<>();
            cateData.add(CHINESE);
            cateData.add(KOREAN);
            cateData.add(JAPANESE);
            cateData.add(CHICKEN);
            cateData.add(MEAT);
            cateData.add(EXTRA);

            CateSpinnerAdapter spinAdapter = new CateSpinnerAdapter(this, cateData);
            marketType.setAdapter(spinAdapter);

            switch((item.get(0).getCategory())){
                case CHINESE:
                    marketType.setSelection(CHINESE);
                    break;
                case KOREAN:
                    marketType.setSelection(KOREAN);
                    break;
                case JAPANESE:
                    marketType.setSelection(JAPANESE);
                    break;
                case CHICKEN:
                    marketType.setSelection(CHICKEN);
                    break;
                case MEAT:
                    marketType.setSelection(MEAT);
                    break;
                case EXTRA:
                    marketType.setSelection(EXTRA);
                    break;
            }

            marketCnt.setText(item.get(0).getVisitCount() + "");
            up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int cnt = Integer.parseInt(marketCnt.getText().toString());
                    cnt ++ ;
                    marketCnt.setText(cnt + "");
                }
            });

            down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int cnt = Integer.parseInt(marketCnt.getText().toString());
                    cnt --;
                    if(cnt < 0)
                        cnt = 0;
                    marketCnt.setText(cnt + "");
                }
            });

            marketComent.setText(item.get(0).getComment());

        }

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTakePhotoIntent();
            }
        });

        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectUpload();
            }
        });

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //insertMarket(String name, String phone, String address, String officehours, int visitcount, String category, String imagepath, String comment )
                updateMarket();
                updatePref();

            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        prefCate = findViewById(R.id.pref_cate);
        marketPref = findViewById(R.id.market_pref);
        Button addCate = findViewById(R.id.add_cate);
        layout = findViewById(R.id.layout);

        holder = new ArrayList<>();

        addCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prefCate.getText() == null || "".equals(prefCate.getText().toString())){
                    Toast.makeText(getApplicationContext(), "선호도 대상을 입력해 주세요", Toast.LENGTH_LONG).show();
                    return;
                }

                getPrefs(prefCate.getText().toString(), (int)marketPref.getRating());

//                LinearLayout prefLayout = new LinearLayout(getApplicationContext());
//                prefLayout.setGravity(Gravity.CENTER);
//                LinearLayout.LayoutParams params =
//                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 3f);
//                prefLayout.setLayoutParams(params);
//
//                TextView addPrefCate = new EditText(getApplicationContext());
//                addPrefCate.setText(prefCate.getText());
//                addPrefCate.setHintTextColor(Color.BLACK);
//
//                params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 3f);
//                addPrefCate.setGravity(Gravity.CENTER);
//                addPrefCate.setLayoutParams(params);
//
//                RatingBar addMarketPref = new RatingBar(getApplicationContext(), null, android.R.attr.ratingBarStyleIndicator);
//                addMarketPref.setRating(marketPref.getRating());
//                params = new LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.WRAP_CONTENT,
//                        LinearLayout.LayoutParams.WRAP_CONTENT);
//                addMarketPref.setIsIndicator(false);
//                addMarketPref.setNumStars(5);
//                addMarketPref.setMax(5);
//                addMarketPref.setRating(3f);
//                addMarketPref.setLayoutParams(params);
//                // ?android:attr/ratingBarStyleIndicator
//
//                Button removeCate = new Button(getApplicationContext());
//                removeCate.setBackgroundResource(R.drawable.remove_red);
//                params = new LinearLayout.LayoutParams(convertDpToPixel(30), convertDpToPixel(30));
//                params.setMargins(convertDpToPixel(10), convertDpToPixel(10), convertDpToPixel(10), convertDpToPixel(10));
//                removeCate.setLayoutParams(params);
//
//
//                prefLayout.setTag(id);
//                removeCate.setTag(id);
//                id++;

//                removeCate.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Button b = (Button) v;
//                        int tag = (int) b.getTag();
//                        Log.e("click tag", "" + tag);
//
//                        for(int i = 0; i < holder.size(); i++) {
//                            if((int)(holder.get(i).getLayout().getTag()) == tag) {
//                                holder.remove(i);
//                                layout.removeViewAt(i + 2);
//                                break;
//                            }
//                        }
//                    }
//                });
//                prefLayout.addView(addPrefCate);
//                prefLayout.addView(addMarketPref);
//                prefLayout.addView(removeCate);
//
//                holder.add(new PrefHolder(prefLayout, removeCate, prefCate.getText().toString(), (int)marketPref.getRating()));
//
//                layout.addView(prefLayout);
            }
        });

        for(int i = 0; i < prefItem.size(); i++){
            getPrefs(prefItem.get(i).getName(), prefItem.get(i).getScore());
        }


    }

    public void getPrefs(String prefCateText, int prefRating){

        LinearLayout prefLayout = new LinearLayout(getApplicationContext());
        prefLayout.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 3f);
        prefLayout.setLayoutParams(params);

        TextView addPrefCate = new TextView(getApplicationContext());
        addPrefCate.setText(prefCateText);
        addPrefCate.setHintTextColor(Color.BLACK);

        params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 3f);
        addPrefCate.setGravity(Gravity.CENTER);
        addPrefCate.setLayoutParams(params);

        RatingBar addMarketPref = new RatingBar(getApplicationContext(), null, android.R.attr.ratingBarStyleIndicator);
        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        addMarketPref.setIsIndicator(true);
        addMarketPref.setNumStars(5);
        addMarketPref.setMax(5);
        addMarketPref.setStepSize(1);
        addMarketPref.setRating(prefRating);
        addMarketPref.setLayoutParams(params);
        // ?android:attr/ratingBarStyleIndicator

        Button removeCate = new Button(getApplicationContext());
        removeCate.setBackgroundResource(R.drawable.remove_red);
        params = new LinearLayout.LayoutParams(convertDpToPixel(30), convertDpToPixel(30));
        params.setMargins(convertDpToPixel(10), convertDpToPixel(10), convertDpToPixel(10), convertDpToPixel(10));
        removeCate.setLayoutParams(params);


        prefLayout.setTag(id);
        removeCate.setTag(id);
        id++;

        removeCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                int tag = (int) b.getTag();
                Log.e("click tag", "" + tag);

                for(int i = 0; i < holder.size(); i++) {
                    if((int)(holder.get(i).getLayout().getTag()) == tag) {
                        holder.remove(i);
                        layout.removeViewAt(i + 2);
                        break;
                    }
                }
            }
        });
        prefLayout.addView(addPrefCate);
        prefLayout.addView(addMarketPref);
        prefLayout.addView(removeCate);

        holder.add(new PrefHolder(prefLayout, removeCate, prefCateText, prefRating));

        layout.addView(prefLayout);
    }

    public class PrefHolder{
        private LinearLayout layout;
        private Button btn;
        private String name;
        private int score;

        public PrefHolder(LinearLayout layout, Button btn, String name, int score) {
            this.layout = layout;
            this.btn = btn;
            this.name = name;
            this.score = score;
        }

        public LinearLayout getLayout() {
            return layout;
        }

        public void setLayout(LinearLayout layout) {
            this.layout = layout;
        }

        public Button getBtn() {
            return btn;
        }

        public void setBtn(Button btn) {
            this.btn = btn;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }
    }

    private void selectUpload(){
        //버튼 클릭시 처리로직
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQ_CODE_SELECT_IMAGE)
        {
            if(resultCode== Activity.RESULT_OK)
            {
                try {
                    //Uri에서 이미지 이름을 얻어온다.
                    String name_Str = getImageNameToUri(data.getData());

                    //이미지 데이터를 비트맵으로 받아온다.
                    Bitmap image_bitmap 	= MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    ImageView image = (ImageView)findViewById(R.id.image);

                    //배치해놓은 ImageView에 set
                    image.setImageBitmap(image_bitmap);

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagepath);
            ExifInterface exif = null;

            try {
                exif = new ExifInterface(imagepath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int exifOrientation;
            int exifDegree;

            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);
            } else {
                exifDegree = 0;
            }

            marketImage.setImageBitmap(rotate(bitmap, exifDegree));
        }
    }

    public String getImageNameToUri(Uri data)
    {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        imagepath = cursor.getString(column_index);

        Bitmap bmImg = BitmapFactory.decodeFile(imagepath);
        marketImage.setImageBitmap(bmImg);

        Log.e("imagepath", imagepath);
        String imgName = imagepath.substring(imagepath.lastIndexOf("/")+1);

        return imgName;
    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private void sendTakePhotoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
//                file:///storage/emulated/0/Android/data/com.example.ggoreb.samplecamera/files/Pictures/
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            Log.e("getPackageName()", getPackageName());

            if (photoFile != null) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 24 버전 이상
                    photoUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", photoFile);
                } else { // 24 버전 미만
                    photoUri = Uri.fromFile(photoFile);
                }
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File storageDir = Environment.getExternalStorageDirectory();

        Log.e("storage1", Environment.DIRECTORY_PICTURES);
        Log.e("storage2", Environment.getExternalStorageDirectory().getAbsolutePath());
        Log.e("storage3", storageDir.getAbsolutePath());

        File image = File.createTempFile(
                imageFileName,      /* prefix */
                ".jpg",         /* suffix */
                storageDir          /* directory */
        );
        imagepath = image.getAbsolutePath();
        Log.e("path", imagepath);
        return image;
    }

    public void checkPermissions() {
        String[] permissions = {
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Log.d("Camera", "권한 있음");
        } else {
            Log.d("Camera", "권한 없음");

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                Log.d("Camera", "권한 설명 필요함");
            } else {
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Camera", "권한 승인됨");
                } else {
                    Log.d("Camera", "권한 승인되지 않음");
                }
            }
        }
    }

    public void updatePref(){
        db.deletePref(market_id);
        for(int i = 0; i < holder.size(); i++){
            db.insertPref(market_id, holder.get(i).getName(), holder.get(i).getScore());
        }
    }

    public void updateMarket(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(UpdateActivity.this);
        builder1.setMessage("정말로 수정하시겠습니까?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if(marketName.getText() == null || "".equals(marketName.getText())){
                            Toast.makeText(getApplicationContext(), "이름을 정확히 입력해 주세요", Toast.LENGTH_LONG).show();
                        }
                        else if(marketLoc.getText() == null || "".equals(marketLoc.getText())){
                            Toast.makeText(getApplicationContext(), "주소를 정확히 입력해 주세요", Toast.LENGTH_LONG).show();
                        }
                        else if(marketCnt.getText() == null || "".equals(marketCnt.getText())){
                            Toast.makeText(getApplicationContext(), "방문 횟수를 정확히 입력해 주세요", Toast.LENGTH_LONG).show();
                        }

                        db.updateMarket(
                                market_id,
                                marketName.getText().toString(),
                                marketTel.getText().toString(),
                                marketLoc.getText().toString(),
                                null,
                                Integer.parseInt(marketCnt.getText().toString()),
                                marketType.getSelectedItemPosition(),
                                imagepath,
                                marketComent.getText().toString()
                        );

                        Toast.makeText(getApplicationContext(), "수정되었습니다", Toast.LENGTH_LONG).show();
                        dialog.cancel();
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

    public int convertDpToPixel(float dp) {

        Resources resources = getResources();

        DisplayMetrics metrics = resources.getDisplayMetrics();

        float px = dp * (metrics.densityDpi / 160f);

        return (int) px;

    }
}
