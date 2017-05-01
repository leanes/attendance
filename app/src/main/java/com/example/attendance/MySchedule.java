package com.example.attendance;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.example.attendance.bean.Coordinate ;
import com.example.attendance.bean.Grade;
import com.example.attendance.bean.User;
import com.example.attendance.test.SaveLocation;
import com.example.attendance.test.ShowAlertDialog;
import com.example.attendance.test.ShowPopwindow;
import com.example.attendance.util.JsonUtil;
import com.example.attendance.util.UrlConstance;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.attendance.util.JsonUtil.dataCreate;
import static com.example.attendance.util.JsonUtil.dataEditor;
import static com.example.attendance.util.JsonUtil.dataParse;
import static com.example.attendance.util.JsonUtil.gradeParse;

/**
 * Created by 陈振聪 on 2017/2/13.
 */
public class MySchedule extends ViewGroup {
    private Context context ;
    private ArrayList<Coordinate> list ;
    private ArrayList<TextView>textViewList ;
    private static final String file_path = "/coursetable.txt" ;            //存储路径
    private static final String file_name = "/servercourse.txt" ;            //存储路径
    private File sdDir = Environment.getExternalStorageDirectory() ; //获取根目录

    private EditText etClass ;
    private List<Coordinate> gradeList;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final int ADD_VIEW = 1;
    public static final int UPLOAD_COURSES = 2 ;
    public static final int SAVE_COURSES = 3 ;
    public static final int DELETE_COURSES = 4 ;
    public static final int FAILURE = 5 ;
    public static final int EDITER_COURSES = 6 ;


    public MySchedule(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context ;
        list = new ArrayList<>() ;
        textViewList = new ArrayList<>() ;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childNum = getChildCount();
        for (int i = 0 ; i < childNum ; i++){
            View child = getChildAt(i) ;

            Coordinate child_coordinate = list.get(i) ;
            int position = child_coordinate.getPosition() ;

            //position是从0开始的  这个计算的是第几行和第几列，因为一周固定7天，所以这里直接使用了7
            int line = position / 7 ;
            int vertical = position % 7 ;

            //每个课程最小单元格的宽度和高度，注意，这里布局中的GridView和ListView是除了Divider的
            int item_width = getMeasuredWidth() / 7 ;
            int item_height = getMeasuredHeight() / 12 ;
            //给子View计算位置坐标，分别是左上角和右下角的坐标
            int left = vertical * item_width ;
            int top = line * item_height ;
            int right = (vertical + 1) * item_width ;
            int bottom = (line + child_coordinate.getClassNum()) * item_height ;
            child.layout(left + 5 , top + 5 , right - 5 , bottom - 5);
        }
    }


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case ADD_VIEW :
//                    ArrayList<Coordinate>arrayList = (ArrayList<Coordinate>) msg.obj;
                    list = (ArrayList<Coordinate>) msg.obj;
                    hanleAddViewResult(list);
                    break;
                case UPLOAD_COURSES :
                    JSONObject json = (JSONObject) msg.obj;
                    handleUploadResult(json) ;
                    break;
                case SAVE_COURSES :
                    list = (ArrayList<Coordinate>) msg.obj;
                    save() ;
                    break;
                case DELETE_COURSES :
                    JSONObject deletejson = (JSONObject) msg.obj;
                    handleDeleteResult(deletejson) ;
                    break;
                case FAILURE :
                    String string = (String) msg.obj;
                    Toast.makeText(getContext() , string ,Toast.LENGTH_SHORT).show();
                    break;
                case EDITER_COURSES :
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    handleEditorRusult(jsonObject) ;
                    break;

            }
        }
    };

    //编辑课程
    private void handleEditorRusult(JSONObject jsonObject) {
        int resultCode = 0;
        String msg = null;
        try {
            resultCode = jsonObject.getInt("result");
            msg = jsonObject.getString("msg") ;
        } catch (JSONException e) {
            Toast.makeText(getContext(), "没有获取到网络的响应！", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        switch(resultCode) {
            case 1:
                Toast.makeText(getContext() , msg, Toast.LENGTH_SHORT).show();
                break;
            case -1:
                Toast.makeText(getContext(), msg , Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(getContext(), "登陆失败！未知错误", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    private void handleDeleteResult(JSONObject deletejson) {
        /*
         * 1  删除成功
         * */
        int resultCode = 0;
        String msg = null;
        try {
            resultCode = deletejson.getInt("result");
            msg = deletejson.getString("msg") ;
        } catch (JSONException e) {
            Toast.makeText(getContext(), "没有获取到网络的响应！", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        switch(resultCode) {
            case 1:
                Toast.makeText(getContext() , msg, Toast.LENGTH_SHORT).show();
                break;
            case -1:
                Toast.makeText(getContext(), msg , Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(getContext(), "登陆失败！未知错误", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 保存到本地
     */

    private void save() {
        String json = JsonUtil.dataSave(list) ;
        File sdDir = null ;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ;     //判断SD卡是否存在
        if (sdCardExist){
            sdDir = Environment.getExternalStorageDirectory() ; //获取根目录
            FileOutputStream fout = null ;
            try {
                fout = new FileOutputStream(sdDir + file_path , false) ;
                byte[] bytes = json.getBytes() ;
                fout.write(bytes);
                fout.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void handleUploadResult(JSONObject json) {
        /*
         * 1  上传成功
         * 2  失败
         * */
        int resultCode = 0;
        String msg = null;
        try {
            resultCode = json.getInt("result");
            msg = json.getString("msg") ;
        } catch (JSONException e) {
            Toast.makeText(getContext(), "没有获取到网络的响应！", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        switch(resultCode) {
            case 1:
                Toast.makeText(context , msg, Toast.LENGTH_SHORT).show();
                break;
            case -1:
                Toast.makeText(context, msg , Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(context, "登陆失败！未知错误", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    private void hanleAddViewResult(ArrayList<Coordinate> arrayList) {
        for (TextView textView : textViewList ){
            removeView(textView);
        }
        for (int i = 0 ; i < list.size() ; i++)
        {
            addView(arrayList.get(i));
        }
        Toast.makeText(getContext() , "刷新成功" , Toast.LENGTH_SHORT).show();
    }

    //增加课程表

    //外部调用的、用于添加组件（课程）的方法
    public void addToList(final Coordinate coordinate){
        list.add(coordinate) ;
        addView(coordinate);
        save();
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient() ;
                String json = dataCreate(coordinate) ;
                RequestBody body = RequestBody.create(JSON , json) ;
                Request request = new Request.Builder()
                        .url(UrlConstance.COURSES_UP)
                        .post(body)
                        .build() ;
                Call call = client.newCall(request) ;
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        sendMessage(FAILURE , "请求失败,上传课程表失败！");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()){
                            String responseData = response.body().string() ;
                            Log.d("TableActivityADD" , responseData) ;
                            try {
                                JSONObject jsonObject = new JSONObject(responseData) ;
                                String mm = jsonObject.getString("msg") ;
                                sendMessage(UPLOAD_COURSES , jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

            }
        }).start();
        httpGet();
    }


    private String TAG = "MySchedule" ;
    //添加视图
    private void addView(final Coordinate coordinate) {
        TextView tv = new TextView(context) ;
        tv.setText(coordinate.getClassName()  + "\n" + "@" + coordinate.getClassRoom()  + "\n"
                + "#" + coordinate.getGrade()  + "\n" + "&" + coordinate.getWeek() );
        tv.setBackgroundColor(randomColor());
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(Color.parseColor("#ffffff"));
        tv.setTextSize(12);
        tv.setAlpha(0.80f);
        tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击打开查看课程信息
                openAlter(coordinate.getPosition() , coordinate.getClassName() ,
                        coordinate.getClassRoom() , coordinate.getGrade() , coordinate.getWeek()  , coordinate.getCourseId() ) ;
            }
        });
        textViewList.add(tv) ;
        addView(tv) ;
    }

    //查看课程信息
    private void openAlter(final int position, final String className , final String classRoom , final String grade ,
                           final String week , final int courseId) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create() ;
        alertDialog.show();
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        final Window window = alertDialog.getWindow() ;
        window.setContentView(R.layout.dialog_edit);
        TextView tvContent = (TextView) window.findViewById(R.id.tvContent);
        tvContent.setText(className);

        TextView tvRoom = (TextView) window.findViewById(R.id.tvclassRoom);
        tvRoom.setText(classRoom);

        TextView tvClass = (TextView) window.findViewById(R.id.tvClass);
        tvClass.setText(grade);

        TextView tvWeek = (TextView) window.findViewById(R.id.tvWeek);
        tvWeek.setText(week);




        Button btnDele = (Button) window.findViewById(R.id.btnDele);
        Button btnEditor = (Button) window.findViewById(R.id.btnEditor);
        Button btnAtten = (Button) window.findViewById(R.id.btnatten);

        btnDele.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext()) ;
                dialog.setTitle("警告") ;
                dialog.setMessage("你确定要删除本课程吗？") ;
                dialog.setCancelable(true) ;
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        remove(courseId) ;
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show() ;
                alertDialog.dismiss();
            }
        });

        btnAtten.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext() , QrCodeActivity.class) ;
                intent.putExtra("courseId" , courseId) ;
                context.startActivity(intent);
            }
        });

        btnEditor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                window.setContentView(R.layout.editor_dialog);
                gradeList = new ArrayList<Coordinate>() ;
                httpgetGrade();

                final EditText etName = (EditText) window.findViewById(R.id.etName);
                final EditText etNum = (EditText) window.findViewById(R.id.etNum);
                etClass = (EditText) window.findViewById(R.id.etClass);
                etClass.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShowAlertDialog.showpopwindow(getContext() , gradeList , etClass);
                    }
                });
                final EditText etRoom = (EditText) window.findViewById(R.id.etRoom);
                final EditText etPosi = (EditText) window.findViewById(R.id.position);
                final EditText etWeek = (EditText) window.findViewById(R.id.etWeek);
                Button btnSure = (Button) window.findViewById(R.id.btnSure);
                Button btnCancel = (Button) window.findViewById(R.id.btnCancel);

                etName.setText(className);
                etRoom.setText(classRoom);
//                etPosi.setText(position);
//                etClass.setText(grade);
                etWeek.setText(week);

                btnSure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(etName.getText().toString()) && !TextUtils.isEmpty(etNum.getText().toString())&&
                                !TextUtils.isEmpty(etPosi.getText().toString()) &&
                                !TextUtils.isEmpty(etClass.getText().toString())&& !TextUtils.isEmpty(etRoom.getText().toString())&&
                                !TextUtils.isEmpty(etWeek.getText().toString())){
                            int num = Integer.valueOf(etNum.getText().toString()) ;
                            int posi = Integer.parseInt(etPosi.getText().toString().trim());
                            if (num <= 12 & num > 0) {
                                Coordinate coordinate = new Coordinate(posi, num, etName.getText().toString(),
                                        etClass.getText().toString(), etRoom.getText().toString(),courseId  ,  etWeek.getText().toString()  );
                                editorCourse(coordinate);
                                alertDialog.dismiss();
                            }

                        }
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });
    }


 //编辑课程
    private void editorCourse(final Coordinate coordinate) {
        list.add(coordinate) ;
        addView(coordinate);
        save();
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient() ;
                String json = dataEditor(coordinate) ;
                RequestBody body = RequestBody.create(JSON , json) ;
                Request request = new Request.Builder()
                        .url(UrlConstance.EDITOR_COURSES)
                        .post(body)
                        .build() ;
                Call call = client.newCall(request) ;
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        sendMessage(FAILURE , "请求失败,上传课程表失败！");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()){
                            String responseData = response.body().string() ;
                            Log.d("TableActivityADD" , responseData) ;
                            try {
                                JSONObject jsonObject = new JSONObject(responseData) ;
                                sendMessage(EDITER_COURSES , jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

            }
        }).start();
    }


    //删除课程表

    private void remove(final int courseId) {
        /**
         * 向服务器请求删除课程
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient() ;
                String json = JsonUtil.deleteCourse(courseId) ;
                Log.d("TableActivity" , json) ;
                RequestBody body = RequestBody.create(JSON , json) ;
                Request request = new Request.Builder()
                        .url(UrlConstance.DELETE_COURSES)
                        .post(body)
                        .build() ;
                Call call = client.newCall(request) ;
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        sendMessage(FAILURE , "请求失败,删除课程表失败！");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()){
                            String responseData = response.body().string() ;
                            Log.d("TableActivity" , responseData) ;
                            try {
                                JSONObject jsonObject = new JSONObject(responseData) ;
                                sendMessage(DELETE_COURSES , jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

            }
        }).start();

        for (int i = 0 ; i < list.size() ; i++){
            if (list.get(i).getCourseId() == courseId){
                list.remove(i) ;
                removeView(getChildAt(i));
            }
        }
        save();
    }

    //随机生成颜色
    private int randomColor() {
        Random random = new Random() ;
        return Color.argb(255 , random.nextInt(256) , random.nextInt(256) , random.nextInt(256));
    }

    //把SD卡下的数据读取到list中，然后一一显示
    public void read(int who){
        if (who == 1){
            httpGet();
        }
        if (who == 2){
            readLocation() ;
        }
    }

    private void readLocation() {
        File sdDir = null ;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ;     //判断SD卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory(); //获取根目录
            File file = new File(sdDir + file_name) ;
            if (!file.exists()){
                return;
            }
            FileInputStream fin = null ;
            try {
                fin = new FileInputStream(sdDir + file_name) ;
                int length = fin.available() ;
                byte[] bytes = new byte[length] ;
                fin.read(bytes) ;
                String res = new String(bytes , "utf-8") ;
                fin.close();
                dataParse(res , list);
                //此时已经将读取的数据全部存入到list中，只需要将list中的view显示出来就可，不需要保存
                for (int i = 0 ; i < list.size() ; i++)
                {
                    addView(list.get(i));
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void httpGet(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient() ;
                String query = "{\"accessToken\":"+ User.sharedUser().getAccessToken() + "}" ;
                RequestBody body = RequestBody.create(JSON , query) ;
                Request request = new Request.Builder()
                        .url(UrlConstance.COURSES_INFO)
//                        .post(body)
                        .build() ;
                Call call = client.newCall(request) ;
                call.enqueue(new Callback() {
                    //请求失败回调
                    @Override
                    public void onFailure(Call call, IOException e) {
                        sendMessage(FAILURE , "请求失败！" + e);

                    }

                    //请求成功时回调
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()){
                            String responseData = response.body().string() ;
                            Log.d("TableActivity" , responseData) ;
                            SaveLocation.saveLocal(responseData , sdDir , file_name);
                            JsonUtil.dataParse(responseData , list ) ;

                            sendMessage(ADD_VIEW , list);
                            sendMessage(SAVE_COURSES , list);

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


    private void httpgetGrade() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(UrlConstance.GRADE_INFO)
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    //请求失败时调用
                    @Override
                    public void onFailure(Call call, IOException e) {
                        sendMessage(FAILURE , "请求失败！" + e);
                    }

                    //请求成功时调用
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String responseData = response.body().string();
                            gradeParse(responseData, gradeList);
                        }
                    }
                });
            }
        }).start();
    }
}
