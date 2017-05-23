package com.example.attendance;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendance.bean.User;
import com.example.attendance.service.MyReceiver;
import com.example.attendance.service.PollingService;
import com.example.attendance.test.ShowPopwindow;
import com.example.attendance.util.JsonUtil;
import com.example.attendance.util.UrlConstance;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class QrCodeActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener ,  MyReceiver.Message{
    private ImageView imageView ;
    private EditText accountweekEdit ;
    private ImageView down ;
    private Button attendanbtn ;
    private TextView number ;

    private IntentFilter intentFilter ;
    private MyReceiver myReceiver;
    private Intent intentService ;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private List<String>weekList ;
    private int week = 0 ;
    private int selectWeek ;
    private int courseId ;
    private PopupWindow popwindow;
    private ListView lv_list;
    private String codeSting ;
    private int attendId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        Toolbar toolbar = (Toolbar) findViewById(R.id.code_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar() ;
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent() ;
        Bundle bundle = intent.getExtras() ;
        courseId = bundle.getInt("courseId") ;
        selectWeek = bundle.getInt("week") ;
        String[] weeks = getResources().getStringArray(R.array.weeks) ;
        weekList = new ArrayList<>() ;
        for (int i = 0 ; i < weeks.length ; i++){
            weekList.add(weeks[i]) ;
        }
        Log.d("week" , String.valueOf(weekList)) ;
        imageView = (ImageView) findViewById(R.id.codeImage);
        number = (TextView) findViewById(R.id.code_num);
        accountweekEdit = (EditText) findViewById(R.id.accountweek);
        down = (ImageView) findViewById(R.id.down);
        attendanbtn = (Button) findViewById(R.id.attendanbtn);
        down.setOnClickListener(this);
        attendanbtn.setOnClickListener(this);

        accountweekEdit.setText(User.sharedUser().getWeekNow());
        try {
//            Bitmap temp = crateBitmap(new String(responseData.getBytes() , "ISO-8859-1"));
            String string = User.sharedUser().getCodeString() ;
            Bitmap temp = crateBitmap(new String(string.getBytes() , "ISO-8859-1"));
            imageView.setImageBitmap(temp);
        } catch (Exception e) {
            e.printStackTrace();
        }
            //注册广播接收器
            myReceiver = new MyReceiver();
            intentFilter = new IntentFilter("com.example.attendance.POLLING_BROADCAST");
            registerReceiver(myReceiver, intentFilter);
            //因为这里需要注入Message，所以不能在AndroidManifest文件中静态注册广播接收器
            myReceiver.setMessage(this);
            intentService = new Intent(this , PollingService.class) ;
            serviceThread() ;


    }

    private void serviceThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    startService(intentService) ;
                }

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


    private Bitmap crateBitmap(String s) throws WriterException {
            BitMatrix matrix = new MultiFormatWriter().encode(s , BarcodeFormat.QR_CODE , 300 , 300) ;
            int width = matrix.getWidth() ;
            int height = matrix.getHeight() ;
            int[] pixels = new int[width * height] ;
            for (int y = 0 ; y < height ; y++){
                for (int x = 0 ; x < width ; x++){
                    if (matrix.get(x , y)){
                        pixels[y * width + x] = 0xff000000 ;
                    }else {
                        pixels[y * width + x] = 0xffffffff ;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width , height , Bitmap.Config.ARGB_8888) ;
            bitmap.setPixels(pixels , 0 , width , 0 , 0 , width , height);
        return bitmap;
    }


    private void httpGre() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient() ;
//                String json = "{\"accessToken\":"+ User.sharedUser().getAccessToken() + "}" ;
                String json = JsonUtil.codeParse(courseId ,week) ;
                RequestBody body = RequestBody.create(JSON , json) ;
                Request request = new Request.Builder()
                        .url(UrlConstance.CODE_INFO)
                        .post(body)
                        .build() ;
                Call call = client.newCall(request) ;
                call.enqueue(new Callback() {
                    //请求失败回调
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(QrCodeActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    //请求成功时回调
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String responseData = response.body().string();
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(responseData);
                                JSONObject object = jsonObject.getJSONObject("data") ;
                                attendId = object.getInt("attendId") ;
                                User.sharedUser().setAttendId(attendId);
                                codeSting = object.toString() ;
                                User.sharedUser().setCodeString(codeSting);
//                                User.sharedUser().setRunning(true);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Bitmap temp = crateBitmap(new String(codeSting.getBytes() , "ISO-8859-1"));
                                        imageView.setImageBitmap(temp);


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                }) ;
            }
        }).start();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.down:
                showPopwindow() ;
//                ShowPopwindow.showpopwindow(this , weekList , accountweekEdit);
                break;
            case R.id.attendanbtn :
                if (accountweekEdit.getText().toString().trim().equals("")){
                    Toast.makeText(this , "请选择当前是第几周" , Toast.LENGTH_SHORT).show();
                }else {
                    week = Integer.parseInt(accountweekEdit.getText().toString().trim()) ;
                    AlertDialog.Builder dialog = new AlertDialog.Builder(QrCodeActivity.this) ;
                    dialog.setTitle("提示") ;
                    dialog.setMessage("你选择考勤的是第" + week + "周的星期"+ selectWeek) ;
                    dialog.setCancelable(true) ;
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int thisWeek = getWeek() ;
                            if (thisWeek == selectWeek){
                                httpGre();
                            }else {
                                Toast.makeText(QrCodeActivity.this , "当前不是该课程的考勤日期" , Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show() ;
                }
                break;
        }
    }

    private int getWeek() {
        Calendar calendar = Calendar.getInstance();
        java.util.Date date = new java.util.Date() ;
        calendar.setTime(date);
        int w =  calendar.get(Calendar.DAY_OF_WEEK) - 1 ;
        if(w == 0){
            w = 7 ;
        }
        return w;
    }


    private void showPopwindow() {
        initListview();
        popwindow = new PopupWindow(lv_list, accountweekEdit.getWidth() - 4, 500);

        // 设置点击外部可以被关闭.
        popwindow.setOutsideTouchable(true);
        popwindow.setBackgroundDrawable(new BitmapDrawable());

        // 获取焦点
        popwindow.setFocusable(true);
        // 显示在输入框的左下角位置
        popwindow.showAsDropDown(accountweekEdit, 2, -4);
    }
    private void initListview() {
        lv_list=new ListView(this);
        lv_list.setDivider(null);
        lv_list.setDividerHeight(0);
        lv_list.setBackgroundResource(R.drawable.listview_background);
        lv_list.setOnItemClickListener(this);

        // 绑定数据
        lv_list.setAdapter(new MyNumberAdapter());
    }

    @Override
    public void getMsg(String str) {
        number.setText(str);
    }

    class MyNumberAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return weekList.size();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            NumberViewHolder mHolder = null;
            if(convertView == null) {
                convertView = View.inflate(QrCodeActivity.this, R.layout.week_list, null);
                mHolder = new NumberViewHolder();
                mHolder.tvNumber = (TextView) convertView.findViewById(R.id.week_list);
                convertView.setTag(mHolder);
            } else {
                // 取出holder类
                mHolder = (NumberViewHolder) convertView.getTag();
            }

            // 给mHolder类中的对象赋值.
            mHolder.tvNumber.setText(weekList.get(position));
            return convertView;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }
    }

    class NumberViewHolder {
        public TextView tvNumber;
    }
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        String number = weekList.get(position);
        accountweekEdit.setText("    "+number);
        User.sharedUser().setWeekNow("    "+number);
        popwindow.dismiss();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(myReceiver);
        stopService(intentService) ;
        super.onDestroy();
    }
}
