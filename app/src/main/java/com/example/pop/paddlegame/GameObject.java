package com.example.pop.paddlegame;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;

import static android.R.attr.x;
import static android.R.attr.y;
import static android.view.View.X;

/**
 * Created by kavya on 17/9/17.
 */

abstract class GameObject {
    // Representing all objects as rectangles
    RectF rectF;
    float speedX = 0;
    float speedY = 0;

    float objHeight;
    float objWidth;

    public GameObject(int height, int width) {
        objHeight = height;
        objWidth = width;
        rectF = new RectF();
        speedX = 0;
        speedY = 0;
    }

    public void update(long fps) {
        rectF.left = rectF.left + speedX / fps;
        rectF.top = rectF.top + speedY / fps;
        rectF.right = rectF.left + objWidth;
        rectF.bottom = rectF.top + objHeight;
    }

    public RectF getRectF() {
        return this.rectF;
    }

    public void reset() {
    }

    public void setRectF(float left, float top){
        rectF = new RectF(left, top, left + objWidth, top + objHeight);
    }

}
