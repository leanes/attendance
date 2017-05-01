package com.example.attendance.bean;

/**
 * Created by 陈振聪 on 2017/4/17.
 */
public class User {
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String accessToken ;

    public String codeString ;          //记录二维码字段

    public String weekNow ;

    public boolean isRunning ;          //轮询查当前考勤

    public int attendId ;          //考勤id

    public int getAttendId() {
        return attendId;
    }

    public void setAttendId(int attendId) {
        this.attendId = attendId;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public String getWeekNow() {
        return weekNow;
    }

    public void setWeekNow(String weekNow) {
        this.weekNow = weekNow;
    }

    public String getCodeString() {
        return codeString;
    }

    public void setCodeString(String codeString) {
        this.codeString = codeString;
    }

    private static User sharedUser = null;
    public static User sharedUser() {
        if (sharedUser == null) {
            sharedUser = new User();
        }
        return sharedUser;
    }
}
