package com.example.attendance.test;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.attendance.R;
import com.example.attendance.adapter.GradeAdapter;

import java.util.List;

/**
 * Created by 陈振聪 on 2017/4/25.
 */
public class ShowPopwindow {
    private static Context mContext ;
    private static PopupWindow popwindow;
    private static ListView lv_list ;
    private static MyNumberAdapter adapter ;
    private static List<Integer>integerList ;
    public static void showpopwindow(Context context , List<Integer>list , EditText etClass){
        mContext = context ;
        integerList = list ;
        initListview(context , list , etClass);
        popwindow = new PopupWindow(lv_list, etClass.getWidth() - 4, 500);

        // 设置点击外部可以被关闭.
        popwindow.setOutsideTouchable(true);
        popwindow.setBackgroundDrawable(new BitmapDrawable());

        // 获取焦点
        popwindow.setFocusable(true);
        // 显示在输入框的左下角位置
        popwindow.showAsDropDown(etClass, 2, -4);
    }

    private static void initListview(Context context , final List<Integer>list , final EditText etClass) {

        lv_list = new ListView(context);
//        lv_list.setDivider(null);

        lv_list.setDividerHeight(1);
        lv_list.setBackgroundResource(R.drawable.lbg);
        adapter = new MyNumberAdapter(mContext , R.layout.week_list , list) ;
        lv_list.setAdapter(adapter);
        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int number = list.get(position);
                etClass.setText(number);
                popwindow.dismiss();
            }
        });
        // 绑定数据
       /* adapter =  new GradeAdapter(context , R.layout.grade_list , list) ;
        lv_list.setAdapter(adapter);*/
    }
    static class MyNumberAdapter extends ArrayAdapter<Integer> {
        private int resourceId;

        public MyNumberAdapter(Context context, int resource, List<Integer> objects) {
            super(context, resource, objects);
            resourceId = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            int week = getItem(position) ;
            NumberViewHolder mHolder = null;
            if (convertView == null) {
                convertView = View.inflate(mContext, resourceId, null);
                mHolder = new NumberViewHolder();
                mHolder.tvNumber = (TextView) convertView.findViewById(R.id.week_list);
                convertView.setTag(mHolder);
            } else {
                // 取出holder类
                mHolder = (NumberViewHolder) convertView.getTag();
            }

            // 给mHolder类中的对象赋值.
            mHolder.tvNumber.setText(week);
            return convertView;
        }
    }


        static class NumberViewHolder {
            public TextView tvNumber;
        }


}

