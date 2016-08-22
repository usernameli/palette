package com.seewo.palette.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.seewo.palette.R;
import com.seewo.palette.util.Constants;

/**
 * Created by user on 2016/8/3.
 * 图形选择UI
 */
public class ShapeSelectView extends LinearLayout implements View.OnClickListener {

    private final Context mContext;
    ImageView mSelectInkBtn;
    ImageView mSelectLineBtn;
    ImageView mSelectRectBtn;
    ImageView mSelectCircleBtn;

    //默认选择的是曲线
    public static int kind;

    public static int getKind() {
        return kind;
    }

    public static void setKind(int kind) {
        ShapeSelectView.kind = kind;
    }


    public ShapeSelectView(Context context) {
        this(context,null);
    }

    public ShapeSelectView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ShapeSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.shape_palette,this);
        initView();
        initEvent();
    }



    private void initView() {
        mSelectInkBtn=(ImageView)findViewById(R.id.id_select_ink);
        mSelectLineBtn=(ImageView)findViewById(R.id.id_select_line);
        mSelectRectBtn=(ImageView)findViewById(R.id.id_select_rect);
        mSelectCircleBtn=(ImageView)findViewById(R.id.id_select_circle);
    }

    private void initEvent() {
        mSelectInkBtn.setOnClickListener(this);
        mSelectLineBtn.setOnClickListener(this);
        mSelectRectBtn.setOnClickListener(this);
        mSelectCircleBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.id_select_ink:
                setKind(Constants.INK);
                break;
            case R.id.id_select_line:
                setKind(Constants.LINE);
                break;
            case R.id.id_select_rect:
                setKind(Constants.RECT);
                break;
            case R.id.id_select_circle:
                setKind(Constants.CIRCLE);
                break;
        }
        if(mKindBtnClickedListener!=null){
            mKindBtnClickedListener.onKindBtnClicked(kind);
        }

    }

    public interface KindBtnClickedListener{
        void onKindBtnClicked(int kind);
    }
    private KindBtnClickedListener mKindBtnClickedListener;
    public void setKindBtnClickedListener(KindBtnClickedListener listener){
        this.mKindBtnClickedListener=listener;
    }
}
