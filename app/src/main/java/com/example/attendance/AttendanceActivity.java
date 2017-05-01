package com.example.attendance;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.attendance.adapter.AttendanceAdapter;
import com.example.attendance.adapter.StudentAdapter;
import com.example.attendance.bean.Coordinate;
import com.example.attendance.bean.Students;
import com.example.attendance.bean.WeeksNum;
import com.example.attendance.test.ExcelAttendance;
import com.example.attendance.test.SaveLocation;
import com.example.attendance.util.UrlConstance;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.attendance.util.JsonUtil.attendParse;
import static com.example.attendance.util.JsonUtil.attendSpan;
import static com.example.attendance.util.JsonUtil.signParse;
import static com.example.attendance.util.JsonUtil.signSpan;

public class AttendanceActivity extends AppCompatActivity {
    private Coordinate coordinate ;
    private ArrayList<WeeksNum>weeksNumArrayList ;
    private int courseId ;
    private int attendId ;
    private ArrayList<Students>studentses = new ArrayList<Students>() ;
    private ListView listView ;
    private int tag = 0 ;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private  static final String path_name = "/situation.txt" ;
    private File sdDir = Environment.getExternalStorageDirectory() ; //获取根目录
    private StringBuilder stringBuilder = new StringBuilder() ;
    private String className;
    private int section;
    private int week;
    private int weeks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        listView = (ListView) findViewById(R.id.attendance_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar() ;
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        final Intent intent = getIntent() ;
        Bundle bundle = intent.getExtras() ;
        tag = bundle.getInt("tag") ;
//        weeksNumArrayList = (ArrayList<WeeksNum>) bundle.getSerializable("weekNa");
        weeksNumArrayList = new ArrayList<>() ;
        coordinate = (Coordinate) bundle.getSerializable("coordinate");
        courseId = coordinate.getCourseId() ;
        httpget() ;
    }

    private void httpget() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient() ;
                final String json = attendSpan(courseId) ;
                RequestBody body = RequestBody.create(JSON , json) ;
                Request request = new Request.Builder()
                        .url(UrlConstance.ATTENDCODE_INFO)
                        .post(body)
                        .build() ;
                Call call = client.newCall(request) ;
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AttendanceActivity.this, "请求失败" , Toast.LENGTH_SHORT).show();
                                SaveLocation.readLocal(stringBuilder , sdDir , path_name);        //读取本地数据
                                String jsonStr = stringBuilder.toString() ;
                                attendParse(jsonStr , weeksNumArrayList);
                                AttendanceAdapter adapter = new AttendanceAdapter(AttendanceActivity.this , R.layout.attendance_list , weeksNumArrayList) ;
                                listView.setAdapter(adapter);

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        WeeksNum weeksNum = weeksNumArrayList.get(position) ;
                                        attendId = weeksNum.getAttenceId() ;
                                        if (tag == 1){
                                            Intent intentReport = new Intent(AttendanceActivity.this , ReportActivity.class) ;
                                            intentReport.putExtra("weekNum" , weeksNum) ;
                                            intentReport.putExtra("courseId" , courseId) ;
                                            startActivity(intentReport) ;
                                        }else if (tag == 2){
                                            String className = coordinate.getClassName() ;
                                            int section = coordinate.getPosition()/7 + 1;
                                            int week = coordinate.getPosition()%7 + 1 ;
                                            int weeks = weeksNum.getWeeks() ;
                                            httpGetStu();
                                            ArrayList<Students>studentses = weeksNum.getStudentsArrayList() ;
                                            ExcelAttendance.writeExcelAttendance(AttendanceActivity.this , studentses , "/"+className+weeks+"周"+week+"第"+section+"节"+".xlsx");
                                        }

                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String responseData = response.body().string() ;
                            Log.d("LoginActivity" ,responseData) ;
                            SaveLocation.saveLocal(responseData , sdDir , path_name);
                            attendParse(responseData , weeksNumArrayList);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AttendanceAdapter adapter = new AttendanceAdapter(AttendanceActivity.this , R.layout.attendance_list , weeksNumArrayList) ;
                                    listView.setAdapter(adapter);

                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            WeeksNum weeksNum = weeksNumArrayList.get(position) ;
                                            attendId = weeksNum.getAttenceId() ;
                                            if (tag == 1){
                                                Intent intentReport = new Intent(AttendanceActivity.this , ReportActivity.class) ;
                                                intentReport.putExtra("weekNum" , weeksNum) ;
                                                intentReport.putExtra("courseId" , courseId) ;
                                                startActivity(intentReport) ;
                                            }else if (tag == 2){
                                                className = coordinate.getClassName() ;
                                                section = coordinate.getPosition()/7 + 1;
                                                week = coordinate.getPosition()%7 + 1 ;
                                                weeks = weeksNum.getWeeks() ;


                                                httpGetStu() ;

//                                                ExcelAttendance.writeExcelAttendance(AttendanceActivity.this , studentses , "/"+className+weeks+"周"+week+"第"+section+"节"+".xlsx");
                                            }

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

    private void httpGetStu() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient() ;
                String json = signSpan(courseId , attendId) ;
                RequestBody body = RequestBody.create(JSON , json) ;
                Request request = new Request.Builder()
                        .url(UrlConstance.SIGN_INFO)
                        .post(body)
                        .build() ;
                Call call = client.newCall(request) ;
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AttendanceActivity.this, "请求失败" , Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()){
                            String responseData = response.body().string() ;
                            signParse(responseData , studentses);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ExcelAttendance.writeExcelAttendance(AttendanceActivity.this , studentses , "/"+className+weeks+"周"+week+"第"+section+"节"+".xlsx");
                                }
                            });
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (tag == 1){
            getMenuInflater().inflate(R.menu.toolbar , menu) ;
        }

        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            /*case R.id.add :
                Intent code = new Intent(AttendanceActivity.this , QrCodeActivity.class) ;
                code.putExtra("courseId" , courseId) ;
                startActivity(code);
                break;*/
            case android.R.id.home :
                finish();
                break;
        }
        return true ;
    }
}
