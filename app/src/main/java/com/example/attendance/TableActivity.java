package com.example.attendance;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;

import com.example.attendance.adapter.GvContentAdapter;
import com.example.attendance.adapter.GvDateAdapter;
import com.example.attendance.adapter.LvNumAdapter;
import com.example.attendance.bean.Coordinate ;
import com.example.attendance.test.MyGridView ;

/**
 * Created by 陈振聪 on 2017/2/13.
 */
public class TableActivity extends Activity {
    private GridView gvDate , gvContent ;
    private MyGridView lvNum ;
    private ImageButton refresh ;
    private MySchedule mySchedule ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        init() ;
        gvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openAlter(position) ;
            }
        });
        mySchedule.read(2) ;
    }
private String TAG = "TableActivity" ;
    private void openAlter(final int position) {
        final AlertDialog alertDialog = new AlertDialog.Builder(TableActivity.this).create() ;
        alertDialog.show();
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        Window window = alertDialog.getWindow() ;
        window.setContentView(R.layout.dialog);
        final EditText etName = (EditText) window.findViewById(R.id.etName);
        final EditText etNum = (EditText) window.findViewById(R.id.etNum);
        final EditText etClass = (EditText) window.findViewById(R.id.etClass);
        final EditText etRoom = (EditText) window.findViewById(R.id.etRoom);
        final EditText etWeek = (EditText) window.findViewById(R.id.etWeek);
        Button btnSure = (Button) window.findViewById(R.id.btnSure);
        Button btnCancel = (Button) window.findViewById(R.id.btnCancel);
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(etName.getText().toString()) && !TextUtils.isEmpty(etNum.getText().toString())&&
                        !TextUtils.isEmpty(etClass.getText().toString())&& !TextUtils.isEmpty(etRoom.getText().toString())&&
                        !TextUtils.isEmpty(etWeek.getText().toString())){
                    int num = Integer.valueOf(etNum.getText().toString()) ;
                    if (num <= 12 & num > 0){
                        Coordinate coordinate = new Coordinate(position , num , etName.getText().toString(),
                                etClass.getText().toString() , etRoom.getText().toString() , etWeek.getText().toString()) ;
                        mySchedule.addToList(coordinate) ;
                        Log.d(TAG, "coordinate.getClassRoom() = " + coordinate.getClassRoom() );
                        Log.d(TAG, "coordinate.getGrade() = " + coordinate.getGrade() );
                        Log.d(TAG, "coordinate.getWeek() = " + coordinate.getWeek() );
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

        refresh = (ImageButton) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mySchedule.read(1);
            }
        });

        lvNum = (MyGridView) findViewById(R.id.lvNum);
        lvNum.setAdapter(new LvNumAdapter(TableActivity.this));

        gvContent = (GridView) findViewById(R.id.gvContent);
        gvContent.setAdapter(new GvContentAdapter(TableActivity.this));

        mySchedule = (MySchedule) findViewById(R.id.mySchedule);

    }
}
