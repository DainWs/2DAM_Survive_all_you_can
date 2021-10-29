package com.joseduarte.dwssurviveallyoucan.entities.monsters;

import android.graphics.Canvas;

import com.joseduarte.dwssurviveallyoucan.R;
import com.joseduarte.dwssurviveallyoucan.graphics.Sprite;
import com.joseduarte.dwssurviveallyoucan.graphics.SpriteSettings;

public class Skeleton extends Monster {

    public Skeleton(MonsterBuilder builder) {
        super(builder);
        speed *= 1.5f;
    }

    @Override
    public String toString() {
        return "Type : Skeleton\nLives : "+lives+"\nSpeed : "+speed;
    }

    public static class SkeletonBuilder extends MonsterBuilder {
        @Override
        public Skeleton build() {
            return new Skeleton(this);
        }
    }
}
