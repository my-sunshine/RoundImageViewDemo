package com.liyi.roundimageviewdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;

import com.liyi.roundimageviewdemo.R;

/**
 * author: ly
 * data: 2016/7/21
 *
 * http://www.runoob.com/w3cnote/android-tutorial-canvas-api1.html
 *
 * http://blog.csdn.net/lmj623565791/article/details/41967509/
 *
 * http://blog.csdn.net/iispring/article/details/50472485
 *
 * http://blog.csdn.net/lmj623565791/article/details/24555655
 *
 */
public class RoundImageView extends ImageView {

    private int borderWidth;
    private int borderColor;
    private int borderRadius;
    private int type;

    private final int DEFAULT_TYPE_CIRCLE = 0;

    private final static int DEFAULT_COLOR = Color.WHITE;

    private final static int DEFAULT_BORDER_WIDTH = 0;

    private final static int DEFAULT_RADIUS = 0;

    //circle 的宽，半径
    private int width;
    private float radius;

    /**
     * 绘图的Paint
     */
    private Paint bitmapPaint;
    private Paint borderPaint;

    /**
     * 3x3 矩阵，主要用于缩小放大
     */
    private Matrix matrix;
    /**
     * 渲染图像，使用图像为绘制图形着色
     */
    private BitmapShader bitmapShader;

    private RectF roundRect;
    private RectF borderRect;

    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.i("RoundImageView", "RoundImageView");
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView, defStyleAttr, 0);

        borderColor = typedArray.getColor(R.styleable.RoundImageView_round_border_color, DEFAULT_COLOR);

        borderWidth = typedArray.getDimensionPixelSize(R.styleable.RoundImageView_round_border_width, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_BORDER_WIDTH, context.getResources().getDisplayMetrics()));

        borderRadius = typedArray.getDimensionPixelSize(R.styleable.RoundImageView_round_radius, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_RADIUS, context.getResources().getDisplayMetrics()));

        type=typedArray.getInt(R.styleable.RoundImageView_round_type, DEFAULT_TYPE_CIRCLE);

        typedArray.recycle();

        matrix=new Matrix();
        bitmapPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint=new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setAntiAlias(true);
        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(borderWidth);
        //borderPaint.setStrokeCap(Paint.Cap.BUTT);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i("RoundImageView","onMeasure");
        if(type==DEFAULT_TYPE_CIRCLE){
            width=Math.min(getMeasuredWidth(),getMeasuredHeight());
            radius=(width*1.0f)/2;
            setMeasuredDimension(width, width);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        Log.i("RoundImageView", "onDraw");
        if(getDrawable()==null){
            return;
        }
        setShader();
        if(type==DEFAULT_TYPE_CIRCLE){
            canvas.drawCircle(radius,radius,radius-borderWidth,bitmapPaint);
            if(borderWidth>0){
                canvas.drawCircle(radius,radius,radius-borderWidth/2,borderPaint);
            }

        }else {
            canvas.drawRoundRect(roundRect, borderRadius, borderRadius, bitmapPaint);
            if(borderWidth>0){
                canvas.drawRoundRect(borderRect, borderRadius, borderRadius, borderPaint);
            }
        }
    }

    /*
    TileMode：（一共有三种）
            （1）CLAMP：如果渲染器超出原始边界范围，会复制范围内边缘染色。
            （2）REPEAT：横向和纵向的重复渲染器图片，平铺。
            （3）MIRROR：横向和纵向的重复渲染器图片，这个和REPEAT 重复方式不一样，他是以镜像方式平铺。

    为BitmapShader设置一个matrix，去适当的放大或者缩小图片，不会让“ view的宽或者高大于我们的bitmap宽或者高 ”

    */
    private void setShader() {
        Drawable drawable = getDrawable();
        Bitmap bitmap=drawableToBitmap(drawable);
        // 将bitmapShader作为着色器，就是在指定区域内绘制bmp
        bitmapShader=new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = 1.0f;
        if(type==DEFAULT_TYPE_CIRCLE){
            int bitmapWidth=Math.min(bitmap.getWidth(),bitmap.getHeight());
            scale=width*1.0f/bitmapWidth;
        }else{
            float scaleX = 1.0f;
            float scaleY = 1.0f;
            int bitmapWidth=bitmap.getWidth();
            int bitmapHeight=bitmap.getHeight();
            scaleX=getWidth()*1.0f/bitmapWidth;
            scaleY=getHeight()*1.0f/bitmapHeight;
            scale=Math.max(scaleX,scaleY);
        }
        // shader的变换矩阵，我们这里主要用于放大或者缩小
        matrix.setScale(scale,scale);
        // 设置变换矩阵
        bitmapShader.setLocalMatrix(matrix);

        bitmapPaint.setShader(bitmapShader);
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable)
        {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }

        int w=drawable.getIntrinsicWidth();
        int h=drawable.getIntrinsicHeight();

        Bitmap bitmap=Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }


    //因为有border的宽度，所以图片的RectF要减小的
    //border的宽度是在borderRect的两侧各加一半的宽度
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.i("RoundImageView", "onSizeChanged");
        if(type!=DEFAULT_TYPE_CIRCLE){
            float bw=borderWidth*1.0f/2;
            roundRect = new RectF(borderWidth, borderWidth, getWidth()-borderWidth, getHeight()-borderWidth);
            borderRect= new RectF(bw, bw, getWidth()-bw, getHeight()-bw);
        }
    }


    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
    }


    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
    }
}
