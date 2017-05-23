package com.example.attendance.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

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

import static com.example.attendance.util.JsonUtil.pollSpan;

/**
 * Created by 陈振聪 on 2017/4/30.
 */
public class PollingService extends Service {
    private IntentFilter intentFilter ;
//    private LocalReceiver localReceiver;

    private String TAG = "PollingService" ;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final int FAILURE = 0;
    public static final int SEND = 1;


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case FAILURE:
                    String s = (String) msg.obj;
                    Toast.makeText(getApplicationContext() , s , Toast.LENGTH_SHORT).show();
                    break;
                case SEND :
                    String data = (String) msg.obj;
                    sendLocalBroadcast(data);
            }
        };
    };
    private void sendLocalBroadcast(String string) {
        Intent intent=new Intent("com.example.attendance.POLLING_BROADCAST") ;
        intent.putExtra("data" , string) ;
        sendBroadcast(intent);
    }

    private void sendMessage(int what, Object obj) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = obj;
        mHandler.sendMessage(msg);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        /*intentFilter=new IntentFilter();
        intentFilter.addAction("com.example.push.LOCAL_BROADCAST");*/
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new PollingThread().start();
        Log.d(TAG , "服务开启") ;
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * Polling thread
     * 模拟向Server轮询的异步线程
     */
    int count = 0;
    class PollingThread extends Thread {
        @Override
        public void run() {
            httpGet();


        }
    }

    private void httpGet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                OkHttpClient client = new OkHttpClient() ;
                String json = pollSpan() ;
                RequestBody body = RequestBody.create(JSON , json) ;
                Request request = new Request.Builder()
                        .url(UrlConstance.POLLING_INFO)
                        .post(body)
                        .build() ;
                Call call = client.newCall(request) ;
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
//                        sendMessage(FAILURE, "请求失败");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()){
                            String responseData = response.body().string() ;
                            try {
                                JSONObject jsonObject =new JSONObject(responseData) ;
                                JSONObject object = jsonObject.getJSONObject("data") ;
                                int num = object.getInt("attendNum") ;
                                String numbe = String.valueOf(num);
                                sendMessage(SEND ,numbe);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }
                });
            }
        }).start();
    }


    @Override
    public void onDestroy() {
        Log.d(TAG , "服务关闭") ;
        super.onDestroy();
    }
}
