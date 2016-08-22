package com.seewo.palette.save;

import android.graphics.Bitmap;

import com.seewo.palette.ui.MyCanvas;

/**
 * Created by user on 2016/8/9.
 * 抽象另存类
 */
public abstract class SaveOperation {
    String filepath;
    String filename=null;

    public String getFilepath() {
        return filepath;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public abstract  String GetAbusoluteFileName();
    public abstract void SavePainting();
    public abstract void GetContent(MyCanvas myCanvas);
}
