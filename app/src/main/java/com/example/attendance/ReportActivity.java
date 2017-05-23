package com.example.attendance;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.attendance.adapter.StudentAdapter;
import com.example.attendance.bean.Students;
import com.example.attendance.bean.WeeksNum;
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

import static com.example.attendance.util.JsonUtil.signParse;
import static com.example.attendance.util.JsonUtil.signSpan;
import static com.example.attendance.util.JsonUtil.studentSpan;

/**
 * 详细罗列考勤学生信息
 */
public class ReportActivity extends AppCompatActivity {
    private WeeksNum weeksNum ;
    private ListView listView ;
    private int courseId ;
    private int attenceId ;
    private ArrayList<Students>studentsArrayList ;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private String msg ;

    private  static final String path_name = "/signstudent.txt" ;
    private File sdDir = Environment.getExternalStorageDirectory() ; //获取根目录
    private StringBuilder stringBuilder = new StringBuilder() ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.rpt_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar() ;
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        listView = (ListView) findViewById(R.id.student_list) ;
        Intent intent = getIntent() ;
        Bundle bundle = intent.getExtras() ;
        weeksNum = (WeeksNum) bundle.getSerializable("weekNum");
        attenceId = weeksNum.getAttenceId() ;
        courseId = bundle.getInt("courseId") ;
        studentsArrayList = new ArrayList<>() ;

        httpGet() ;

/*        StudentAdapter adapter = new StudentAdapter(ReportActivity.this , R.layout.student_list , studentsArrayList) ;
        listView.setAdapter(adapter) ;*/

    }

    private void httpGet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient() ;
                final String json = signSpan(courseId , attenceId) ;
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
                                Toast.makeText(ReportActivity.this, "请求失败" , Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()){
                            String responseData = response.body().string() ;
                            try {
                                JSONObject jsonObject = new JSONObject(responseData) ;
                                msg = jsonObject.getString("msg") ;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            signParse(responseData , studentsArrayList);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ReportActivity.this, msg , Toast.LENGTH_SHORT).show();
                                    StudentAdapter adapter = new StudentAdapter(ReportActivity.this , R.layout.student_list , studentsArrayList) ;
                                    listView.setAdapter(adapter) ;
                                }
                            });

                        }
                    }
                });
            }
        }).start();
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
}
