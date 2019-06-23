package com.example.csolter3.a2048clone.sprites;

import android.graphics.Canvas;

import com.example.csolter3.a2048clone.TileManagerCallback;

public class Tile implements Sprite {

    private int screenWidth, screenHeight, standardSize;
    private TileManagerCallback callback;
    private int count = 3;

    public Tile(int standardSize, int screenWidth, int screenHeight, TileManagerCallback callback){
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        this.standardSize = standardSize;
        this.callback = callback;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(callback.getBitmap(count), screenWidth / 2 - 2 * standardSize, screenHeight / 2 - 2 * standardSize, null);
    }

    @Override
    public void update() {

    }
}
