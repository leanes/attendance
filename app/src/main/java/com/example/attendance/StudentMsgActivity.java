package com.example.attendance;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.attendance.R;
import com.example.attendance.adapter.StudentAdapter;
import com.example.attendance.bean.Students;
import com.example.attendance.test.ExcelStudent;

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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StudentMsgActivity extends AppCompatActivity {
    private Button exportbtn ;
    private ListView studentListView ;
    private List<Students> list ;
    private  static final String path_name = "/student.txt" ;
    File sdDir = Environment.getExternalStorageDirectory() ; //获取根目录
    StringBuilder stringBuilder = new StringBuilder() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_msg);
        list = new ArrayList<>() ;
        studentListView = (ListView) findViewById(R.id.student_list);
        exportbtn = (Button) findViewById(R.id.export_student);
        checkStdent() ;
        exportbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExcelStudent.writeExcelStudent(StudentMsgActivity.this , list);
            }
        });

    }
    private void checkStdent() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient() ;
                    Request request = new Request.Builder()
                            .url("http://192.168.191.1:8080/student.json")
                            .build() ;
                    Call call = client.newCall(request);
                    call.enqueue(new Callback() {
                                     @Override
                                     public void onFailure(Call call, IOException e) {
                                         runOnUiThread(new Runnable() {
                                             @Override
                                             public void run() {
                                                 Toast.makeText(StudentMsgActivity.this , "连接服务器失败！" , Toast.LENGTH_SHORT).show();
                                                 File filetxt = new File(sdDir + path_name);
                                                 if (!filetxt.exists()) {
                                                     return;
                                                 }
                                                 try{
                                                     InputStream in = new FileInputStream(filetxt);
                                                     if (in != null) {
                                                         InputStreamReader isr = new InputStreamReader(in);
                                                         BufferedReader br = new BufferedReader(isr);
                                                         String line;
                                                         while ((line = br.readLine()) != null) {
                                                             stringBuilder.append(line);
                                                         }
                                                     }
                                                     String jsonStr = stringBuilder.toString() ;
                                                     parseJSONWithJSONObject(jsonStr);
                                                     setListAdapter();
                                                 } catch (Exception e1) {
                                                     e1.printStackTrace();
                                                 }

                                             }
                                         });
                                     }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData = response.body().string() ;
                            parseJSONWithJSONObject(responseData) ;

                            boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ;     //判断SD卡是否存在
                            if (sdCardExist){

                                FileOutputStream fout = null ;
                                fout = new FileOutputStream(sdDir + path_name) ;
                                byte[] bytes = responseData.getBytes() ;
                                fout.write(bytes);
                                fout.close();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setListAdapter() ;
                                }
                            });
                        }
                    });
                   /* Response response = client.newCall(request).execute() ;
                    String responseData = response.body().string() ;
                    parseJSONWithJSONObject(responseData) ;

                    boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ;     //判断SD卡是否存在
                    if (sdCardExist){

                        FileOutputStream fout = null ;
                        fout = new FileOutputStream(sdDir + path_name) ;
                        byte[] bytes = responseData.getBytes() ;
                        fout.write(bytes);
                        fout.close();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setListAdapter() ;
                        }
                    });*/

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void setListAdapter() {
        StudentAdapter adapter = new StudentAdapter(StudentMsgActivity.this , R.layout.student_list , list) ;
        studentListView.setAdapter(adapter);
    }

    private void parseJSONWithJSONObject(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData) ;

            for (int i = 0 ; i < jsonArray.length()  ; i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i) ;
                String grade = jsonObject.getString("grade") ;
                String studentName = jsonObject.getString("student_name") ;
                String studentId = jsonObject.getString("student_id") ;

                Students students = new Students(grade , studentName , studentId) ;
                Log.d("StudentMsg" , "班级是：" + jsonObject.getString("grade")) ;
                Log.d("StudentMsg" , "姓名是：" + jsonObject.getString("student_name")) ;
                Log.d("StudentMsg" , "学号是：" + jsonObject.getString("student_id")) ;
                list.add(students) ;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
