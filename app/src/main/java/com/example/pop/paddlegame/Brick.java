package com.example.pop.paddlegame;

import android.graphics.RectF;

/**
 * Created by moni on 19/9/17.
 */

public class Brick extends GameObject {

    // using this variable to not display after ball has touched a brick
    private boolean isVisible;

    public Brick(int row, int column, int width, int height, int color) {
        super(height, width, color);
        isVisible = true;

        int padding = 1;

        rectF = new RectF(column * width + padding,
                row * height + padding,
                column * width + width - padding,
                row * height + height - padding);
    }

    public void setInvisible() {
        isVisible = false;
    }

    public boolean getVisibility() {
        return isVisible;
    }
}
