package com.example.attendance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.attendance.R;

import java.util.ArrayList;

/**
 * Created by 陈振聪 on 2017/2/13.
 */
public class LvNumAdapter extends BaseAdapter {
    private Context context ;
    private ArrayList<String> list ;

    public LvNumAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>() ;
        for (int i = 1 ; i <= 12 ; i++){
            list.add(i + "") ;
        }
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0 ;
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
        ViewHolder vh = null ;
        if (convertView == null){
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_lv_num , null) ;
            vh.tvNum = (TextView) convertView.findViewById(R.id.tvNum);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.tvNum.setText(list.get(position));
        return convertView;
    }

    public static final class ViewHolder{
        TextView tvNum ;
    }
}