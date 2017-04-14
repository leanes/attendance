package com.example.attendance.test;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.example.attendance.bean.Students;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
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

/**
 * Created by 陈振聪 on 2017/4/13.
 */
public class ExcelAttendance {
//    private static  String excel_name = "/attendance.xlsx";
    private static Context mContext;
    private static WritableSheet sheet;

    public static void writeExcelAttendance(Context context, ArrayList<Students> studentses , String excel_name ) {
        mContext = context;
        String[] title = {"班级", "姓名", "学号"};
        File sdDir = null;
        StringBuilder stringBuilder = new StringBuilder();
        sdDir = Environment.getExternalStorageDirectory(); //获取根目录
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);     //判断SD卡是否存在
        if (sdCardExist) {
            File file = new File(sdDir + excel_name);
            //创建Excel工作表
            WritableWorkbook wwbook = null;
            try {
                OutputStream os = new FileOutputStream(file);
                wwbook = Workbook.createWorkbook(os);
                //添加第一个工作表并设置第一个Sheet的名字
                sheet = wwbook.createSheet("考勤信息", 0);
                Label label;
                for (int i = 0; i < title.length; i++) {
                    //Label(x,y,z)代表单元格的第x+1列，第y+1行，内容z
                    //在Label对象的子对象中指明单元格的位置和内容
                    label = new Label(i, 0, title[i], getHeader());
                    //将定义好的单元格添加到工作表中
                    sheet.addCell(label);
                }
                for (int j = 0 ; j < studentses.size() ; j++){
                    String grade = studentses.get(j).getGrade() ;
                    String studentName = studentses.get(j).getStudent_name() ;
                    String studentId = studentses.get(j).getStudent_id() ;
                    Label gradeLabel = new Label(0 , j + 1 , grade) ;
                    Label studentNameLabel = new Label(1 , j + 1 , studentName) ;
                    Label studentIdLabel = new Label(2 , j + 1 , studentId) ;
                    sheet.addCell(gradeLabel);
                    sheet.addCell(studentNameLabel);
                    sheet.addCell(studentIdLabel);
                }
                wwbook.write();
                Toast.makeText(mContext , "成功导出已考勤学生信息" , Toast.LENGTH_SHORT).show();
            } catch (WriteException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (wwbook != null) {
                    try {
                        wwbook.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (WriteException e) {
                        e.printStackTrace();
                    }
                }
            }
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
