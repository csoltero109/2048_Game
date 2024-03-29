package com.example.csolter3.a2048clone.sprites;

import android.graphics.Canvas;

import com.example.csolter3.a2048clone.TileManagerCallback;

import java.util.Random;

public class Tile implements Sprite {

    private int screenWidth, screenHeight, standardSize;
    private TileManagerCallback callback;
    private int count = 1;
    private int currentX, currentY;
    private int destX, destY;
    private boolean moving = false;
    private int speed = 200;
    private boolean increment = false;


    public Tile(int standardSize, int screenWidth, int screenHeight, TileManagerCallback callback, int matrixX, int matrixY, int count){
        this(standardSize, screenWidth, screenHeight, callback, matrixX, matrixY );
        this.count = count;
    }


    public Tile(int standardSize, int screenWidth, int screenHeight, TileManagerCallback callback, int matrixX, int matrixY){
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        this.standardSize = standardSize;
        this.callback = callback;
        currentX = destX = screenWidth / 2 - 2 * standardSize + matrixY * standardSize;
        currentY = destY = screenHeight / 2 - 2 * standardSize + matrixX * standardSize;
        int chance = new Random().nextInt(100);
        if(chance >= 90){
            count = 2;
        }
    }

    public void move(int matrixX, int matrixY){
        moving = true;
        destX = screenWidth / 2 - 2 * standardSize + matrixY * standardSize;
        destY = screenHeight / 2 - 2 * standardSize + matrixX * standardSize;
    }

    public int getValue(){
        return count;
    }

    public Tile increment(){
        increment = true;
        return this;
    }

    public boolean toIncrement(){
        return increment;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(callback.getBitmap(count), currentX, currentY, null);
        if(moving && currentX == destX && currentY == destY){
            moving = false;
            if(increment){
                count++;
                increment = false;
                int amount = (int) Math.pow(2, count);
                callback.updateScore(amount);
                if(count == 11){
                    callback.reached2048();
                }
            }
            callback.finishedMoving(this);
        }
    }

    @Override
    public void update() {
        if(currentX < destX){
            if(currentX + speed > destX){
                currentX = destX;
            }
            else{
                currentX += speed;
            }
        }
        else if(currentX > destX){
            if(currentX - speed < destX){
                currentX = destX;
            }
            else{
                currentX -= speed;
            }
        }

        if(currentY < destY){
            if(currentY + speed > destY){
                currentY = destY;
            }
            else{
                currentY += speed;
            }
        }
        else if(currentY > destY){
            if(currentY - speed < destY){
                currentY = destY;
            }
            else{
                currentY -= speed;
            }
        }
    }
}
