package com.seewo.palette.save;

import android.graphics.Bitmap;
import android.os.Environment;

import com.seewo.palette.ui.MyCanvas;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by user on 2016/8/9.
 * 保存为png类
 */
public class SavePngOperation extends SaveOperation{

    Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public void SavePainting() {
        File file = new File(GetAbusoluteFileName());
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String GetAbusoluteFileName() {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String fileAbusoluteName = filepath + "/" +filename+ ".png";
            return fileAbusoluteName;
        } else {
            return null;
        }
    }


    @Override
    public void GetContent(MyCanvas myCanvas) {
        this.bitmap=Bitmap.createBitmap(myCanvas.getmBitmap());
    }

}
