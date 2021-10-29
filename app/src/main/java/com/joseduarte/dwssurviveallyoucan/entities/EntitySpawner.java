package com.joseduarte.dwssurviveallyoucan.entities;

import android.graphics.Point;
import android.media.MediaPlayer;

import com.joseduarte.dwssurviveallyoucan.GameActivity;
import com.joseduarte.dwssurviveallyoucan.R;
import com.joseduarte.dwssurviveallyoucan.entities.monsters.Monster;
import com.joseduarte.dwssurviveallyoucan.entities.monsters.MonsterBuilder;
import com.joseduarte.dwssurviveallyoucan.game.Game;
import com.joseduarte.dwssurviveallyoucan.graphics.Screen;
import com.joseduarte.dwssurviveallyoucan.graphics.Sprite;
import com.joseduarte.dwssurviveallyoucan.graphics.SpriteSettings;
import com.joseduarte.dwssurviveallyoucan.physics.action.Attack;
import com.joseduarte.dwssurviveallyoucan.util.CollisionHandler;
import com.joseduarte.dwssurviveallyoucan.util.GameInformation;
import com.joseduarte.dwssurviveallyoucan.util.GlobalInformation;

import java.util.Random;

/**
 *
 * @author Jose-DainWs
 */
public class EntitySpawner {

    public static int COINS_PER_MONSTER = 1;

    private Game game;

    private long lastEnemySpawnTime = System.currentTimeMillis();
    private float limiter =  0.001f;

    private float enemySpawnCoolDown;
    private float minEnemySpawnCoolDown;

    public EntitySpawner(Game game) {
        this.game = game;

        switch (GlobalInformation.SELECTED_TIME_INDEX) {
            case 0:
                enemySpawnCoolDown = 1f;
                minEnemySpawnCoolDown = 0.7f;
                Attack.ATTACK_RANGE = 180;
                COINS_PER_MONSTER = 4;
                break;
            case 1:
                enemySpawnCoolDown = 2f;
                minEnemySpawnCoolDown = 1f;
                Attack.ATTACK_RANGE = 170;
                COINS_PER_MONSTER = 8;
                break;
            case 2:
                enemySpawnCoolDown = 3f;
                minEnemySpawnCoolDown = 1.3f;
                Attack.ATTACK_RANGE = 140;
                COINS_PER_MONSTER = 12;
                break;
            default:
                enemySpawnCoolDown = 2f;
                minEnemySpawnCoolDown = 1.6f;
                Attack.ATTACK_RANGE = 120;
                COINS_PER_MONSTER = 16;
                break;
        }

        GameInformation.entitySpawner = this;
    }
    
    public void update() {
        long timeLapsedOnSeconds = (System.currentTimeMillis() - lastEnemySpawnTime)/1000;

        if (timeLapsedOnSeconds >= enemySpawnCoolDown) {
            lastEnemySpawnTime = System.currentTimeMillis();
            enemySpawnCoolDown -= limiter;
            limiter -= limiter/2;

            if (EntitiesManager.getQuantity() < 20) {
                    int randomMonsterSelector = new Random().nextInt(Monster.MONSTERS_TYPES.length);

                    //SELECT RANDOM MONSTER BUILDER
                    Class<? extends MonsterBuilder> selectedMonster =
                            Monster.MONSTERS_TYPES[randomMonsterSelector];

                    //MAKE RANDOM MONSTER DATA
                    int resourceId = Monster.MONSTERS_RESOURCES.get(selectedMonster);
                    int fromSide = getSpawnSide();
                    Point spawnPosition = getSpawnPosition(fromSide);

                    //MONSTERS CONFIG
                    SpriteSettings monsterSpriteSettings = new SpriteSettings(
                            3,
                            1
                    );

                    //MAKE SPRITE AND MONSTER
                    Sprite sprite = new Sprite(resourceId, spawnPosition, monsterSpriteSettings);
                    Monster monster = EntityFactory.buildMonster(game, selectedMonster, sprite, fromSide);

                    if(null==monster) return;
                    else {
                        MediaPlayer.create(GlobalInformation.context, R.raw.sound_game_zombie).start();
                        monster.updateSpeed(monster.speed + Math.abs(limiter/10));
                    }
            }
        }

        if(enemySpawnCoolDown < minEnemySpawnCoolDown)
            enemySpawnCoolDown = minEnemySpawnCoolDown;
    }

    public void restartLastEnemySpawnTime() {
        lastEnemySpawnTime = System.currentTimeMillis();
    }

    private int getSpawnSide() {
        int sideRandomSelection = (new Random().nextInt(2) >= 1) ? Screen.LEFT_SIDE : Screen.RIGHT_SIDE ;
        return sideRandomSelection;
    }

    private Point getSpawnPosition(int side) {
        return game.getScreen().getSidePosition(side);
    }
}
