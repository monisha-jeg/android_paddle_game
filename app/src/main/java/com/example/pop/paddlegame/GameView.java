package com.example.pop.paddlegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


/**
 * Created by kavya on 17/9/17.
 */

public class GameView extends SurfaceView implements Runnable {

    // game related variables
    volatile boolean playing;
    private Thread gameThread = null;
    private boolean isGameOver = false;

    int screenX, screenY;
    SurfaceHolder ourHolder;
    Canvas canvas;
    Paint paint;
    long fps = 17;

    // Up to 200 bricks
    Brick[] bricks = new Brick[200];
    int num_bricks = 0;
    int numberBricks1 = 8;
    int numberBricks2 = 11;

    // Game paddle and ball
    Paddle paddle;
    Ball ball;
    int ballRadius = 20;

    public GameView(Context context, int screenX, int screenY) {
        super(context);
        isGameOver = false;
        this.screenX = screenX;
        this.screenY = screenY;

        ourHolder = getHolder();
        paint = new Paint();
        paddle = new Paddle(screenX, screenY);
        ball = new Ball(paddle, ballRadius);

        createBricksAndRestart();
    }

    public void createBricksAndRestart() {

        int brickWidth1 = screenX / numberBricks1, brickWidth2 = screenX / numberBricks2;
        int brickHeight = screenY / 13;

        // Build a wall of bricks
        num_bricks = 0;
        for (int row = 0; row < 5; row++) {
            if (row % 2 == 0) {
                for (int column = 0; column < numberBricks1; column++) {
                    bricks[num_bricks] = new Brick(row, column, brickWidth1, brickHeight);
                    num_bricks++;
                }
            } else {
                for (int column = 0; column < numberBricks2; column++) {
                    bricks[num_bricks] = new Brick(row, column, brickWidth2, brickHeight);
                    num_bricks++;
                }
            }
        }

        // Reset ball position
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

    /**
     * Helper function which updates all game objects
     * Checks collision of ball.
     * Moves paddle as required
     * Removes bricks from game if ball has touched them
     */
    private void update() {
        paddle.update(fps);
        ball.update(fps);

        // Check collision of ball with brick, wall or paddle
        // 1. Collision with brick
        for (int brickIndex = 0; brickIndex < num_bricks; brickIndex++) {
            if (ball.checkCollision(bricks[brickIndex])) {
                return;
            }
        }
        // 2. Collision with paddle
        if (ball.checkCollision(paddle)) {
            return;
        }
        // 3. Collision with screen wall top, left or right.
        // Collision with bottom is counted as game over
        if (!ball.collideWithWall(screenX, screenY)) {
            playing = false;
            isGameOver = true;
        }
    }

    public void draw() {

        if (ourHolder.getSurface().isValid()) {
            canvas = ourHolder.lockCanvas();

            // Draw the background color
            canvas.drawColor(Color.argb(255, 0, 0, 0));

            // Define brush colors for bricks
            int brick_color1 = Color.argb(255, 255, 0, 0);
            int brick_color2 = Color.argb(255, 255, 255, 0);
            // Draw bricks
            int i = 0;
            for (int row = 0; row < 5; row++) {
                if (row % 2 == 0) {
                    paint.setColor(brick_color1);
                    for (int column = 0; column < numberBricks1; column++) {
                        if (bricks[i].getVisibility()) {
                            canvas.drawRect(bricks[i].getRectF(), paint);
                        }
                        i++;
                    }
                } else {
                    paint.setColor(brick_color2);
                    for (int column = 0; column < numberBricks2; column++) {
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

            // Draw ball
            paint.setColor(Color.argb(255, 255, 255, 255));
            canvas.drawRoundRect(ball.getRectF(), ballRadius, ballRadius, paint);
//            canvas.drawRect(ball.getRectF(), paint);

            if (isGameOver) {
                paint.setTextSize(150);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("Game over", 950, 700, paint);
            }

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
                float paddleMid = (paddle.getRectF().left + paddle.getRectF().right) / 2;
                if (motionEvent.getX() > paddleMid) {
                    paddle.movePaddle(1);
                } else paddle.movePaddle(2);
                break;
            case MotionEvent.ACTION_UP:
                paddle.movePaddle(0);
                break;
        }
        return true;
    }
}
