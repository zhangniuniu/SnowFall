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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.currentTimeMillis;

/**
 * Author：zhangyong on 2017/9/1 09:53
 * Email：zhangyonglncn@gmail.com
 * Description：随机掉落雪花
 */

public class SnowFallView2 extends View implements View.OnTouchListener {


    private Paint snowPaint;
    //屏幕雪花总数
    private int snowCount = 10;
    private List<SnowBean> snowBeanList = new ArrayList<>();
    private Bitmap snowBitmap;
    private int snowWidth;
    private int snowHeight;

    public SnowFallView2(Context context) {
        this(context, null);
    }

    public SnowFallView2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SnowFallView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        snowBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        snowWidth = snowBitmap.getWidth();
        snowHeight = snowBitmap.getHeight();

        snowPaint = new Paint();
        snowPaint.setAntiAlias(true);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (snowBeanList.size() != 0) {
                    invalidate();
                }
                postDelayed(this, 10);
            }
        }, 10);

        setOnTouchListener(this);
    }


    public void startFall() {
        int lateSpace = snowCount - snowBeanList.size();
        //随机生成雪花
        for (int i = 0; i < lateSpace; i++) {
            float initLeft = (float) (Math.random() * getWidth());
            long upTime = System.currentTimeMillis();
            SnowBean snowBean = new SnowBean();
            snowBean.preTime = upTime;
            snowBean.prePositonY = 0;
            snowBean.prePositonX = initLeft;
            float fallTotalTime = (float) (2 + Math.random() * (3));
            snowBean.fallSpeed = getHeight() / fallTotalTime;
            snowBeanList.add(snowBean);
        }
        invalidate();
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
        if (dropList.size() != 0) {
            startFall();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_UP:
                for (int i = 0; i < snowBeanList.size(); i++) {
                    SnowBean snowBean = snowBeanList.get(i);
                    float clickX = motionEvent.getX();
                    float clickY = motionEvent.getY();
                    if (clickX >= snowBean.prePositonX && clickX <= snowBean.prePositonX + snowWidth && clickY >= snowBean.prePositonY && clickY <= snowBean.prePositonY + snowHeight) {
                        Toast.makeText(getContext(), "点到红包了", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
        //手抬起的位置生成雪花，就行掉落
        return true;
    }
}
