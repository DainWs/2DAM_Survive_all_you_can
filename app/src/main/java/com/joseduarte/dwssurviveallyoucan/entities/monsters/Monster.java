package com.joseduarte.dwssurviveallyoucan.entities.monsters;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;

import com.joseduarte.dwssurviveallyoucan.R;
import com.joseduarte.dwssurviveallyoucan.entities.Entity;
import com.joseduarte.dwssurviveallyoucan.entities.Player;
import com.joseduarte.dwssurviveallyoucan.graphics.EffectSprite;
import com.joseduarte.dwssurviveallyoucan.graphics.Screen;
import com.joseduarte.dwssurviveallyoucan.graphics.Sprite;
import com.joseduarte.dwssurviveallyoucan.graphics.SpriteSettings;
import com.joseduarte.dwssurviveallyoucan.graphics.effects.AttackEffect;
import com.joseduarte.dwssurviveallyoucan.graphics.effects.LifeEffect;
import com.joseduarte.dwssurviveallyoucan.physics.action.Attack;
import com.joseduarte.dwssurviveallyoucan.physics.action.MonsterAttack;
import com.joseduarte.dwssurviveallyoucan.util.GameInformation;
import com.joseduarte.dwssurviveallyoucan.util.GlobalInformation;
import com.joseduarte.dwssurviveallyoucan.util.ResourceManager;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jose-DainWs
 */
public abstract class Monster extends Entity {

    public static final Class<? extends MonsterBuilder>[] MONSTERS_TYPES = new Class[] {
            Skeleton.SkeletonBuilder.class,
            DragonHead.DragonHeadBuilder.class
    };

    public static final Map<Class<? extends MonsterBuilder>, Integer> MONSTERS_RESOURCES =
            new HashMap<>();

    static {
        MONSTERS_RESOURCES.put(Skeleton.SkeletonBuilder.class, R.drawable.entity_esqueleto);
        MONSTERS_RESOURCES.put(DragonHead.DragonHeadBuilder.class, R.drawable.entity_cabeza_dragon);
    }

    protected final int fromSide;
    protected final int toSide;

    public Monster(MonsterBuilder builder) {
        super(builder.game, builder.sprite);
        this.fromSide = builder.fromSide;
        this.toSide = Screen.getInverseSide(fromSide);
        this.side = toSide;

        attack = new MonsterAttack(this, side);
    }

    public void updateSpeed(float speed) {
        if(this.speed < speed) {
            this.speed = speed;
        }
    }

    public void updateLives(int lives) {
        if(this.lives < lives) {
            this.lives = lives;
        }
    }

    public void attack(Entity entity) {
        isAttacking = true;
        attackingTime = 0;

        if(entity != null && entity instanceof Player) {
            attack.attackTo(entity, side);
        }
    }

    public int getFromSide() {
        return fromSide;
    }

    @Override
    public void hurt() {
        super.hurt();

        if(lives > 0) {
            this.addEffect(
                new LifeEffect(
                    R.drawable.heart,
                    this,
                    new SpriteSettings(1, 1)
                )
            );
        }
    }

    @Override
    public void update() {
        super.update();
        int playerPosition = GameInformation.player.getPosition().x;
        int moveXDistance = (int) (
                (sprite.getPosition().x > playerPosition) ? -speed : speed
        );

        Rect monsterBounds = sprite.getBounds().getBounds();
        Rect playerBounds = GameInformation.player.getSprite().getBounds().getBounds();

        if (sprite.getPosition().x > playerPosition) {
            if(!(playerBounds.right < monsterBounds.left - 50)) {
                attack(GameInformation.player);
            }
        }
        else {
            if(!(playerBounds.left  > monsterBounds.right + 50)) {
                attack(GameInformation.player);
            }
        }

        sprite.update(moveXDistance);

        for (EffectSprite effect : effects) {
            effect.update();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);

        if((isAttacking || isBeingAttacked)  && ++attackingTime >= 1) {
            AttackEffect.removeEffectFromEntity(this);
            isAttacking = false;
        }
    }

    public int getToSide() {
        return toSide;
    }
}
