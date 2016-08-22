package com.seewo.palette.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.seewo.palette.R;
import com.seewo.palette.callback.ColorCallBack;
import com.seewo.palette.callback.PageChangeCall;
import com.seewo.palette.callback.ShapeChangeCall;
import com.seewo.palette.callback.SizeChangeCall;
import com.seewo.palette.save.SaveOperation;
import com.seewo.palette.save.SavePngOperation;
import com.seewo.palette.save.SaveSvgOperation;
import com.seewo.palette.ui.ChooseUiManager;

import com.seewo.palette.ui.FootUiManager;
import com.seewo.palette.ui.MyCanvas;
import com.seewo.palette.ui.SaveMenuManager;
import com.seewo.palette.util.Constants;
import com.seewo.palette.util.SaveUtil;

/**
 * Created by user on 2016/7/26.
 * 绘制图像的Activity
 */
public class PaintActivity extends Activity implements View.OnClickListener, FootUiManager.SizeBtnOnclickListener, FootUiManager.ColorBtnOnclickListener, FootUiManager.ShapeBtnClickListener, FootUiManager.PageBtnOnClickListener, SaveMenuManager.SaveBtnClickListener, FootUiManager.EraserBtnOnClickListener, FootUiManager.ShapeChooseBtnOnclickListener {


    MyCanvas mMyCanvas;
    ImageView mExitBtn;
    ImageView mUndoBtn;
    ImageView mRedoBtn;
    ImageView mMoreBtn;
    LinearLayout mFootLayout;
    FootUiManager mFootUiManager;
    LinearLayout mSaveMenuLayout;
    SaveMenuManager mSaveMenuManager;

    boolean IsMenuShow = false;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == Constants.MSG_EXIT)
                finish();
            if(msg.what==Constants.MSG_REDRAW) {
                try {
                    mMyCanvas.redrawOnBitmap();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    Handler handlertest = new Handler();
    String filename;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paint_layout);
        //初始化相关绘图工具及画布
        mMyCanvas = (MyCanvas) findViewById(R.id.id_paint_view);
        mExitBtn = (ImageView) findViewById(R.id.id_exit_btn);
        mUndoBtn = (ImageView) findViewById(R.id.id_undo_btn);
        mRedoBtn = (ImageView) findViewById(R.id.id_redo_btn);
        mMoreBtn = (ImageView) findViewById(R.id.id_save_btn);

        mFootLayout = (LinearLayout) findViewById(R.id.id_foot_layout);
        mChooseLayout = (LinearLayout) findViewById(R.id.id_choose_layout);
        mSaveMenuLayout = (LinearLayout) findViewById(R.id.save_menu_layout);
        mSaveMenuManager = new SaveMenuManager(this, mSaveMenuLayout);
        mFootUiManager = new FootUiManager(this, mFootLayout);
        mChooseUiManager = new ChooseUiManager(this, mChooseLayout);

        //TODO 装载之前的画，有就装载没有就不做处理,暂时以这种方式
        handlertest.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = getIntent();
                filename = intent.getStringExtra("openfilename");
                if (filename != null) {
                    mMyCanvas.LoadPreImage(filename);
                }
            }
        }, 500);
        //相关控件事件事件监听
        initEvent();

    }

    /**
     * 初始化监听事件
     */
    private void initEvent() {
        mExitBtn.setOnClickListener(this);
        mUndoBtn.setOnClickListener(this);
        mRedoBtn.setOnClickListener(this);
        mMoreBtn.setOnClickListener(this);

        mFootUiManager.setSizeBtnListener(this);//选择画笔大小事件监听
        mFootUiManager.setColorBtnOnclickListener(this);//选择画笔颜色事件监听
        mFootUiManager.setShapeBtnClickListener(this);//选择形状事件监听
        mFootUiManager.setPageBtnOnClickListener(this);//加页监听
        mFootUiManager.setEraserBtnOnClickListener(this);//橡皮擦监听
        mFootUiManager.setShapeChooseBtnOnclickListener(this);//笔迹操作监听
        mSaveMenuManager.setSaveBtnClickListener(this); //保存监听


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_exit_btn:
                //退出保存操作
                ExitLogic();
                break;
            case R.id.id_undo_btn:
                //撤销操作
                mMyCanvas.Undo();
                break;
            case R.id.id_redo_btn:
                //重做操作
                mMyCanvas.Redo();
                break;
            case R.id.id_save_btn:
                //另存为相关操作
                ShowMenuLogic();
                break;
        }

    }

    /**
     * 菜单显示逻辑
     */
    private void ShowMenuLogic() {
        if (IsMenuShow) {
            mSaveMenuLayout.setVisibility(View.VISIBLE);
        } else {
            mSaveMenuLayout.setVisibility(View.GONE);
        }
        IsMenuShow = !IsMenuShow;
    }

    /**
     * 退出逻辑执行
     */
    private void ExitLogic() {
        //判断是否有内容
        if (!mMyCanvas.isEmpty()) {
            //判断是否处于已存在的文件
            if (mMyCanvas.isFileExist()) {
                //是否发生了修改
                if (mMyCanvas.isEdited()) {
                    //保存最后一张
                    mMyCanvas.saveTheLast();
                    //进入覆盖保存
                    ChooseCoverOrNot();
                } else {
                    //直接退出
                    finish();
                }
            } else {
                //保存最后一张
                mMyCanvas.saveTheLast();
                //进入选择名字保存
                ChooseSaveOrNot();
            }
        } else {
            finish();
        }

    }

    /**
     * 选择是否覆盖已存在图片
     */
    private void ChooseCoverOrNot() {
        new AlertDialog.Builder(this)
                .setMessage("是否覆盖当前图片")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Message msg = new Message();
                        msg.what = Constants.MSG_EXIT;
                        handler.sendMessage(msg);
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String pathname = mMyCanvas.getmGallery().getName();
                        SaveUtil suThread = new SaveUtil(handler, pathname, mMyCanvas);
                        suThread.start();
                        dialog.dismiss();
                    }
                }).show();
    }

    /**
     * 是否选择保存绘制图案
     *
     * @return
     */
    private void ChooseSaveOrNot() {

        final EditText et = new EditText(PaintActivity.this);
        new AlertDialog.Builder(this)
                .setMessage("是否保存？")
                .setView(et)
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (et.getText().toString().equals("")) {
                            Toast.makeText(PaintActivity.this, "请输入保存文件的名字", Toast.LENGTH_SHORT).show();
                        } else {
                            //输入的名字作为文件夹的名字
                            String name = et.getText().toString();
                            //TODO 开启子线程处理保存相关
                            SaveUtil suThread = new SaveUtil(handler, name, mMyCanvas);
                            suThread.start();
                            dialog.dismiss();
                        }
                    }
                })
                .setNegativeButton("不保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Message msg = new Message();
                        msg.what = Constants.MSG_EXIT;
                        handler.sendMessage(msg);
                        dialog.dismiss();
                    }
                }).show();
    }

    LinearLayout mChooseLayout;
    ChooseUiManager mChooseUiManager;

    /**
     * 画笔粗细点击事件
     */
    @Override
    public void Clicked(boolean isshow) {
        if (isshow) {
            mChooseLayout.removeAllViews();
            mChooseLayout.setVisibility(View.GONE);
        } else {
            mChooseLayout.setVisibility(View.VISIBLE);
            mChooseUiManager.ShowSizeUi(mMyCanvas.getBrushSize());
            mChooseUiManager.setSizeCallback(new SizeChangeCall() {
                @Override
                public void callBySizeChange(float size) {
                    mMyCanvas.setBrushSize(size);
                }
            });
        }
    }

    /**
     * 颜色点击事件
     *
     * @param isShow
     */
    @Override
    public void ColorClicked(boolean isShow) {
        if (isShow) {
            mChooseLayout.removeAllViews();
            mChooseLayout.setVisibility(View.GONE);
        } else {
            mChooseLayout.setVisibility(View.VISIBLE);
            mChooseUiManager.ShowColorUi();
            mChooseUiManager.setColorCallBack(new ColorCallBack() {
                @Override
                public void setChangeColor(String color) {
                    mMyCanvas.setBrushColor(color);
                }
            });
        }
    }


    /**
     * 图形点击事件
     *
     * @param isShow
     */
    @Override
    public void onShapeBtnClicked(boolean isShow) {
        if (isShow) {
            mChooseLayout.removeAllViews();
            mChooseLayout.setVisibility(View.GONE);
        } else {
            mChooseLayout.setVisibility(View.VISIBLE);
            mChooseUiManager.ShowShapeUi(mMyCanvas.getCurrentkind());
            mChooseUiManager.setShapeChangeCall(new ShapeChangeCall() {
                @Override
                public void CallByShapeChange(int kind) {
                    mMyCanvas.setCurrentkind(kind);
                }
            });
        }
    }

    /**
     * 多页点击事件
     *
     * @param isShow
     */
    @Override
    public void PageClicked(boolean isShow) {
        if (isShow) {
            mChooseLayout.removeAllViews();
            mChooseLayout.setVisibility(View.GONE);
        } else {
            mChooseLayout.setVisibility(View.VISIBLE);
            mChooseUiManager.ShowPageUi(mMyCanvas.getCurrentPageNum(), mMyCanvas.getCurrentPageIndex());
            //接口回调
            mChooseUiManager.setPageChangeCall(new PageChangeCall() {
                @Override
                public void PageAddCall(int pagenum, int pageindex) {
                    mMyCanvas.setCurrentPageNum(pagenum);
                    mMyCanvas.setCurrentPageIndex(pageindex);
                    mMyCanvas.DrawNewImage();
                }

                @Override
                public void PagePreCall(int pageindex) {
                    mMyCanvas.setCurrentPageIndex(pageindex);
                    mMyCanvas.TurnToPrePage();
                }

                @Override
                public void PageNextCall(int pageindex) {
                    mMyCanvas.setCurrentPageIndex(pageindex);
                    mMyCanvas.TurnToNextPage();
                }
            });
        }
    }



    String saveFileName;
    SaveOperation so;
    /**
     * 另存点击事件
     * @param savekind
     */
    @Override
    public void SaveClick(int savekind) {
        switch (savekind){
            case Constants.PNG:
                so=new SavePngOperation();
                break;
            case Constants.SVG:
                so=new SaveSvgOperation();
                break;
        }
        //获取需要保存的内容
        so.GetContent(mMyCanvas);
        final EditText et = new EditText(PaintActivity.this);
        new AlertDialog.Builder(this)
                .setMessage("输入保存文件名")
                .setView(et)
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (et.getText().toString().equals("")) {
                            Toast.makeText(PaintActivity.this, "文件名不能为空", Toast.LENGTH_SHORT).show();
                        }else {
                            try {
                                saveFileName=et.getText().toString();
                                //开启线程保存
                                new Thread(new MyPicSaveRunnable()).start();
                                Message msg=new Message();
                                msg.what=Constants.MSG_REDRAW;
                                handler.sendMessageDelayed(msg,300);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    /**
     * 橡皮擦监听
     */
    @Override
    public void EraserClicked() {
        mMyCanvas.ChangeEraserState();
    }

    /**
     * 笔迹操作监听
     */
    @Override
    public void ChooseOnClicked() {
        mMyCanvas.ChangeCutState();
    }


    class MyPicSaveRunnable implements Runnable{

        @Override
        public void run() {
            if(so!=null){
                so.setFilepath(Environment.getExternalStorageDirectory()+"/palette");
                so.setFilename(saveFileName);
                so.GetAbusoluteFileName();
                so.SavePainting();
            }
        }
    }


}
