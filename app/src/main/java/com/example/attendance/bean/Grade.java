package com.example.attendance.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by 陈振聪 on 2017/4/14.
 */
public class Grade implements Serializable {
    private String grade ;

    private ArrayList<Students>studentsList = new ArrayList<>() ;

    private Students students ;

    public Students getStudents() {
        return students;
    }

    public void setStudents(Students students) {
        this.students = students;
    }

    public ArrayList<Students> getStudentsList() {
        return studentsList;
    }

    public void setStudentsList(ArrayList<Students> studentsList) {
        this.studentsList = studentsList;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
