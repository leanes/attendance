package com.example.attendance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.attendance.bean.Teachers;
import com.example.attendance.db.UserDataManager;
import com.example.attendance.util.UrlConstance;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.attendance.util.JsonUtil.loginCreate;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText userEditext ;
    private EditText passwordEditext ;
    private Button loginButton ;
    private Button registerButton ;

    private CheckBox rememberPass ;

    private SharedPreferences pref ;
    private SharedPreferences.Editor editor ;

    private Teachers teachers ;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private UserDataManager mUserDataManager ;
    private String userName ;
    private String password ;

    private ProgressDialog loginProgress;

    public static final int MSG_LOGIN_RESULT = 0;


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case MSG_LOGIN_RESULT:
                    loginProgress.dismiss();
                    JSONObject json = (JSONObject) msg.obj;
                    handleLoginResult(json);
                    break;
            }
        };
    };
    private void handleLoginResult(JSONObject json){
        /*
         *
         * */
        int resultCode = -1;
        String msg = null;
        try {
            resultCode = json.getInt("result");
            msg = json.getString("msg") ;
        } catch (JSONException e) {
            Toast.makeText(this, "没有获取到网络的响应！", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        switch(resultCode) {
            case 1:
                loginProgress.dismiss();
                editor = pref.edit() ;
                if (rememberPass.isChecked()){
                    editor.putBoolean("remember_pass" , true) ;
                    editor.putString("userName" , userName) ;
                    editor.putString("password" , password) ;
                }else {
                    editor.clear() ;
                }
                editor.apply();
                Intent intent = new Intent(LoginActivity.this , HomeActivity.class) ;
                startActivity(intent);
                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                LoginActivity.this.finish();
                break;
            case 2:
                loginProgress.dismiss();
                Toast.makeText(this, msg , Toast.LENGTH_SHORT).show();
                break;
            case 3:
                loginProgress.dismiss();
                Toast.makeText(this, msg , Toast.LENGTH_SHORT).show();
                break;
            case -1:
            default:
                loginProgress.dismiss();
                Toast.makeText(this, "登陆失败！未知错误", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initView() ;

    }

    private void initView() {
        userEditext = (EditText) findViewById(R.id.user);
        passwordEditext = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.loginButton);
        registerButton = (Button) findViewById(R.id.registerButton);

        rememberPass = (CheckBox) findViewById(R.id.remember_pass);
        pref = PreferenceManager.getDefaultSharedPreferences(this) ;

        boolean isRemember = pref.getBoolean("remember_pass" , false) ;
        if (isRemember){
            String userName = pref.getString("userName" , "") ;
            String password = pref.getString("password" , "") ;
            userEditext.setText(userName);
            passwordEditext.setText(password);
            rememberPass.setChecked(true);
        }

        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginButton :
                login() ;
                break;
            case R.id.registerButton :
                register() ;
                break;
        }
    }

    private void register() {
        Intent registerIntent = new Intent(LoginActivity.this , RegisterActivity.class) ;
        startActivity(registerIntent);
    }

    private void login() {
        if (isUserNameAndPwdValid()){
            loginProgress = new ProgressDialog(this);
            loginProgress.setCancelable(true);
            loginProgress.setCanceledOnTouchOutside(false);
            loginProgress.setMessage("登陆中...");
            loginProgress.show();
            userName = userEditext.getText().toString().trim() ;
            password = passwordEditext.getText().toString().trim() ;
            teachers = new Teachers(userName , password) ;
            postRequest() ;
        }
    }

    private void postRequest() {
       new Thread(new Runnable() {
           @Override
           public void run() {
               OkHttpClient client = new OkHttpClient() ;
               String json = loginCreate(teachers) ;
               RequestBody body = RequestBody.create(JSON , json) ;
               Request request = new Request.Builder()
                       .url(UrlConstance.LOGIN_INFO)
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
                               loginProgress.dismiss();
                               Toast.makeText(LoginActivity.this, "请求失败" , Toast.LENGTH_SHORT).show();
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
                               sendMessage(MSG_LOGIN_RESULT, jsonObject);
                           } catch (JSONException e) {
                               e.printStackTrace();
                           }
                       }
                   }
               });
           }
       }).start();

    }

    private void sendMessage(int what, Object obj) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = obj;
        mHandler.sendMessage(msg);
    }

    private boolean isUserNameAndPwdValid() {
        if (userEditext.getText().toString().trim().equals("")){
            Toast.makeText(this , "用户名为空，请重新输入！" , Toast.LENGTH_SHORT).show();
            return false ;
        }else if (passwordEditext.getText().toString().trim().equals("")){
            Toast.makeText(this , "密码为空，请重新输入！" , Toast.LENGTH_SHORT).show();
            return false ;
        }
        return true ;
    }

    @Override
    protected void onResume() {
        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (mUserDataManager != null) {
            mUserDataManager.closeDataBase();
            mUserDataManager = null;
        }
        super.onPause();
    }


}
