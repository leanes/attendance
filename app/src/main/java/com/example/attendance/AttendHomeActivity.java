package com.example.attendance;

import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Environment;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.attendance.adapter.AttendAdapter;
import com.example.attendance.adapter.GradeStudentAdapter;
import com.example.attendance.bean.Coordinate;
import com.example.attendance.bean.Grade;
import com.example.attendance.bean.WeeksNum;
import com.example.attendance.test.SaveLocation;
import com.example.attendance.util.JsonUtil;

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

public class AttendHomeActivity extends AppCompatActivity {
    private ListView attendlist ;
    private ArrayList<Coordinate> data ;
    private  static final String path_name = "/attendance.txt" ;
    File sdDir = Environment.getExternalStorageDirectory() ; //获取根目录
    StringBuilder stringBuilder = new StringBuilder() ;

    private int tag = 0 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attend_home);
        attendlist = (ListView) findViewById(R.id.list_attendced);
        Intent intent = getIntent() ;
        Bundle bundle = intent.getExtras() ;
        tag = bundle.getInt("tag") ;
        data = new ArrayList<>() ;
        httpGet();
       /* File sdDir = null ;
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
        });*/
    }
    private void httpGet(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient() ;
                Request request = new Request.Builder()
                        .url("http://192.168.191.1:8080/attendance.json")
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
                                  attendancePare(jsonStr , data) ;
                                  AttendAdapter adapter = new AttendAdapter(AttendHomeActivity.this , R.layout.list_attend , data) ;
                                  attendlist.setAdapter(adapter);
                                  attendlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                      @Override
                                      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                          Coordinate coordinate = data.get(position) ;
//                                            ArrayList<WeeksNum>weeksNa = coordinate.getWeeksNumArrayList() ;
                                          Intent intent = new Intent(AttendHomeActivity.this , AttendanceActivity.class) ;
//                                            intent.putExtra("weekNa" , weeksNa) ;
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
                            attendancePare(responseData , data) ;
                            /*boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ;     //判断SD卡是否存在
                            if (sdCardExist){
                                FileOutputStream fout = null ;
                                fout = new FileOutputStream(sdDir + path_name) ;
                                byte[] bytes = responseData.getBytes() ;
                                fout.write(bytes);
                                fout.close();
                            }*/
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
//                                            ArrayList<WeeksNum>weeksNa = coordinate.getWeeksNumArrayList() ;
                                            Intent intent = new Intent(AttendHomeActivity.this , AttendanceActivity.class) ;
//                                            intent.putExtra("weekNa" , weeksNa) ;
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
