package com.seewo.palette.util;

import com.seewo.palette.bean.Point;

/**
 * Created by user on 2016/8/10.
 */
public class InterSectUtil {

    private  Point p1;
    private  Point p2;
    private  Point p3;
    private  Point p4;

    public InterSectUtil(Point p1, Point p2, Point p3, Point p4) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
    }

    public float det(Point pi, Point pj) { // 叉积
        return pi.getX() * pj.getY() - pj.getX() * pi.getY();
    }

    public Point PiPj(Point pi, Point pj) { // 构造向量
        Point p = new Point();
        p.setX(pj.getX() - pi.getX());
        p.setY(pj.getY() - pi.getY());
        return p;
    }

    public float Direction(Point pi, Point pj, Point pk) {
        // 大於零表示順時針，即右轉，小於零表示逆時針，即左轉，等於零，表示共綫。
        return det(PiPj(pk, pi), PiPj(pj, pi));
    }

    public boolean On_Segment(Point pi, Point pj, Point pk) {
        float max_x = (pi.getX() - pj.getX()) > 0 ? pi.getX() : pj.getX();
        float min_x = (pi.getX() - pj.getX()) < 0 ? pi.getX() : pj.getX();
        float max_y = (pi.getY() - pj.getY()) > 0 ? pi.getY() : pj.getY();
        float min_y = (pi.getY() - pj.getY()) < 0 ? pi.getY() : pj.getY();
        if ((min_x <= pk.getX()) && (pk.getX() <= max_x)
                && (min_y <= pk.getY()) && (pk.getY() <= max_y))
            return true;
        else
            return false;
    }

    public boolean Segment_Intersect() {

        double d1 = Direction(this.p3, this.p4, this.p1);
        double d2 = Direction(p3, p4, p2);
        double d3 = Direction(p1, p2, p3);
        double d4 = Direction(p1, p2, p4);
        if (((d1 > 0 && d2 < 0) || (d1 < 0 && d2 > 0))
                && ((d3 > 0 && d4 < 0) || (d3 < 0 && d4 > 0)))
            return true;
        else if (d1 == 0 && On_Segment(p3, p4, p1))
            return true;
        else if (d2 == 0 && On_Segment(p3, p4, p2))
            return true;
        else if (d3 == 0 && On_Segment(p1, p2, p3))
            return true;
        else if (d4 == 0 && On_Segment(p1, p2, p4))
            return true;
        else
            return false;
    }


}
