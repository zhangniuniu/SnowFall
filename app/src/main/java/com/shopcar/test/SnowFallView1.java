package com.shopcar.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.currentTimeMillis;

/**
 * Author：zhangyong on 2017/9/1 09:53
 * Email：zhangyonglncn@gmail.com
 * Description：点哪在哪出现雪花
 */

public class SnowFallView1 extends View implements View.OnTouchListener {


    private Paint snowPaint;

    private List<SnowBean> snowBeanList = new ArrayList<>();
    private Bitmap snowBitmap;

    public SnowFallView1(Context context) {
        this(context, null);
    }

    public SnowFallView1(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SnowFallView1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        snowBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        snowPaint = new Paint();
        snowPaint.setAntiAlias(true);

        setOnTouchListener(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (snowBeanList.size() != 0) {
                    invalidate();
                }
                postDelayed(this, 10);
            }
        }, 10);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_UP:
                float upX = motionEvent.getX();
                float upY = motionEvent.getY();
                long upTime = System.currentTimeMillis();
                SnowBean snowBean = new SnowBean();
                snowBean.preTime = upTime;
                snowBean.prePositonY = upY;
                snowBean.prePositonX = upX;
                float fallTotalTime = (float) (2 + Math.random() * (3));
                snowBean.fallSpeed = getHeight() / fallTotalTime;
                snowBeanList.add(snowBean);
                invalidate();
                break;
        }
        //手抬起的位置生成雪花，就行掉落
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {


        List<SnowBean> dropList = new ArrayList<>();
        for (int i = 0; i < snowBeanList.size(); i++) {
            SnowBean snowBean = snowBeanList.get(i);
            if (snowBean.prePositonY >= getHeight()) {
                dropList.add(snowBean);
            } else {
                long curTime = currentTimeMillis();
                //(当前时间ms-上次时间ms)/1000*速度 +上次的位置
                float snowTop = (curTime - snowBean.preTime) / 1000.f * snowBean.fallSpeed + snowBean.prePositonY;
                snowBean.prePositonY = snowTop;
                snowBean.preTime = curTime;
                float snowLeft = snowBean.prePositonX;
                canvas.drawBitmap(snowBitmap, snowLeft, snowTop, snowPaint);
            }
        }
        snowBeanList.removeAll(dropList);
    }

}
