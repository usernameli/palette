package com.seewo.palette.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.seewo.palette.R;

/**
 * Created by user on 2016/8/8.
 * 添加多页view
 */
public class PageSelectView extends LinearLayout implements View.OnClickListener {

    private Context mContext;
    private int pagenum=0;
    private int pageindex=0;

    private ImageView addPageBtn;
    private ImageView turnPrePageBtn;
    private ImageView turnNextPageBtn;
    private TextView pageNumText;


    public int getPagenum() {
        return pagenum;
    }

    public void setPagenum(int pagenum) {
        this.pagenum = pagenum;
    }

    public int getPageindex() {
        return pageindex;
    }


    public void setPageindex(int pageindex) {
        this.pageindex = pageindex;
        pageNumText.setText(pageindex+"");
    }

    public PageSelectView(Context context) {
       this(context,null);
    }

    public PageSelectView(Context context, AttributeSet attrs) {

        this(context, attrs,0);
    }

    public PageSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.add_page_layout, this);
        initView();
        initEvent();
    }

    private void initView() {
        addPageBtn = (ImageView) findViewById(R.id.id_add_page);
        turnPrePageBtn = (ImageView) findViewById(R.id.id_pre_page);
        pageNumText = (TextView) findViewById(R.id.id_page_num);
        turnNextPageBtn = (ImageView) findViewById(R.id.id_next_page);
    }

    private void initEvent() {
        addPageBtn.setOnClickListener(this);
        turnPrePageBtn.setOnClickListener(this);
        turnNextPageBtn.setOnClickListener(this);
        pageNumText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_add_page:
                //加页，显示最新一页
                pagenum++;
                pageindex=pagenum;
                pageNumText.setText(pageindex+"");
                if (mPageComponentClickListener != null) {
                    mPageComponentClickListener.AddPageClicked(pagenum,pageindex);
                }
                break;
            case R.id.id_pre_page:
                //回调前一页
                if(pageindex>1){
                    pageindex--;
                    pageNumText.setText(pageindex+"");
                    if (mPageComponentClickListener != null)
                        mPageComponentClickListener.PrePageClicked(pageindex);
                }
                break;
            case R.id.id_page_num:
                //TODO 显示浏览组件 暂时不写

                break;
            case R.id.id_next_page:
                //调至下一页
                if(pageindex<pagenum){
                    pageindex++;
                    pageNumText.setText(pageindex+"");
                    if (mPageComponentClickListener != null)
                        mPageComponentClickListener.NextPageClicked(pageindex);
                }
                break;
        }
    }


    public interface PageComponentClickListener {

        void AddPageClicked(int pagenum,int pageindex);

        void PrePageClicked(int pageindex);

        void NextPageClicked(int pageindex);

    }

    private PageComponentClickListener mPageComponentClickListener;

    public void SetPageComponentClickListener(PageComponentClickListener listener) {
        this.mPageComponentClickListener = listener;
    }
}
