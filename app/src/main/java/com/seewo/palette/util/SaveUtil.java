package com.seewo.palette.util;

import android.os.Handler;
import android.os.Message;

import com.seewo.palette.ui.MyCanvas;

/**
 * Created by user on 2016/8/9.
 * 保存xml线程
 */
public class SaveUtil extends Thread{


    private Handler mHandler;
    private String  mName;
    private MyCanvas mMycanvas;

    public SaveUtil(Handler mHandler, String mName, MyCanvas mMycanvas) {
        this.mHandler = mHandler;
        this.mName = mName;
        this.mMycanvas = mMycanvas;
    }

    @Override
    public void run() {
        super.run();
        //创建palette下的文件夹
        //向文件夹存储xml
        for(int i=0;i<mMycanvas.getmGallery().getPaintingList().size();i++){
            //创建xml文件名
            String xmlfilename=StoreOperation.getXmlFileName(this.mName,i+"");
            //创建xml文件
            XmlOperation.CreatXml(
                    mMycanvas.getmGallery().getPaintingList().get(i),
                    xmlfilename);
            //向主线程发送信息
            Message msg=new Message();
            msg.what=Constants.MSG_EXIT;
            mHandler.sendMessage(msg);
        }
    }

}
