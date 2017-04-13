package com.example.attendance.bean;

import java.io.Serializable;

/**
 * Created by 陈振聪 on 2017/3/16.
 */
public class Students implements Serializable{
    private String student_id ;
    private String student_name ;
    private String grade ;

    public Students() {

    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Students(String grade, String student_name, String student_id) {
        this.student_id = student_id;
        this.student_name = student_name;
        this.grade = grade;
    }
}
