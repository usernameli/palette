package com.seewo.palette.bean;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/7/29.
 * 抽象笔迹类
 */
public abstract class Shape {

    String color; //颜色
    float width; //宽度
    List<Point>  pointList; //笔迹上点集合
    Paint paint;

    /**
     * *************************
     * Construct Methond
     * *************************
     */
    public Shape() {

        pointList =new ArrayList<>();
        paint =new Paint();
    }

    /**
     * **************************
     * getter and setter methond
     *
     * **************************
     */
    public String getColor() {
        return color;
    }

    public float getWidth() {
        return width;
    }

    public List<Point> getPointList() {
        return pointList;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setPointList(List<Point> pointList) {
        this.pointList = pointList;
    }
    public void setPaint(Paint paint) {
        this.paint.setColor(paint.getColor());
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(paint.getStrokeWidth());
    }

    /**
     * 添加点函数
     * @param x
     * @param y
     */
    public void addPoint(float x,float y){
        pointList.add(new Point(x,y));
    }
    /**
     * 绘制函数
     * @param mCanvas
     */
    public abstract void  draw(Canvas mCanvas);
    /**
     * 按下操作对应的相关处理
     * @param x
     * @param y
     */
    public abstract void DownAction(float x,float y);
    /**
     * 移动过程中相关操作
     * @param x
     * @param y
     */
    public abstract void MoveAction(float mx,float my,float x,float y);
    /**
     * 抬起操作对应的相关处理
     * @param x
     * @param y
     */
    public abstract void UpAction(float x,float y);
    /**
     * 返回自己对应种类
     * @return
     */
    public abstract int GetKind();
    /**
     * 设置自己特有属性
     */
    public abstract void setOwnProperty();
    /**
     * 找到笔迹的边缘矩形
     * @return
     */
    public  RectF findShapeEdge(){
        float minx=pointList.get(0).getX();
        float miny=pointList.get(0).getY();
        float maxx=pointList.get(0).getX();
        float maxy=pointList.get(0).getY();
        for(int i=1;i<pointList.size();i++){
            if(maxx<pointList.get(i).getX())
                maxx=pointList.get(i).getX();
            if(minx>pointList.get(i).getX())
                minx=pointList.get(i).getX();
            if(maxy<pointList.get(i).getY())
                maxy=pointList.get(i).getY();
            if(miny>pointList.get(i).getY())
                miny=pointList.get(i).getY();
        }
        System.out.println(minx+"----"+maxy+"----"+maxx+"------"+miny);
        RectF rect =new RectF(minx,maxy,maxx,miny);

        return rect;
    }
    /**
     * 判断是否相交并返回对应的list的位置
     * @param lastx
     * @param lasty
     * @param x
     * @param y
     * @return
     */
    public abstract boolean IsInterSect(float lastx,float lasty,float x,float y);

    /**
     * 判断是否进入边缘矩形
     * @param x
     * @param y
     * @return
     */
    public boolean IsEnterShapeEdge(float x,float y) {
        float minx = pointList.get(0).getX();
        float miny = pointList.get(0).getY();
        float maxx = pointList.get(0).getX();
        float maxy = pointList.get(0).getY();
        for (int i = 1; i < pointList.size(); i++) {
            if (maxx < pointList.get(i).getX())
                maxx = pointList.get(i).getX();
            if (minx > pointList.get(i).getX())
                minx = pointList.get(i).getX();
            if (maxy < pointList.get(i).getY())
                maxy = pointList.get(i).getY();
            if (miny > pointList.get(i).getY())
                miny = pointList.get(i).getY();
        }
       // System.out.println(minx + "----" + maxy + "----" + maxx + "------" + miny);
        if((x>=minx && x<=maxx)&&(y>=miny && y<=maxy)){
            return true;
        }else{
            return false;
        }
    }

}
