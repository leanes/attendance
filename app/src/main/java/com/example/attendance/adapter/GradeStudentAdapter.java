package com.example.attendance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.attendance.R;
import com.example.attendance.bean.Grade;

import java.util.List;

/**
 * Created by 陈振聪 on 2017/4/14.
 */
public class GradeStudentAdapter extends ArrayAdapter<Grade> {
    private int resourceId ;
    public GradeStudentAdapter(Context context, int resource, List<Grade> objects) {
        super(context, resource, objects);
        resourceId = resource ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Grade grade = getItem(position) ;
        View view ;
        ViewHolder holder ;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId , parent , false) ;
            holder = new ViewHolder() ;
            holder.attendGrade = (TextView) view.findViewById(R.id.grade);
            holder.numberText = (TextView) view.findViewById(R.id.number);
            view.setTag(holder);
        }else {
            view = convertView ;
            holder = (ViewHolder) view.getTag();
        }
        holder.attendGrade.setText(grade.getGrade());
        holder.numberText.setText(grade.getNumber() + "位");

        return view;
    }

    class ViewHolder{
        TextView attendGrade ;
        TextView numberText ;
    }

}
