package com.example.attendance.util;

import com.example.attendance.bean.User;

/**
 * Created by 陈振聪 on 2017/4/8.
 */
public class UrlConstance {
    public static final String APP_URL = "http://4q3irm.natappfree.cc/";

    //4.6注册用户接口
    public static final String REGIST_INFO = APP_URL +"attendance/public/index.php/index/teacher/register" ;

    //4.8登录用户接口
    public static final String LOGIN_INFO = APP_URL + "attendance/public/index.php/index/teacher/login";

    //查询课程表接口searchCourse
    public static final String COURSES_INFO = APP_URL + "attendance/public/index.php/index/teacher/searchCourse?accessToken=" + User.sharedUser().getAccessToken() ;

    //上传课程接口
    public static final String COURSES_UP = APP_URL + "attendance/public/index.php/index/teacher/addCourse" ;

    //课表删除接口
    public static final String DELETE_COURSES = APP_URL + "attendance/public/index.php/index/teacher/deleteCourse" ;

    //课程编辑
    public static final String EDITOR_COURSES = APP_URL + "attendance/public/index.php/index/teacher/editCourse" ;

    //获取班级数据
    public static final String GRADE_INFO =APP_URL+"attendance/public/index.php/index/teacher/getAllGrade";

    //提交二维码接口
    public static final String CODE_INFO =APP_URL+"attendance/public/index.php/index/attend/getAttendCourseMsg";

    //签到学生接口
    public static final String SIGN_INFO =APP_URL+"attendance/public/index.php/index/attend/getAttendDetail";

    //考勤记录接口
    public static final String ATTENDCODE_INFO =APP_URL+"attendance/public/index.php/index/attend/getAllAttendByCourse";

    //获取班级接口
    public static final String CLASS_INFO =APP_URL+"attendance/public/index.php/index/teacher/getClassByTeacher";

    //获取班级学生接口
    public static final String STUDENT_INFO =APP_URL+"attendance/public/index.php/index/teacher/getStudentByClass";

    //轮询接口attend/getAttendNum
    public static final String POLLING_INFO =APP_URL+"attendance/public/index.php/index/attend/getAttendNum";

}
