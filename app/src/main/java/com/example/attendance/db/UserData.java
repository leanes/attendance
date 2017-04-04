package com.example.attendance.db;

/**
 * Created by 陈振聪 on 2017/2/8.
 */
public class UserData {
    private String userName;
    private String userPwd;
    private int userId;
    private String schoolName ;
    private String phoneNumber ;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public UserData(String userName , String schoolName , String phoneNumber , String userPwd , int userId) {
        super();
        this.userName = userName;
        this.schoolName = schoolName ;
        this.phoneNumber = phoneNumber ;
        this.userPwd = userPwd;
        this.userId = userId;
    }

    public UserData(String userName , String schoolName , String phoneNumber , String userPwd) {
        super();
        this.userName = userName;
        this.schoolName = schoolName ;
        this.phoneNumber = phoneNumber ;
        this.userPwd = userPwd;
    }
}
