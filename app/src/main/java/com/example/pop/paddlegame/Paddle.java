package com.example.pop.paddlegame;

import android.graphics.RectF;
import android.graphics.drawable.shapes.RectShape;

/**
 * Created by kavya on 17/9/17.
 */

public class Paddle extends GameObject {
    public Paddle(int screenWidth, int screenHeight){
        super(20, 130);
        float left= screenWidth/2;
        float top= screenHeight - 30;

        rectF = new RectF(left, top, left + objWidth, top + objHeight);
        speedX = 0;
        speedY = 0;
    }

    public void bounce(){
        speedX = -speedX;
    }

    public void moveRight(){
        speedX = 300;
    }

    public void moveLeft(){
        speedX = -300;
    }

    public void stop(){
        speedX = 0;
    }
}
