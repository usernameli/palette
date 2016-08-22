package com.seewo.palette.bean;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.seewo.palette.util.Constants;
import com.seewo.palette.util.InterSectUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/8/4.
 * 矩形类
 */
public class Rectangle extends Shape{

    Point startpoint;
    Point endPoint;

    /**
     * **************************
     * construct methond
     * **************************
     */

    public Rectangle() {

    }


    /**
     * **************************
     * getter and setter methond
     *
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
        if(startpoint!=null && endPoint!=null){
            mCanvas.drawRect(startpoint.getX(),startpoint.getY(),endPoint.getX(),endPoint.getY(),paint);
        }
    }

    @Override
    public void DownAction(float x, float y) {
        //设置初始点和终止点
        setStartpoint(new Point(x,y));
        setEndPoint(new Point(x,y));
    }

    @Override
    public void MoveAction(float mx, float my, float x, float y) {
        //修改终止点
        setEndPoint(new Point(x,y));
    }

    @Override
    public void UpAction(float x, float y) {
        //设置终止点
        setEndPoint(new Point(x,y));
    }

    @Override
    public int GetKind() {
        return Constants.RECT;
    }

    @Override
    public void setOwnProperty() {
        //获取关键点
        setStartpoint(pointList.get(0));
        setEndPoint(pointList.get(1));
    }


    @Override
    public boolean IsInterSect(float lastx, float lasty, float x, float y) {

        //矩形逻辑和曲线也是一样的，一共四个点
        List<Point> JudgePointList=new ArrayList<>();
        JudgePointList.add(pointList.get(0));
        JudgePointList.add(new Point(pointList.get(1).getX(),pointList.get(0).getY()));
        JudgePointList.add(new Point(pointList.get(1).getX(),pointList.get(1).getY()));
        JudgePointList.add(new Point(pointList.get(0).getX(),pointList.get(1).getY()));
        JudgePointList.add(pointList.get(0));
        for(int i=1;i<JudgePointList.size();i++){
            if(new InterSectUtil(new Point(lastx,lasty),new Point(x,y),JudgePointList.get(i-1),JudgePointList.get(i)).Segment_Intersect()){
                return true;
            }

        }
        return false;
    }



}
