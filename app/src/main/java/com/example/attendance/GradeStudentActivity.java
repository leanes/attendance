package com.example.attendance;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.attendance.adapter.AttendAdapter;
import com.example.attendance.adapter.GradeStudentAdapter;
import com.example.attendance.bean.Coordinate;
import com.example.attendance.bean.Grade;
import com.example.attendance.bean.User;
import com.example.attendance.test.SaveLocation;
import com.example.attendance.util.UrlConstance;

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
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.attendance.util.JsonUtil.gradeAllParse;
import static com.example.attendance.util.JsonUtil.gradeSpan;
import static com.example.attendance.util.JsonUtil.parseGradeStudent;

public class GradeStudentActivity extends AppCompatActivity {
    private ListView listView ;
    private ArrayList<Grade>list ;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grade_student);
        Toolbar toolbar = (Toolbar) findViewById(R.id.grade_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar() ;
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        listView = (ListView) findViewById(R.id.gradelist);
        list = new ArrayList<>() ;
        httpGet() ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                finish();
                break;
            default:
        }
        return true ;
    }

    private void httpGet(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient() ;
                String json = gradeSpan() ;
                RequestBody body = RequestBody.create(JSON , json) ;
                Request request = new Request.Builder()
                        .url(UrlConstance.CLASS_INFO)
                        .post(body)
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
                            }
                        });

                    }

                    //请求成功时回调
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()){
                            String responseData = response.body().string() ;
                            gradeAllParse(responseData , list);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    GradeStudentAdapter adapter = new GradeStudentAdapter(GradeStudentActivity.this , R.layout.grade_stu_list , list) ;
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
