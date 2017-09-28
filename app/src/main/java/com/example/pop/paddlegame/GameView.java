package com.example.pop.paddlegame;

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;


/**
 * Created by kavya on 17/9/17.
 */

public class GameView extends SurfaceView implements Runnable {
    volatile boolean playing;

    private Thread gameThread = null;

    int screenX, screenY;
    SurfaceHolder ourHolder;
    Canvas canvas;
    Paint paint;
    long fps = 17;

    // Up to 200 bricks
    Brick[] bricks = new Brick[200];
    int num_bricks = 0;

    // Game paddle and ball
    Paddle paddle;
    Ball ball;

    public GameView(Context context, int screenX, int screenY) {
        super(context);
        this.screenX = screenX;
        this.screenY = screenY;

        ourHolder = getHolder();
        paint = new Paint();
        paddle = new Paddle(screenX, screenY);
        ball = new Ball(paddle);

        createBricksAndRestart();
    }


    public void createBricksAndRestart() {

        int brick_width0 = screenX / 8, brick_width1 = screenX / 11;
        int brick_height = screenY / 13;

        // Build a wall of bricks
        num_bricks = 0;
        for (int row = 0; row < 5; row++) {
            if (row % 2 == 0) {
                for (int column = 0; column < 8; column++) {
                    bricks[num_bricks] = new Brick(row, column, brick_width0, brick_height);
                    num_bricks++;
                }
            } else {
                for (int column = 0; column < 11; column++) {
                    bricks[num_bricks] = new Brick(row, column, brick_width1, brick_height);
                    num_bricks++;
                }
            }
        }

        // Put the ball back to the start
        ball.reset(paddle);

    }

    @Override
    public void run() {
        while (playing) {
            update();
            draw();
            control();
        }
    }

    private void update() {
        paddle.update(fps);
        ball.update(fps);
    }

    public void draw() {

        if (ourHolder.getSurface().isValid()) {
            canvas = ourHolder.lockCanvas();

            // Draw the background color
            canvas.drawColor(Color.argb(255, 0, 0, 0));

            // Define brush colors for bricks
            int brick_color1 = Color.argb(255, 255, 0, 0);
            int brick_color2 = Color.argb(255, 255, 255, 0);
            //Draw bricks
            int i = 0;
            for (int row = 0; row < 5; row++) {
                if (row % 2 == 0) {
                    paint.setColor(brick_color1);
                    for (int column = 0; column < 8; column++) {
                        if (bricks[i].getVisibility()) {
                            canvas.drawRect(bricks[i].getRectF(), paint);
                            i++;
                        }

                    }
                } else {
                    paint.setColor(brick_color2);
                    for (int column = 0; column < 11; column++) {
                        if (bricks[i].getVisibility()) {
                            canvas.drawRect(bricks[i].getRectF(), paint);
                        }
                        i++;
                    }
                }


            }

            // Draw the paddle
            paint.setColor(Color.argb(255, 0, 255, 0));
            canvas.drawRect(paddle.getRectF(), paint);

            //Draw ball
            paint.setColor(Color.argb(255, 255, 255, 255));
            canvas.drawRect(ball.getRectF(), paint);

            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void control() {
        try {
            gameThread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {

        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            // Player has touched the screen
            case MotionEvent.ACTION_DOWN:
                if (motionEvent.getX() > screenX / 2) {
                    paddle.moveRight();
                } else paddle.moveLeft();
                break;
            case MotionEvent.ACTION_UP:
                paddle.stop();
                break;
        }
        return true;
    }


}
