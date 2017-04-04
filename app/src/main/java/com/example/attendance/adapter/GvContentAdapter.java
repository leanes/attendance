package com.example.attendance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.attendance.R;
import com.example.attendance.test.MyCardView;

import java.util.ArrayList;

/**
 * Created by 陈振聪 on 2017/2/13.
 */
public class GvContentAdapter extends BaseAdapter {
    private Context context ;
    private ArrayList<String>list ;

    public GvContentAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>() ;
        for (int i = 0 ; i < 84 ; i++){
            list.add("") ;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_gv_content , null) ;
            vh.mcv = (MyCardView) convertView.findViewById(R.id.mcv);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.mcv.setText(list.get(position));
        return convertView;
    }

    public static final class ViewHolder{
        MyCardView mcv ;
    }
}
