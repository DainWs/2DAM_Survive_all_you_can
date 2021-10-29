package com.joseduarte.dwssurviveallyoucan.graphics.effects;

import android.graphics.Canvas;

import com.joseduarte.dwssurviveallyoucan.entities.Entity;
import com.joseduarte.dwssurviveallyoucan.graphics.EffectSprite;
import com.joseduarte.dwssurviveallyoucan.graphics.Screen;
import com.joseduarte.dwssurviveallyoucan.graphics.SpriteSettings;

public class AttackEffect extends EffectSprite {

    public AttackEffect(int resId, Entity entity, SpriteSettings settings) {
        super(ATTACK_EFFECT_ID, resId, entity, settings);
    }

    public static void removeEffectFromEntity(Entity entity) {
        for (EffectSprite effect: entity.getEffects()) {
            if(effect.equals(EffectSprite.ATTACK_EFFECT_ID)) {
                entity.getEffects().remove(effect);
            }
        }
    }
}
