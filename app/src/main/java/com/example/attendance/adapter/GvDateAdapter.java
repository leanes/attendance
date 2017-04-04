package com.example.attendance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.attendance.R;

import java.security.PublicKey;
import java.util.ArrayList;

/**
 * Created by 陈振聪 on 2017/2/13.
 */
public class GvDateAdapter extends BaseAdapter {
    private Context context ;
    private ArrayList<String>list ;

    public GvDateAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>() ;
        list.add(" ") ;
        list.add("周一") ;
        list.add("周二") ;
        list.add("周三") ;
        list.add("周四") ;
        list.add("周五") ;
        list.add("周六") ;
        list.add("周日") ;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_gv_date , null) ;
            vh.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.tvDate.setText(list.get(position));
        return convertView;
    }

    public static final class ViewHolder{
        TextView tvDate ;
    }
}
