package com.seewo.palette.util;

import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;

/**
 * Created by user on 2016/7/29.
 * 存储操作类
 */
public class StoreOperation {


    /**
     * 获取Xml文件存储名字
     *
     * @return
     */
    public static String getXmlFileName(String dirName,String name) {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //自己命名名字
            String filename = getFileStorePath(dirName) + "/" + name + ".xml";
            return filename;
        } else {
            return null;
        }
    }

    /**
     * 获取文件夹
     * @return
     */
    public static String getFileStorePath(String name) {
        String filepath = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //获得完整的保存路径
            File file = new File(Environment.getExternalStorageDirectory() + "/palette/"+name);
            if (!file.exists()) {
                file.mkdirs();
            }
            filepath = file.getAbsolutePath();
        }
        return filepath;
    }

}
