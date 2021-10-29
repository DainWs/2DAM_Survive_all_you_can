package com.joseduarte.dwssurviveallyoucan.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.joseduarte.dwssurviveallyoucan.GameActivity;
import com.joseduarte.dwssurviveallyoucan.StartActivity;
import com.joseduarte.dwssurviveallyoucan.TopActivity;
import com.joseduarte.dwssurviveallyoucan.entities.EntitiesManager;
import com.joseduarte.dwssurviveallyoucan.entities.EntitySpawner;
import com.joseduarte.dwssurviveallyoucan.entities.Player;
import com.joseduarte.dwssurviveallyoucan.firebase.FirebaseDBManager;
import com.joseduarte.dwssurviveallyoucan.graphics.Screen;
import com.joseduarte.dwssurviveallyoucan.models.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class GlobalInformation {

    public static final int[] TIME_ARRAY = new int[] { 10, 30, 60, 120 };
    public static Collection<User> TOP_LIST = new ArrayList<>();

    public static FirebaseDBManager dbManager;
    public static FirebaseDatabase database;
    public static DatabaseReference myRef;

    /**
     Las siglas QA se refieren a Quick Access
     */
    public static int QA_HORIZONTAL_SPEED = 0;

    public static String USERNAME = "User";
    public static int lastGameCoins = 0;
    public static User loggedUser = new User("NONE", USERNAME, 0);

    public static Context context = null;
    public static StartActivity startActivity = null;
    public static GameActivity gameActivity = null;
    public static TopActivity topActivity = null;

    public static int SELECTED_FPS_INDEX = 2;
    public static int FPS = 30;

    public static int SELECTED_TIME_INDEX = 2;
    public static int selectedTime = 120;

}
