package com.example.danceciliochua.surfaceviewexample;

public class FlashLightCone {

    private int mX;
    private int mY;
    private int mRadius;

    public FlashLightCone(int viewWidth, int viewHeight) {
        mX = viewWidth / 2;
        mY = viewHeight / 2;
        mRadius = ((viewWidth <= viewHeight) ? mX / 3 : mY / 3);
    }

    public void update(int newX, int newY) {
        mX = newX;
        mY = newY;
    }

    public int getX() {
        return mX;

    }

    public int getY() {
        return mY;

    }

    public int getRadius() {
        return mRadius;

    }

}
