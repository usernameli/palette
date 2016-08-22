package com.seewo.palette.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by user on 2016/7/28.
 * 通用适配器类
 */
public abstract class CommonAdapter<T> extends BaseAdapter
{
    protected LayoutInflater mInflater;
    protected Context mContext;
    protected List<T> mDatas;
    protected final int mItemLayoutId;

    public CommonAdapter(Context context, List<T> mDatas,int layoutid) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mDatas = mDatas;
        this.mItemLayoutId=layoutid;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //得到viewholder
        ViewHolder holder =ViewHolder.get(mContext,convertView,parent,mItemLayoutId,position);
        convert(holder, getItem(position));
        //返回convertview
        return holder.getConvertView();
    }

    /**
     * 抽象函数 对view中的组件进行相关操作
     * @param holder
     * @param item
     */
    protected abstract void convert(ViewHolder holder, T item);
}
