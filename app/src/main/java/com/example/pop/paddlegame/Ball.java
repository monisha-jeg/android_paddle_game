package com.example.pop.paddlegame;

/**
 * Created by moni on 25/9/17.
 */

import java.util.Random;

public class Ball extends GameObject {
    /**
     * Constructor for the ball object
     *
     * @param paddle: Since initial ball placement depends on the paddle
     */
    public Ball(Paddle paddle, int radius, int screenWidth, int screenHeight) {
        super(radius, radius);

        // Start the ball travelling left or right randomly
        Random generator = new Random();
        if (generator.nextInt(2) == 0) {
            speedX = screenWidth / 9;
        } else speedX = -screenWidth / 9;
        speedY = -screenHeight / 5;

        // set ball position on top of paddle
        this.reset(paddle);
    }

    public void reverseSpeedY() {
        speedY = -speedY;
    }

    public void reverseSpeedX() {
        speedX = -speedX;
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

    /**
     * Function to check Collision of ball with a brick
     *
     * @param brick
     * @return True if ball has collided and false if it has not
     */
    public boolean checkCollision(Brick brick) {
        // Do only if brick is in game => is visible
        if (brick.getVisibility()) {
            // check intersection of ball and brick
            if (brick.getRectF().intersect(this.getRectF())) {
                // remove brick from game
                brick.setInvisible();
                // change course of ball
                this.reverseSpeedY();
                return true;
            }
        }
        return false;
    }

    public boolean checkCollision(Paddle paddle) {
        if (paddle.getRectF().top < this.getRectF().bottom
                && paddle.getRectF().left < this.getRectF().right
                && paddle.getRectF().right > this.getRectF().left) {
            // reflect ball off paddle
            this.reverseSpeedY();
        }
        return false;
    }

    /**
     * Function which checks if ball collides with a wall and redirects it as necessary
     *
     * @param screenX
     * @param screenY
     * @return True if collision is valid, false if it collides with bottom wall in which case
     * game is over.
     */
    public boolean collideWithWall(int screenX, int screenY) {
        if (this.getRectF().left <= 0) {
            this.reverseSpeedX();
        } else if (this.getRectF().right >= screenX) {
            this.reverseSpeedX();
        } else if (this.getRectF().top <= 0) {
            this.reverseSpeedY();
        } else if (this.getRectF().bottom >= screenY) {
            // ball has fallen down, so game is over
            return false;
        }
        return true;
    }
}