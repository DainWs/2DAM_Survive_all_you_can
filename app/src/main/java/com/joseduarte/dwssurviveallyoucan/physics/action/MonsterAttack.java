package com.joseduarte.dwssurviveallyoucan.physics.action;

import com.joseduarte.dwssurviveallyoucan.entities.Entity;
import com.joseduarte.dwssurviveallyoucan.entities.EntitySpawner;
import com.joseduarte.dwssurviveallyoucan.entities.Player;
import com.joseduarte.dwssurviveallyoucan.entities.monsters.DragonHead;
import com.joseduarte.dwssurviveallyoucan.entities.monsters.Monster;
import com.joseduarte.dwssurviveallyoucan.graphics.Screen;
import com.joseduarte.dwssurviveallyoucan.physics.CollisionBox;

public class MonsterAttack extends Attack {

    private int attackRange = 20;

    public MonsterAttack(Entity producerEntity, int toSide) {
        super(producerEntity, toSide);
    }

    public void attackTo(Entity entity) {
        entity.isBeingAttacked(true);

        if(inAttackRange(entity, attackRange)) {
            entity.addEffect(effect);
            entity.hurt();
        }
    }

    public void attackTo(Entity entity, int side) {
        entity.isBeingAttacked(true);

        attackingToSide = side;
        if(inAttackRange(entity, attackRange)) {
            entity.addEffect(effect);
            entity.hurt();
        }
    }

    public boolean inAttackRange(Entity entity, int range) {

        CollisionBox producerEntityBox = producerEntity.getSprite().getBounds();
        CollisionBox attackedEntityBox = entity.getSprite().getBounds();

        double[] producerBounds = producerEntityBox.getBoundsArray();
        double[] attackedBounds = attackedEntityBox.getBoundsArray();

        if(attackedBounds[CollisionBox.RIGHT_X] < producerBounds[CollisionBox.LEFT_X]) {

        }

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
