package com.example.pop.paddlegame;
import android.graphics.Color;
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

    // Up to 200 bricks
    Brick[] bricks = new Brick[200];
    int num_bricks = 0;



    public GameView(Context context, int screenX, int screenY){
        super(context);
        this.screenX = screenX;
        this.screenY = screenY;

        ourHolder = getHolder();
        paint = new Paint();

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

}
