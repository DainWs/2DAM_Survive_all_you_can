package com.joseduarte.dwssurviveallyoucan.entities;

import android.graphics.Canvas;
import android.graphics.Point;

import com.joseduarte.dwssurviveallyoucan.R;
import com.joseduarte.dwssurviveallyoucan.game.Game;
import com.joseduarte.dwssurviveallyoucan.game.SynchronizerPauseMenu;
import com.joseduarte.dwssurviveallyoucan.graphics.EffectSprite;
import com.joseduarte.dwssurviveallyoucan.graphics.Screen;
import com.joseduarte.dwssurviveallyoucan.graphics.Sprite;
import com.joseduarte.dwssurviveallyoucan.graphics.effects.AttackEffect;
import com.joseduarte.dwssurviveallyoucan.physics.action.Attack;
import com.joseduarte.dwssurviveallyoucan.util.GameInformation;
import com.joseduarte.dwssurviveallyoucan.util.GlobalInformation;
import com.joseduarte.dwssurviveallyoucan.util.ResourceManager;

/**
 *
 * @author Jose-DainWs
 */
public class Player extends Entity implements Runnable {

    private final String name;
    private final Sprite attackSprite;
    private Sprite drawingSprite;

    private Thread playerThread;

    private boolean running;
    private boolean isPaused;

    private Player(PlayerBuilder builder) {
        super(builder.game, builder.sprite);
        name = builder.name;

        attackSprite = builder.attackSprite;
        this.attackSprite.setEntity(this);
        drawingSprite = builder.sprite;

        attack = new Attack(this, Screen.RIGHT_SIDE);
        side = Screen.RIGHT_SIDE;

        playerThread = new Thread(this);
        if(GameInformation.playerThread != playerThread)
            GameInformation.playerThread = playerThread;

        running = true;
        isPaused = false;

        GameInformation.player = this;
    }




    public void attack(Entity entity, int side) {
        isAttacking = true;
        drawingSprite = attackSprite;
        this.side = side;
        attackingTime = 0;

        if(entity != null) {
            attack.attackTo(entity, side);
        }
    }

    @Override
    public void update() {
        if(!isAlive) {
            die();
            running = false;
            return;
        }

        speed = GlobalInformation.QA_HORIZONTAL_SPEED;

        int moveXDistance = 0;
        if(!isAttacking && (speed > 1 || speed < -1)) {
            moveXDistance = (int) (speed*2);

            if(moveXDistance < -6) moveXDistance = -6;
            if(moveXDistance > 6) moveXDistance = 6;

            if (speed > 0) side = Screen.RIGHT_SIDE;
            else side = Screen.LEFT_SIDE;

            sprite.isMoving(true);
        }
        else sprite.isMoving(false);

        boolean leftSide = (sprite.getPosition().x + moveXDistance) >= 0;
        boolean rightSide = (sprite.getPosition().x + sprite.getBounds().getWidth()) + moveXDistance <= game.getScreen().getResolutionWidth();

        if( leftSide && rightSide) {
            sprite.update(moveXDistance);
            attackSprite.update(moveXDistance);
        }

        for (EffectSprite effect : effects) {
            effect.update();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        drawingSprite.draw(canvas);

        if((isAttacking || isBeingAttacked) && ++attackingTime >= 1) {
            drawingSprite = sprite;
            AttackEffect.removeEffectFromEntity(this);
            isAttacking = false;
        }
    }

    @Override
    public Sprite getSprite() {
        return drawingSprite;
    }

    @Override
    protected void die() {
        game.over();
    }

    public Thread getPlayerThread() {
        return playerThread;
    }

    public void stopRun() {
        running = false;
    }

    public void run() {
        final double nanosecPerUpdate = 1000000000 / GlobalInformation.FPS;
        long rACL = System.nanoTime();

        double timeLapsed;
        double delta = 0;

        while (running) {
            final long initLoop = System.nanoTime();
            timeLapsed = initLoop - rACL;
            rACL = initLoop;

            delta += timeLapsed / nanosecPerUpdate;

            while (running && delta >= 1) {
                update();
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

    public void resume() {
        isPaused = false;
    }

    public String getName() {
        return name;
    }

    public static class PlayerBuilder {

        private final String name;
        private final Game game;
        private final Sprite sprite;
        private final Sprite attackSprite;
        
        public PlayerBuilder(String name, Game game, Sprite sprite, Sprite attackSprite) {
            this.name = name;
            this.game = game;
            this.sprite = sprite;
            this.attackSprite = attackSprite;
            this.attackSprite.setBounds(this.sprite.getBounds());
        }

        public Player build() {
            return new Player(this);
        }
    }

}
