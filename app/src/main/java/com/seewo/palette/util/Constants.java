package com.seewo.palette.util;

import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by user on 2016/7/28.
 * 常量类
 */
public class Constants {

    public static final int maxBrushSize=20; //画笔最大
    public static final int minBrushSize=5; //画笔最小
    public static final String[] colors = new String[]{
            "#242424",
            "#FF0000",
            "#9ACD32",
            "#473C8B",
            "#EEEE00",
            "#EE8262",
            "#EE3A8C",
            "#836FFF",
            "#CDCDB4",
            "#FF7F24"

    };

    final HashMap<Integer,String> colormap=new HashMap<>();

    public static final int INK=1;//曲线笔迹
    public static final int LINE=2;//直线
    public static final int RECT=3;//矩阵
    public static final int CIRCLE=4;//圆

    public static final int MSG_EXIT = 0X111;//退出信号

    public static final int PNG=5;
    public static final int SVG=6;

    public static final int MSG_REDRAW=0X112;//重绘信号
}
