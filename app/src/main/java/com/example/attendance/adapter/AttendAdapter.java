package com.example.attendance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.attendance.R;
import com.example.attendance.bean.Coordinate;

import java.util.List;

/**
 * Created by 陈振聪 on 2017/3/17.
 */
public class AttendAdapter extends ArrayAdapter<Coordinate> {
    private int resourceId ;
    public AttendAdapter(Context context, int resource, List<Coordinate> objects) {
        super(context, resource, objects);
        resourceId = resource ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Coordinate coordinate = getItem(position) ;
        View view ;
        ViewHolder holder ;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_attend , parent , false) ;
            holder = new ViewHolder() ;
            holder.attendClass = (TextView) view.findViewById(R.id.attend_class);
            holder.attendGrade = (TextView) view.findViewById(R.id.attend_grade);
            holder.attendWeek = (TextView) view.findViewById(R.id.attend_week);
            view.setTag(holder);
        }else {
            view = convertView ;
            holder = (ViewHolder) view.getTag();
        }
        holder.attendGrade.setText(coordinate.getGrade());
        holder.attendClass.setText(coordinate.getClassName());
        holder.attendWeek.setText("周" + (coordinate.getPosition()%7 + 1) );

        return view;
    }
    class ViewHolder{
        TextView attendClass ;
        TextView attendGrade ;
        TextView attendWeek ;
    }
}
