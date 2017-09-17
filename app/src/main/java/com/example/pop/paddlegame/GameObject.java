package com.example.pop.paddlegame;

import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;

/**
 * Created by kavya on 17/9/17.
 */

abstract class GameObject {
    Shape shape;
    int x;
    int y;

    int speed = 0;

    public GameObject(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void draw(Canvas canvas){
        ShapeDrawable shapeDrawable = new ShapeDrawable(shape);
        shapeDrawable.draw(canvas);
    }

    public int getX(){
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
