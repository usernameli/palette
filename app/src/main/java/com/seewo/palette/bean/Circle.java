package com.seewo.palette.bean;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.seewo.palette.util.Constants;

/**
 * Created by user on 2016/8/4.
 * 圆形类
 */
public class Circle extends Shape {

    Point startpoint;
    Point endPoint;


    /**
     * **************************
     * construct methond
     * **************************
     */

    public Circle() {

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
            float radius = Math.abs(endPoint.getY() - startpoint.getY()) >= Math.abs(endPoint.getX() - startpoint.getX()) ? Math.abs(endPoint.getX() - startpoint.getX()) / 2 : Math.abs(endPoint.getY() - startpoint.getY()) / 2;
            mCanvas.drawCircle(
                    (endPoint.getX() + startpoint.getX()) / 2,
                    (endPoint.getY() + startpoint.getY()) / 2,
                    radius,
                    paint);
        }
    }

    @Override
    public void DownAction(float x, float y) {
        //设置初始点和终止点
        setStartpoint(new Point(x, y));
        setEndPoint(new Point(x, y));
    }

    @Override
    public void MoveAction(float mx, float my, float x, float y) {
        //修改终止点
        setEndPoint(new Point(x, y));
    }

    @Override
    public void UpAction(float x, float y) {
        //设置终止点
        setEndPoint(new Point(x, y));
    }

    @Override
    public int GetKind() {
        return Constants.CIRCLE;
    }

    @Override
    public void setOwnProperty() {
        //获取关键点
        setStartpoint(pointList.get(0));
        setEndPoint(pointList.get(1));
    }

    @Override
    public boolean IsInterSect(float lastx, float lasty, float x, float y) {
        Point center =new Point( (endPoint.getX() + startpoint.getX()) / 2, (endPoint.getY() + startpoint.getY()) / 2);
        float radius = Math.abs(
                endPoint.getY() - startpoint.getY()) >= Math.abs(endPoint.getX() - startpoint.getX())
                ? Math.abs(endPoint.getX() - startpoint.getX()) / 2
                : Math.abs(endPoint.getY() - startpoint.getY()) / 2;
//        System.out.println("circlecenter:"+center.getX()+";"+center.getY());
//        System.out.println("radius:"+radius);
//        double lastd=getDistance(lastx,lasty,center.getX(),center.getY());
//        double contentd=getDistance(x,y,center.getX(),center.getY());
//        System.out.println("distance:"+lastd+";"+contentd);
//        if((getDistance(lastx,lasty,center.getX(),center.getY())>=radius && getDistance(x,y,center.getX(),center.getY())<=radius)
//                ||
//                (getDistance(lastx,lasty,center.getX(),center.getY())<=radius && getDistance(x,y,center.getX(),center.getY())>=radius)){
//            return  true;
//        }
//        return false;
        //TODO 目前采取这种方式
        if(getDistance(x,y,center.getX(),center.getY())<radius){
            return true;
        }else
            return false;
    }

    public static float getDistance(float x1,float y1,float x2,float y2){
        return (float) Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
    }
}
