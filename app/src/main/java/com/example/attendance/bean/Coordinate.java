package com.example.attendance.bean;

import java.io.Serializable;

/**
 * Created by 陈振聪 on 2017/2/13.
 */
public class Coordinate implements Serializable {
    private int position ;
    private int classNum ;
    private String className ;
    private String grade ;
    private String classRoom ;

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

    public Coordinate(int position, int classNum, String className, String grade, String classRoom, String week) {
        this.position = position;
        this.classNum = classNum;
        this.className = className;
        this.grade = grade;
        this.classRoom = classRoom;
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
