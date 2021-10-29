package com.joseduarte.dwssurviveallyoucan.firebase;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.joseduarte.dwssurviveallyoucan.models.User;
import com.joseduarte.dwssurviveallyoucan.util.GlobalInformation;

import java.util.ArrayList;
import java.util.Collections;

public class FirebaseDBManager {

    private static final String VALID_CHARACTERS_REGEX = "[^a-zA-Z0-9]+";

    private static FirebaseDatabase database;
    private static DatabaseReference myRef;

    static {
        database = FirebaseDatabase.getInstance();
        GlobalInformation.database = database;

        myRef = database.getReference("top_list");
        GlobalInformation.myRef = myRef;
    }

    public static String makeFirebaseURLPath(String mac) {
        return mac.replaceAll(VALID_CHARACTERS_REGEX, "_");
    }

    public static void saveUserData(User user) {
        if(!user.getNetworkId().equalsIgnoreCase("NONE")) {
            String path = makeFirebaseURLPath(user.getNetworkId());
            myRef.child(path).setValue(user);
        }
    }

    public static void saveUserData(String userNetworkID, String dataPath, Object object) {
        if(!userNetworkID.equalsIgnoreCase("NONE")) {
            String path = makeFirebaseURLPath(userNetworkID);
            myRef.child(path).child(dataPath).setValue(object);
        }
    }

    public static void saveUserData() {
        User user = GlobalInformation.loggedUser;
        if(user != null) saveUserData(user);
    }

    private ValueEventListener eventListener;
    private Activity activity;

    private boolean isDataLoaded = false;

    public FirebaseDBManager(Activity activity) {
        this.activity = activity;
        initialization();
    }

    public void initialization() {
        initListeners();
        GlobalInformation.dbManager = this;
    }

    private void initListeners() {
        eventListener = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> topList = dataSnapshot.getChildren();

                ArrayList<User> usersTopList = new ArrayList<>();
                for (DataSnapshot user : topList) {
                    User userItem = user.getValue(User.class);
                    usersTopList.add(userItem);
                }

                GlobalInformation.TOP_LIST = usersTopList;
                isDataLoaded = true;

                if(GlobalInformation.topActivity != null)
                    GlobalInformation.topActivity.update();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public boolean isDataLoaded() {
        return isDataLoaded;
    }

    public boolean loadUser(User user) {
        User[] users = GlobalInformation.TOP_LIST.toArray(new User[0]);
        boolean found = false;
        for (int i = 0; i < users.length; i++) {
            if(users[i].getNetworkId().equalsIgnoreCase(user.getNetworkId())) {
                GlobalInformation.loggedUser = users[i];
                found = true;
                break;
            }
        }

        return found;
    }

    public void pauseListeners() {
        if(myRef != null && eventListener != null) {
            myRef.removeEventListener(eventListener);
        }
    }

    public void restartListeners() {
        if(myRef != null && eventListener != null) {
            myRef.addValueEventListener(eventListener);
        }
    }
}
