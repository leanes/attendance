package com.example.attendance.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by 陈振聪 on 2017/4/30.
 */
public class MyReceiver extends BroadcastReceiver {
    private Message message;
    private String TAG = "MyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        //接收MainActivity传过来的数据
        String result = intent.getStringExtra("data") ;
        Log.d(TAG , "传过来的消息：" + result) ;
        //调用Message接口的方法
        message.getMsg(result);

    }

    public interface Message {
        public void getMsg(String str);
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}