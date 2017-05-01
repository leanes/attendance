package com.example.attendance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.attendance.R;
import com.example.attendance.bean.Students;

import java.util.List;

/**
 * Created by 陈振聪 on 2017/3/16.
 */
public class StudentMsgAdapter extends ArrayAdapter<Students> {
    private int resourceId ;
    public StudentMsgAdapter(Context context, int resource, List<Students> objects) {
        super(context, resource, objects);
        resourceId = resource ;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Students students = getItem(position) ;
        View view ;
        ViewHolder holder ;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId , parent , false) ;
            holder = new ViewHolder() ;
            holder.gradeText = (TextView) view.findViewById(R.id.guide_grade);
            holder.nameText = (TextView) view.findViewById(R.id.guide_name);
            holder.idText = (TextView) view.findViewById(R.id.guide_id);
            view.setTag(holder);
        }else {
            view = convertView ;
            holder = (ViewHolder) view.getTag();
        }
        holder.gradeText.setText(students.getGrade());
        holder.nameText.setText(students.getStudent_name());
        holder.idText.setText(students.getStudent_id());

        return view;
    }
    class ViewHolder{
        TextView gradeText ;
        TextView nameText ;
        TextView idText ;
    }
}
