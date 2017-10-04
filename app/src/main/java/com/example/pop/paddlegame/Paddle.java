package com.example.pop.paddlegame;

import android.graphics.Color;

/**
 * Created by kavya on 17/9/17.
 */
public class Paddle extends GameObject {
    int rightLimit;
    int speedMax;

    float constLeft;
    float constTop;

    public Paddle(int screenWidth, int screenHeight) {
        super(screenHeight / 50, screenWidth / 8, Color.argb(255, 0, 180, 0));

        // Placing paddle with left side in the middle of the screen
        constLeft = screenWidth / 2 - objWidth / 2;
        constTop = screenHeight - screenHeight / 45;

        reset();

        // setting speeds and movement
        speedY = 0;
        speedMax = 2 * screenWidth / 9;

        // setting limit on right side movement
        rightLimit = screenWidth;
    }

    public void reset(){
        setRectF(constLeft, constTop);
        speedX = 0;
    }

    public void movePaddle(int direction) {
        switch (direction) {
            case 0:
                speedX = 0;
                break;
            case 1:
                speedX = speedMax;
                break;
            case 2:
                speedX = -speedMax;
                break;
        }
    }

    public void update(long fps) {
        if (this.getRectF().left <= 0 && speedX < 0) return;
        if (this.getRectF().right >= rightLimit && speedX > 0) return;
        super.update(fps);
    }
}
