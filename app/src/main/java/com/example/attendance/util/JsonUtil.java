package com.example.attendance.util;

import com.example.attendance.bean.Attendance;
import com.example.attendance.bean.Coordinate;
import com.example.attendance.bean.Students;
import com.example.attendance.bean.Teachers;
import com.example.attendance.bean.WeeksNum;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 陈振聪 on 2017/3/17.
 */
public class JsonUtil {
    /**
     * 解析JSON课程表
     * @param data
     * @param list
     */
    public static void dataParse(String data , ArrayList<Coordinate> list) {
        try {
            list.clear();
            JSONArray jsonArray = new JSONArray(data) ;
            for (int i = 0 ; i < jsonArray.length() ; i++){
                JSONObject object = jsonArray.optJSONObject(i) ;
                Coordinate coordinate = new Coordinate();
                coordinate.setPosition( object.optInt("position")) ;
                coordinate.setClassName(object.optString("className"));
                coordinate.setClassNum(object.optInt("classNum"));
                coordinate.setClassRoom(object.optString("classRoom")) ;
                coordinate.setGrade(object.optString("grade")) ;
                coordinate.setWeek(object.optString("week")) ;
                list.add(coordinate) ;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 考勤信息的JSON解析
     * @param data
     * @param list
     */
    public static void attendancePare(String data , ArrayList<Coordinate>list){
        try {
            list.clear();
            JSONArray attendanceArray = new JSONArray(data) ;
            for (int p = 0 ; p < attendanceArray.length() ; p++){
                JSONObject attendanceObject = attendanceArray.getJSONObject(p) ;
                Coordinate coordinate = new Coordinate() ;
                coordinate.setPosition( attendanceObject.optInt("position")) ;
                coordinate.setClassName(attendanceObject.optString("className"));
                coordinate.setClassNum(attendanceObject.optInt("classNum"));
                coordinate.setClassRoom(attendanceObject.optString("classRoom")) ;
                coordinate.setGrade(attendanceObject.optString("grade")) ;
                coordinate.setWeek(attendanceObject.optString("week")) ;
                ArrayList<WeeksNum>weeksNa = new ArrayList<>() ;
                JSONArray jsonArray = attendanceObject.getJSONArray("attendance") ;
                for (int i = 0 ; i < jsonArray.length() ; i++){
                    JSONObject object = jsonArray.getJSONObject(i) ;
                    WeeksNum weeksNum = new WeeksNum() ;
                    weeksNum.setWeeks(object.getString("weeks"));
                    weeksNum.setAttendance_num(object.getString("attendance_num"));
                    ArrayList<Students>studentses = new ArrayList<>() ;
                    JSONArray array = object.getJSONArray("students") ;
                    for (int s = 0 ; s < array.length() ; s++){
                        JSONObject studentObject = array.getJSONObject(s) ;
                        Students students = new Students() ;
                        students.setStudent_name(studentObject.getString("student_name"));
                        students.setStudent_id(studentObject.getString("student_id"));
                        students.setGrade(studentObject.getString("grade"));
                        studentses.add(students) ;
                        weeksNum.setStudentsArrayList(studentses);
                        weeksNum.setStudents(students);
                    }
                    weeksNa.add(weeksNum) ;
                    coordinate.setWeeksNumArrayList(weeksNa);
                    coordinate.setWeeksNum(weeksNum);
                }
                list.add(coordinate) ;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 课程表JSON生成
     * @return
     */
    public static String dataSave(ArrayList<Coordinate> coordinates) {
        StringBuilder sb = new StringBuilder() ;
        sb.append("[\n") ;

        for (int i = 0 ; i < coordinates.size() ; i++){
            if (i < (coordinates.size() - 1)){
                sb.append("{\"position\""+":"+ coordinates.get(i).getPosition() + ",") ;
                sb.append("\"classNum\""+":"+ coordinates.get(i).getClassNum() + ",") ;
                sb.append("\"className\""+":"+ coordinates.get(i).getClassName() + ",") ;
                sb.append("\"classRoom\""+":"+ coordinates.get(i).getClassRoom() + ",") ;
                sb.append("\"grade\""+":"+ coordinates.get(i).getGrade() + ",") ;
                sb.append("\"week\""+":"+ coordinates.get(i).getWeek() + "}" + ","+"\n") ;
            }else if (i == (coordinates.size() - 1)){
                sb.append("{\"position\""+":"+ coordinates.get(i).getPosition() + ",") ;
                sb.append("\"classNum\""+":"+ coordinates.get(i).getClassNum() + ",") ;
                sb.append("\"className\""+":"+ coordinates.get(i).getClassName() + ",") ;
                sb.append("\"classRoom\""+":"+ coordinates.get(i).getClassRoom() + ",") ;
                sb.append("\"grade\""+":"+ coordinates.get(i).getGrade() + ",") ;
                sb.append("\"week\""+":"+ coordinates.get(i).getWeek() + "}" +"\n]") ;
            }
        }

        return sb.toString() ;

    }
    /**
     * 课程表JSON的语句生成
     * @param coordinates
     * @return
     */
    public static String dataCreate(Coordinate coordinates) {
        JSONObject jsonObject = new JSONObject() ;
        try {
            jsonObject.put("position" , coordinates.getPosition()) ;
            jsonObject.put("classNum" , coordinates.getClassNum()) ;
            jsonObject.put("className" , coordinates.getClassName()) ;
            jsonObject.put("classRoom" , coordinates.getClassRoom()) ;
            jsonObject.put("grade" , coordinates.getGrade()) ;
            jsonObject.put("week" , coordinates.getWeek()) ;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String s = jsonObject.toString() ;
        return s ;


       /* JSONObject jsonObject = new JSONObject() ;
        try {
            JSONArray jsonArray = new JSONArray() ;
            for (int i = 0 ; i < coordinates.size() ; i++)
            {
                JSONObject object = new JSONObject() ;
                object.put("position" , coordinates.get(i).getPosition()) ;
                object.put("classNum" , coordinates.get(i).getClassNum()) ;
                object.put("className" , coordinates.get(i).getClassName()) ;
                object.put("classRoom" , coordinates.get(i).getClassRoom()) ;
                object.put("grade" , coordinates.get(i).getGrade()) ;
                object.put("week" , coordinates.get(i).getWeek()) ;
                jsonArray.put(i , object) ;
            }
            jsonObject.put("coordinate" , jsonArray) ;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;*/
    }

    /**
     * 注册JSON的生成
     */
    public static String registerCreate(Teachers teachers){
        JSONObject jsonObject = new JSONObject() ;
        try {
            jsonObject.put("classTeacher" , teachers.getClassTeacher()) ;
            jsonObject.put("userPwd" , teachers.getUserPwd()) ;
            jsonObject.put("schoolName" , teachers.getSchoolName()) ;
            jsonObject.put("phoneNumber" , teachers.getPhoneNumber()) ;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String s = jsonObject.toString() ;
        return s ;
    }

    /**
     * 登录生成的JSON
     */
    public static String loginCreate(Teachers teachers){
        JSONObject jsonObject = new JSONObject() ;
        try {
            jsonObject.put("phoneNumber" , teachers.getPhoneNumber()) ;
            jsonObject.put("userPwd" , teachers.getUserPwd()) ;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String s = jsonObject.toString() ;
        return s ;

    }


    public static void attendanceJSON(String data , ArrayList<Attendance>list){
        try {
            JSONArray jsonArray = new JSONArray(data) ;
            for (int i = 0 ; i < jsonArray.length() ; i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i) ;
                String attendanceDate = jsonObject.getString("attendance_date") ;
                String course = jsonObject.getString("course") ;
                String grade = jsonObject.getString("grade") ;
                String studentName = jsonObject.getString("student_name") ;
                String studentId = jsonObject.getString("student_id") ;
                Attendance attendance = new Attendance(attendanceDate , course , grade , studentName , studentId) ;
                list.add(attendance) ;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
