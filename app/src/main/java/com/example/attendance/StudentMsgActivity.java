package com.example.attendance;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.attendance.R;
import com.example.attendance.adapter.GradeStudentAdapter;
import com.example.attendance.adapter.StudentAdapter;
import com.example.attendance.adapter.StudentMsgAdapter;
import com.example.attendance.bean.Grade;
import com.example.attendance.bean.Students;
import com.example.attendance.test.ExcelStudent;
import com.example.attendance.test.SaveLocation;
import com.example.attendance.util.UrlConstance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
import static com.example.attendance.util.JsonUtil.studentParse;
import static com.example.attendance.util.JsonUtil.studentSpan;

public class StudentMsgActivity extends AppCompatActivity {
    private Button exportbtn ;
    private ListView studentListView ;
    private ArrayList<Students> list ;
    private Grade grade ;
    private int classId ;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private  static final String path_name = "/student.txt" ;
    File sdDir = Environment.getExternalStorageDirectory() ; //获取根目录
    StringBuilder stringBuilder = new StringBuilder() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_msg);
        Toolbar toolbar = (Toolbar) findViewById(R.id.stu_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar() ;
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        studentListView = (ListView) findViewById(R.id.student_list);
        exportbtn = (Button) findViewById(R.id.export_student);
        Intent intent = getIntent() ;
        Bundle bundle = intent.getExtras() ;
        grade = (Grade) bundle.getSerializable("grade");
        classId = grade.getClassId() ;
        list = grade.getStudentsList() ;
        httpGet();




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
                String json = studentSpan(classId) ;
                RequestBody body = RequestBody.create(JSON , json) ;
                Request request = new Request.Builder()
                        .url(UrlConstance.STUDENT_INFO)
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
                                Toast.makeText(StudentMsgActivity.this , "请求失败" , Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    //请求成功时回调
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()){
                            String responseData = response.body().string() ;
                            studentParse(responseData , list); ;

                            SaveLocation.saveLocal(responseData , sdDir , path_name);           //保存在本地
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    StudentMsgAdapter adapter = new StudentMsgAdapter(StudentMsgActivity.this , R.layout.guide_student_list , list) ;
                                    studentListView.setAdapter(adapter);
                                    exportbtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ExcelStudent.writeExcelStudent(StudentMsgActivity.this , list , "/"+grade.getGrade()+".xlsx");
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
