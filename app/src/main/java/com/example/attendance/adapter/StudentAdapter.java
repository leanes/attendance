package com.example.attendance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.attendance.R;
import com.example.attendance.StudentMsgActivity;
import com.example.attendance.bean.Students;

import java.util.List;

/**
 * Created by 陈振聪 on 2017/3/16.
 */
public class StudentAdapter extends ArrayAdapter<Students> {
    private int resourceId ;
    private Context mContext ;
    public StudentAdapter(Context context, int resource, List<Students> objects) {
        super(context, resource, objects);
        resourceId = resource ;
        mContext = context ;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Students students = getItem(position) ;
        View view ;
        ViewHolder holder ;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId , parent , false) ;
            holder = new ViewHolder() ;
            holder.gradeText = (TextView) view.findViewById(R.id.grade);
            holder.nameText = (TextView) view.findViewById(R.id.student_name);
            holder.idText = (TextView) view.findViewById(R.id.student_id);
            holder.attendText = (TextView) view.findViewById(R.id.isattend);
            holder.imgview = (ImageView) view.findViewById(R.id.stu_photo);
            holder.signTime = (TextView) view.findViewById(R.id.sign_time);
            view.setTag(holder);
        }else {
            view = convertView ;
            holder = (ViewHolder) view.getTag();
        }
        holder.gradeText.setText(students.getGrade());
        holder.nameText.setText(students.getStudent_name());
        holder.idText.setText(students.getStudent_id());
        holder.attendText.setText(students.getIsAttend());
        holder.signTime.setText(students.getSignTime());
        Glide.with(mContext).load(students.getImageUrl()).into(holder.imgview) ;
        return view;
    }
    class ViewHolder{
        TextView gradeText ;
        TextView nameText ;
        TextView idText ;
        TextView attendText ;
        ImageView imgview ;
        TextView signTime ;
    }
}
