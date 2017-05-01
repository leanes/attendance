package com.example.attendance.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by 陈振聪 on 2017/2/13.
 */
public class Coordinate implements Serializable {
    private int position ;          //从哪一节开始
    private int classNum ;          //连续上几节课
    private String className ;        //课程名
    private int classId ;            //班级id
    private String grade ;              //上课班级
    private String classRoom ;          //课室
    private int courseId ;           //课程Id



    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getCourseId() {
        return courseId;
    }


    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    private WeeksNum weeksNum ;

    public WeeksNum getWeeksNum() {
        return weeksNum;
    }

    public void setWeeksNum(WeeksNum weeksNum) {
        this.weeksNum = weeksNum;
    }

    private ArrayList<WeeksNum>weeksNumArrayList = new ArrayList<>() ;
//    private ArrayList<Students>studentsArrayList = new ArrayList<>() ;

    public ArrayList<WeeksNum> getWeeksNumArrayList() {
        return weeksNumArrayList;
    }

    public void setWeeksNumArrayList(ArrayList<WeeksNum> weeksNumArrayList) {
        this.weeksNumArrayList = weeksNumArrayList;
    }

/*    public ArrayList<Students> getStudentsArrayList() {
        return studentsArrayList;
    }

    public void setStudentsArrayList(ArrayList<Students> studentsArrayList) {
        this.studentsArrayList = studentsArrayList;
    }*/

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    private String week ;

    public Coordinate() {

    }

    public Coordinate(String className, String grade) {
        this.className = className;
        this.grade = grade;
    }

    public Coordinate(int position, int classNum, String className, String grade, String classRoom, String week ) {
        this.position = position;
        this.classNum = classNum;
        this.className = className;
        this.grade = grade;
        this.classRoom = classRoom;
        this.week = week;
    }

    public Coordinate(int position, int classNum, String className, String grade, String classRoom, int courseId, String week ) {
        this.position = position;
        this.classNum = classNum;
        this.className = className;
        this.grade = grade;
        this.classRoom = classRoom;
        this.courseId = courseId;
        this.week = week;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getClassNum() {
        return classNum;
    }

    public void setClassNum(int classNum) {
        this.classNum = classNum;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
