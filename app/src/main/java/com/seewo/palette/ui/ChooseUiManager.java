package com.seewo.palette.ui;


import android.content.Context;

import android.widget.LinearLayout;

import com.seewo.palette.callback.ColorCallBack;
import com.seewo.palette.callback.ColorChangeCall;
import com.seewo.palette.callback.PageChangeCall;
import com.seewo.palette.callback.ShapeChangeCall;
import com.seewo.palette.callback.SizeChangeCall;
import com.seewo.palette.util.Constants;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

/**
 * Created by user on 2016/7/28.
 * 选择框组件管理
 */
public class ChooseUiManager {
    Context mContext;
    LinearLayout mView;


    public ChooseUiManager(Context context, LinearLayout layout) {
        this.mContext = context;
        this.mView = layout;

    }

    SizeChangeCall mCall;


    /**
     * 注册画笔大小设定回调
     *
     * @param call
     */
    public void setSizeCallback(SizeChangeCall call) {
        this.mCall = call;
    }

    /**
     * 显示画笔尺寸选择组件
     */
    public void ShowSizeUi(float currentsize) {

        DiscreteSeekBar seekBar = new DiscreteSeekBar(mContext);
        seekBar.setMax(Constants.maxBrushSize);
        seekBar.setMin(Constants.minBrushSize);
        seekBar.setMinimumHeight(10);
        seekBar.setProgress(Math.round(currentsize)); //四舍五入
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(100, 0, 100, 30);
        mView.addView(seekBar, lp);
        seekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
                //将停下来时候的值作为画笔的当前粗细大小
                int currentnum = seekBar.getProgress();
                mCall.callBySizeChange(currentnum);
            }
        });
    }

    ColorCallBack colorCallback;

    /**
     * 注册颜色修改回调
     *
     * @param back
     */
    public void setColorCallBack(ColorCallBack back) {
        this.colorCallback = back;
    }

    /**
     * 显示画笔颜色选择组件
     */
    public void ShowColorUi() {
        ColorPalette cp = new ColorPalette(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(30, 30, 10, 40);
        mView.addView(cp, lp);
        cp.setColorChangeCall(new ColorChangeCall() {
            @Override
            public void callByColorChange(String color) {
                //将选中的值作为画笔的颜色
                colorCallback.setChangeColor(color);
            }
        });
    }

    ShapeChangeCall shapeChangeCall;

    /**
     * 注册图形选择回调
     *
     * @param back
     */
    public void setShapeChangeCall(ShapeChangeCall back) {
        this.shapeChangeCall = back;
    }


    /**
     * 显示图形绘制选择组件
     */
    public void ShowShapeUi(int currentkind) {
        ShapeSelectView sv = new ShapeSelectView(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(30, 30, 10, 30);
        mView.addView(sv, lp);
        sv.setKind(currentkind);
        sv.setKindBtnClickedListener(new ShapeSelectView.KindBtnClickedListener() {
            @Override
            public void onKindBtnClicked(int kind) {
                shapeChangeCall.CallByShapeChange(kind);
            }
        });

    }

    PageChangeCall pageChangeCall;

    public void setPageChangeCall(PageChangeCall back) {
        this.pageChangeCall = back;
    }

    /**
     * 显示多页添加组件
     */
    public void ShowPageUi(int currentpagenum, int currentpageindex) {

        PageSelectView pv = new PageSelectView(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(30, 30, 10, 30);
        mView.addView(pv, lp);
        pv.setPagenum(currentpagenum);
        pv.setPageindex(currentpageindex);
        pv.SetPageComponentClickListener(new PageSelectView.PageComponentClickListener() {
            @Override
            public void AddPageClicked(int pagenum, int pageindex) {
                pageChangeCall.PageAddCall(pagenum, pageindex);
            }

            @Override
            public void PrePageClicked(int pageindex) {

                pageChangeCall.PagePreCall(pageindex);
            }

            @Override
            public void NextPageClicked(int pageindex) {
                pageChangeCall.PageNextCall(pageindex);
            }
        });
    }
}
