package com.example.attendance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.attendance.R;
import com.example.attendance.bean.Attendance;
import com.example.attendance.bean.Coordinate;
import com.example.attendance.bean.Students;
import com.example.attendance.bean.WeeksNum;

import java.util.List;

/**
 * Created by 陈振聪 on 2017/3/23.
 */
public class AttendanceAdapter extends ArrayAdapter<WeeksNum> {
    private int resourceId ;

    public AttendanceAdapter(Context context, int resource, List<WeeksNum> objects) {
        super(context, resource, objects);
        resourceId = resource ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WeeksNum weeksNum = getItem(position) ;
//        Coordinate coordinate = getItem(position);
        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.attendance_list, parent, false);
            holder = new ViewHolder();
            holder.attendance_time = (TextView) view.findViewById(R.id.attendance_time);
            holder.attendance_num = (TextView) view.findViewById(R.id.attendance_num);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        holder.attendance_time.setText(weeksNum.getWeeks()+"") ;
        holder.attendance_num.setText(weeksNum.getAttendance_num()+"人");

        return view ;
    }


    class ViewHolder{
        TextView attendance_time ;
        TextView attendance_num ;
    }


}
