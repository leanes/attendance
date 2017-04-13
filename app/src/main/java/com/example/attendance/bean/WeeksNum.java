package com.example.attendance.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by 陈振聪 on 2017/4/13.
 */
public class WeeksNum implements Serializable{
    private String weeks ;
    private String attendance_num ;

    private Students students ;
    private ArrayList<Students>studentsArrayList ;

    public Students getStudents() {
        return students;
    }

    public void setStudents(Students students) {
        this.students = students;
    }

    public ArrayList<Students> getStudentsArrayList() {
        return studentsArrayList;
    }

    public void setStudentsArrayList(ArrayList<Students> studentsArrayList) {
        this.studentsArrayList = studentsArrayList;
    }

    public String getWeeks() {
        return weeks;
    }

    public void setWeeks(String weeks) {
        this.weeks = weeks;
    }

    public String getAttendance_num() {
        return attendance_num;
    }

    public void setAttendance_num(String attendance_num) {
        this.attendance_num = attendance_num;
    }
}
