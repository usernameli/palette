package com.seewo.palette.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.seewo.palette.bean.Circle;
import com.seewo.palette.bean.Gallery;
import com.seewo.palette.bean.Ink;
import com.seewo.palette.bean.Line;
import com.seewo.palette.bean.Point;
import com.seewo.palette.bean.Rectangle;
import com.seewo.palette.bean.Shape;
import com.seewo.palette.util.Constants;
import com.seewo.palette.util.XmlOperation;

import org.dom4j.DocumentException;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by user on 2016/7/26.
 * 绘制的画布
 */
public class MyCanvas extends View {


    private float currentWidth = 5; //默认字体大小
    private String currentColor = Constants.colors[0]; //默认字体颜色
    private int currentkind = Constants.INK; //默认绘图类型


    private int CurrentPageNum = 1;//默认当前页数
    private int CurrentPageIndex = 1;//默认当前页序号


    private Paint mPaint;//画笔
    private Bitmap mBitmap;//画布的bitmap
    private Canvas mCanvas;//画布

    int CanvansWidth; //画布的宽
    int CanvansHeight;//画布的高
    float mx, my; //当前画笔位置

    List<Shape> SaveShapeList; //已保存笔迹List
    List<Shape> DeleteShapeList; //删除笔迹List

    Gallery mGallery;//画册类


    private boolean IsFileExist = false;//是否有编辑过标志
    private int PreShapeListSize = 0; //最初list的长度

    private boolean EraserState = false;//是否处于橡皮擦状态
    private boolean MoveShapeState = false;//是否处于笔迹操作状态

    private boolean Selected = false;//已选中状态

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                try {
                    redrawOnBitmap();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private boolean isMoving=false; //移动笔迹的标志

    /**
     * **************************
     * construct methond
     * **************************
     */

    public MyCanvas(Context context) {
        this(context, null);
    }

    public MyCanvas(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCanvas(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        SaveShapeList = new ArrayList<>();
        DeleteShapeList = new ArrayList<>();
        mGallery = new Gallery();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        CanvansWidth = getMeasuredWidth();
        CanvansHeight = getMeasuredHeight();
        initCanvas();
    }

    /**
     * 初始化画布
     */
    private void initCanvas() {
        //初始化画布
        mBitmap = Bitmap.createBitmap(CanvansWidth, CanvansHeight, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);  //所有mCanvas画的东西都被保存在了mBitmap中
        mCanvas.drawColor(Color.WHITE);
        //初始化画笔
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(currentWidth);
        mPaint.setColor(Color.parseColor(currentColor));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                break;
            case MotionEvent.ACTION_UP:

                touchUp(x, y);
                break;
        }
        invalidate();
        return true;
    }

    Shape currentShape;
    Path EraserPath;
    Path MovePath;

    /**
     * 按下操作对应处理
     *
     * @param x
     * @param y
     */
    private void touchStart(float x, float y) {
        //进入橡皮擦模式
        if (EraserState) {
            //准备画虚线需要的相关属性
            PathEffect effects = new DashPathEffect(new float[]{8, 8, 8, 8}, 1);//设置虚线的间隔和点的长度
            mPaint.setPathEffect(effects);
            mPaint.setColor(Color.parseColor(Constants.colors[1]));
            SweepList = new ArrayList<>();
            NeedHandleList = new ArrayList<>();
            //创建橡皮擦的path
            EraserPath = new Path();
            EraserPath.moveTo(x, y);
        }
        //进入选择线条模式
        else if (MoveShapeState) {
            if(Selected){
                //判断点击的地方是否是在NeedRect内部如果
                if(!IsNotInside(x,y)){//不在范围内，相关参数清零从头开始
                    NeedMoveRect=null;
                    MoveList=null;
                    NeedMoveList=null;
                    System.out.println("NeedMoveRect:"+NeedMoveRect);
                    System.out.println("MoveList:"+MoveList);
                    System.out.println("NeedMoveRect:"+NeedMoveRect);
                    System.out.println("Selected:"+Selected);
                    System.out.println("MoveShapeState:"+MoveShapeState);
                }else{
                    //在范围内采取相关措施
                    isMoving=true;
                    //TODO 移动和缩放相关
                    //脏区为NeedMoveRect

                }
            }else {
                //准备画虚线需要的相关属性
                PathEffect effects = new DashPathEffect(new float[]{8, 8, 8, 8}, 1);//设置虚线的间隔和点的长度
                mPaint.setPathEffect(effects);
                mPaint.setColor(Color.parseColor(Constants.colors[1]));

                MoveList = new ArrayList<>();
                NeedMoveList = new ArrayList<>();
                //创建选中笔迹的path
                MovePath = new Path();
                MovePath.moveTo(x, y);
            }
        } else {
            //判断当前类型，根据类型选择构造函数
            switch (currentkind) {
                case Constants.INK:
                    currentShape = new Ink();
                    break;
                case Constants.LINE:
                    currentShape = new Line();
                    break;
                case Constants.RECT:
                    currentShape = new Rectangle();
                    break;
                case Constants.CIRCLE:
                    currentShape = new Circle();
                    break;
            }
            //执行对应操作
            currentShape.DownAction(x, y);
            //设置画笔
            currentShape.setPaint(mPaint);
            //记录起始点
            currentShape.addPoint(x, y);
            //获得颜色和宽度数据
            currentShape.setColor(currentColor);
            currentShape.setWidth(currentWidth);
        }
        mx = x;
        my = y;
    }

    List<Integer> SweepList;
    List<Shape> NeedHandleList;

    List<Integer> MoveList;
    List<Shape> NeedMoveList;
    RectF NeedMoveRect;

    /**
     * 移动操作对应处理
     */
    private void touchMove(float x, float y) {

        if (EraserState) {
            EraserPath.quadTo(mx, my, x, y);
            //遍历笔迹
            for (int i = 0; i < SaveShapeList.size(); i++) {
                //判断进入对应的矩形
                if (SaveShapeList.get(i).IsEnterShapeEdge(x, y)) {
                    System.out.println("----------->enter");
                    //判断是否发生相交
                    if (SaveShapeList.get(i).IsInterSect(mx, my, x, y)) {
                        System.out.println("------------>isInterSect");
                        //记录当前shape的position
                        SweepList.add(i);
                    }
                }
            }
        } else if (MoveShapeState) {

            if(Selected){
//                if(isMoving==true) {
//                    for (int k = 0; k < NeedMoveList.size(); k++) {
//                        //取出
//                        Shape shape = NeedMoveList.get(k);
//                        for (int j = 0; j < shape.getPointList().size(); j++) {
//                            //修改坐标
//                            float movex = shape.getPointList().get(j).getX() + (x - mx);
//                            float movey = shape.getPointList().get(j).getY() + (y - my);
//                            //保存坐标
//                            shape.getPointList().set(j, new Point(movex, movey));
//                        }
//                        //替换SaveList中的对应shape
//                        SaveShapeList.set(MoveList.get(k), shape);
//                    }
//                }
            }else{
                MovePath.quadTo(mx, my, x, y);
                //遍历笔迹
                for (int i = 0; i < SaveShapeList.size(); i++) {
                    //判断进入对应的矩形
                    if (SaveShapeList.get(i).IsEnterShapeEdge(x, y)) {
                        System.out.println("----------->enter");
                        //判断是否发生相交
                        if (SaveShapeList.get(i).IsInterSect(mx, my, x, y)) {
                            System.out.println("------------>isInterSect");
                            //记录当前shape的position
                            MoveList.add(i);
                        }
                    }
                }
            }



        } else {
            //执行相关操作
            currentShape.MoveAction(mx, my, x, y);
        }
        //记录当前坐标点
        mx = x;
        my = y;
    }

    /**
     * 点击区域不在对应范围内
     *
     * @return
     */
    private boolean IsNotInside(float x, float y) {
        if(NeedMoveRect!=null){
            if (x >= NeedMoveRect.left && x <= NeedMoveRect.right && y >= NeedMoveRect.bottom && y <= NeedMoveRect.top)
                return true;
        }
            return false;
    }

    /**
     * 抬起操作对应处理
     */
    private void touchUp(float x, float y) {

        if (EraserState) {
            EraserPath.lineTo(x, y);
            EraserPath = null;
            if (SweepList.size() != 0) {
                //删除选中的shape
                for (int i = 0; i < SweepList.size(); i++) {
                    //根据下标取出对象
                    NeedHandleList.add(SaveShapeList.get(SweepList.get(i)));
                }
                //遍历对象依次删除
                for (int j = 0; j < NeedHandleList.size(); j++) {
                    Shape deleteObject = NeedHandleList.get(j);
                    Iterator<Shape> it = SaveShapeList.iterator();
                    while (it.hasNext()) {
                        Shape i = it.next();
                        if (i == deleteObject) {
                            //删除的笔迹放入DeleteList
                            DeleteShapeList.add(i);
                            it.remove();
                        }
                    }
                }
            }
            //相关参数清空
            SweepList = null;
            NeedHandleList = null;
            System.out.println(SaveShapeList.size());
            System.out.println(DeleteShapeList.size());
            //通知系统重绘
            Message msg = new Message();
            msg.what = 0;
            handler.sendMessageDelayed(msg, 100);
        } else if (MoveShapeState) {
            if(Selected){
                if(NeedMoveList==null)
                    Selected=false;
                    try {
                        redrawOnBitmap();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }else{
                MovePath.lineTo(x, y);
                if (MoveList.size() != 0) {
                    //删除选中的shape
                    for (int i = 0; i < MoveList.size(); i++) {
                        //根据下标取出对象
                        NeedMoveList.add(SaveShapeList.get(MoveList.get(i)));
                    }
                    //遍历找到笔迹最大的Rect区域
                    NeedMoveRect = findBiggestRect(NeedMoveList);

                }
                //相关参数清空
                MovePath = null;
                //设置为已选中状态
                Selected = true;
                //现在NeedMoveList中有保存对应笔迹.NeedMoveRect不为空.MoveList也保存有笔迹对应下标

            }
            //通知系统重绘
            Message msg = new Message();
            msg.what = 0;
            handler.sendMessageDelayed(msg, 100);

        } else {

            //执行相关操作
            currentShape.UpAction(x, y);
            //绘制到Bitmap上去
            currentShape.draw(mCanvas);
            //保存终结点
            currentShape.addPoint(x, y);
            //将笔迹添加到栈中
            SaveShapeList.add(currentShape);
            //对象置空
            currentShape = null;
        }

//        System.out.println("MoveSate:"+MoveShapeState);
//        System.out.println("Selected:"+Selected);
//        System.out.println("EraserState:"+EraserState);

        System.out.println("NeedMoveRect:"+NeedMoveRect);
        System.out.println("MoveList:"+MoveList);
        System.out.println("NeedMoveRect:"+NeedMoveRect);
        System.out.println("Selected:"+Selected);
        System.out.println("MoveShapeState:"+MoveShapeState);
    }

    /**
     * 找到要移动的区域
     *
     * @param needMoveList
     * @return
     */
    private RectF findBiggestRect(List<Shape> needMoveList) {
        float minx = needMoveList.get(0).getPointList().get(0).getX();
        float miny = needMoveList.get(0).getPointList().get(0).getY();
        float maxx = needMoveList.get(0).getPointList().get(0).getX();
        float maxy = needMoveList.get(0).getPointList().get(0).getY();
        for (int k = 0; k < needMoveList.size(); k++) {
            List<Point> pointList = needMoveList.get(k).getPointList();
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
        }
        System.out.println(minx + "----" + maxy + "----" + maxx + "------" + miny);
        RectF rect = new RectF(minx, maxy, maxx, miny);
        return rect;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, 0, 0, null);
        if (currentShape != null) {
            currentShape.draw(canvas);
        }
        if (EraserState) {
            if (EraserPath != null) {
                canvas.drawPath(EraserPath, mPaint);
            }
        }
        if (MoveShapeState) {
            if (MovePath != null) {
                canvas.drawPath(MovePath, mPaint);
            }
        }

    }


    /**
     * 撤销操作
     */
    public void Undo() {
        if (SaveShapeList != null && SaveShapeList.size() >= 1) {
            DeleteShapeList.add(SaveShapeList.get(SaveShapeList.size() - 1));
            SaveShapeList.remove(SaveShapeList.size() - 1);
            try {
                redrawOnBitmap();//重新绘制图案
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 重做操作
     */
    public void Redo() {
        if (DeleteShapeList != null && DeleteShapeList.size() >= 1) {
            SaveShapeList.add(DeleteShapeList.get(DeleteShapeList.size() - 1));
            DeleteShapeList.remove(DeleteShapeList.size() - 1);
            try {
                redrawOnBitmap();//重新绘制图案
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * 加载之前的画
     *
     * @param filename
     */
    public void LoadPreImage(String filename) {

        IsFileExist = true;
        //遍历文件夹下每个xml文件
        File scanFilePath = new File(filename);
        if (scanFilePath.isDirectory()) {
            for (File file : scanFilePath.listFiles()) {
                String fileAbsolutePath = file.getAbsolutePath();
                System.out.println(fileAbsolutePath);
                if (fileAbsolutePath.endsWith(".xml")) {
                    //将xml解析放入mGallery中
                    try {
                        mGallery.AddPainting(
                                XmlOperation.TransXmlToShape(fileAbsolutePath),
                                mBitmap);
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                }

            }
            //添加名字属性
            String name = filename.substring(filename.lastIndexOf("/") + 1);
            mGallery.setName(name);
            //修改对应页数相关
            CurrentPageNum = mGallery.getNum();
            //载入当前第一页内容
            SaveShapeList.clear();
            SaveShapeList.addAll(mGallery.getPaintingList().get(0));
            System.out.println("载入成功");
            System.out.println("笔迹个数" + SaveShapeList.size());
            try {
                redrawOnBitmap();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    /**
     * ************************************
     * getter and setter methond
     * <p/>
     * ************************************
     */

    public int getCurrentPageNum() {
        return CurrentPageNum;
    }

    public void setCurrentPageNum(int currentPageNum) {
        CurrentPageNum = currentPageNum;
    }

    public int getCurrentPageIndex() {
        return CurrentPageIndex;
    }

    public void setCurrentPageIndex(int currentPageIndex) {
        CurrentPageIndex = currentPageIndex;
    }

    /**
     * 设置笔迹宽度
     */
    public void setBrushSize(float brushsize) {
        currentWidth = brushsize;
        mPaint.setStrokeWidth(brushsize);
    }

    /**
     * 获取笔迹宽度
     *
     * @return
     */
    public float getBrushSize() {
        return mPaint.getStrokeWidth();
    }

    /**
     * 设置笔迹颜色
     *
     * @param color
     */
    public void setBrushColor(String color) {
        currentColor = color;
        mPaint.setColor(Color.parseColor(currentColor));
    }


    /**
     * 获取笔迹颜色
     *
     * @return
     */
    public String getBrushColor() {

        return currentColor;
    }

    /**
     * 设置笔迹类型
     *
     * @param currentkind
     */
    public void setCurrentkind(int currentkind) {
        this.currentkind = currentkind;
    }

    /**
     * 获取笔迹类型
     *
     * @return
     */
    public int getCurrentkind() {
        return currentkind;
    }

    /**
     * 获取SaveList
     *
     * @return
     */
    public List<Shape> getSaveShapeList() {

        return SaveShapeList;
    }

    /**
     * 设置SaveList
     *
     * @param saveShapeList
     */
    public void setSaveShapeList(List<Shape> saveShapeList) {
        SaveShapeList = saveShapeList;
    }

    /**
     * 获取Bitmap
     *
     * @return
     */
    public Bitmap getmBitmap() {

        return mBitmap;
    }

    /**
     * 设置Bitmap
     *
     * @param mBitmap
     */
    public void setmBitmap(Bitmap mBitmap) {

        this.mBitmap = mBitmap;
    }

    /**
     * 设置画布宽度
     *
     * @param canvansWidth
     */
    public void setCanvansWidth(int canvansWidth) {
        CanvansWidth = canvansWidth;
    }

    /**
     * 设置画布高度
     *
     * @param canvansHeight
     */
    public void setCanvansHeight(int canvansHeight) {
        CanvansHeight = canvansHeight;
    }

    public Gallery getmGallery() {
        return mGallery;
    }

    public void setmGallery(Gallery mGallery) {
        this.mGallery = mGallery;
    }
    /**
     * **************************************
     * other methond
     *
     * **************************************
     */


    /**
     * 重新绘制Bitmap上的图案
     */
    public void redrawOnBitmap() throws Exception {
        System.out.println("------------------->redraw");
        // 重新设置画布，相当于清空画布
        initCanvas();
        //依次遍历，绘制对应图案
        for (int i = 0; i < SaveShapeList.size(); i++) {
            SaveShapeList.get(i).draw(mCanvas);
        }
        if (MoveShapeState) {
            if (NeedMoveList != null) {
                PathEffect effects = new DashPathEffect(new float[]{8, 8, 8, 8}, 1);//设置虚线的间隔和点的长度
                Paint newpaint = new Paint();
                newpaint.setPathEffect(effects);
                newpaint.setColor(Color.parseColor(Constants.colors[1]));
                newpaint.setStyle(Paint.Style.STROKE);
                mCanvas.drawRect(NeedMoveRect.left, NeedMoveRect.top, NeedMoveRect.right, NeedMoveRect.bottom, newpaint);
            }
        }
        invalidate();
    }

    /**
     * 判断是否有内容
     *
     * @return
     */
    public boolean isEmpty() {
        if (SaveShapeList.size() == 0)
            return true;
        else
            return false;
    }

    /**
     * 判断是否是在已存在文件上编辑
     *
     * @return
     */
    public boolean isFileExist() {

        return IsFileExist;
    }

    /**
     * 判断是否发生了编辑
     *
     * @return
     */
    public boolean isEdited() {
        if (PreShapeListSize == SaveShapeList.size()) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * 绘制新的图形
     */
    public void DrawNewImage() {
        if (mGallery.getNum() != CurrentPageNum) {
            //保存当前笔迹集合及Bitmap
            mGallery.AddPainting(SaveShapeList, mBitmap);

        }
        SaveShapeList.clear();
        DeleteShapeList.clear();
        //清空画布及相关数据
        initCanvas();

        invalidate();
    }

    /**
     * 返回到上一页
     */
    public void TurnToPrePage() {

        //刚好处于最后一页要往前翻
        if (mGallery.getNum() == CurrentPageNum - 1) {
            //保存当前图形
            mGallery.AddPainting(SaveShapeList, mBitmap);
        } else {
            //覆盖当前图形
            mGallery.CoverPainting(SaveShapeList, mBitmap, CurrentPageIndex);
        }
        //清空画布及相关数据
        initCanvas();
        SaveShapeList.clear();
        DeleteShapeList.clear();
        //加载上一页内容
        SaveShapeList.addAll(mGallery.getPaintingList().get(CurrentPageIndex - 1));
        try {
            redrawOnBitmap();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 跳转下一页
     */
    public void TurnToNextPage() {

        //覆盖当前图形
        mGallery.CoverPainting(SaveShapeList, mBitmap, CurrentPageIndex - 2);
        //清空画布及相关数据
        initCanvas();
        SaveShapeList.clear();
        DeleteShapeList.clear();
        //加载下一页内容
        SaveShapeList.addAll(mGallery.getPaintingList().get(CurrentPageIndex - 1));
        try {
            redrawOnBitmap();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 判断是否需要保存最后一张并处理
     */
    public void saveTheLast() {
        if (mGallery.getNum() == CurrentPageNum - 1) {
            mGallery.AddPainting(SaveShapeList, mBitmap);
        } else {
            mGallery.CoverPainting(SaveShapeList, mBitmap, CurrentPageIndex - 1);
        }
    }


    /**
     * 修改橡皮擦状态
     */
    public void ChangeEraserState() {
        if (MoveShapeState == true) {
            MoveShapeState = false;
        }
        EraserState = !EraserState;
        System.out.println(EraserState);
    }


    /**
     * 修改笔迹相关操作状态
     */
    public void ChangeCutState() {
        if (EraserState == true) {
            EraserState = false;
        }
        MoveShapeState = !MoveShapeState;
        System.out.println(MoveShapeState);
    }
}
