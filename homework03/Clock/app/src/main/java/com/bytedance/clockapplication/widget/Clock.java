package com.bytedance.clockapplication.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;
import java.util.Locale;

public class Clock extends View {

    private final static String TAG = Clock.class.getSimpleName();

    private static final int FULL_ANGLE = 360;

    private static final int CUSTOM_ALPHA = 140;
    private static final int FULL_ALPHA = 255;

    private static final int DEFAULT_PRIMARY_COLOR = Color.WHITE;
    private static final int DEFAULT_SECONDARY_COLOR = Color.LTGRAY;

    private static final float DEFAULT_DEGREE_STROKE_WIDTH = 0.010f;

    public final static int AM = 0;

    private static final int RIGHT_ANGLE = 90;

    private int mWidth, mCenterX, mCenterY, mRadius;

    /**
     * properties
     */
    private int centerInnerColor;
    private int centerOuterColor;

    private int secondsNeedleColor;
    private int hoursNeedleColor;
    private int minutesNeedleColor;

    private int degreesColor;

    private int hoursValuesColor;

    private int numbersColor;

    private boolean mShowAnalog = true;

    public Clock(Context context) {
        super(context);
        init(context, null);
    }

    public Clock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Clock(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int size;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int widthWithoutPadding = width - getPaddingLeft() - getPaddingRight();
        int heightWithoutPadding = height - getPaddingTop() - getPaddingBottom();

        if (widthWithoutPadding > heightWithoutPadding) {
            size = heightWithoutPadding;
        } else {
            size = widthWithoutPadding;
        }

        setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(), size + getPaddingTop() + getPaddingBottom());
    }

    private void init(Context context, AttributeSet attrs) {

        this.centerInnerColor = Color.LTGRAY;
        this.centerOuterColor = DEFAULT_PRIMARY_COLOR;

        this.secondsNeedleColor = DEFAULT_SECONDARY_COLOR;
        this.hoursNeedleColor = DEFAULT_PRIMARY_COLOR;
        this.minutesNeedleColor = DEFAULT_PRIMARY_COLOR;

        this.degreesColor = DEFAULT_PRIMARY_COLOR;

        this.hoursValuesColor = DEFAULT_PRIMARY_COLOR;

        numbersColor = Color.WHITE;
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        mWidth = getHeight() > getWidth() ? getWidth() : getHeight();

        int halfWidth = mWidth / 2;
        mCenterX = halfWidth;
        mCenterY = halfWidth;
        mRadius = halfWidth;

        if (mShowAnalog) {
            drawDegrees(canvas);
            drawHoursValues(canvas);
            drawNeedles(canvas);
            drawCenter(canvas);
        } else {
            drawNumbers(canvas);
        }

        postInvalidateDelayed(1000);  //每一秒执行一次  这是实现时间流动的关键


    }

    private void drawDegrees(Canvas canvas) {  //画度数

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(mWidth * DEFAULT_DEGREE_STROKE_WIDTH);
        paint.setColor(degreesColor);

        int rPadded = mCenterX - (int) (mWidth * 0.01f);
        int rEnd = mCenterX - (int) (mWidth * 0.05f);

        for (int i = 0; i < FULL_ANGLE; i += 6 /* Step */) {

            if ((i % RIGHT_ANGLE) != 0 && (i % 15) != 0)
                paint.setAlpha(CUSTOM_ALPHA);
            else {
                paint.setAlpha(FULL_ALPHA);   //透明度有区别
            }

            int startX = (int) (mCenterX + rPadded * Math.cos(Math.toRadians(i)));
            int startY = (int) (mCenterX - rPadded * Math.sin(Math.toRadians(i)));

            int stopX = (int) (mCenterX + rEnd * Math.cos(Math.toRadians(i)));
            int stopY = (int) (mCenterX - rEnd * Math.sin(Math.toRadians(i)));

            canvas.drawLine(startX, startY, stopX, stopY, paint);

        }
    }

    /**
     * @param canvas
     */
    private void drawNumbers(Canvas canvas) {  //第二个页面画数字

        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(mWidth * 0.2f);
        textPaint.setColor(numbersColor);
        textPaint.setColor(numbersColor);
        textPaint.setAntiAlias(true);

        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int amPm = calendar.get(Calendar.AM_PM);

        String time = String.format("%s:%s:%s%s",
                String.format(Locale.getDefault(), "%02d", hour),
                String.format(Locale.getDefault(), "%02d", minute),
                String.format(Locale.getDefault(), "%02d", second),
                amPm == AM ? "AM" : "PM");

        SpannableStringBuilder spannableString = new SpannableStringBuilder(time);
        spannableString.setSpan(new RelativeSizeSpan(0.3f), spannableString.toString().length() - 2, spannableString.toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // se superscript percent

        StaticLayout layout = new StaticLayout(spannableString, textPaint, canvas.getWidth(), Layout.Alignment.ALIGN_CENTER, 1, 1, true);
        canvas.translate(mCenterX - layout.getWidth() / 2f, mCenterY - layout.getHeight() / 2f);
        layout.draw(canvas);
    }

    /**
     * Draw Hour Text Values, such as 1 2 3 ...
     *
     * @param canvas
     */
    private void drawHoursValues(Canvas canvas) { //小时的那些数字
        // Default Color:
        // - hoursValuesColor
        Paint textPaint=new Paint();
        textPaint.setColor(hoursValuesColor);
        textPaint.setTextSize(50);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign((Paint.Align.CENTER));
        Paint.FontMetrics fontMetrics=textPaint.getFontMetrics();


        for (int i = 0; i < FULL_ANGLE; i += 30 /* Step */) {

//            if ((i % RIGHT_ANGLE) != 0 && (i % 15) != 0)
//                paint.setAlpha(CUSTOM_ALPHA);
//            else {
//                paint.setAlpha(FULL_ALPHA);   //透明度有区别
//            }
            int rPadded = mCenterX - (int) (mWidth * 0.1f);
            int startX = (int) (mCenterX + rPadded * Math.cos(Math.toRadians(i)));
            int startY = (int) (mCenterX - rPadded * Math.sin(Math.toRadians(i)));

//            int stopX = (int) (mCenterX + rEnd * Math.cos(Math.toRadians(i)));
//            int stopY = (int) (mCenterX - rEnd * Math.sin(Math.toRadians(i)));
            if(3-i/30>0)
            canvas.drawText(String.valueOf(3-i/30),startX,startY,textPaint);
            else canvas.drawText(String.valueOf(3-i/30+12),startX,startY,textPaint);

        }




    }

    /**
     * Draw hours, minutes needles
     * Draw progress that indicates hours needle disposition.
     *
     * @param canvas
     */
    private void drawNeedles(final Canvas canvas) {  //时间指针
        // Default Color:
        // - secondsNeedleColor
        // - hoursNeedleColor
        // - minutesNeedleColor
        Calendar calendar = Calendar.getInstance();
        float hour = calendar.get(Calendar.HOUR);
        float minute = calendar.get(Calendar.MINUTE);
        float second = calendar.get(Calendar.SECOND);
        int amPm = calendar.get(Calendar.AM_PM);

        Paint linePaint1 =new Paint();
        linePaint1.setStyle(Paint.Style.FILL);
        linePaint1.setStrokeWidth(9f);
        linePaint1.setColor(secondsNeedleColor);

        Paint linePaint2 =new Paint();
        linePaint2.setStyle(Paint.Style.FILL);
        linePaint2.setStrokeWidth(10f);
        linePaint2.setColor(minutesNeedleColor);

        Paint linePaint3 =new Paint();
        linePaint3.setStyle(Paint.Style.FILL);
        linePaint3.setStrokeWidth(12f);
        linePaint3.setColor(hoursValuesColor);

        float secondsangle=90-second*6;
        float minutesangle=90-(minute+second/60)*6;
        double hoursangle=90-(hour+minute/60*1.0+second/360*1.0)*30;


        float rPadded1 = mCenterX - (int) (mWidth * 0.15f);
        float startX1 = (int) (mCenterX + rPadded1* Math.cos(Math.toRadians(secondsangle)));
        float startY1 = (int) (mCenterX - rPadded1 * Math.sin(Math.toRadians(secondsangle)));

        float rPadded2 = mCenterX - (int) (mWidth * 0.18f);
        float startX2 = (int) (mCenterX + rPadded2 * Math.cos(Math.toRadians(minutesangle)));
        float startY2 = (int) (mCenterX - rPadded2 * Math.sin(Math.toRadians(minutesangle)));

        float rPadded3 = mCenterX - (int) (mWidth * 0.26f);
        float startX3 = (int) (mCenterX + rPadded3 * Math.cos(Math.toRadians(hoursangle)));
        float startY3 = (int) (mCenterX - rPadded3 * Math.sin(Math.toRadians(hoursangle)));

        canvas.drawLine(mCenterX,mCenterY,startX1,startY1,linePaint1);
        canvas.drawLine(mCenterX,mCenterY,startX2,startY2,linePaint2);
        canvas.drawLine(mCenterX,mCenterY,startX3,startY3,linePaint3);

    }

    /**
     * Draw Center Dot
     *
     * @param canvas
     */
    private Paint circlePaint;
    private Paint circlePaint2;
    private void drawCenter(Canvas canvas) {  //中心那个圆形
        // Default Color:
        // - centerInnerColor
        // - centerOuterColor

        circlePaint=new Paint();
        circlePaint.setColor(centerInnerColor);


        circlePaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mCenterX,mCenterY,20,circlePaint);

        circlePaint2=new Paint();
        circlePaint2.setColor(centerOuterColor);
        circlePaint2.setStrokeWidth(5);
        circlePaint2.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(mCenterX,mCenterY,20,circlePaint2);



    }

    public void setShowAnalog(boolean showAnalog) {
        mShowAnalog = showAnalog;
        invalidate();
    }

    public boolean isShowAnalog() {
        return mShowAnalog;
    }

}