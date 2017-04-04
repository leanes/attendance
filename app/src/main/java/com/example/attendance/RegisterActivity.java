package com.example.attendance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.attendance.db.UserData;
import com.example.attendance.db.UserDataManager;

/**
 * Created by 陈振聪 on 2017/2/8.
 */
public class RegisterActivity extends Activity implements View.OnClickListener {
    private EditText schoolEditext ;
    private EditText passwordSet ;
    private EditText passwordAgaint ;
    private EditText teacherName ;
    private EditText phoneNumber ;
    private Button registerOk ;
    private Button backButton ;

    private UserDataManager userDataManager ;

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

                int count = userDataManager.findPhoneNumber(phone) ;

                if (count > 0 ){
                    Toast.makeText(this , "该手机号码已注册，请去登录！" , Toast.LENGTH_SHORT).show();
                    return ;
                }
                UserData mUser = new UserData(teacher , school , phone , password) ;
                userDataManager.openDataBase();
                long flag = userDataManager.insertUserData(mUser) ;
                if (flag == -1){
                    Toast.makeText(this , "注册用户失败，请重新尝试！" , Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this , "注册成功！" , Toast.LENGTH_SHORT).show();
                    Intent mIntent = new Intent(this , LoginActivity.class) ;
                    startActivity(mIntent);
                    this.finish();
                }
            }else {
                Toast.makeText(this , "密码不一致，请重新输入密码！" , Toast.LENGTH_SHORT).show();
            }
        }
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
        if (userDataManager == null) {
            userDataManager = new UserDataManager(this);
            userDataManager.openDataBase();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (userDataManager != null) {
            userDataManager.closeDataBase();
            userDataManager = null;
        }

        super.onPause();
    }
}
