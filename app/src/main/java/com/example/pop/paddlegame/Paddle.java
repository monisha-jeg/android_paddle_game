package com.example.pop.paddlegame;

import android.graphics.RectF;
import android.graphics.drawable.shapes.RectShape;

import static android.R.transition.move;

/**
 * Created by kavya on 17/9/17.
 */
public class Paddle extends GameObject {
    int rightLimit;
    public Paddle(int screenWidth, int screenHeight) {
        super(20, 130);

        // Placing paddle with left side in the middle of the screen
        float left = screenWidth / 2 - objWidth/2;
        float top = screenHeight - 30;
        setRectF(left, top);

        // setting speeds and movement
        speedX = 400;
        speedY = 0;

        // setting limit on right side movement
        rightLimit = screenWidth;
    }

    public void movePaddle(int direction){
        switch(direction){
            case 0:
                speedX = 0;
                break;
            case 1:
                speedX = 400;
                break;
            case 2:
                speedX = -400;
                break;
        }
    }

    public void update(long fps){
        if(this.getRectF().left <= 0 && speedX < 0) return;
        if(this.getRectF().right >= rightLimit && speedX > 0) return;
        super.update(fps);
    }
}
