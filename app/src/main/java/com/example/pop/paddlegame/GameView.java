package com.example.pop.paddlegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.graphics.Color.MAGENTA;


/**
 * Created by kavya on 17/9/17.
 */

public class GameView extends SurfaceView implements Runnable {

    // game related variables
    volatile boolean playing;
    private Thread gameThread = null;
    boolean isGameOver = false;
    int score = 0;
    int lives = 3;

    int screenX, screenY;
    SurfaceHolder ourHolder;
    Canvas canvas;
    Paint paint;
    long fps = 20;

    // Up to 200 bricks
    Brick[] bricks = new Brick[200];
    int num_bricks = 0;
    int numberBricks1;
    int numberBricks2;
    int numberRows;

    // Game paddle and ball
    Paddle paddle;
    Ball ball;
    int ballRadius;

    public GameView(Context context, int screenX, int screenY) {
        super(context);
        isGameOver = false;
        playing = false;
        this.screenX = screenX;
        this.screenY = screenY;

        Random random = new Random();

        numberBricks1 = 7 + random.nextInt(6);
        numberBricks2 = 7 + random.nextInt(6);
        numberRows = 4 + random.nextInt(2);

        ourHolder = getHolder();
        paint = new Paint();
        paddle = new Paddle(screenX, screenY);
        ballRadius = screenX / 70;
        ball = new Ball(paddle, ballRadius, screenX, screenY);

        createBricksAndRestart();
    }

    public void createBricksAndRestart() {

        int brickWidth1 = screenX / numberBricks1, brickWidth2 = screenX / numberBricks2;
        int brickHeight = screenY / 13;

        Random random = new Random();

        List<Integer> colors = new ArrayList<>();
        colors.add(Color.MAGENTA);
        colors.add(Color.BLUE);
        colors.add(Color.CYAN);
        colors.add(Color.YELLOW);

        // Build a wall of bricks
        num_bricks = 0;
        for (int row = 1; row <= numberRows; row++) {
            if (row % 2 == 0) {
                for (int column = 0; column < numberBricks1; column++) {
                    int color = random.nextInt(2);
                    bricks[num_bricks] = new Brick(row, column, brickWidth1, brickHeight, colors.get(color));
                    num_bricks++;
                }
            } else {
                for (int column = 0; column < numberBricks2; column++) {
                    int color = 2 + random.nextInt(2);
                    bricks[num_bricks] = new Brick(row, column, brickWidth2, brickHeight, colors.get(color));
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
                score += 1;
                if(score == num_bricks){
                    // All the blocks have been cleared.
                    playing = false;
                    isGameOver = true;
                }
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
            lives--;
            if(lives == 0) {
                playing = false;
                isGameOver = true;
            }
            paddle.reset();
            ball.reset(paddle);
            this.pause();
        }
    }

    public void draw() {

        if (ourHolder.getSurface().isValid()) {
            canvas = ourHolder.lockCanvas();

            // Draw the background color
            canvas.drawColor(Color.argb(255, 0, 0, 0));

            // Define brush colors for bricks
            // Draw bricks
            int i = 0;
            for (int row = 1; row <= numberRows; row++) {
                if (row % 2 == 0) {
                    for (int column = 0; column < numberBricks1; column++) {
                        if (bricks[i].getVisibility()) {
                            paint.setColor(bricks[i].color);
                            canvas.drawRect(bricks[i].getRectF(), paint);
                        }
                        i++;
                    }
                } else {
                    for (int column = 0; column < numberBricks2; column++) {
                        if (bricks[i].getVisibility()) {
                            paint.setColor(bricks[i].color);
                            canvas.drawRect(bricks[i].getRectF(), paint);
                        }
                        i++;
                    }
                }
            }

            // Draw the paddle
            paint.setColor(paddle.color);
            canvas.drawRect(paddle.getRectF(), paint);

            // Draw ball
            paint.setColor(ball.color);
            canvas.drawRoundRect(ball.getRectF(), ballRadius, ballRadius, paint);
//            canvas.drawRect(ball.getRectF(), paint);

            // Write number of lives in left corner
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.RED);
            paint.setTextSize(screenX / 25);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("Lives: " + lives, 3 * screenX / 25, 19 * screenY / 20, paint);

            // Write score in right corner
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.BLUE);
            paint.setTextSize(screenX / 25);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("Score: " + score, 23 * screenX / 25, 19 * screenY / 20, paint);

            if (isGameOver) {
                paint.setColor(Color.WHITE);
                paint.setTextSize(screenX / 15);
                paint.setTextAlign(Paint.Align.CENTER);
                if(score == num_bricks){
                    paint.setColor(Color.GREEN);
                    canvas.drawText("Congratulations!", screenX/2, 4*screenY/10, paint);
                    canvas.drawText("You have cleared", screenX/2, 5*screenY/10, paint);
                    canvas.drawText("all bricks", screenX/2, 6*screenY/10, paint);
                }
                else {
                    canvas.drawText("Game over!", screenX / 2, 6 * screenY / 10, paint);
                    canvas.drawText("Score: " + score, screenX / 2, 7 * screenY / 10, paint);
                }
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
                if(!isGameOver) {
                    this.resume();
                    float paddleMid = (paddle.getRectF().left + paddle.getRectF().right) / 2;
                    if (motionEvent.getX() > paddleMid) {
                        paddle.movePaddle(1);
                    } else paddle.movePaddle(2);
                    break;
                }
            case MotionEvent.ACTION_UP:
                paddle.movePaddle(0);
                break;
        }
        return true;
    }
}
