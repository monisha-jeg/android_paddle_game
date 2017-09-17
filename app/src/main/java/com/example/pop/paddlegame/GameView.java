package com.example.pop.paddlegame;

import android.view.SurfaceView;
import android.content.Context;

/**
 * Created by kavya on 17/9/17.
 */

public class GameView extends SurfaceView implements Runnable {
    volatile boolean playing;

    private Thread gameThread = null;

    public GameView(Context context){
        super(context);
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
