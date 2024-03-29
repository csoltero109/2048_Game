package com.example.csolter3.a2048clone.sprites;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.csolter3.a2048clone.R;

public class EndGame implements Sprite {

    private int screenWidth, screenHeight;
    private Bitmap bmp;

    public EndGame(Resources resources, int screenWidth, int screenHeight){
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        int endGameWidth = (int)resources.getDimension(R.dimen.endgame_width);
        int endGameHeight = (int)resources.getDimension(R.dimen.endgame_height);

        Bitmap b = BitmapFactory.decodeResource(resources, R.drawable.gameover);
        bmp = Bitmap.createScaledBitmap(b, endGameWidth, endGameHeight, false);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bmp, screenWidth/2 - bmp.getWidth()/2, screenHeight/2 - bmp.getHeight()/2, null);
    }

    @Override
    public void update() {

    }
}
