package com.example.attendance;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.attendance.bean.Teachers;
import com.example.attendance.util.UrlConstance;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.attendance.util.JsonUtil.registerCreate;

/**
 * Created by 陈振聪 on 2017/2/8.
 */
public class RegisterActivity extends Activity implements View.OnClickListener{
    private EditText schoolEditext ;
    private EditText passwordSet ;
    private EditText passwordAgaint ;
    private EditText teacherName ;
    private EditText phoneNumber ;
    private Button registerOk ;
    private Button backButton ;

    private ProgressDialog progress ;
    public static final int MSG_CREATE_RESULT = 1;

    private Teachers teachers ;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case MSG_CREATE_RESULT:
                    progress.dismiss();
                    JSONObject json = (JSONObject) msg.obj;
                    hanleCreateAccountResult(json);
                    break;
            }
        }
    };

    private void hanleCreateAccountResult(JSONObject json) {
        /*
         * 1  注册成功
         * 2  用户名已存在
         * */
        int result;
        String msg = null ;
        try {
            result = json.getInt("result");
            msg = json.getString("msg") ;
        } catch (JSONException e) {
            Toast.makeText(this, "没有获取到网络的响应！", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return;
        }

        if(result == 2) {
            progress.dismiss();
            Toast.makeText(this, msg , Toast.LENGTH_LONG).show();
            return;
        }


        if(result == 1) {
            progress.dismiss();
            RegisterActivity.this.finish();
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

            return;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        initView() ;

    }

    private void initView() {
        schoolEditext = (EditText) findViewById(R.id.school);
        passwordSet = (EditText) findViewById(R.id.passwordset);
        passwordAgaint = (EditText) findViewById(R.id.passwordagaint);
        teacherName = (EditText) findViewById(R.id.teacherName);
        phoneNumber = (EditText) findViewById(R.id.phonenumber);
        registerOk = (Button) findViewById(R.id.registerOk);
        backButton = (Button) findViewById(R.id.backButton);
        registerOk.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerOk :
                registration() ;
                break;
            case R.id.backButton :
                comeback() ;
                break;
        }
    }

    private void registration() {
        if (isNotEmpty()){
            String school = schoolEditext.getText().toString().trim() ;
            String password = passwordSet.getText().toString().trim() ;
            String teacher = teacherName.getText().toString().trim() ;
            String phone = phoneNumber.getText().toString().trim() ;
            String rePassword = passwordAgaint.getText().toString().trim() ;
            if (rePassword.equals(password)){
                progress = new ProgressDialog(this);
                progress.setCancelable(true);
                progress.setCanceledOnTouchOutside(false);
                progress.setMessage("注册中...");
                progress.show();
                teachers = new Teachers(teacher , password , school , phone) ;
                postRequest() ;
            }else {
                Toast.makeText(this , "密码不一致，请重新输入密码！" , Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void postRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
               /* OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(40 , TimeUnit.SECONDS)
                        .readTimeout(60 , TimeUnit.SECONDS)
                        .build() ;*/
                OkHttpClient client = new OkHttpClient() ;
                String json = registerCreate(teachers) ;
                RequestBody body = RequestBody.create(JSON , json) ;
                Request request = new Request.Builder()
                        .url(UrlConstance.REGIST_INFO)
                        .post(body)
                        .build() ;
                Call call = client.newCall(request) ;
                call.enqueue(new Callback() {
                    //请求失败时调用
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegisterActivity.this, "Failure :" , Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    //请求成功时调用
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String responseData = response.body().string() ;
                            try {
                                JSONObject jsonObject = new JSONObject(responseData) ;
                                sendMessage(MSG_CREATE_RESULT, jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }) ;
            }
        }).start();


    }

    private void sendMessage(int what, Object obj) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = obj;
        mHandler.sendMessage(msg);
    }


    private boolean isNotEmpty() {
       if (schoolEditext.getText().toString().trim().equals("")){
           Toast.makeText(this , "学校为空，请重新输入！" , Toast.LENGTH_SHORT).show();
           return false ;
       }else if (passwordSet.getText().toString().trim().equals("")){
           Toast.makeText(this , "密码为空，请重新输入！" , Toast.LENGTH_SHORT).show();
           return false ;
       }else if (passwordAgaint.getText().toString().trim().equals("")){
           Toast.makeText(this , "确认密码为空，请重新输入！" , Toast.LENGTH_SHORT).show();
           return false ;
       }else if (teacherName.getText().toString().trim().equals("")){
           Toast.makeText(this , "教师名为空，请重新输入！" , Toast.LENGTH_SHORT).show();
           return false ;
       }else if (phoneNumber.getText().toString().trim().equals("")){
           Toast.makeText(this , "手机号码为空，请重新输入！" , Toast.LENGTH_SHORT).show();
           return false ;
       }
        return true;
    }

    private void comeback() {
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

}
