package com.joseduarte.dwssurviveallyoucan.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

public class ResourceManager {

    public static BitmapDrawable loadDrawable(int id) {
        Bitmap btmp = BitmapFactory.decodeResource(
                GlobalInformation.context.getResources(),
                id
        );

        Bitmap scaledBtmp = Bitmap.createScaledBitmap(
                btmp,
                btmp.getWidth() * 3,
                btmp.getHeight() * 3,
                false
        );

        return new BitmapDrawable(
                GlobalInformation.context.getResources(),
                scaledBtmp
        );
    }

    public static BitmapDrawable loadBackground(int id) {
        Bitmap btmp = BitmapFactory.decodeResource(
                GlobalInformation.context.getResources(),
                id
        );

        return new BitmapDrawable(
                GlobalInformation.context.getResources(),
                btmp
        );
    }

    public static BitmapDrawable loadEffect(int id) {
        Bitmap btmp = BitmapFactory.decodeResource(
                GlobalInformation.context.getResources(),
                id
        );

        return new BitmapDrawable(
                GlobalInformation.context.getResources(),
                btmp
        );
    }

    public static Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap(
                v.getLayoutParams().width,
                v.getLayoutParams().height,
                Bitmap.Config.ARGB_8888
        );
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return b;
    }
}
