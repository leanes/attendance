package com.example.attendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.attendance.adapter.AttendanceAdapter;
import com.example.attendance.bean.Coordinate;
import com.example.attendance.bean.Students;
import com.example.attendance.bean.WeeksNum;
import com.example.attendance.test.ExcelAttendance;

import java.util.ArrayList;

public class AttendanceActivity extends AppCompatActivity {
    private Coordinate coordinate ;
    private ArrayList<WeeksNum>weeksNumArrayList ;
    private ArrayList<Students>studentsArrayList ;
    private ListView listView ;
    private int tag = 0 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        listView = (ListView) findViewById(R.id.attendance_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Intent intent = getIntent() ;
        Bundle bundle = intent.getExtras() ;
        tag = bundle.getInt("tag") ;
//        weeksNumArrayList = (ArrayList<WeeksNum>) bundle.getSerializable("weekNa");
        coordinate = (Coordinate) bundle.getSerializable("coordinate");
        weeksNumArrayList = coordinate.getWeeksNumArrayList() ;
        AttendanceAdapter adapter = new AttendanceAdapter(AttendanceActivity.this , R.layout.attendance_list , weeksNumArrayList) ;
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WeeksNum weeksNum = weeksNumArrayList.get(position) ;
                if (tag == 1){
                    Intent intentReport = new Intent(AttendanceActivity.this , ReportActivity.class) ;
                    intentReport.putExtra("weekNum" , weeksNum) ;
                    startActivity(intentReport) ;
                }else if (tag == 2){
                    String className = coordinate.getClassName() ;
                    int section = coordinate.getPosition()/7 + 1;
                    int week = coordinate.getPosition()%7 + 1 ;
                    String weeks = weeksNum.getWeeks() ;
                    ArrayList<Students>studentses = weeksNum.getStudentsArrayList() ;
                    ExcelAttendance.writeExcelAttendance(AttendanceActivity.this , studentses , "/"+className+weeks+"周"+week+"第"+section+"节"+".xlsx");
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (tag == 1){
            getMenuInflater().inflate(R.menu.toolbar , menu) ;
        }

        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add :
                Intent code = new Intent(AttendanceActivity.this , QrCodeActivity.class) ;
                startActivity(code);
                break;
        }
        return true ;
    }
}
