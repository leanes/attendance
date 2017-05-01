package com.example.attendance.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by 陈振聪 on 2017/4/13.
 */
public class WeeksNum implements Serializable{
    private int weeks ;
    private int attendance_num ;
    private int attenceId ;

    public int getAttenceId() {
        return attenceId;
    }

    public void setAttenceId(int attenceId) {
        this.attenceId = attenceId;
    }

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

    public int getWeeks() {
        return weeks;
    }

    public void setWeeks(int weeks) {
        this.weeks = weeks;
    }

    public int getAttendance_num() {
        return attendance_num;
    }

    public void setAttendance_num(int attendance_num) {
        this.attendance_num = attendance_num;
    }
}
