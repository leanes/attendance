package com.example.attendance;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.attendance.adapter.AttendAdapter;
import com.example.attendance.bean.Coordinate;
import com.example.attendance.util.JsonUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class AttendHomeActivity extends AppCompatActivity {
    private ListView attendlist ;
    private ArrayList<Coordinate> data ;
    private static final String file_name = "/coursetable.txt" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attend_home);
        attendlist = (ListView) findViewById(R.id.list_attendced);
        data = new ArrayList<>() ;
        File sdDir = null ;
        StringBuilder stringBuilder = new StringBuilder() ;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ;     //判断SD卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory(); //获取根目录
            File file = new File(sdDir + file_name);
            if (!file.exists()) {
                return;
            }
            try {
                InputStream in = new FileInputStream(file);
                if (in != null) {
                    InputStreamReader isr = new InputStreamReader(in);
                    BufferedReader br = new BufferedReader(isr);
                    String line;
                    while ((line = br.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String jsonStr = stringBuilder.toString() ;
        JsonUtil.dataParse(jsonStr , data);
        AttendAdapter adapter = new AttendAdapter(AttendHomeActivity.this , R.layout.list_attend , data) ;
        attendlist.setAdapter(adapter);
        attendlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(AttendHomeActivity.this , "你点击了：" +  data.get(position).getClassName() , Toast.LENGTH_SHORT).show();
            }
        });
    }

}
