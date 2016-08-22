package com.seewo.palette.bean;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.seewo.palette.util.Constants;
import com.seewo.palette.util.InterSectUtil;

/**
 * Created by user on 2016/8/3.
 * 直线类
 */
public class Line extends Shape {

    Point startpoint;
    Point endPoint;

    /**
     * **************************
     * construct methond
     * **************************
     */
    public Line() {

    }


    /**
     * **************************
     * getter and setter methond
     * <p/>
     * **************************
     */


    public Point getStartpoint() {
        return startpoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public void setStartpoint(Point startpoint) {
        this.startpoint = startpoint;
    }

    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
    }


    @Override
    public void draw(Canvas mCanvas) {
        if (startpoint != null && endPoint != null) {
            mCanvas.drawLine(
                    startpoint.getX(),
                    startpoint.getY(),
                    endPoint.getX(),
                    endPoint.getY(),
                    paint);
        }
    }

    @Override
    public void DownAction(float x, float y) {
        //设置初始点和终结点
        setStartpoint(new Point(x, y));
        setEndPoint(new Point(x, y));
    }

    @Override
    public void MoveAction(float mx, float my, float x, float y) {
        //修改终结点
        setEndPoint(new Point(x, y));
    }

    @Override
    public void UpAction(float x, float y) {
        //设置终结点
        setEndPoint(new Point(x, y));
    }

    @Override
    public int GetKind() {

        return Constants.LINE;
    }

    @Override
    public void setOwnProperty() {
        //获取关键点
        setStartpoint(pointList.get(0));
        setEndPoint(pointList.get(1));
    }

    @Override
    public boolean IsInterSect(float lastx, float lasty, float x, float y) {
        //直线从逻辑上来讲和曲线是一样的
        for(int i=1;i<pointList.size();i++){
            if(new InterSectUtil(new Point(lastx,lasty),new Point(x,y),pointList.get(i-1),pointList.get(i)).Segment_Intersect()){
                return true;
            }

        }
        return false;
    }


}
