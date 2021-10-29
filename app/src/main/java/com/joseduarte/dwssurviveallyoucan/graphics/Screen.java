package com.joseduarte.dwssurviveallyoucan.graphics;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.constraintlayout.solver.widgets.Rectangle;

import com.joseduarte.dwssurviveallyoucan.R;
import com.joseduarte.dwssurviveallyoucan.entities.EntitiesManager;
import com.joseduarte.dwssurviveallyoucan.game.Game;
import com.joseduarte.dwssurviveallyoucan.util.GameInformation;
import com.joseduarte.dwssurviveallyoucan.util.GlobalInformation;
import com.joseduarte.dwssurviveallyoucan.util.ResourceManager;

public class Screen extends SurfaceView {

    public static final int CENTER_SIDE = 0;
    public static final int LEFT_SIDE = 1;
    public static final int RIGHT_SIDE = 2;

    public static int getInverseSide(int side) {
        switch (side){
            case LEFT_SIDE:
                return RIGHT_SIDE;
            case RIGHT_SIDE:
                return LEFT_SIDE;
        }
        return -1;
    }

    private Context context;
    private Game game;
    private Rectangle resolution = null;

    private Loop loop;
    private SurfaceHolder holder;

    private Rect borderRect;
    private Drawable image;

    public Screen(Game game, Context context) {
        super(context);
        this.context = context;
        this.game = game;
        resolution = calcResolution();
        image = ResourceManager.loadBackground(R.mipmap.scene_game_background);

        GameInformation.screen = this;
    }

    public void destroyLoop() {
        loop.stopLoop();
        loop = null;
        holder = null;
    }

    public void startLoop() {
        if(loop != null) return;

        loop = new Loop(this, game);

        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                System.out.println(loop.getState());

                if(loop.getState() == Thread.State.TERMINATED)
                    loop = new Loop(Screen.this, game);

                loop.startLoop();

                if(game.isPaused()) loop.pause();
                System.out.println(loop.getState());
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;

                if(loop != null) {
                    loop.stopLoop();
                    while (retry) {
                        try {
                            loop.join();
                            retry = false;
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }
        });

    }

    public void restartLoop() {

    }

    private Rectangle calcResolution() {
        Rectangle rectangle = new Rectangle();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);

        rectangle.setBounds(
                0,
                0,
                displayMetrics.widthPixels,
                displayMetrics.heightPixels
        );

        return rectangle;
    }

    public Point getSidePosition(int side) {
        switch (side) {
            case CENTER_SIDE:
                return new Point(
                        resolution.getCenterX(),
                        0
                );
            case LEFT_SIDE:
                return new Point(
                        resolution.x - (resolution.width/10),
                        0
                );
            default:
                return new Point(
                        (resolution.x + resolution.width) + (resolution.width/10),
                        0
                );
        }
    }

    public int getResolutionWidth() {
        return resolution.width;
    }

    public int getResolutionHeight() {
        return resolution.height;
    }

    public Rectangle getResolution() {
        return this.resolution;
    }

    public Rect getBorderRect() {
        return borderRect;
    }

    public int getBorderWidth() {
        return 25/2;
    }

    public void update() {
        if(!game.isGameOver()) {
            game.getCollisionHandler().update();
            EntitiesManager.updateEntities();
            game.getEntitySpawner().update();
        }
    }

    public void drawIt(Canvas canvas) {
        if (canvas != null) {
            borderRect = new Rect(
                    resolution.x,
                    resolution.y,
                    resolution.width,
                    resolution.height
            );

            image.setBounds(borderRect);
            image.draw(canvas);

            //if(!game.isGameOver())
            EntitiesManager.drawEntities(canvas);
        }
    }

    public Loop getLoop() {
        return loop;
    }
}