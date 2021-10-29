package com.joseduarte.dwssurviveallyoucan.graphics.effects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.joseduarte.dwssurviveallyoucan.entities.Entity;
import com.joseduarte.dwssurviveallyoucan.graphics.EffectSprite;
import com.joseduarte.dwssurviveallyoucan.graphics.SpriteSettings;

public class LifeEffect extends EffectSprite {

    private int centerX;
    private int centerY;

    public LifeEffect(int resId, Entity entity, SpriteSettings settings) {
        super(LIFE_EFFECT_ID, resId, entity, settings);
    }

    public void update() {
        updateGraphics();
    }

    @Override
    protected void updateGraphics() {
        tileSheets.update();

        bOutput = tileSheets.getSpriteBitmap();

        centerX = bOutput.getWidth()/2;
        centerY = bOutput.getHeight()/2;
    }

    public void draw(Canvas canvas, Point imgPoint) {
        canvas.drawBitmap(
                bOutput,
                imgPoint.x,
                imgPoint.y - bOutput.getWidth(),
                null
        );

        Paint p = new Paint();
        p.setColor(Color.WHITE);
        p.setTextSize(70);
        canvas.drawText(
                ""+entity.getLives(),
                (imgPoint.x + centerX) - ((""+entity.getLives()).length()*20),
                (imgPoint.y - bOutput.getWidth()) + centerY + 10,
                p
        );
    }

    public static void removeEffectFromEntity(Entity entity) {
        for (EffectSprite effect: entity.getEffects()) {
            if(effect.equals(EffectSprite.LIFE_EFFECT_ID)) {
                entity.getEffects().remove(effect);
            }
        }
    }
}

