package io.github.koocci.maknesecretnote.Adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.koocci.maknesecretnote.DO.FoodMarketItem;
import io.github.koocci.maknesecretnote.R;

/**
 * Created by gujinhyeon on 2018. 8. 24..
 *
 * 음식점 리스트 Adapter
 *
 */

public class FoodMarketAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<FoodMarketItem> items;
    private LayoutInflater inflater;

    private static final int CHINESE = 1;
    private static final int KOREAN = 2;
    private static final int JAPANESE = 3;
    private static final int CHICKEN = 4;
    private static final int MEAT = 5;
    private static final int EXTRA = 6;


    public FoodMarketAdapter(Context context, int layout, ArrayList<FoodMarketItem> items) {
        this.context = context;
        this.layout = layout;
        this.items = items;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }

        ImageView thumb = convertView.findViewById(R.id.market_thumb);
        thumb.setImageResource((items.get(position).getThumbnail()));

        ImageView type = convertView.findViewById(R.id.market_type);
        switch((items.get(position).getType())){
            case CHINESE:
                type.setImageResource(R.drawable.chinese);
                break;
            case KOREAN:
                type.setImageResource(R.drawable.korean);
                break;
            case JAPANESE:
                type.setImageResource(R.drawable.japanese);
                break;
            case CHICKEN:
                type.setImageResource(R.drawable.chicken);
                break;
            case MEAT:
                type.setImageResource(R.drawable.meat);
                break;
            case EXTRA:
                type.setImageResource(R.drawable.extra_food);
                break;
        }


        TextView name = convertView.findViewById(R.id.market_name);
        name.setText(items.get(position).getName());

        TextView loc = convertView.findViewById(R.id.market_loc);
        loc.setText(items.get(position).getLoc());

//        TextView pref = convertView.findViewById(R.id.market_pref);
//        pref.setText(items.get(position).getPref() + "");
        RatingBar pref = convertView.findViewById(R.id.market_pref);
        pref.setRating(items.get(position).getPref());

        TextView cnt = convertView.findViewById(R.id.market_cnt);
        cnt.setText(items.get(position).getCount() + " 회 방문");

        return convertView;
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}
