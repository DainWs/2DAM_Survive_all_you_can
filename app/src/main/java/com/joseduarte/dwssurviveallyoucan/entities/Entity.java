package com.joseduarte.dwssurviveallyoucan.entities;

import android.graphics.Canvas;
import android.graphics.Point;

import com.joseduarte.dwssurviveallyoucan.game.Game;
import com.joseduarte.dwssurviveallyoucan.graphics.EffectSprite;
import com.joseduarte.dwssurviveallyoucan.graphics.Sprite;
import com.joseduarte.dwssurviveallyoucan.physics.action.Attack;

import java.util.ArrayList;
import java.util.Collection;

public abstract class Entity {

    protected Collection<EffectSprite> effects;

    protected boolean isAlive;
    protected boolean isAttacking;

    protected Game game;

    protected int side;
    protected Attack attack;
    protected long attackingTime;

    protected Sprite sprite;

    protected float speed;
    protected int lives;
    protected boolean isBeingAttacked;

    public Entity(Game game, Sprite sprite) {
        effects = new ArrayList<>();

        isAlive = true;
        isAttacking = false;
        isBeingAttacked = false;
        attackingTime = 0l;

        speed = 50f;
        lives = 1;

        this.game = game;
        this.sprite = sprite;
        this.sprite.setEntity(this);
    }

    public void hurt() {
        lives -= 1;
        if(lives <= 0) isAlive = false;
    }

    public void update() {
        if(!isAlive) {
            die();
        }
    }

    public boolean isEntityAlive() {
        return isAlive;
    }

    public Sprite getSprite() {
        return sprite;
    }

    protected void die() {
        EntitiesManager.removeEntity(this);
    }

    public abstract void draw(Canvas canvas);

    public int getSide() {
        return side;
    }

    public Point getPosition() {
        return sprite.getPosition();
    }

    public Point getImgPosition() {
        return sprite.getImgPosition();
    }

    public Collection<EffectSprite> getEffects() {
        return effects;
    }

    public void addEffect(EffectSprite effect) {
        effects.add(effect);
    }

    public Game getGame() {
        return game;
    }

    public void isBeingAttacked(boolean b) {
        isBeingAttacked = b;
        attackingTime = 0;
    }

    public int getLives() {
        return lives;
    }
}
