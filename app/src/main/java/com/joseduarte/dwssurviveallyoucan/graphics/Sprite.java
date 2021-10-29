package com.joseduarte.dwssurviveallyoucan.graphics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.constraintlayout.solver.widgets.Rectangle;

import com.joseduarte.dwssurviveallyoucan.R;
import com.joseduarte.dwssurviveallyoucan.entities.Entity;
import com.joseduarte.dwssurviveallyoucan.entities.Player;
import com.joseduarte.dwssurviveallyoucan.physics.CollisionBox;
import com.joseduarte.dwssurviveallyoucan.util.GameInformation;
import com.joseduarte.dwssurviveallyoucan.util.GlobalInformation;
import com.joseduarte.dwssurviveallyoucan.util.ResourceManager;

import java.util.ArrayList;
import java.util.Collection;

public class Sprite {
    protected static final int screenHeight = GameInformation.screen.getResolutionHeight();

    protected TileSheets tileSheets;
    protected SpriteSettings settings;

    protected int width;
    protected int height;

    protected Entity entity;
    protected CollisionBox bounds;

    protected Point position = new Point(0, 0);
    protected Point imgPosition = new Point(0, 0);

    protected Bitmap bOutput;

    public Sprite(SpriteSettings settings) {
        this.settings = settings;
    }

    public Sprite(int resId, SpriteSettings settings) {
        this.tileSheets = new TileSheets(this, resId, settings);
        this.settings = settings;
        initSprite();
        updateGraphics();
    }

    public Sprite(int resId, CollisionBox bounds, SpriteSettings settings) {
        this.tileSheets = new TileSheets(this, resId, settings);
        this.settings = settings;
        initSprite();
        this.bounds = bounds;
        updateGraphics();
    }

    public Sprite(int resId, Point position, SpriteSettings settings) {
        this.tileSheets = new TileSheets(this, resId, settings);
        this.settings = settings;
        this.position = position;
        initSprite();
        updateGraphics();
    }

    protected void initSprite() {
        tileSheets.initialize();

        width = settings.getSpriteWidth();
        height = settings.getSpriteHeight();

        bounds = new CollisionBox(
                position.x,
                position.y,
                width,
                height
        );

        position = new Point((int) bounds.getX(), (int) bounds.getY());
    }

    public Point getPosition() {
        return position;
    }

    public Point getImgPosition() {
        return imgPosition;
    }

    public CollisionBox getBounds() {
        return bounds;
    }

    public boolean inCollisionWith(Sprite sprite) {
        return bounds.collidesWith(sprite.getBounds());
    }

    public void update(int x) {
        position.x += x;
        bounds.add(new Point(x, 0));
        updateGraphics();
    }

    protected void updateGraphics() {
        tileSheets.update();

        imgPosition = new Point(
                position.x,
                position.y + (screenHeight - height)
        );

        bOutput = tileSheets.getSpriteBitmap();

        bounds.setPosition(new Rect(
                imgPosition.x,
                imgPosition.y,
                bOutput.getWidth(),
                bOutput.getHeight()
        ));
    }

    public void draw(Canvas canvas) {

        if(entity != null && entity.getSide() == Screen.LEFT_SIDE) {
            Bitmap copBitmap = bOutput;
            Matrix matrix = new Matrix();
            matrix.preScale(-1.0f, 1.0f);
            bOutput = Bitmap.createBitmap(
                    copBitmap,
                    0,
                    0,
                    copBitmap.getWidth(),
                    copBitmap.getHeight(),
                    matrix,
                    true
            );
        }

        canvas.drawBitmap(
                bOutput,
                imgPosition.x,
                imgPosition.y,
                null
        );

        /*Paint t = new Paint();
        t.setColor(Color.WHITE);
        canvas.drawRect(
                bounds.getBounds(),
                t
        );*/

        Collection<EffectSprite> effects = new ArrayList<>(entity.getEffects());
        for (EffectSprite effect : effects) {
            effect.draw(canvas, imgPosition);
        }
    }

    public void isMoving(boolean isMoving) {
        if(isMoving) tileSheets.setLine(1);
        else tileSheets.setLine(0);
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public void setBounds(CollisionBox bounds) {
        this.bounds = bounds;
    }
}
