package com.joseduarte.dwssurviveallyoucan.physics.action;

import com.joseduarte.dwssurviveallyoucan.R;
import com.joseduarte.dwssurviveallyoucan.entities.Entity;
import com.joseduarte.dwssurviveallyoucan.entities.EntitySpawner;
import com.joseduarte.dwssurviveallyoucan.entities.Player;
import com.joseduarte.dwssurviveallyoucan.graphics.EffectSprite;
import com.joseduarte.dwssurviveallyoucan.graphics.Screen;
import com.joseduarte.dwssurviveallyoucan.graphics.SpriteSettings;
import com.joseduarte.dwssurviveallyoucan.graphics.effects.AttackEffect;
import com.joseduarte.dwssurviveallyoucan.physics.CollisionBox;
import com.joseduarte.dwssurviveallyoucan.util.GlobalInformation;

import java.util.Collection;
import java.util.function.Predicate;

public class Attack {
    public static int ATTACK_RANGE = 200;

    protected final EffectSprite effect;
    protected final Entity producerEntity;

    protected int attackingToSide;

    public Attack(Entity producerEntity, int toSide) {
        this.producerEntity = producerEntity;
        this.attackingToSide = toSide;

        effect = new AttackEffect(
                R.drawable.hit_effect,
                producerEntity,
                new SpriteSettings(1, 1)
        );
    }

    public EffectSprite getEffect() {
        return effect;
    }

    public void attackTo(Entity entity) {
        entity.isBeingAttacked(true);

        if(inAttackRange(entity, ATTACK_RANGE)) {
            entity.addEffect(effect);
            entity.hurt();
        }
    }

    public void attackTo(Entity entity, int side) {
        entity.isBeingAttacked(true);

        attackingToSide = side;
        if(inAttackRange(entity, ATTACK_RANGE)) {
            entity.addEffect(effect);
            entity.hurt();

            if(!entity.isEntityAlive()) {
                producerEntity.getGame().addCoins(EntitySpawner.COINS_PER_MONSTER);
            }
        }
    }

    public boolean inAttackRange(Entity entity, int range) {

        CollisionBox producerEntityBox = producerEntity.getSprite().getBounds();
        CollisionBox attackedEntityBox = entity.getSprite().getBounds();

        boolean isCollisionsReverted = (
                producerEntityBox.isCollisionsReverted() ||
                attackedEntityBox.isCollisionsReverted()
        );

        if(isCollisionsReverted) return false;

        double[] producerBounds = producerEntityBox.getBoundsArray();
        double[] attackedBounds = attackedEntityBox.getBoundsArray();

        double xDiff = 0;
        if(attackingToSide == Screen.LEFT_SIDE)
            xDiff = Math.abs(attackedBounds[CollisionBox.RIGHT_X]) -
                    Math.abs(producerBounds[CollisionBox.LEFT_X]);
        else
            xDiff = Math.abs(producerBounds[CollisionBox.RIGHT_X]) -
                    Math.abs(attackedBounds[CollisionBox.LEFT_X]);

        if (Math.abs(xDiff) <= range) return true;
        return false;
    }
}
