package com.joseduarte.dwssurviveallyoucan.util;

import androidx.core.util.Predicate;

import com.joseduarte.dwssurviveallyoucan.entities.EntitiesManager;
import com.joseduarte.dwssurviveallyoucan.entities.Entity;
import com.joseduarte.dwssurviveallyoucan.entities.Player;
import com.joseduarte.dwssurviveallyoucan.entities.monsters.Monster;
import com.joseduarte.dwssurviveallyoucan.game.Game;

import java.util.Collection;

/**
 *
 * @author Jose-DainWs
 */
public class CollisionHandler {

    private Game game;
    private Player player;

    public CollisionHandler(Game game) {
        this.game = game;
        this.player = game.getPlayer();

        GameInformation.collisionHandler = this;
    }

    public void update() {
        if(player == null && game.getPlayer() != null)
            player = game.getPlayer();

        if(player != null) {
            collisionsForPlayer();
        }
    }
    
    private void collisionsForPlayer() {
        Collection<Entity>  enemies = EntitiesManager.getEntitiesWhere(new Predicate() {
            @Override
            public boolean test(Object o) {
                return (o instanceof Monster);
            }
        });

        for (Entity entity : enemies) {
            if (entity.isEntityAlive()) {
                Monster monster = (Monster)entity;

                monster.attack(player);
            }
        }
    }
    
    private boolean checkCollision(Player player, Monster monster) {
        return player.getSprite().inCollisionWith(monster.getSprite());
    }
    
    
    
}
