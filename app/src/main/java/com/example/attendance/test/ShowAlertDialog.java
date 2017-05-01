package com.example.attendance.test;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.attendance.R;
import com.example.attendance.adapter.GradeAdapter;
import com.example.attendance.bean.Cid;
import com.example.attendance.bean.Coordinate;
import com.example.attendance.bean.Grade;

import java.util.List;


/**
 * Created by 陈振聪 on 2017/4/25.
 */
public class ShowAlertDialog {
    private static ListView listview ;
    private static GradeAdapter adapter ;
    public static void showpopwindow(final Context context , final List<Coordinate> list , final EditText etClass){
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.show();
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.activity_grade);
        listview = (ListView) window.findViewById(R.id.grade_list);
        Button surebtn = (Button) window.findViewById(R.id.surebtn);
        adapter = new GradeAdapter(context  , list) ;
        listview.setAdapter(adapter);

        surebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long selected[]=listview.getCheckedItemIds();
                String str = "";
                String cid = "" ;
                for (int i = 0; i < selected.length; i++) {
                    if (i == selected.length - 1){
                        str=str+list.get(((int)selected[i])).getClassName();
                        cid=cid+list.get(((int)selected[i])).getClassId();

                    }else {
                        str=str+list.get(((int)selected[i])).getClassName()+",";
                        cid=cid+list.get(((int)selected[i])).getClassId()+",";
                    }
                }
                etClass.setText(str);
                Cid.sharedCid().setCid(cid);
                alertDialog.dismiss();
            }
        });

    }

}
