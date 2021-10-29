package com.joseduarte.dwssurviveallyoucan;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.joseduarte.dwssurviveallyoucan.firebase.FirebaseDBManager;
import com.joseduarte.dwssurviveallyoucan.util.GlobalInformation;

import static com.joseduarte.dwssurviveallyoucan.util.GlobalInformation.context;

public class MyPreferencesActivity  extends AppCompatActivity {

    public static final String FPS_ID = "FPS";
    public static final String TIMER_ID = "TIMER";
    public static final String USERNAME = "USERNAME";
    public static final String NETWORK_ID = "NETWORK_ID";
    public static final String COINS = "COINS";
    public static final String SIGN_IN_WITH_GOOGLE = "SIGN_IN_WITH_GOOGLE";
    public static final String BEST_RECORDS = "BEST_RECORDS";

    public static final String PREFERENCES_NAME = "preferences";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        GlobalInformation.context = this;

        setContentView(R.layout.activity_preferences);

        final SoundPool soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        final int soundId = soundPool.load(context, R.raw.button_presed, 1);

        final String[] fpsArray = getResources().getStringArray(R.array.fps);

        Spinner spinner = findViewById(R.id.settings_fps_spinner);

        if(GlobalInformation.SELECTED_FPS_INDEX >= fpsArray.length)
            GlobalInformation.SELECTED_FPS_INDEX = 0;

        spinner.setPopupBackgroundDrawable(
                getResources().getDrawable(R.drawable.drawable_menu_button)
        );

        spinner.setSelection(GlobalInformation.SELECTED_FPS_INDEX);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                int fps = Integer.parseInt(fpsArray[position]);

                SharedPreferences.Editor prefs = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE).edit();
                prefs.putInt(FPS_ID, position);
                prefs.commit();

                GlobalInformation.FPS = fps;
                GlobalInformation.SELECTED_FPS_INDEX = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SharedPreferences prefs = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);

        final EditText usernameField = findViewById(R.id.username_field);
        usernameField.setText(prefs.getString(USERNAME, "User"));
        findViewById(R.id.save_username_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                soundPool.play(soundId, 1, 1, 0, 0, 1);

                SharedPreferences prefs = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
                String usernameText = usernameField.getText().toString();
                if(!prefs.getString(USERNAME, "User")
                        .equalsIgnoreCase(usernameText)) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(USERNAME, usernameText);
                    editor.commit();

                    GlobalInformation.USERNAME = usernameText;
                    GlobalInformation.loggedUser.setCurrentName(usernameText);
                    FirebaseDBManager.saveUserData();
                }
            }
        });


        final String[] timeArray = getResources().getStringArray(R.array.fps);

        Spinner timeSpinner = findViewById(R.id.settings_time_spinner);

        if(GlobalInformation.SELECTED_TIME_INDEX >= timeArray.length)
            GlobalInformation.SELECTED_TIME_INDEX = 0;

        timeSpinner.setPopupBackgroundDrawable(
                getResources().getDrawable(R.drawable.drawable_menu_button)
        );

        timeSpinner.setSelection(GlobalInformation.SELECTED_TIME_INDEX);

        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                int time = GlobalInformation.TIME_ARRAY[position];

                SharedPreferences.Editor prefs = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE).edit();
                prefs.putInt(TIMER_ID, position);
                prefs.commit();

                GlobalInformation.selectedTime = time;
                GlobalInformation.SELECTED_TIME_INDEX = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

}
