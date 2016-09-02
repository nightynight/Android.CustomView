package com.brokepal.customviewdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.brokepal.customviewdemo.R;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by Administrator on 2016/8/29.
 */
public class CustomTitleView extends View {
    private String mTitleText;
    private int mTitleTextColor;
    private int mTitleTextSize;

    /**
     * 绘制时控制文本绘制的范围
     */
    private Rect mBound;
    private Paint mPaint;

    public CustomTitleView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public CustomTitleView(Context context)
    {
        this(context, null);
    }

    /**
     * 构造函数，获取属性列表，设置监听器
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public CustomTitleView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        /**
         * 获得我们所定义的自定义样式属性
         */
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomTitleView, defStyle, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++)
        {
            int attr = a.getIndex(i);
            switch (attr)
            {
                case R.styleable.CustomTitleView_myTitleText:
                    mTitleText = a.getString(attr);
                    break;
                case R.styleable.CustomTitleView_myTitleTextColor:
                    // 默认颜色设置为黑色
                    mTitleTextColor = a.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CustomTitleView_myTitleTextSize:
                    // 默认设置为16sp，TypeValue也可以把sp转化为px
                    mTitleTextSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
            }

        }
        /**
         * 获得绘制文本的宽和高
         */
        mPaint = new Paint();
        mPaint.setTextSize(mTitleTextSize);
        mBound = new Rect();
        mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);

        //设置监听器
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mTitleText = randomText();
                postInvalidate();   //刷新页面，会再次执行onMeasure方法和onDraw方法
            }
        });
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height ;
        /*
        MeasureSpec的specMode,一共三种类型：
            EXACTLY：一般是设置了明确的值或者是MATCH_PARENT
            AT_MOST：表示子布局限制在一个最大值内，一般为WARP_CONTENT
            UNSPECIFIED：表示子布局想要多大就多大，很少使用
        */
        if (widthMode == MeasureSpec.EXACTLY)
        {
            width = widthSize;
        } else
        {
            mPaint.setTextSize(mTitleTextSize);
            mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);
            float textWidth = mBound.width();
            int desired = (int) (getPaddingLeft() + textWidth + getPaddingRight());
            width = desired;
        }

        if (heightMode == MeasureSpec.EXACTLY)
        {
            height = heightSize;
        } else
        {
            mPaint.setTextSize(mTitleTextSize);
            mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);
            float textHeight = mBound.height();
            int desired = (int) (getPaddingTop() + textHeight + getPaddingBottom());
            height = desired;
        }

        //设置控件的宽高
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        mPaint.setColor(mTitleTextColor);
        canvas.drawText(mTitleText, getWidth() / 2 - mBound.width() / 2, getHeight() / 2 + mBound.height() / 2, mPaint);
        drawBackground(canvas);
    }

    //获取随机字符串
    private String randomText()
    {
        Random random = new Random();
        Set<Integer> set = new HashSet<Integer>();
        while (set.size() < 4)
        {
            int randomInt = random.nextInt(10);
            set.add(randomInt);
        }
        StringBuffer sb = new StringBuffer();
        for (Integer i : set)
        {
            sb.append("" + i);
        }
        return sb.toString();
    }

    //给mTitleText设置set方法，一般会设置所有属性的get、set方法，这里就不多设置了
    public void setmTitleText(String text){
        mTitleText=text;
        invalidate();   //mTitleText改变之后要重新绘制控件
    }

    //验证码的背景
    private void drawBackground(Canvas canvas){
        int colors[]=new int[] { Color.BLACK,Color.RED, Color.GREEN, Color.BLUE,Color.LTGRAY };
        Paint paint=new Paint();
        Random random = new Random();

        //画线
        int lineColor=random.nextInt(colors.length);
        paint.setColor(colors[lineColor]);
        paint.setStrokeWidth(3);
        int randomInt1 = random.nextInt(getHeight());
        int randomInt2 = random.nextInt(getHeight());
        int randomInt3 = random.nextInt(getHeight());
        int randomInt4 = random.nextInt(getHeight());
        float xFrom=randomInt1;
        float yFrom=randomInt2;
        float xTo=getWidth()-randomInt3;
        float yTo=getHeight()-randomInt4;
        canvas.drawLine(xFrom,yFrom,xTo,yTo,paint);

        //画点
        float positionX=0;
        float positionY=0;
        int color=0;
        for(int i=0;i<50;i++){
            positionX=random.nextInt(getWidth());
            positionY=random.nextInt(getHeight());
            color=random.nextInt(colors.length);
            paint.setColor(colors[color]);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(positionX,positionY,3,paint);
        }
    }
}
