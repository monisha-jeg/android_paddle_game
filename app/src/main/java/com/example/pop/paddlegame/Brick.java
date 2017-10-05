package com.example.pop.paddlegame;

import android.graphics.RectF;

/**
 * Created by moni on 19/9/17.
 */

public class Brick extends GameObject {

    // using this variable to not display after ball has touched a brick
    private boolean isVisible;

    public Brick(int row, int column, int width, int height, int color, int screenWidth, int screenHeight) {
        super(height, width, color);
        isVisible = true;

        int paddingX = screenWidth / 250;
        int paddingY = screenHeight / 200;

        rectF = new RectF(column * width + paddingX,
                row * height + paddingY,
                column * width + width - paddingX,
                row * height + height - paddingY);
    }

    public void setInvisible() {
        isVisible = false;
    }

    public boolean getVisibility() {
        return isVisible;
    }
}
