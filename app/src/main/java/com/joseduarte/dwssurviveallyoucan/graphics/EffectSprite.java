package com.joseduarte.dwssurviveallyoucan.graphics;

import android.graphics.Canvas;
import android.graphics.Point;

import com.joseduarte.dwssurviveallyoucan.entities.Entity;
import com.joseduarte.dwssurviveallyoucan.util.ResourceManager;

public class EffectSprite extends Sprite {

    public static final int ATTACK_EFFECT_ID = 1;
    public static final int LIFE_EFFECT_ID = 2;

    public final int id;

    public EffectSprite(int id, int resId, Entity entity, SpriteSettings settings) {
        super(settings);
        this.id = id;
        this.tileSheets = new TileSheets(
                this,
                ResourceManager.loadDrawable(resId),
                settings
        );
        this.entity = entity;
        initSprite();
        updateGraphics();
    }

    public void update() {
        updateGraphics();
    }

    @Override
    protected void updateGraphics() {
        tileSheets.update();

        bOutput = tileSheets.getSpriteBitmap();
    }

    public void draw(Canvas canvas, Point imgPoint) {
        canvas.drawBitmap(
                bOutput,
                imgPoint.x,
                imgPoint.y,
                null
        );
    }

    public boolean isEquals(EffectSprite effectSprite) {
        return (effectSprite.id == id);
    }
}
