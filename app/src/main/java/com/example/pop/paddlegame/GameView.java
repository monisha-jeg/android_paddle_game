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

    // Game paddle
    Paddle paddle;

    public GameView(Context context, int screenX, int screenY){
        super(context);
        this.screenX = screenX;
        this.screenY = screenY;

        ourHolder = getHolder();
        paint = new Paint();
        paddle = new Paddle(screenX, screenY);

        make_bricks();
    }

    public void make_bricks() {

        int brick_width = screenX / 8;
        int brick_height = screenY / 10;

        // Build a wall of bricks
        num_bricks = 0;
        for (int column = 0; column < 8; column++) {
            for (int row = 0; row < 3; row++) {
                bricks[num_bricks] = new Brick(row, column, brick_width, brick_height);
                num_bricks++;
            }
        }
    }

    @Override
    public void run(){
        while(playing){
            update();
            draw();
            control();
        }
    }

    private void update(){
        paddle.update(fps);

    }

    public void draw(){

        if (ourHolder.getSurface().isValid()) {
            canvas = ourHolder.lockCanvas();

            // Draw the background color
            canvas.drawColor(Color.argb(255, 220, 220, 220));
            // Choose the brush color for drawing
            paint.setColor(Color.argb(255, 255, 0, 0));

            // Draw the bricks if visible
            for (int i = 0; i < num_bricks; i++) {
                if (bricks[i].getVisibility()) {
                    canvas.drawRect(bricks[i].getRect(), paint);
                }
            }

            paint.setColor(Color.argb(255, 0, 255, 0));
            // Draw the paddle
            canvas.drawRect(paddle.getRectF(), paint);

            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void control(){
        try{
            gameThread.sleep(10);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void pause(){
        playing = false;
        try {
            gameThread.join();
        }
        catch(InterruptedException e){

        }
    }

    public void resume(){
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        switch(motionEvent.getAction() & MotionEvent.ACTION_MASK){
            // Player has touched the screen
            case MotionEvent.ACTION_DOWN:
                if(motionEvent.getX() > screenX / 2){
                    paddle.moveRight();
                }
                else paddle.moveLeft();
                break;
            case MotionEvent.ACTION_UP:
                paddle.stop();
                break;
        }
        return true;
    }

}
