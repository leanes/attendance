package com.example.attendance.test;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 运用在本地
 * Created by 陈振聪 on 2017/4/14.
 */


public class SaveLocation {
    /**
     * 把数据存储保存在本地
     * @param data
     * @param sdDir
     * @param path_name
     */
    public static void saveLocal( String data ,File sdDir , String path_name ){
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ;     //判断SD卡是否存在
        if (sdCardExist){
            FileOutputStream fout = null ;
            try {
                fout = new FileOutputStream(sdDir + path_name) ;
                byte[] bytes = data.getBytes() ;
                fout.write(bytes);
                fout.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 读取本地数据
     */
    public static void readLocal(StringBuilder stringBuilder , File sdDir , String path_name ){
        File filetxt = new File(sdDir + path_name);
        if (!filetxt.exists()) {
            return;
        }
        InputStream in = null;
        try {
            in = new FileInputStream(filetxt);
            if (in != null) {
                InputStreamReader isr = new InputStreamReader(in);
                BufferedReader br = new BufferedReader(isr);
                String line;
                while ((line = br.readLine()) != null) {
                    stringBuilder.append(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
