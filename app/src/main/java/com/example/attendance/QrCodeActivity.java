package com.example.attendance;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.UnsupportedEncodingException;

public class QrCodeActivity extends AppCompatActivity {
    private ImageView imageView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        imageView = (ImageView) findViewById(R.id.codeImage);
        String s = "签到" ;
        try {
            Bitmap temp = crateBitmap(new String(s.getBytes() , "ISO-8859-1"));
            imageView.setImageBitmap(temp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap crateBitmap(String s) throws WriterException {
            BitMatrix matrix = new MultiFormatWriter().encode(s , BarcodeFormat.QR_CODE , 300 , 300) ;
            int width = matrix.getWidth() ;
            int height = matrix.getHeight() ;

            int[] pixels = new int[width * height] ;
            for (int y = 0 ; y < height ; y++){
                for (int x = 0 ; x < width ; x++){
                    if (matrix.get(x , y)){
                        pixels[y * width + x] = 0xff000000 ;
                    }else {
                        pixels[y * width + x] = 0xffffffff ;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width , height , Bitmap.Config.ARGB_8888) ;
            bitmap.setPixels(pixels , 0 , width , 0 , 0 , width , height);

        return bitmap;
    }
}
