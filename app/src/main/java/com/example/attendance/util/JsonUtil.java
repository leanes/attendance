package com.example.attendance.util;

import android.util.Log;

import com.example.attendance.LoginActivity;
import com.example.attendance.bean.Attendance;
import com.example.attendance.bean.Cid;
import com.example.attendance.bean.Coordinate;
import com.example.attendance.bean.Grade;
import com.example.attendance.bean.Students;
import com.example.attendance.bean.Teachers;
import com.example.attendance.bean.User;
import com.example.attendance.bean.WeeksNum;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 陈振聪 on 2017/3/17.
 */
public class JsonUtil {
    /**
     * 解析JSON课程表
     *
     * @param data
     * @param list
     */
    public static void dataParse(String data, ArrayList<Coordinate> list) {
        try {
            list.clear();
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.optJSONObject(i);
                Coordinate coordinate = new Coordinate();
                coordinate.setCourseId(object.optInt("courseId"));
                coordinate.setPosition(object.optInt("coursePosition"));
                coordinate.setClassName(object.optString("courseName"));
                coordinate.setClassNum(object.optInt("courseNum"));
                coordinate.setClassRoom(object.optString("courseAddress"));
                coordinate.setGrade(object.optString("courseGrade"));
                coordinate.setWeek(object.optString("courseWeek"));
                list.add(coordinate);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 课程表JSON生成
     *
     * @return
     */
    public static String dataSave(ArrayList<Coordinate> coordinates) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n\"data\":[");

        for (int i = 0; i < coordinates.size(); i++) {
            if (i < (coordinates.size() - 1)) {
                sb.append("{\"coursePosition\"" + ":" + coordinates.get(i).getPosition() + ",");
                sb.append("\"courseNum\"" + ":" + coordinates.get(i).getClassNum() + ",");
                sb.append("\"courseName\"" + ":" + coordinates.get(i).getClassName() + ",");
                sb.append("\"courseAddress\"" + ":" + coordinates.get(i).getClassRoom() + ",");
                sb.append("\"courseGrade\"" + ":" + coordinates.get(i).getGrade() + ",");
                sb.append("\"courseWeek\"" + ":" + coordinates.get(i).getWeek() + "}" + "," + "\n");
            } else if (i == (coordinates.size() - 1)) {
                sb.append("{\"coursePosition\"" + ":" + coordinates.get(i).getPosition() + ",");
                sb.append("\"courseNum\"" + ":" + coordinates.get(i).getClassNum() + ",");
                sb.append("\"courseName\"" + ":" + coordinates.get(i).getClassName() + ",");
                sb.append("\"courseAddress\"" + ":" + coordinates.get(i).getClassRoom() + ",");
                sb.append("\"courseGrade\"" + ":" + coordinates.get(i).getGrade() + ",");
                sb.append("\"courseWeek\"" + ":" + coordinates.get(i).getWeek() + "}" + "\n]}");
            }
        }

        return sb.toString();

    }

    /**
     * 课程表JSON的语句生成
     *
     * @param coordinates
     * @return
     */
    public static String dataCreate(Coordinate coordinates) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("coursePosition", coordinates.getPosition());
            jsonObject.put("courseNum", coordinates.getClassNum());
            jsonObject.put("courseName", coordinates.getClassName());
            jsonObject.put("courseAddress", coordinates.getClassRoom());
            jsonObject.put("classId", Cid.sharedCid().getCid());
            jsonObject.put("courseWeek", coordinates.getWeek());
            jsonObject.put("accessToken", User.sharedUser().getAccessToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String s = jsonObject.toString();
        return s;

    }


    /**
     * 编辑课程表JSON的语句生成
     *
     * @param coordinates
     * @return
     */
    public static String dataEditor(Coordinate coordinates) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("coursePosition", coordinates.getPosition());
            jsonObject.put("courseNum", coordinates.getClassNum());
            jsonObject.put("courseName", coordinates.getClassName());
            jsonObject.put("courseAddress", coordinates.getClassRoom());
            jsonObject.put("classId", Cid.sharedCid().getCid());
            jsonObject.put("courseWeek", coordinates.getWeek());
            jsonObject.put("courseId", coordinates.getCourseId());
            jsonObject.put("accessToken", User.sharedUser().getAccessToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String s = jsonObject.toString();
        return s;

    }


    /**
     * 注册JSON的生成
     */
    public static String registerCreate(Teachers teachers) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("classTeacher", teachers.getClassTeacher());
            jsonObject.put("userPwd", teachers.getUserPwd());
            jsonObject.put("schoolName", teachers.getSchoolName());
            jsonObject.put("phoneNumber", teachers.getPhoneNumber());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String s = jsonObject.toString();
        return s;
    }

    /**
     * 登录生成的JSON
     */
    public static String loginCreate(Teachers teachers) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phoneNumber", teachers.getPhoneNumber());
            jsonObject.put("userPwd", teachers.getUserPwd());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String s = jsonObject.toString();
        return s;

    }


    public static void attendanceJSON(String data, ArrayList<Attendance> list) {
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String attendanceDate = jsonObject.getString("attendance_date");
                String course = jsonObject.getString("course");
                String grade = jsonObject.getString("grade");
                String studentName = jsonObject.getString("student_name");
                String studentId = jsonObject.getString("student_id");
                Attendance attendance = new Attendance(attendanceDate, course, grade, studentName, studentId);
                list.add(attendance);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    /**
     * 删除课程表
     *
     * @return
     */
    public static String deleteCourse(int courseId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("accessToken", User.sharedUser().getAccessToken());
            jsonObject.put("courseId", courseId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String s = jsonObject.toString();
        return s;

    }

    /**
     * 请求班级
     */
    public static String gradePost() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("accessToken", User.sharedUser().getAccessToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String s = jsonObject.toString();
        return s;

    }

    /**
     * 解析班级数据
     */
    public static void gradeParse(String data, List<Coordinate> list) {
        try {
            list.clear();
            JSONObject jsonObject = new JSONObject(data);

            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                Coordinate coordinate = new Coordinate();
                JSONObject object = jsonArray.getJSONObject(i);
                coordinate.setClassName(object.optString("class_name"));
                coordinate.setClassId(object.optInt("class_id"));
                list.add(coordinate);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * {"result":1,"msg":"\u8fd4\u56de\u6210\u529f","data":{"courseId":"43","week":"3","attendId":"15"}}
     * 考勤提交二维码信息
     */
    public static String codeParse(int courseId, int week) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("accessToken", User.sharedUser().getAccessToken());
            jsonObject.put("courseId", courseId);
            jsonObject.put("week", week);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String s = jsonObject.toString();
        return s;
    }

    /**
     * {"result":1,"msg":"\u83b7\u53d6\u6210\u529f","data":{"attendNum":0}}
     * 提交轮询当前考勤人数
     */
    public static String pollSpan() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("accessToken", User.sharedUser().getAccessToken());
            jsonObject.put("attenceId", User.sharedUser().getAttendId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String s = jsonObject.toString();
        return s;
    }



    /**
     * 提交查看考勤情况
     */
    public static String attendSpan(int courseId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("accessToken", User.sharedUser().getAccessToken());
            jsonObject.put("courseId", courseId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String s = jsonObject.toString();
        return s;
    }

    /**
     * {"result":1,"msg":"\u83b7\u53d6\u6210\u529f","data":[{"attence_id":11,"course_id":48,"now_week":1,"attence_num":1}]}
     * {"result":1,"msg":"\u83b7\u53d6\u6210\u529f","data":[{"attend_id":19,"course_id":51,"now_week":1,"attend_num":1},
     * {"attend_id":21,"course_id":51,"now_week":2,"attend_num":1}]}
     * 解析考勤情况
     */
    public static void attendParse(String data, List<WeeksNum> list) {
        try {
            list.clear();
            JSONObject jsonObject = new JSONObject(data);

            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                WeeksNum weeksNum = new WeeksNum();
                JSONObject object = jsonArray.getJSONObject(i);
                weeksNum.setWeeks(object.getInt("now_week"));
                weeksNum.setAttendance_num(object.getInt("attend_num"));
                weeksNum.setAttenceId(object.getInt("attend_id"));
                list.add(weeksNum);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 提交查看签到学生
     */
    public static String signSpan(int courseId , int attenceId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("accessToken", User.sharedUser().getAccessToken());
            jsonObject.put("attenceId", attenceId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String s = jsonObject.toString();
        return s;
    }

    /**
     * {"result":1,"msg":"\u83b7\u53d6\u6210\u529f","data":[{"studentAttenceTime":1493557077,"isLate":1,"stuName":"\u5468\u6b66\u6770","stuNumber":"1300002081","className":"\u8ba1\u79d1137"},
     * {"studentAttenceTime":1493558107,"isLate":0,"stuName":"\u674e\u534e","stuNumber":"1300002001","className":"\u8ba1\u79d1137"}]}
     * 解析签到学生
     */
    public static void signParse(String data, List<Students> list) {
        try {
            list.clear();
            JSONObject jsonObject = new JSONObject(data);

            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                Students students = new Students();
                JSONObject object = jsonArray.getJSONObject(i);
                students.setStudent_name(object.getString("stuName"));
                students.setStudent_id(object.getString("stuNumber"));
                students.setGrade(object.getString("className"));
                students.setIsAttend(String.valueOf(object.getInt("isLate")));
                list.add(students);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 提交查看班级
     */
    public static String gradeSpan() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("accessToken", User.sharedUser().getAccessToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String s = jsonObject.toString();
        return s;
    }

    /**
     * {"result":1,"msg":"\u83b7\u53d6\u6210\u529f","data":[{"classId":5,"className":"\u7f51\u7edc\u5de5\u7a0b133"},
     * {"classId":6,"className":"\u8f6f\u4ef6\u5de5\u7a0b135"},
     * {"classId":1,"className":"\u8ba1\u79d1137"},{"classId":2,"className":"\u8ba1\u79d1138"}]}
     * 解析全部班级
     */
    public static void gradeAllParse(String data, List<Grade> list) {
        try {
            list.clear();
            JSONObject jsonObject = new JSONObject(data);

            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                Grade grade = new Grade();
                JSONObject object = jsonArray.getJSONObject(i);
                grade.setClassId(object.getInt("classId"));
                grade.setGrade(object.getString("className"));
                list.add(grade);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 提交查看学生
     */
    public static String studentSpan(int classId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("accessToken", User.sharedUser().getAccessToken());
            jsonObject.put("classId" ,classId) ;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String s = jsonObject.toString();
        return s;
    }

    /**
     * {"result":1,"msg":"\u83b7\u53d6\u6210\u529f","data":[{"className":"\u7f51\u7edc\u5de5\u7a0b133","stuName":"wo","stuNumber":"99909","studentId":6},
     * {"className":"\u7f51\u7edc\u5de5\u7a0b133","stuName":"\u56f0","stuNumber":"123123","studentId":8}]}
     * 解析学生
     */
    public static void studentParse(String data, List<Students> list) {
        try {
            list.clear();
            JSONObject jsonObject = new JSONObject(data);

            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                Students students = new Students();
                JSONObject object = jsonArray.getJSONObject(i);
                students.setStudent_name(object.getString("stuName"));
                students.setStudent_id(object.getString("stuNumber"));
                students.setGrade(object.getString("className"));
                list.add(students);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 班级学生的JSON解析
     *
     * @param data
     * @param list
     */
    public static void parseGradeStudent(String data, ArrayList<Grade> list) {
        try {
            list.clear();
            JSONArray gradeArray = new JSONArray(data);
            for (int p = 0; p < gradeArray.length(); p++) {
                JSONObject gradeObject = gradeArray.getJSONObject(p);
                Grade grade = new Grade();
                grade.setGrade(gradeObject.getString("grade"));
                ArrayList<Students> studentses = new ArrayList<>();
                JSONArray jsonArray = gradeObject.getJSONArray("students");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    Students students = new Students();
                    students.setStudent_name(object.getString("student_name"));
                    students.setStudent_id(object.getString("student_id"));
                    students.setGrade(object.getString("grade"));
                    studentses.add(students);
                    grade.setStudentsList(studentses);
                    grade.setStudents(students);
                }
                list.add(grade);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
