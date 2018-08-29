package io.github.koocci.maknesecretnote.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.github.koocci.maknesecretnote.R;

/**
 * Created by gujinhyeon on 2018. 8. 29..
 */

public class CateSpinnerAdapter extends BaseAdapter {

    private static final int CHINESE = 1;
    private static final int KOREAN = 2;
    private static final int JAPANESE = 3;
    private static final int CHICKEN = 4;
    private static final int MEAT = 5;
    private static final int EXTRA = 6;

    Context context;
    List<Number> data;
    LayoutInflater inflater;

    public CateSpinnerAdapter(Context context, List<Number> data){
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    public void addAll(List<Number> result) {
        data.clear();
        if(data==null){
            data = new ArrayList<>();
        }
        data.addAll(result);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null) {
            convertView = inflater.inflate(R.layout.cate_spinner_normal, parent, false);
        }

        if(data!=null){
            //데이터세팅
            switch((int)data.get(position)){
                case CHINESE:
                    ((TextView)convertView.findViewById(R.id.spinner_text)).setText("중식");
                    break;
                case KOREAN:
                    ((TextView)convertView.findViewById(R.id.spinner_text)).setText("한식");
                    break;
                case JAPANESE:
                    ((TextView)convertView.findViewById(R.id.spinner_text)).setText("일식");
                    break;
                case CHICKEN:
                    ((TextView)convertView.findViewById(R.id.spinner_text)).setText("치킨");
                    break;
                case MEAT:
                    ((TextView)convertView.findViewById(R.id.spinner_text)).setText("고기");
                    break;
                case EXTRA:
                    ((TextView)convertView.findViewById(R.id.spinner_text)).setText("기타");
                    break;
            }

        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = inflater.inflate(R.layout.cate_spinner_contain, parent, false);
        }

        //데이터세팅
        switch((int)data.get(position)){
            case CHINESE:
                ((TextView)convertView.findViewById(R.id.spinner_text)).setText("중식");
                break;
            case KOREAN:
                ((TextView)convertView.findViewById(R.id.spinner_text)).setText("한식");
                break;
            case JAPANESE:
                ((TextView)convertView.findViewById(R.id.spinner_text)).setText("일식");
                break;
            case CHICKEN:
                ((TextView)convertView.findViewById(R.id.spinner_text)).setText("치킨");
                break;
            case MEAT:
                ((TextView)convertView.findViewById(R.id.spinner_text)).setText("고기");
                break;
            case EXTRA:
                ((TextView)convertView.findViewById(R.id.spinner_text)).setText("기타");
                break;
        }

        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}

