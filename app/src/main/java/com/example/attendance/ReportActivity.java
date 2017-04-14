package com.example.attendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.attendance.adapter.StudentAdapter;
import com.example.attendance.bean.Students;
import com.example.attendance.bean.WeeksNum;

import java.util.ArrayList;

/**
 * 详细罗列考勤学生信息
 */
public class ReportActivity extends AppCompatActivity {
    private WeeksNum weeksNum ;
    private ListView listView ;
    private ArrayList<Students>studentsArrayList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        listView = (ListView) findViewById(R.id.student_list) ;
        Intent intent = getIntent() ;
        Bundle bundle = intent.getExtras() ;
        weeksNum = (WeeksNum) bundle.getSerializable("weekNum");
        studentsArrayList = weeksNum.getStudentsArrayList() ;

        StudentAdapter adapter = new StudentAdapter(ReportActivity.this , R.layout.student_list , studentsArrayList) ;
        listView.setAdapter(adapter) ;

    }
}
