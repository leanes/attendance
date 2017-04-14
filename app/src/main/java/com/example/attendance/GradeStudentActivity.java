package com.example.attendance;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.attendance.adapter.AttendAdapter;
import com.example.attendance.adapter.GradeStudentAdapter;
import com.example.attendance.bean.Coordinate;
import com.example.attendance.bean.Grade;
import com.example.attendance.test.SaveLocation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.attendance.util.JsonUtil.attendancePare;
import static com.example.attendance.util.JsonUtil.parseGradeStudent;

public class GradeStudentActivity extends AppCompatActivity {
    private ListView listView ;
    private ArrayList<Grade>list ;

    private  static final String path_name = "/student.txt" ;
    File sdDir = Environment.getExternalStorageDirectory() ; //获取根目录
    StringBuilder stringBuilder = new StringBuilder() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grade_student);
        listView = (ListView) findViewById(R.id.gradelist);
        list = new ArrayList<>() ;
        httpGet() ;
    }

    private void httpGet(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient() ;
                Request request = new Request.Builder()
                        .url("http://192.168.191.1:8080/student.json")
                        .build() ;
                Call call = client.newCall(request) ;
                call.enqueue(new Callback() {
                    //请求失败回调
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GradeStudentActivity.this , "请求失败" , Toast.LENGTH_SHORT).show();

                                try{
                                    SaveLocation.readLocal(stringBuilder , sdDir , path_name);          //读取本地数据
                                    String jsonStr = stringBuilder.toString() ;
                                    parseGradeStudent(jsonStr , list) ;
                                    GradeStudentAdapter adapter = new GradeStudentAdapter(GradeStudentActivity.this , R.layout.student_list , list) ;
                                    listView.setAdapter(adapter);
                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Grade grade = list.get(position) ;
                                            Intent intent = new Intent(GradeStudentActivity.this , StudentMsgActivity.class) ;
                                            intent.putExtra("grade" , grade) ;
                                            startActivity(intent);
                                        }
                                    });
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }
                        });

                    }

                    //请求成功时回调
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()){
                            String responseData = response.body().string() ;
                            parseGradeStudent(responseData , list) ;

                            SaveLocation.saveLocal(responseData , sdDir , path_name);           //保存在本地
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    GradeStudentAdapter adapter = new GradeStudentAdapter(GradeStudentActivity.this , R.layout.student_list , list) ;
                                    listView.setAdapter(adapter);
                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Grade grade = list.get(position) ;
                                            Intent intent = new Intent(GradeStudentActivity.this , StudentMsgActivity.class) ;
                                            intent.putExtra("grade" , grade) ;
                                            startActivity(intent);
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
            }
        }).start();
    }

}
