package com.example.danceciliochua.surfaceviewexample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable {

    private boolean mRunning;
    private Thread mGameThread = null;
    private Path mPath;

    private Context mContext;

    private FlashLightCone mFlashlightCone;

    private Paint mPaint;
    private Bitmap mBitmap;
    private RectF mWinnerRect;
    private int mBitmapX;
    private int mBitmapY;
    private int mViewWidth;
    private int mViewHeight;
    private SurfaceHolder mSurfaceHolder;

    public GameView(Context context) {
        super(context);
        init(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mSurfaceHolder = getHolder();
        mPaint = new Paint();
        mPaint.setColor(Color.DKGRAY);
        mPath = new Path();
    }

    public void pause(){
        mRunning = false;
        try {
            mGameThread.join();
        } catch (InterruptedException e) {

        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewHeight = h;
        mViewWidth = w;
        mFlashlightCone = new FlashLightCone(mViewWidth,mViewHeight);
        mPaint.setTextSize(mViewHeight/5);
        mBitmap = BitmapFactory.decodeResource(
                mContext.getResources(),R.drawable.android);
        setUpBitmap();
    }

    public void resume(){
        mRunning = true;
        mGameThread = new Thread(this);
        mGameThread.start();
    }

    private void setUpBitmap() {
        mBitmapX = (int) Math.floor(Math.random() * (mViewWidth - mBitmap.getWidth()));
        mBitmapY = (int) Math.floor(Math.random() * (mViewHeight - mBitmap.getHeight()));
        mWinnerRect = new RectF(mBitmapX, mBitmapY,
                mBitmapX + mBitmap.getWidth(),
                mBitmapY + mBitmap.getHeight());
    }

    @Override
    public void run() {
        Canvas canvas;
        while (mRunning) {
            if(mSurfaceHolder.getSurface().isValid()) {
                int x = mFlashlightCone.getX();
                int y = mFlashlightCone.getY();
                int radius = mFlashlightCone.getRadius();

                canvas = mSurfaceHolder.lockCanvas();
                canvas.save();
                canvas.drawColor(Color.WHITE);
                canvas.drawBitmap(mBitmap,mBitmapX, mBitmapY,mPaint);
                mPath.addCircle(x,y,radius,Path.Direction.CCW);
                canvas.clipPath(mPath, Region.Op.DIFFERENCE);
                canvas.drawColor(Color.BLACK);
                if(x>mWinnerRect.left && x < mWinnerRect.right && y > mWinnerRect.top && y < mWinnerRect.bottom) {
                    canvas.drawColor(Color.WHITE);
                    canvas.drawBitmap(mBitmap,mBitmapX, mBitmapY, mPaint);
                    canvas.drawText("WIN", mViewWidth / 3, mViewHeight / 2, mPaint);

                }
                mPath.rewind();
                canvas.restore();
                mSurfaceHolder.unlockCanvasAndPost(canvas);

            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setUpBitmap();;
                updateFrame((int) x, (int) y );
                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:
                updateFrame((int) x, (int) y);
                invalidate();
                break;

            default:

        }

        return true;
    }

    private void updateFrame(int newX, int newY) {
        mFlashlightCone.update(newX, newY);
    }

}
