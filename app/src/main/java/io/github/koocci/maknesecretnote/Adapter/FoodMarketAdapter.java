package io.github.koocci.maknesecretnote.Adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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

        TextView name = convertView.findViewById(R.id.market_name);
        name.setText(items.get(position).getName());

        TextView loc = convertView.findViewById(R.id.market_loc);
        loc.setText(items.get(position).getLoc());

        TextView pref = convertView.findViewById(R.id.market_pref);
        pref.setText(items.get(position).getPref() + "");

        TextView cnt = convertView.findViewById(R.id.market_cnt);
        cnt.setText(items.get(position).getCount() + "");

        return convertView;
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}
