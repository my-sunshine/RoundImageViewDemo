package com.liyi.circleimageview;

/**
 * author: ly
 * data: 2016/7/22
 */

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
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;

public class CircleImageView extends ImageView {

    private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;

    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int COLORDRAWABLE_DIMENSION = 2;

    private static final int DEFAULT_BORDER_WIDTH = 0;
    private static final int DEFAULT_BORDER_COLOR = Color.BLACK;

    private final int DEFAULT_TYPE_CIRCLE = 0;
    private final static int DEFAULT_RADIUS = 0;

    private final RectF mDrawableRect = new RectF();
    private final RectF mBorderRect = new RectF();

    private final Matrix mShaderMatrix = new Matrix();
    private final Paint mBitmapPaint = new Paint();
    private final Paint mBorderPaint = new Paint();

    private int mBorderColor;
    private int mBorderWidth;
    private int mAngle;
    private int mType;

    private Bitmap mBitmap;
    private BitmapShader mBitmapShader;
    private int mBitmapWidth;
    private int mBitmapHeight;


    private float mDrawableRadius;
    private float mBorderRadius;
    private boolean mReady;
    private boolean mSetupPending;

    public CircleImageView(Context context) {
        super(context);

        init();
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Log.i("CircleImageView", "CircleImageView");
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView, defStyle, 0);
        mBorderWidth = a.getDimensionPixelSize(R.styleable.RoundImageView_round_border_width, DEFAULT_BORDER_WIDTH);
        mBorderColor = a.getColor(R.styleable.RoundImageView_round_border_color, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,DEFAULT_BORDER_COLOR,context.getResources().getDisplayMetrics()));
        mAngle = a.getDimensionPixelSize(R.styleable.RoundImageView_round_radius, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,DEFAULT_RADIUS,context.getResources().getDisplayMetrics()));
        mType=a.getInt(R.styleable.RoundImageView_round_type, DEFAULT_TYPE_CIRCLE);
        a.recycle();
        init();
    }

    private void init() {
        super.setScaleType(SCALE_TYPE);
        mReady = true;
        if (mSetupPending) {
            setup();
            mSetupPending = false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i("CircleImageView", "onDraw");
        if (mBitmap == null) {
            return;
        }

        if(mType==DEFAULT_TYPE_CIRCLE){
            canvas.drawCircle(mDrawableRect.centerX(), mDrawableRect.centerY(), mDrawableRadius, mBitmapPaint);
            if (mBorderWidth > 0) {
                canvas.drawCircle(mBorderRect.centerX(), mBorderRect.centerY(), mBorderRadius, mBorderPaint);
            }
        }else{

            canvas.drawRoundRect(mDrawableRect, mAngle,mAngle, mBitmapPaint);
            if (mBorderWidth > 0) {
                canvas.drawRoundRect(mBorderRect, mAngle,mAngle, mBorderPaint);
            }

        }

    }

    /**
     * This is called during layout when the size of this view has changed. If
     * you were just added to the view hierarchy, you're called with the old
     * values of 0.
     *
     * @param w Current width of this view.
     * @param h Current height of this view.
     * @param oldw Old width of this view.
     * @param oldh Old height of this view.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.i("CircleImageView", "onSizeChanged");
        setup();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.i("CircleImageView", "onLayout");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i("CircleImageView", "onMeasure");
    }

    /**
     * Finalize inflating a view from XML.  This is called as the last phase
     * of inflation, after all child views have been added.
     *
     * <p>Even if the subclass overrides onFinishInflate, they should always be
     * sure to call the super method, so that we get called.
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.i("CircleImageView", "onFinishInflate");
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        Log.i("CircleImageView", "setImageBitmap");
        initializeBitmap();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        Log.i("CircleImageView", "setImageDrawable");
        initializeBitmap();
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        Log.i("CircleImageView", "setImageResource");
        initializeBitmap();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        Log.i("CircleImageView", "setImageURI");
        initializeBitmap();
    }


    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void initializeBitmap() {
        mBitmap = getBitmapFromDrawable(getDrawable());
        setup();
    }

    private void setup() {
        if (!mReady) {
            mSetupPending = true;
            return;
        }
        if (getWidth() == 0 && getHeight() == 0) {
            return;
        }

        if (mBitmap == null) {
            invalidate();
            return;
        }

        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setShader(mBitmapShader);

        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);

        mBitmapHeight = mBitmap.getHeight();
        mBitmapWidth = mBitmap.getWidth();

        mBorderRect.set(calculateBounds());
        mBorderRadius = Math.min((mBorderRect.height() - mBorderWidth) / 2.0f, (mBorderRect.width() - mBorderWidth) / 2.0f);

        mDrawableRect.set(mBorderRect);

        if (mBorderWidth > 0) {
            if(mType==DEFAULT_TYPE_CIRCLE){
                mDrawableRect.inset(mBorderWidth - 1.0f, mBorderWidth - 1.0f);
            }else{
                float borderW=((float)mBorderWidth)/2;
                mDrawableRect.inset(borderW- 1.0f, borderW- 1.0f);
            }
        }
        mDrawableRadius = Math.min(mDrawableRect.height() / 2.0f, mDrawableRect.width() / 2.0f);
        updateShaderMatrix();
        invalidate();
    }

    private RectF calculateBounds() {

        int w= getWidth();
        int h=getHeight();
        int pl=getPaddingLeft();
        int pr=getPaddingRight();
        int pt=getPaddingTop();
        int pb=getPaddingBottom();

        int availableWidth  = getWidth() - getPaddingLeft() - getPaddingRight();
        int availableHeight = getHeight() - getPaddingTop() - getPaddingBottom();

        if(mType==DEFAULT_TYPE_CIRCLE){

            int sideLength = Math.min(availableWidth, availableHeight);

            float left = getPaddingLeft() + (availableWidth - sideLength) / 2f;
            float top = getPaddingTop() + (availableHeight - sideLength) / 2f;

            return new RectF(left, top, left + sideLength, top + sideLength);
        }else {
            float left = getPaddingLeft() ;
            float top = getPaddingTop() ;
            float borderW=((float)mBorderWidth)/2;
            return new RectF(left+borderW, top+borderW, left + availableWidth-borderW, top + availableHeight-borderW);
        }


        /*int sideLength = Math.min(availableWidth, availableHeight);

        float left = getPaddingLeft() + (availableWidth - sideLength) / 2f;
        float top = getPaddingTop() + (availableHeight - sideLength) / 2f;

        return new RectF(left, top, left + sideLength, top + sideLength);*/
    }

    private void updateShaderMatrix() {
        float scale;
        float dx = 0;
        float dy = 0;

        mShaderMatrix.set(null);

        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
            scale = mDrawableRect.height() / (float) mBitmapHeight;
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
        } else {
            scale = mDrawableRect.width() / (float) mBitmapWidth;
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
        }

        mShaderMatrix.setScale(scale, scale);
        mShaderMatrix.postTranslate((int) (dx + 0.5f) + mDrawableRect.left, (int) (dy + 0.5f) + mDrawableRect.top);

        mBitmapShader.setLocalMatrix(mShaderMatrix);
    }

}