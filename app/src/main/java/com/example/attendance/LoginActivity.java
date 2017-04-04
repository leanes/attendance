package com.example.attendance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.attendance.db.UserDataManager;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText userEditext ;
    private EditText passwordEditext ;
    private Button loginButton ;
    private Button registerButton ;

    private CheckBox rememberPass ;

    private SharedPreferences pref ;
    private SharedPreferences.Editor editor ;

    private UserDataManager mUserDataManager ;
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

        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this) ;
            mUserDataManager.openDataBase();
        }

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
            String userName = userEditext.getText().toString().trim() ;
            String password = passwordEditext.getText().toString().trim() ;
          /*  if (userName.equals("123")&&password.equals("123")){
                Intent loginSuccess = new Intent(LoginActivity.this , HomeActivity.class) ;
                startActivity(loginSuccess);
            }*/
            int result = mUserDataManager.findUserByPhoneAndPwd(userName , password) ;
            if (result == 1){
                //login success
                editor = pref.edit() ;
                if (rememberPass.isChecked()){
                    editor.putBoolean("remember_pass" , true) ;
                    editor.putString("userName" , userName) ;
                    editor.putString("password" , password) ;
                }else {
                    editor.clear() ;
                }
                editor.apply();
                Intent loginSuccess = new Intent(LoginActivity.this , HomeActivity.class) ;
                startActivity(loginSuccess);
            }else if (result == 0){
                //login failed
                Toast.makeText(this , "登陆失败！请输入正确的用户名与密码！" , Toast.LENGTH_SHORT).show();
            }
        }
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
