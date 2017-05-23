package com.example.attendance.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 陈振聪 on 2017/3/16.
 */
public class Students implements Serializable{
    private String student_id ;
    private String student_name ;
    private String grade ;
    private int classId ;
    private String isAttend ;
    private String imageUrl ;
    private String signTime ;

    public String getSignTime() {
        return signTime;
    }

    public void setSignTime(int signTime) {
        if (signTime == 0){
            this.signTime = "" ;
        }else {
            String time = null ;
            time = getdatatime(signTime) ;
            this.signTime = time;
        }

    }


    private String getdatatime(int signTime) {
        long time = (long)signTime * 1000 ;
        Timestamp ts = new Timestamp(time) ;
        String tsStr = "";
        DateFormat dateFormat = new SimpleDateFormat("MM月dd HH:mm:ss");
        tsStr = dateFormat.format(ts);
        return tsStr;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getIsAttend() {
        return isAttend;
    }

    public void setIsAttend(String isAttend) {
        String isLate = null ;
        if (isAttend.equals("0")){
            isLate = "迟到" ;
        }else if (isAttend.equals("1")){
            isLate = "正常" ;
        }else if (isAttend.equals("-1")){
            isLate = "缺勤" ;
        }
        this.isAttend = isLate ;
    }

    public Students() {

    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
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
