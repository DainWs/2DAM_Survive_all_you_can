package com.joseduarte.dwssurviveallyoucan.physics;

import android.graphics.Point;
import android.graphics.Rect;

import com.joseduarte.dwssurviveallyoucan.entities.Entity;

public class CollisionBox {

    public static final int LEFT_X = 0;
    public static final int RIGHT_X = 1;
    public static final int TOP_Y = 2;
    public static final int BOTTOM_Y = 3;

    private double x, y, width, height;
    private double[] bounds;

    private boolean reversedCollisions = false;

    public CollisionBox(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        updateBounds();
    }

    public CollisionBox(double x, double y, double width, double height, boolean reverseCollisions) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        reversedCollisions = reverseCollisions;
        updateBounds();
    }

    public void add(Point position) {
        this.x += position.x;
        this.y += position.y;
        updateBounds();
    }

    public void setPosition(Point position) {
        this.x = position.x;
        this.y = position.y;
        updateBounds();
    }

    public void setPosition(Rect position) {
        this.x = position.left;
        this.y = position.top;

        bounds = new double[]{
                x,
                (x + position.right),
                y,
                (y + position.bottom)
        };
    }

    private void updateBounds() {
        bounds = new double[]{ x, (x + width), y, (y + height) };
    }

    public boolean isCollisionsReverted() {
        return reversedCollisions;
    }

    public double[] getBoundsArray() {
        return bounds;
    }

    public Rect getBounds() {
        return new Rect(
                (int) x,
                (int) y,
                (int) (x + width),
                (int) (y + height)
        );
    }

    public boolean collidesWith(CollisionBox with) {
        double[] withBounds = with.getBoundsArray();

        boolean isCollisionsReverted = (reversedCollisions || with.isCollisionsReverted());

        /*
         Si La [LEFT_X] del primer CollisionBox esta a la DERECHA del
         [RIGHT_X] del segundo CollisionBox, NO HAY COLISION
        */

        System.out.println(" por izquierda " + (bounds[LEFT_X] > withBounds[RIGHT_X]) + " right : " + bounds[LEFT_X] + " left : " + withBounds[RIGHT_X]);
        System.out.println(" por derecha " + (bounds[RIGHT_X] < withBounds[LEFT_X]) + " left : " + bounds[RIGHT_X] + " right : " + withBounds[LEFT_X]);
        System.out.println("------------------------------------------------");
        if ( bounds[LEFT_X] > withBounds[RIGHT_X]  ) {

            // Si collisionInverted == false entonces se devuelve False
            return false;
        }

        /*
         Si no, si La [RIGHT_X] del primer CollisionBox esta a la IZQUIERDA del
         [LEFT_X] del segundo CollisionBox, NO HAY COLISION
        */
        else if ( bounds[RIGHT_X] < withBounds[LEFT_X] ) {
            return false;
        }

        /*
         Si La [TOP_Y] del primer CollisionBox esta por DEBAJO del
         [BOTTOM_Y] del segundo CollisionBox, NO HAY COLISION
        *
        if ( bounds[TOP_Y] > withBounds[BOTTOM_Y] ) {
            return false;
        }

        /*
         Si no, si La [BOTTOM_Y] del primer CollisionBox esta por ENCIMA del
         [TOP_Y] del segundo CollisionBox, NO HAY COLISION
        *
        else if ( bounds[BOTTOM_Y] < withBounds[TOP_Y] ) {
            return false;
        }

        /* En cualquier otro caso SI hay colision */
        return true;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}