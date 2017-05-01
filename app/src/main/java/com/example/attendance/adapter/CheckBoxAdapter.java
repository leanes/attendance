package com.example.attendance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.attendance.R;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 陈振聪 on 2017/4/20.
 */
public class CheckBoxAdapter extends BaseAdapter {
    private ArrayList<String>list ;
    private static HashMap<Integer , Boolean> isSelected ;
    private Context context ;
    private LayoutInflater inflater = null ;

    public CheckBoxAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context) ;
        isSelected = new HashMap<>() ;
        initDate() ;
    }

    private void initDate() {
        for (int i = 0 ; i < list.size() ; i++){
            getIsSelected().put(i , false) ;
        }
    }
    public static HashMap<Integer,Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer,Boolean> isSelected) {
        CheckBoxAdapter.isSelected = isSelected;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null ;
        if (convertView == null){
            holder = new ViewHolder() ;
            convertView = inflater.inflate(R.layout.checkbox_list , parent) ;
            holder.tv = (TextView) convertView.findViewById(R.id.item_tv);
            holder.cb = (CheckBox) convertView.findViewById(R.id.item_cb);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv.setText(list.get(position));
        holder.cb.setChecked(getIsSelected().get(position));

        return convertView;
    }

    class ViewHolder{
        TextView tv ;
        CheckBox cb ;
    }
}
