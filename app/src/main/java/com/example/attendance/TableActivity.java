package com.example.attendance;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendance.adapter.GradeAdapter;
import com.example.attendance.adapter.GvContentAdapter;
import com.example.attendance.adapter.GvDateAdapter;
import com.example.attendance.adapter.LvNumAdapter;
import com.example.attendance.bean.Coordinate ;
import com.example.attendance.bean.Grade;
import com.example.attendance.test.MyGridView ;
import com.example.attendance.test.ShowAlertDialog;
import com.example.attendance.test.ShowPopwindow;
import com.example.attendance.util.UrlConstance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.attendance.util.JsonUtil.gradeParse;

/**
 * Created by 陈振聪 on 2017/2/13.
 */
public class TableActivity extends AppCompatActivity implements View.OnClickListener {
    private GridView gvDate, gvContent;
    private MyGridView lvNum;
    private MySchedule mySchedule;
    private boolean isClick = false;

    private EditText etClass;
    private List<Coordinate> gradeList;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tabe_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        init();
        gvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openAlter(position);
            }
        });

        if (!isClick) {
            mySchedule.read(2);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.table_toolbar , menu) ;
        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                finish();
                break;
            case R.id.refresh_table :
                mySchedule.read(1);
                isClick = true;
                break;
            default:
        }
        return true ;
    }

    private String TAG = "TableActivity";

    private void openAlter(final int position) {
        final AlertDialog alertDialog = new AlertDialog.Builder(TableActivity.this).create();
        alertDialog.show();
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog);
        final EditText etName = (EditText) window.findViewById(R.id.etName);
        final EditText etNum = (EditText) window.findViewById(R.id.etNum);
        etClass = (EditText) window.findViewById(R.id.etClass);
        etClass.setOnClickListener(this);
        final EditText etRoom = (EditText) window.findViewById(R.id.etRoom);
        final EditText etWeek = (EditText) window.findViewById(R.id.etWeek);
        Button btnSure = (Button) window.findViewById(R.id.btnSure);
        Button btnCancel = (Button) window.findViewById(R.id.btnCancel);
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(etName.getText().toString()) && !TextUtils.isEmpty(etNum.getText().toString()) &&
                        !TextUtils.isEmpty(etClass.getText().toString()) && !TextUtils.isEmpty(etRoom.getText().toString()) &&
                        !TextUtils.isEmpty(etWeek.getText().toString())) {
                    int num = Integer.valueOf(etNum.getText().toString());
                    if (num <= 12 & num > 0) {
                        Coordinate coordinate = new Coordinate(position, num, etName.getText().toString(),
                                etClass.getText().toString(), etRoom.getText().toString(), etWeek.getText().toString() );
                        mySchedule.addToList(coordinate);
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

    private void init() {
        gvDate = (GridView) findViewById(R.id.gvDate);
        gvDate.setAdapter(new GvDateAdapter(TableActivity.this));



        lvNum = (MyGridView) findViewById(R.id.lvNum);
        lvNum.setAdapter(new LvNumAdapter(TableActivity.this));

        gvContent = (GridView) findViewById(R.id.gvContent);
        gvContent.setAdapter(new GvContentAdapter(TableActivity.this));

        mySchedule = (MySchedule) findViewById(R.id.mySchedule);

        gradeList = new ArrayList<>();
        httpget();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.etClass:
                ShowAlertDialog.showpopwindow(this , gradeList , etClass);
                break;
        }
    }

    private void httpget() {
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(TableActivity.this, "获取班级信息失败 :", Toast.LENGTH_SHORT).show();
                            }
                        });

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