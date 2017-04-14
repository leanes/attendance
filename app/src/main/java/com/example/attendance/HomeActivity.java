package com.example.attendance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by 陈振聪 on 2017/2/7.
 */
public class HomeActivity extends Activity{
    private GridView list_home ;
    private MyAdapter adapter ;

    private static String[] names = { "课程查询" , "考勤情况" , "导出表格" } ;

    private static int[] ids = { R.drawable.clas , R.drawable.attend , R.drawable.table} ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        list_home = (GridView) findViewById(R.id.list_home);
        adapter = new MyAdapter(this , names , ids) ;
        list_home.setAdapter(adapter);

        list_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    case 0 :
                        Intent intent = new Intent(HomeActivity.this , TableActivity.class) ;
                        startActivity(intent);
                        break;
                    case 1 :
                        Intent attend = new Intent(HomeActivity.this , AttendHomeActivity.class) ;
                        attend.putExtra("tag" , 1) ;
                        startActivity(attend);
                        break;
                    case 2 :
                        Intent guide = new Intent(HomeActivity.this , GuideTableActivity.class) ;
                        startActivity(guide);
                }
            }
        });
    }

    private class MyAdapter extends BaseAdapter {

        private String mTitleArr[] = null;
        private int mImgIdArr[] = null;
        private LayoutInflater inflater = null;

        public MyAdapter(Context context, String[] titleArr, int[] imgId) {
            super();
            this.mTitleArr = titleArr;
            this.mImgIdArr = imgId;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return names.length;
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
            ViewHolder holder;
            if (convertView == null) {

                convertView = inflater.inflate(R.layout.home_list, null);
                holder = new ViewHolder();
                holder.titleName = (TextView) convertView
                        .findViewById(R.id.tv_item);
                holder.image = (ImageView) convertView
                        .findViewById(R.id.iv_item);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();

            }

            // 设置内容
            holder.titleName.setText(mTitleArr[position]);
            holder.image.setImageResource(mImgIdArr[position]);
           // holder.image.setBackgroundResource(mImgIdArr[position]);

            return convertView;
        }

        /**
         * 工具类
         */
        private class ViewHolder {
            TextView titleName = null;
            ImageView image = null;
        }
    }
}
