package com.joseduarte.dwssurviveallyoucan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.firebase.auth.FirebaseAuth;
import com.joseduarte.dwssurviveallyoucan.firebase.FirebaseDBManager;
import com.joseduarte.dwssurviveallyoucan.firebase.GoogleSignInManager;
import com.joseduarte.dwssurviveallyoucan.models.User;
import com.joseduarte.dwssurviveallyoucan.transformations.CircleTransform;
import com.joseduarte.dwssurviveallyoucan.util.GlobalInformation;
import com.squareup.picasso.Picasso;

import static com.joseduarte.dwssurviveallyoucan.util.GlobalInformation.context;

public class StartActivity  extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDBManager dbManager;
    private GoogleSignInManager googleSignInManager;

    private boolean signInWithGoogle = false;

    private User user;
    private boolean hasLoaded = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        context = this;
        setContentView(R.layout.activity_start);
        GlobalInformation.startActivity = this;

        auth = FirebaseAuth.getInstance();
        dbManager = new FirebaseDBManager(this);
        googleSignInManager = new GoogleSignInManager(this);

        //CARGAMOS LAS PREFERENCIAS
        SharedPreferences prefs = getSharedPreferences(MyPreferencesActivity.PREFERENCES_NAME, MODE_PRIVATE);

        GlobalInformation.SELECTED_FPS_INDEX = prefs.getInt(MyPreferencesActivity.FPS_ID, 2);
        GlobalInformation.SELECTED_TIME_INDEX = prefs.getInt(MyPreferencesActivity.TIMER_ID, 2);

        //CARGAMOS LOS DATOS DEL USUARIO
        String networkID = prefs.getString(MyPreferencesActivity.NETWORK_ID, "NONE");
        signInWithGoogle = prefs.getBoolean(MyPreferencesActivity.SIGN_IN_WITH_GOOGLE, false);
        String username = prefs.getString(MyPreferencesActivity.USERNAME, "User");
        GlobalInformation.USERNAME = username;

        int userCoins = prefs.getInt(MyPreferencesActivity.COINS, 0);
        ((TextView)findViewById(R.id.coins_field))
                .setText(""+userCoins);

        //GUARDAMOS EL USUARIO, YA QUE ESTE PUEDE SER EL USUARIO DEFINITIVO, O UNO TEMPORAL
        user = new User(networkID, username, userCoins);
        user.setSignIn(signInWithGoogle);
        GlobalInformation.loggedUser = user;

        ImageButton googleSignInBtn = findViewById(R.id.google_sign_in_button);

        if(signInWithGoogle) {
            googleSignInManager.signInSilenceMode();
            googleSignInManager.addSignOutListener(googleSignInBtn);
        }
        else googleSignInManager.addListener(googleSignInBtn);

        MediaPlayer.create(StartActivity.this, R.raw.sounds_menu_ambient).start();

        final SoundPool soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        final int soundId = soundPool.load(context, R.raw.button_presed, 1);

        findViewById(R.id.settings_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, MyPreferencesActivity.class);
                soundPool.play(soundId, 1, 1, 0, 0, 1);
                startActivity(intent);
            }
        });

        findViewById(R.id.start_game_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, GameActivity.class);
                soundPool.play(soundId, 1, 1, 0, 0, 1);
                startActivity(intent);
            }
        });

        findViewById(R.id.top_game_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, TopActivity.class);
                soundPool.play(soundId, 1, 1, 0, 0, 1);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(GlobalInformation.lastGameCoins > 0) {
            SharedPreferences prefs = getSharedPreferences(MyPreferencesActivity.PREFERENCES_NAME, MODE_PRIVATE);
            user.setCoins( prefs.getInt(MyPreferencesActivity.COINS, 0) );
            user.setCoins( user.getCoins() + GlobalInformation.lastGameCoins );
            GlobalInformation.lastGameCoins = 0;

            SharedPreferences.Editor prefsEditor = prefs.edit();
            prefsEditor.putString(MyPreferencesActivity.NETWORK_ID, user.getNetworkId());
            prefsEditor.putString(MyPreferencesActivity.USERNAME, user.getCurrentName());
            prefsEditor.putInt(MyPreferencesActivity.COINS, (int) user.getCoins());
            prefsEditor.commit();

            ((TextView)findViewById(R.id.coins_field))
                    .setText(""+user.getCoins());

            FirebaseDBManager.saveUserData(user);
        }

        dbManager.restartListeners();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dbManager.pauseListeners();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GoogleSignInManager.RC_SIGN_IN) {
            if(resultCode == RESULT_OK) {
                googleSignInManager.firebaseAuthWithGoogle(
                        GoogleSignIn.getSignedInAccountFromIntent(data),
                        auth
                );
            }
        }
    }

    public FirebaseDBManager getDBManager() {
        return dbManager;
    }

    public void restartGoogleBtn() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageButton googleSignInBtn = findViewById(R.id.google_sign_in_button);
                googleSignInManager.addListener(googleSignInBtn);

                Picasso.get()
                        .load(R.drawable.google_favicon)
                        .placeholder(R.drawable.google_favicon)
                        .error(R.drawable.google_favicon)
                        .transform(new CircleTransform())
                        .into((ImageButton)findViewById(R.id.google_sign_in_button));

                user.setSignIn(false);
                GlobalInformation.loggedUser = user;
                FirebaseDBManager.saveUserData(user);

                SharedPreferences.Editor prefsEditor =
                        getSharedPreferences(MyPreferencesActivity.PREFERENCES_NAME, MODE_PRIVATE)
                                .edit();

                prefsEditor.putString(MyPreferencesActivity.NETWORK_ID, user.getNetworkId());
                prefsEditor.putBoolean(MyPreferencesActivity.SIGN_IN_WITH_GOOGLE, user.isSignIn());
                prefsEditor.putString(MyPreferencesActivity.USERNAME, user.getCurrentName());
                prefsEditor.putInt(MyPreferencesActivity.COINS, (int) user.getCoins());
                prefsEditor.commit();
            }
        });
    }

    public void updateUser(User googleUser) {
        user.setMail(googleUser.getMail());
        user.setPhotoURL(googleUser.getPhotoURL());
        user.setNetworkId( FirebaseDBManager.makeFirebaseURLPath( user.getMail() ) );

        if(user.isSignIn() != googleUser.isSignIn()) {
            user.setSignIn(googleUser.isSignIn());
            FirebaseDBManager.saveUserData(user.getNetworkId(), "signIn", user.isSignIn());
            SharedPreferences.Editor prefsEditor =
                    getSharedPreferences(MyPreferencesActivity.PREFERENCES_NAME, MODE_PRIVATE)
                            .edit();
            prefsEditor.putBoolean(MyPreferencesActivity.SIGN_IN_WITH_GOOGLE, user.isSignIn());
            prefsEditor.commit();
        }

        setFinishOnTouchOutside(user.isSignIn());
        GlobalInformation.loggedUser = user;

        updateUser();
        updateUserData();
    }

    public void updateUser() {

        //EL ID SERA "NONE" CUANDO EL INTENTO DE LECTURA FALLE
        if(!user.getNetworkId().equalsIgnoreCase("NONE")) {
            //COMPROBAMOS QUE EL USUARIO NO EXISTA
            if (!hasLoaded && !dbManager.loadUser(user)) {
                hasLoaded = true;
                //EN CASO DE QUE NO EXISTA, GUARDAMOS SUS DATOS EN LA NUBE
                FirebaseDBManager.saveUserData(user);
            }
            //SE ENCONTRO UN USUARIO CON ESTE ID
            else{
                //RECOGEMOS EL USUARIO CARGADO
                if(!hasLoaded) {
                    hasLoaded = true;
                    user = GlobalInformation.loggedUser;
                }
                //Y LO GUARDAMOS EN LAS PREFERENCIAS PARA EL PROXIMO INICIO DE SESION
                SharedPreferences.Editor prefsEditor =
                        getSharedPreferences(MyPreferencesActivity.PREFERENCES_NAME, MODE_PRIVATE)
                                .edit();

                prefsEditor.putString(MyPreferencesActivity.NETWORK_ID, user.getNetworkId());
                prefsEditor.putBoolean(MyPreferencesActivity.SIGN_IN_WITH_GOOGLE, user.isSignIn());
                prefsEditor.putString(MyPreferencesActivity.USERNAME, user.getCurrentName());
                prefsEditor.putInt(MyPreferencesActivity.COINS, (int) user.getCoins());
                prefsEditor.commit();
            }

            updateUserData();
        }
    }

    public void updateUserData() {
        //ACTUALIZAMOS LOS DATOS DEL USUARIO EN EL MENU
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView)findViewById(R.id.coins_field))
                        .setText(""+user.getCoins());

                if(user.getPhotoURL() != null && !user.getPhotoURL().isEmpty()) {
                    Picasso.get()
                            .load(user.getPhotoURL())
                            .placeholder(R.mipmap.default_profile_image)
                            .error(R.mipmap.default_profile_image)
                            .transform(new CircleTransform())
                            .into((ImageButton)findViewById(R.id.google_sign_in_button));
                }
            }
        });

    }
}

