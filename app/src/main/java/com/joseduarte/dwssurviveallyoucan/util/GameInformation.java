package com.joseduarte.dwssurviveallyoucan.util;

import android.graphics.drawable.BitmapDrawable;

import com.joseduarte.dwssurviveallyoucan.entities.EntitySpawner;
import com.joseduarte.dwssurviveallyoucan.entities.Player;
import com.joseduarte.dwssurviveallyoucan.graphics.Screen;

public class GameInformation {
    public static Screen screen = null;
    public static CollisionHandler collisionHandler = null;
    public static EntitySpawner entitySpawner = null;

    public static Player player = null;
    public static Thread playerThread = null;
}
