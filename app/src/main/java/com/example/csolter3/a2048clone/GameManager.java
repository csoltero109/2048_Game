package com.example.csolter3.a2048clone;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.csolter3.a2048clone.sprites.EndGame;
import com.example.csolter3.a2048clone.sprites.Grid;
import com.example.csolter3.a2048clone.sprites.Score;

public class GameManager extends SurfaceView implements SurfaceHolder.Callback, SwipeCallback, GameManagerCallback {


    private static final String APP_NAME = "2048 Game";
    private MainThread thread;
    private Grid grid;
    private int scWidth, scHeight, standardSize;
    private TileManager tileManager;
    private boolean endGame = false;
    private EndGame endGameSprite;
    private Score score;
    private SwipeListener swipe;
    private Bitmap restartButton;
    private int restartButtonX,restartButtonY, restartButtonSize;

    public GameManager (Context context, AttributeSet attrs){
        super(context,attrs);
        setLongClickable(true);
        getHolder().addCallback(this);
        swipe = new SwipeListener(getContext(), this);

        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        scWidth = dm.widthPixels;
        scHeight = dm.heightPixels;
        standardSize = (int) (scWidth * .88) / 4;

        grid = new Grid(getResources(), scWidth, scHeight, standardSize);

        tileManager = new TileManager(getResources(), standardSize, scWidth, scHeight, this);
        endGameSprite = new EndGame(getResources(), scWidth, scHeight);
        score = new Score(getResources(), scWidth, scHeight, standardSize, getContext().getSharedPreferences(APP_NAME,Context.MODE_PRIVATE));

        restartButtonSize = (int) getResources().getDimension(R.dimen.restart_button_size);
        Bitmap bmpRestart = BitmapFactory.decodeResource(getResources(), R.drawable.restart);
        restartButton = Bitmap.createScaledBitmap(bmpRestart, restartButtonSize, restartButtonSize, false);
        restartButtonX = scWidth / 2 + 2 * standardSize - restartButtonSize;
        restartButtonY = scHeight / 2 - 2 * standardSize - 3 * restartButtonSize / 2;
    }

    public void initGame(){
        endGame = false;
        tileManager.initGame();
        score = new Score(getResources(), scWidth, scHeight, standardSize, getContext().getSharedPreferences(APP_NAME,Context.MODE_PRIVATE));
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new MainThread(holder,this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        thread.setSurfaceHolder(holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while(retry){
            try{
                thread.setRunning(false);
                thread.join();
                retry = false;
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public void update(){
        if(!endGame) {
            tileManager.update();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawRGB(255,255,255);
        grid.draw(canvas);
        tileManager.draw(canvas);
        score.draw(canvas);
        canvas.drawBitmap(restartButton, restartButtonX, restartButtonY, null);
        if(endGame){
            endGameSprite.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(endGame){
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                initGame();
            }
        }else{
            float eventX = event.getAxisValue(MotionEvent.AXIS_X);
            float eventY = event.getAxisValue(MotionEvent.AXIS_Y);
            if(event.getAction() == MotionEvent.ACTION_DOWN &&
                    eventX > restartButtonX && eventX < restartButtonX + restartButtonSize &&
                    eventY > restartButtonY && eventY < restartButtonY + restartButtonSize){
                initGame();
            }else {
                swipe.onTouchEvent(event);
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onSwipe(Direction direction) {
        tileManager.onSwipe(direction);
    }

    @Override
    public void gameOver(){
        endGame = true;
    }

    @Override
    public void updateScore(int delta){
        score.updateScore(delta);
    }

    @Override
    public void reached2048() {
        score.reached2048();
    }
}
