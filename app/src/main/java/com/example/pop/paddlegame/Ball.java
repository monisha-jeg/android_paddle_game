package com.example.pop.paddlegame;

/**
 * Created by moni on 25/9/17.
 */

import android.graphics.RectF;

import java.util.Random;

public class Ball extends GameObject {
    /**
     * Constructor for the ball object
     *
     * @param paddle: Since initial ball placement depends on the paddle
     */
    public Ball(Paddle paddle) {
        super(20, 20);

        // Start the ball travelling straight up at 100 pixels per second
//        speedX = 200;
//        speedY = -400;

        // set ball position on top of paddle
        this.reset(paddle);
    }

    public void reverseYVelocity() {
        speedY = -speedY;
    }

    public void reverseXVelocity() {
        speedX = -speedX;
    }

    public void setRandomXVelocity() {
        Random generator = new Random();
        int answer = generator.nextInt(2);

        if (answer == 0) {
            reverseXVelocity();
        }
    }

    public void clearObstacleY(float y) {
        rectF.bottom = y;
        rectF.top = y - objHeight;
    }

    public void clearObstacleX(float x) {
        rectF.left = x;
        rectF.right = x + objWidth;
    }

    /**
     * Resets the ball to start position, which is on top of the paddle
     *
     * @param paddle
     */
    public void reset(Paddle paddle) {
        float left = (paddle.getRectF().left + paddle.getRectF().right) / 2 - objWidth / 2;
        float bottom = paddle.getRectF().top;

        // Placing the ball at the top of the paddle
        setRectF(left, bottom - objHeight);
    }

}