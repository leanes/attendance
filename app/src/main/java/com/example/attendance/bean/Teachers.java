package com.example.attendance.bean;

import java.io.Serializable;

/**
 * Created by 陈振聪 on 2017/4/8.
 */
public class Teachers implements Serializable {
    private String classTeacher;            //教师名字
    private String userPwd;                //密码
    private String schoolName ;             //学校名字
    private String phoneNumber ;            //手机号码

    public Teachers(String classTeacher, String userPwd, String schoolName, String phoneNumber) {
        this.classTeacher = classTeacher;
        this.userPwd = userPwd;
        this.schoolName = schoolName;
        this.phoneNumber = phoneNumber;
    }

    public Teachers(String phoneNumber , String userPwd) {
        this.phoneNumber = phoneNumber;
        this.userPwd = userPwd;
    }

    public String getClassTeacher() {
        return classTeacher;
    }

    public void setClassTeacher(String classTeacher) {
        this.classTeacher = classTeacher;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
