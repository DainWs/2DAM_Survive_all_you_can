package com.joseduarte.dwssurviveallyoucan.graphics;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;

import com.joseduarte.dwssurviveallyoucan.util.ResourceManager;

public class TileSheets {

    public static final int SPRITE_X = 0;
    public static final int SPRITE_Y = 1;
    public static final int SPRITE_WIDTH = 2;
    public static final int SPRITE_HEIGHT = 3;

    private final Sprite sprite;
    private final BitmapDrawable image;
    private final SpriteSettings settings;

    private int spriteWidth;
    private int spriteHeight;

    private int[] spriteDims;

    public TileSheets(Sprite sprite, int resId, SpriteSettings settings) {
        this.sprite = sprite;
        this.settings = settings;
        this.image = ResourceManager.loadDrawable(resId);

        spriteWidth = settings.getSpriteWidth();
        spriteHeight = settings.getSpriteHeight();

        spriteDims = new int[] {0, 0, spriteWidth, spriteHeight};
    }

    public TileSheets(Sprite sprite, BitmapDrawable image, SpriteSettings settings) {
        this.sprite = sprite;
        this.settings = settings;
        this.image = image;

        spriteWidth = settings.getSpriteWidth();
        spriteHeight = settings.getSpriteHeight();

        spriteDims = new int[] {0, 0, spriteWidth, spriteHeight};
    }

    public void initialize() {
        Bitmap bitmap = image.getBitmap();

        if (settings != null) {
            settings.update(bitmap.getWidth(), bitmap.getHeight());

            spriteWidth = settings.getSpriteWidth();
            spriteHeight = settings.getSpriteHeight();

            spriteDims = new int[] {0, 0, spriteWidth, spriteHeight};
        }
    }

    public void update() {

        int newSpritePosX = settings.nextSpriteCol() * spriteWidth;
        int newSpritePosY = settings.getCurrentSpriteLine() * spriteHeight;

        spriteDims = new int[] {
                newSpritePosX,
                newSpritePosY,
                spriteWidth,
                spriteHeight
        };
    }

    public Rect getCurrentSpriteRect() {
        return new Rect(
                spriteDims[SPRITE_X],
                spriteDims[SPRITE_Y],
                spriteDims[SPRITE_WIDTH],
                spriteDims[SPRITE_HEIGHT]
        );
    }

    public Bitmap getSpriteBitmap() {
        Bitmap bitmap =
                Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);

        if (spriteDims[SPRITE_WIDTH] > 0 && spriteDims[SPRITE_HEIGHT] > 0) {
            bitmap = Bitmap.createBitmap(
                    image.getBitmap(),
                    spriteDims[SPRITE_X],
                    spriteDims[SPRITE_Y],
                    spriteDims[SPRITE_WIDTH],
                    spriteDims[SPRITE_HEIGHT]
            );
        }

        return bitmap;
    }

    public void nextLine() {
        settings.nextLine();
    }

    public void setLine(int i) {
        settings.setLine(i);
    }
}
