package com.example.attendance.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.attendance.R;
import com.example.attendance.bean.Coordinate;
import com.example.attendance.bean.Grade;
import com.example.attendance.test.MultipleChoiceListItem;

import java.util.HashMap;
import java.util.List;

/**
 * Created by 陈振聪 on 2017/4/25.
 */
public class GradeAdapter extends BaseAdapter {
    private List<Coordinate> mlistdata;
    private Context context;
    public GradeAdapter(Context context , List<Coordinate> mlistdata) {
        // TODO Auto-generated constructor stub
        this.mlistdata=mlistdata;
        this.context=context;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mlistdata.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mlistdata.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return true;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        MultipleChoiceListItem choicelistitem=new MultipleChoiceListItem(context);
        choicelistitem.setText(mlistdata.get(position).getClassName());
        return choicelistitem;
    }

}
