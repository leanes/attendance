package com.example.attendance.util;

import com.example.attendance.bean.Attendance;
import com.example.attendance.bean.Coordinate;

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
            JSONObject jsonObject = new JSONObject(data) ;
            JSONArray jsonArray = jsonObject.optJSONArray("coordinate") ;
          //  JSONArray jsonArray = new JSONArray(data) ;
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
     * 课程表JSON的生成
     * @param coordinates
     * @return
     */
    public static JSONObject dataCreate(ArrayList<Coordinate> coordinates) {
        JSONObject jsonObject = new JSONObject() ;
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

        return jsonObject;
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
