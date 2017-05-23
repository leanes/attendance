package com.example.attendance.test;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.attendance.bean.Coordinate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.CellFormat;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * Created by 陈振聪 on 2017/3/10.
 */
public class ExcelSchedule {

    private static final String excle_name = "/coursetable.xlsx" ;
    private static final String file_name = "/servercourse.txt" ;
    private static Context mContext;
    private static WritableSheet sheet;

    public static void writeExcelSchedule(Context context, List<Coordinate> coordinates) {
        mContext = context ;
        String[] title = {"编号", "课 程 名 称",  "  课  室  ", " 上 课 班 级 " , "星期几" , "第几节" , "  节  数  " , " 起 始 周 "};
        File sdDir = null ;
        StringBuilder stringBuilder = new StringBuilder() ;
        sdDir = Environment.getExternalStorageDirectory(); //获取根目录
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ;     //判断SD卡是否存在
        if (sdCardExist){
            File file = new File(sdDir + excle_name);
            //创建Excel工作表
            WritableWorkbook wwb = null;
            try {
                OutputStream os = new FileOutputStream(file);
                wwb = Workbook.createWorkbook(os);
                //添加第一个工作表并设置第一个Sheet的名字
                sheet = wwb.createSheet("课程表", 0);
                Label label;
                for (int i = 0; i < title.length; i++) {
                    //Label(x,y,z)代表单元格的第x+1列，第y+1行，内容z
                    //在Label对象的子对象中指明单元格的位置和内容
                    label = new Label(i, 0, title[i], getHeader());
                    //将定义好的单元格添加到工作表中
                    sheet.addCell(label);
                }
                File filetxt = new File(sdDir + file_name);
                if (!filetxt.exists()) {
                    return;
                }
                InputStream in = new FileInputStream(filetxt);
                if (in != null) {
                    InputStreamReader isr = new InputStreamReader(in);
                    BufferedReader br = new BufferedReader(isr);
                    String line;
                    while ((line = br.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                }
                String jsonStr = stringBuilder.toString() ;
                setData(jsonStr) ;
                wwb.write();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //
                if (wwb != null) {
                    try {
                        wwb.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (WriteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else {
            Toast.makeText(context, "SD卡不可用", Toast.LENGTH_SHORT).show();
        }

    }

    private static void setData(String jsonStr) {
        try {
            JSONObject object = new JSONObject(jsonStr);
            JSONArray jsonArray = object.getJSONArray("data");
            int len = jsonArray.length() ;;
            for (int i = 0 ; i < len ; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String className = jsonObject.optString("courseName");
                String classRoom = jsonObject.optString("courseAddress");
                int position = jsonObject.optInt("coursePosition") ;
                int week = position %7 + 1 ;
                int section = position / 7 + 1 ;
                String grade = jsonObject.optString("courseGrade");
                int classNum = jsonObject.optInt("courseNum");
                String weeks = jsonObject.optString("courseWeek");
                int id = i + 1;
                Label idLabel = new Label(0 , i + 1 , String.valueOf(id)) ;
                Label classNameLabel = new Label(1 , i + 1 , className) ;
                Label classRoomLabel = new Label(2 , i + 1 , classRoom) ;
                Label gradeLabel = new Label(3 , i + 1 , grade) ;
                Label weekLabel = new Label(4 , i + 1 , String.valueOf(week)) ;
                Label sectionLabel = new Label(5 , i + 1 , String.valueOf(section)) ;
                Label classNumLabel = new Label(6 , i + 1 , String.valueOf(classNum)) ;
                Label weeksLabel = new Label(7 , i + 1 , weeks) ;
                sheet.addCell(idLabel);
                sheet.addCell(classNameLabel);
                sheet.addCell(weekLabel);
                sheet.addCell(sectionLabel);
                sheet.addCell(classRoomLabel);
                sheet.addCell(gradeLabel);
                sheet.addCell(classNumLabel);
                sheet.addCell(weeksLabel);
            }
            Toast.makeText(mContext , "导出课程表成功在根目录下" , Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }

    }

    private static CellFormat getHeader() {
        WritableFont font = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD);
        try {
            font.setColour(Colour.BLUE);
        } catch (WriteException e) {
            e.printStackTrace();
        }
        WritableCellFormat format = new WritableCellFormat(font);
        try {
            format.setAlignment(Alignment.CENTRE); //左右居中
            format.setVerticalAlignment(VerticalAlignment.CENTRE); //上下居中
            format.setBackground(Colour.YELLOW);
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return format;
    }

}