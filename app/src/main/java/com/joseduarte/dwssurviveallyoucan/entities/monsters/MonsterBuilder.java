package com.joseduarte.dwssurviveallyoucan.entities.monsters;

import com.joseduarte.dwssurviveallyoucan.game.Game;
import com.joseduarte.dwssurviveallyoucan.graphics.Sprite;

public abstract class MonsterBuilder {

        protected Game game;
        protected Sprite sprite;
        protected int fromSide;
        
        public MonsterBuilder() {}

        public MonsterBuilder addSprite(Sprite sprite) {
                this.sprite = sprite;
                return this;
        }

        public MonsterBuilder setFromSide(int fromSide) {
                this.fromSide = fromSide;
                return this;
        }

        public MonsterBuilder setGame(Game game) {
                this.game = game;
                return this;
        }

        public abstract <T extends Monster> T build();
}
