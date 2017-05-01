package com.example.attendance;

import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Environment;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.attendance.adapter.AttendAdapter;
import com.example.attendance.adapter.GradeStudentAdapter;
import com.example.attendance.bean.Coordinate;
import com.example.attendance.bean.Grade;
import com.example.attendance.bean.User;
import com.example.attendance.bean.WeeksNum;
import com.example.attendance.test.SaveLocation;
import com.example.attendance.util.JsonUtil;
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

import static com.example.attendance.util.JsonUtil.dataParse;
import static com.example.attendance.util.JsonUtil.parseGradeStudent;

public class AttendHomeActivity extends AppCompatActivity {
    private ListView attendlist ;
    private ArrayList<Coordinate> data ;
    private  static final String path_name = "/servercourse.txt" ;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private File sdDir = Environment.getExternalStorageDirectory() ; //获取根目录
    private StringBuilder stringBuilder = new StringBuilder() ;

    private int tag = 0 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attend_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.attendance_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar() ;
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        attendlist = (ListView) findViewById(R.id.list_attendced);
        Intent intent = getIntent() ;
        Bundle bundle = intent.getExtras() ;
        tag = bundle.getInt("tag") ;
        data = new ArrayList<>() ;
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
                String json = "{\"accessToken\":"+ User.sharedUser().getAccessToken() + "}" ;
                RequestBody body = RequestBody.create(JSON , json) ;
                Request request = new Request.Builder()
//                        .url("http://192.168.191.1:8080/attendance.json")
                        .url(UrlConstance.COURSES_INFO)
//                        .post(body)
                        .build() ;
                Call call = client.newCall(request) ;
                call.enqueue(new Callback() {
                    //请求失败回调
                    @Override
                    public void onFailure(Call call, IOException e) {
                      runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              Toast.makeText(AttendHomeActivity.this , "请求服务器失败" , Toast.LENGTH_SHORT).show();

                              try{
                                  SaveLocation.readLocal(stringBuilder , sdDir , path_name);        //读取本地数据
                                  String jsonStr = stringBuilder.toString() ;
//                                  attendancePare(jsonStr , data) ;
                                  dataParse(jsonStr , data);
                                  AttendAdapter adapter = new AttendAdapter(AttendHomeActivity.this , R.layout.list_attend , data) ;
                                  attendlist.setAdapter(adapter);
                                  attendlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                      @Override
                                      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                          Coordinate coordinate = data.get(position) ;
                                          Intent intent = new Intent(AttendHomeActivity.this , AttendanceActivity.class) ;
                                          intent.putExtra("coordinate" , coordinate) ;
                                          intent.putExtra("tag" , tag) ;
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
//                            attendancePare(responseData , data) ;
                            dataParse(responseData , data);
                            SaveLocation.saveLocal(responseData , sdDir , path_name);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AttendAdapter adapter = new AttendAdapter(AttendHomeActivity.this , R.layout.list_attend , data) ;
                                    attendlist.setAdapter(adapter);
                                    attendlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Coordinate coordinate = data.get(position) ;
                                            Intent intent = new Intent(AttendHomeActivity.this , AttendanceActivity.class) ;

                                            intent.putExtra("coordinate" , coordinate) ;
                                            intent.putExtra("tag" , tag) ;
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
