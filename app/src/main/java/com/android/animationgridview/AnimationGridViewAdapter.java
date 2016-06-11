package com.android.animationgridview;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.GridView;

public abstract class AnimationGridViewAdapter extends BaseAdapter {
    private int mIndex = Integer.MAX_VALUE;
    private GridView mGridView;
    private int mDirection = 0;

    public AnimationGridViewAdapter(GridView gridView) {
        mGridView = gridView;
    }

    @Override
    final public View getView(int position, View convertView, ViewGroup parent) {
        View view = getConvertView(position, convertView, parent);
        setAnimation(view, position);
        return view;
    }

    //在index处插入新项后, 产生动画
    public void addItemAnimation(int index) {
        itemAnimation(index, +1);
    }

    //在index处移除新项, 产生动画
    public void removeItemAnimation(int index) {
        itemAnimation(index, -1);
    }

    //移动动画, 传入需要移动的x, y, 可被覆盖
    protected Animation moveAnimation(int deltaX, int deltaY) {
        Animation animation = new TranslateAnimation(deltaX, 0, deltaY, 0);
        animation.setDuration(300);
        return animation;
    }

    private void itemAnimation(int index, int direction) {
        if (0 != getCount()) {
            mIndex = index; //修改点, 只有在修改点之后的项需要动画
            mDirection = direction; // direction > 0 为增加动画, direction < 0 为 删除动画, direction = 0 为 停止设置动画
        }
        super.notifyDataSetChanged();
        mGridView.post(new Runnable() {// notifyDataSetChanged 完成后执行
            @Override
            public void run() {
                mDirection = 0;
            }
        });
    }

    //根据position拿到相应view的x,y坐标
    private int[] getPositionXY(View view, int position) {
        if (position < 0) position = 0;
        return new int[]{
                (position % mGridView.getNumColumns()) * (view.getMeasuredWidth()),
                (position / mGridView.getNumColumns()) * (view.getMeasuredHeight())
        };
    }

    //根据两个view的position拿到它们的的x,y坐标差值
    private int[] getDeltaPositionXY(View view, int srcPosition, int dstPosition) {
        int[] src = getPositionXY(view, srcPosition);
        int[] dst = getPositionXY(view, dstPosition);
        return new int[]{src[0] - dst[0], src[1] - dst[1]};
    }

    //设置animation
    private void setAnimation(View view, int position) {
        if (position < mIndex || 0 == mDirection) return; //mIndex为修改点, 修改点之前项不设置动画
        int[] deltaXY = getDeltaPositionXY(view, position - mDirection, position);
        view.startAnimation(moveAnimation(deltaXY[0], deltaXY[1]));
    }

    protected abstract View getConvertView(int position, View convertView, ViewGroup parent);
}
