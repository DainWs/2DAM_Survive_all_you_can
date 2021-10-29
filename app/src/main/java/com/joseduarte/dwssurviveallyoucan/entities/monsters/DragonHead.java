package com.joseduarte.dwssurviveallyoucan.entities.monsters;

import android.graphics.Canvas;

import com.joseduarte.dwssurviveallyoucan.R;
import com.joseduarte.dwssurviveallyoucan.graphics.Sprite;
import com.joseduarte.dwssurviveallyoucan.graphics.SpriteSettings;

public class DragonHead extends Monster {

    public DragonHead(MonsterBuilder builder) {
        super(builder);
        lives *= 2;
    }

    public void updateLives(int lives) {
        if(this.lives < lives*2) {
            this.lives = lives*2;
        }
    }

    @Override
    public String toString() {
        return "Type : DragonHead\nLives : "+lives+"\nSpeed : "+speed;
    }

    public static class DragonHeadBuilder extends MonsterBuilder {
        @Override
        public DragonHead build() {
            return new DragonHead(this);
        }
    }
}
