package com.example.lihao.blogeronline.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by lihao on 17-10-22.
 */

public class BitmapUtils {

    public static File saveBitmapFile(Bitmap bitmap, String filepath){

        File file=new File(filepath);//将要保存图片的路径

        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    public static Bitmap fileToBitmap(String filePath){

        return BitmapFactory.decodeFile(filePath);

    }

}
