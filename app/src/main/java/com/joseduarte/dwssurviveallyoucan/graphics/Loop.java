package com.joseduarte.dwssurviveallyoucan.graphics;

import android.graphics.Canvas;

import com.joseduarte.dwssurviveallyoucan.game.Game;
import com.joseduarte.dwssurviveallyoucan.game.SynchronizerPauseMenu;
import com.joseduarte.dwssurviveallyoucan.util.GlobalInformation;

import java.util.concurrent.TimeUnit;

public class Loop extends Thread {

    private Game game;
    private Screen screen;
    private boolean running = false;
    private boolean isPaused = false;

    public Loop(Screen screen, Game game) {
        this.game = game;
        this.screen = screen;
    }

    public void startLoop() {
        running = true;
        System.out.println(getState());
        start();
    }

    public void stopLoop() {
        running = false;
    }

    @Override
    public void run() {
        final double nanosecPerUpdate = 1000000000 / GlobalInformation.FPS;
        long rACL = System.nanoTime();

        double timeLapsed;
        double delta = 0.0d;

        while (running) {

            final long initLoop = System.nanoTime();
            timeLapsed = initLoop - rACL;
            rACL = initLoop;

            delta += timeLapsed / nanosecPerUpdate;

            while (running && delta >= 1) {
                screen.update();

                Canvas canvas = null;
                try {
                    canvas = screen.getHolder().lockCanvas();
                    synchronized (screen.getHolder()) {
                        screen.drawIt(canvas);
                    }
                } finally {
                    if (canvas != null) {
                        screen.getHolder().unlockCanvasAndPost(canvas);
                    }
                }
                delta--;

                if(isPaused) {
                    SynchronizerPauseMenu synchronizer = game.getSynchronizer();
                    synchronized(synchronizer) {
                        while (isPaused) {
                            try {synchronizer.wait();}
                            catch (Exception e) {}
                        }
                    }
                }
            }
        }
    }

    public void pause() {
        isPaused = true;
    }

    public void resumePause() {
        isPaused = false;
    }
}
