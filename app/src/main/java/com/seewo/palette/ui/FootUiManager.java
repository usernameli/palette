package com.seewo.palette.ui;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;

import com.seewo.palette.R;
import com.seewo.palette.activity.PaintActivity;

import java.security.PublicKey;

/**
 * Created by user on 2016/7/28.
 * 工具栏组件管理类
 */
public class FootUiManager implements View.OnClickListener {

    Context mContext;
    View mView;
    ImageButton mBrushSizeBtn;
    ImageButton mBrushColorBtn;
    ImageButton mAddPageBtn;
    ImageButton mCutPathBtn;
    ImageButton mEraserBtn;
    ImageButton mShapeBtn;

    boolean isShow = false;

    public FootUiManager(Context mContext, View mView) {
        this.mContext = mContext;
        this.mView = mView;
        initView();
        initEvent();
    }

    private void initEvent() {
        mBrushSizeBtn.setOnClickListener(this);
        mBrushColorBtn.setOnClickListener(this);
        mAddPageBtn.setOnClickListener(this);
        mCutPathBtn.setOnClickListener(this);
        mEraserBtn.setOnClickListener(this);
        mShapeBtn.setOnClickListener(this);
    }

    private void initView() {
        mBrushSizeBtn = (ImageButton) mView.findViewById(R.id.brush_size_choose);
        mBrushColorBtn = (ImageButton) mView.findViewById(R.id.id_color_choose);
        mAddPageBtn = (ImageButton) mView.findViewById(R.id.id_add_canvas);
        mCutPathBtn = (ImageButton) mView.findViewById(R.id.id_cut_path);
        mEraserBtn = (ImageButton) mView.findViewById(R.id.id_eraser);
        mShapeBtn = (ImageButton) mView.findViewById(R.id.id_select_shape);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.brush_size_choose:
                //选择宽度功能
                if (mSizeBtnOnclickListener != null) {
                    mSizeBtnOnclickListener.Clicked(isShow);
                    isShow = !isShow;//状态取反
                }
                break;

            case R.id.id_color_choose:
                //选择颜色功能
                if (mColorBtnOnclickListener != null) {
                    mColorBtnOnclickListener.ColorClicked(isShow);
                    isShow = !isShow;
                }
                break;
            case R.id.id_add_canvas:
                //添加多页功能
                if (mPageBtnOnClickListener != null) {
                    mPageBtnOnClickListener.PageClicked(isShow);
                    isShow = !isShow;
                }
                break;
            case R.id.id_cut_path:
                //TODO 笔迹获取及相关操作
                if(mShapeChooseBtnOnclickListener!=null){
                    mShapeChooseBtnOnclickListener.ChooseOnClicked();
                }
                break;
            case R.id.id_eraser:
                //橡皮擦功能
                if (mEraserBtnOnClickListener != null) {
                    mEraserBtnOnClickListener.EraserClicked();
                }
                break;
            case R.id.id_select_shape:
                //选择图形功能
                if (mShapeBtnClickListener != null) {
                    mShapeBtnClickListener.onShapeBtnClicked(isShow);
                    isShow = !isShow;
                }
                break;
        }
    }


    /**
     * 笔迹粗细调整按钮点击监听接口
     */
    public interface SizeBtnOnclickListener {
        void Clicked(boolean isShow);
    }

    private SizeBtnOnclickListener mSizeBtnOnclickListener;

    public void setSizeBtnListener(SizeBtnOnclickListener listener) {
        this.mSizeBtnOnclickListener = listener;
    }

    /**
     * 笔迹颜色调整按钮点击监听接口
     */
    public interface ColorBtnOnclickListener {
        void ColorClicked(boolean isShow);
    }

    private ColorBtnOnclickListener mColorBtnOnclickListener;

    public void setColorBtnOnclickListener(ColorBtnOnclickListener listener) {
        this.mColorBtnOnclickListener = listener;
    }

    /**
     * 图形选择监听接口
     */
    public interface ShapeBtnClickListener {
        void onShapeBtnClicked(boolean isShow);
    }

    private ShapeBtnClickListener mShapeBtnClickListener;

    public void setShapeBtnClickListener(ShapeBtnClickListener listener) {
        this.mShapeBtnClickListener = listener;
    }

    /**
     * 添加多页监听接口
     */
    public interface PageBtnOnClickListener {
        void PageClicked(boolean isShow);
    }

    private PageBtnOnClickListener mPageBtnOnClickListener;

    public void setPageBtnOnClickListener(PageBtnOnClickListener listener) {
        this.mPageBtnOnClickListener = listener;
    }

    /**
     * 橡皮擦监听接口
     */
    public interface EraserBtnOnClickListener {
        void EraserClicked();
    }

    private EraserBtnOnClickListener mEraserBtnOnClickListener;

    public void setEraserBtnOnClickListener(EraserBtnOnClickListener listener) {
        this.mEraserBtnOnClickListener = listener;
    }


    /**
     * 笔迹移动监听接口
     */
    public interface ShapeChooseBtnOnclickListener{
        void ChooseOnClicked();
    }

    private ShapeChooseBtnOnclickListener mShapeChooseBtnOnclickListener;

    public void setShapeChooseBtnOnclickListener(ShapeChooseBtnOnclickListener listener){

        this.mShapeChooseBtnOnclickListener=listener;
    }

}
