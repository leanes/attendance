package com.example.attendance.util;

/**
 * Created by 陈振聪 on 2017/4/8.
 */
public class UrlConstance {
    public static final String APP_URL = "http://pbyy5f.natappfree.cc/";

    //4.6注册用户接口
    public static final String REGIST_INFO = APP_URL + "teacher/register" ;

    //4.8登录用户接口
    public static final String LOGIN_INFO = "http://pbyy5f.natappfree.cc/teacher/login";

    //上传课程表接口
    public static final String COURSES_INFO = APP_URL + "c" ;

    //访问课程表接口
    public static final String COURSES_URL = APP_URL + "coure" ;

    //课表删除接口
    public static final String DELETE_COURSES = APP_URL + "delete" ;

    //4.9获取用户基本信息
    public static final String KEY_USER_BASE_INFO ="users/user_Info_handler.ashx";
}