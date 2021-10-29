package com.joseduarte.dwssurviveallyoucan.game;

import android.widget.ProgressBar;

import com.joseduarte.dwssurviveallyoucan.GameActivity;
import com.joseduarte.dwssurviveallyoucan.entities.EntitiesManager;
import com.joseduarte.dwssurviveallyoucan.entities.EntityFactory;
import com.joseduarte.dwssurviveallyoucan.entities.EntitySpawner;
import com.joseduarte.dwssurviveallyoucan.entities.Player;
import com.joseduarte.dwssurviveallyoucan.graphics.Screen;
import com.joseduarte.dwssurviveallyoucan.util.CollisionHandler;
import com.joseduarte.dwssurviveallyoucan.util.GameInformation;
import com.joseduarte.dwssurviveallyoucan.util.GlobalInformation;

import java.util.concurrent.TimeUnit;

public class Game {

    private GameActivity activity;

    private Screen screen;
    private Player player;
    private Thread playerThread;
    private CollisionHandler collisionHandler;
    private EntitySpawner entitySpawner;

    private SynchronizerPauseMenu synchronizer;

    private int coins;
    private int gameTime;

    private boolean gameOver;
    private String currentTime;

    private Runnable timerRunnable;
    private Thread timer;
    private boolean timerIsPaused;
    private boolean gameIsPaused;

    public Game(GameActivity activity) {
        this.activity = activity;
        this.synchronizer = new SynchronizerPauseMenu();
        initGame();
    }

    private void initGame() {
        gameOver = false;

        screen = new Screen(this, activity);
        if (GameInformation.screen != screen)
            GameInformation.screen = screen;

        player = EntityFactory.buildPlayer(this, screen, GlobalInformation.USERNAME);
        if (GameInformation.player != player)
            GameInformation.player = player;

        playerThread = player.getPlayerThread();
        if (GameInformation.playerThread != playerThread)
            GameInformation.playerThread = playerThread;

        collisionHandler = new CollisionHandler(this);
        if (GameInformation.collisionHandler != collisionHandler)
            GameInformation.collisionHandler = collisionHandler;

        entitySpawner = new EntitySpawner(this);
        if (GameInformation.entitySpawner != entitySpawner)
            GameInformation.entitySpawner = entitySpawner;

        EntitiesManager.clear();

        coins = 0;
        gameTime = GlobalInformation.selectedTime;
        currentTime = gameTime/60+":"+gameTime%60;

        timerRunnable = new Runnable() {
            @Override
            public void run() {
                for (int i = gameTime;i >= 0; i--) {
                    if(timerRunnable != this) return;

                    try {
                        TimeUnit.SECONDS.sleep(1);
                    }
                    catch (InterruptedException e) {}

                    changeTimeRemaining(i);

                    if(timerIsPaused) {
                        synchronized(synchronizer) {
                            while (timerIsPaused) {
                                try {synchronizer.wait();}
                                catch (Exception e) {}
                            }
                        }
                    }
                }

                if(timerRunnable == this) {
                    try {
                        over();
                    } catch (Exception e) {}
                }
            }

        };

        timer = new Thread(timerRunnable);
    }

    public void addCoins(int points) {
        coins += points;
        activity.setCoinsCount(coins);
    }

    public int getCoins() {
        return coins;
    }

    public void restart() {
        stop();
        initGame();
        start();
    }

    public void start() {
        timer.start();
        screen.startLoop();
        playerThread.start();
    }

    public void stop(ProgressBar progressBar) {
        timerRunnable = null;
        progressBar.setProgress(progressBar.getProgress()+1);

        screen.getLoop().stopLoop();
        progressBar.setProgress(progressBar.getProgress()+1);

        player.stopRun();
        progressBar.setProgress(progressBar.getProgress()+1);

        try {
            timer.join(50);
            progressBar.setProgress(progressBar.getProgress()+1);

            screen.getLoop().join(500);
            progressBar.setProgress(progressBar.getProgress()+1);

            playerThread.join(450);
            progressBar.setProgress(progressBar.getProgress()+1);
        }
        catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void stop() {
        timerRunnable = null;
        screen.getLoop().stopLoop();
        player.stopRun();

        if(gameIsPaused) {
            resume();
        }

        try {
            timer.join(1000);
            screen.getLoop().join(1000);
            playerThread.join(1000);
        }
        catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void pause() {
        synchronized (synchronizer) {
            timerIsPaused = true;
            screen.getLoop().pause();
            player.pause();
            gameIsPaused = true;
        }
    }

    public void resume() {
        try {
            synchronized (synchronizer) {
                timerIsPaused = false;
                screen.getLoop().resumePause();
                player.resume();
                synchronizer.notifyThreads();
                entitySpawner.restartLastEnemySpawnTime();
                gameIsPaused = false;
            }
        }catch (Exception e) {}
    }

    public SynchronizerPauseMenu getSynchronizer() {
        return synchronizer;
    }

    public Screen getScreen() {
        return screen;
    }

    public void over() {
        gameOver = true;
        activity.endView();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Player getPlayer() {
        return player;
    }

    public CollisionHandler getCollisionHandler() {
        return collisionHandler;
    }

    public EntitySpawner getEntitySpawner() {
        return entitySpawner;
    }

    public void clear() {
        GameInformation.screen = null;
        GameInformation.collisionHandler = null;
        GameInformation.entitySpawner = null;
        GameInformation.player = null;
        GameInformation.playerThread = null;
    }

    public void changeTimeRemaining(final int secondsRemaining) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                currentTime = secondsRemaining/60+":"+secondsRemaining%60;

                if(secondsRemaining <= 0) {
                    if(!gameOver) {
                        over();
                        currentTime = "00:00";
                    }
                }

                activity.setCountdown(currentTime);
            }
        }).start();
    }

    public String formatTimeString(int timeInSeconds) {
        return timeInSeconds/60+":"+timeInSeconds%60;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public boolean isPaused() {
        return gameIsPaused;
    }
}
