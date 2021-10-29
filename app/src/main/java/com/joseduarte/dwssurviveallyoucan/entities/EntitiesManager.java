package com.joseduarte.dwssurviveallyoucan.entities;

import android.graphics.Canvas;

import androidx.core.util.Predicate;

import com.joseduarte.dwssurviveallyoucan.entities.monsters.Monster;
import com.joseduarte.dwssurviveallyoucan.util.GameInformation;
import com.joseduarte.dwssurviveallyoucan.util.GlobalInformation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class EntitiesManager {

    private static final Collection<Entity> ENTITIES = new ArrayList<>();

    public static void addEntity(Entity e) {
        ENTITIES.add(e);
    }

    public static void playerAttackToSide(final int side) {
        List<Entity> sideList = new ArrayList<>(
                getEntitiesWhere(new Predicate() {
                    @Override
                    public boolean test(Object o) {
                        return ( (o instanceof Monster) &&
                               ( ((Monster)o).getFromSide() == side) );
                    }
                })
        );

        GameInformation.player.attack(
                (sideList.size() > 0) ? sideList.get(0) : null,
                side
        );
    }

    public static void updateEntities() {
        Collection<Entity> entities = new ArrayList<>(ENTITIES);
        for (Entity entity : entities) {
            entity.update();
        }
    }

    public static void drawEntities(Canvas c) {
        if(GameInformation.player != null) {
            GameInformation.player.draw(c);
        }

        for (Entity entity : ENTITIES) {
            entity.draw(c);
        }
    }

    public static int getQuantity() {
        return getEntitiesWhere(new Predicate() {
            @Override
            public boolean test(Object o) {
                return !(o instanceof Player);
            }
        }).size();
    }

    public static Collection<Entity> getEntitiesWhere(Predicate predicate) {
        Collection<Entity> entities = new ArrayList<>();
        for (Entity entity : ENTITIES) {
            if(predicate.test(entity)) {
                entities.add(entity);
            }
        }
        return entities;
    }

    public static Collection<Entity> getCollection() {
        return ENTITIES;
    }

    public static void removeEntity(Entity e) {
        if (ENTITIES.contains(e)) ENTITIES.remove(e);
    }

    public static void clear() {
        ENTITIES.clear();
    }
}

