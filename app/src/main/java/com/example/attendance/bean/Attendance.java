package com.example.attendance.bean;

/**
 * Created by 陈振聪 on 2017/3/23.
 */
public class Attendance {
    private String attendanceDate ;         //考勤时间
    private String course ;                 //课程
    private String grade ;                   //学生班级
    private String studentName ;            //学生名字
    private String studentId ;              //学生学号

    public Attendance() {
    }

    public Attendance(String attendanceDate, String course, String grade, String studentName, String studentId) {
        this.attendanceDate = attendanceDate;
        this.course = course;
        this.grade = grade;
        this.studentName = studentName;
        this.studentId = studentId;
    }

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
