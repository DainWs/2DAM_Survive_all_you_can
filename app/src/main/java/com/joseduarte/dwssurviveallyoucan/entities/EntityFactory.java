package com.joseduarte.dwssurviveallyoucan.entities;

import android.graphics.Point;

import com.joseduarte.dwssurviveallyoucan.R;
import com.joseduarte.dwssurviveallyoucan.entities.monsters.Monster;
import com.joseduarte.dwssurviveallyoucan.entities.monsters.MonsterBuilder;
import com.joseduarte.dwssurviveallyoucan.game.Game;
import com.joseduarte.dwssurviveallyoucan.graphics.Screen;
import com.joseduarte.dwssurviveallyoucan.graphics.Sprite;
import com.joseduarte.dwssurviveallyoucan.graphics.SpriteSettings;
import com.joseduarte.dwssurviveallyoucan.util.GameInformation;
import com.joseduarte.dwssurviveallyoucan.util.GlobalInformation;

public class EntityFactory {

    public static Player buildPlayer(Game game, Screen screen, String name) {

        Point spawnPosition = screen.getSidePosition(Screen.CENTER_SIDE);

        SpriteSettings moveSettings = new SpriteSettings(4, 2);
        SpriteSettings attackSettings = new SpriteSettings(2, 1);

        Sprite moveSprite = new Sprite(R.drawable.entity_player_move, spawnPosition, moveSettings);
        Sprite attackSprite = new Sprite(R.drawable.entity_player_attack, spawnPosition, attackSettings);

        Player player = new Player.PlayerBuilder(name, game, moveSprite, attackSprite)
                .build();

        return player;
    }

    public static Monster buildMonster(Game game, Class<? extends MonsterBuilder> monsterType, Sprite sprite, int side) {

        if(!MonsterBuilder.class.isAssignableFrom(monsterType)) return null;

        Monster monster = null;

        try {
            MonsterBuilder builder = monsterType.newInstance();
            monster = monsterType.cast(builder)
                    .addSprite(sprite)
                    .setFromSide(side)
                    .setGame(game)
                    .build();
        }
        catch (Exception ex) {}

        EntitiesManager.addEntity(monster);

        return monster;
    }
}
