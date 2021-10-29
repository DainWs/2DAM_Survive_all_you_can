package com.joseduarte.dwssurviveallyoucan;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.joseduarte.dwssurviveallyoucan.entities.EntitiesManager;
import com.joseduarte.dwssurviveallyoucan.firebase.FirebaseDBManager;
import com.joseduarte.dwssurviveallyoucan.game.Game;
import com.joseduarte.dwssurviveallyoucan.graphics.Screen;
import com.joseduarte.dwssurviveallyoucan.util.GlobalInformation;

import java.util.concurrent.TimeUnit;

import static com.joseduarte.dwssurviveallyoucan.util.GlobalInformation.context;

public class GameActivity extends AppCompatActivity implements SensorEventListener {

    private Game game;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private boolean charging = false;

    private View countDownView;
    private View coinsCountView;

    private View gameFrame;
    private View pauseBtnFrame;

    private View leftSide;
    private View rightSide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        BroadcastReceiver mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);

        initGlobalConfig();

        setContentView(R.layout.activity_game);

        final SoundPool soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        final int soundId = soundPool.load(context, R.raw.sound_punch, 1);

        ViewGroup frame = (ViewGroup) findViewById(R.id.screen_frame_layout);
        frame.removeAllViews();
        frame.addView(game.getScreen());

        countDownView = getLayoutInflater()
                .inflate(R.layout.game_countdown, null, false);
        frame.addView(countDownView);

        coinsCountView = getLayoutInflater()
                .inflate(R.layout.game_coins_counter, null, false);
        frame.addView(coinsCountView);

        setCountdown (game.getCurrentTime());
        setCoinsCount(game.getCoins());

        gameFrame = findViewById(R.id.game_viewgroup);

        pauseBtnFrame = findViewById(R.id.game_pause_button_frame);
        findViewById(R.id.game_pause_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.pause();

                leftSide.setOnClickListener(null);
                rightSide.setOnClickListener(null);

                final ViewGroup parent = ((ViewGroup)gameFrame);

                parent.removeView(pauseBtnFrame);

                final View pauseMenu = getLayoutInflater()
                        .inflate(R.layout.game_pause_menu, parent, false);

                final int index = parent.getChildCount();
                parent.addView(pauseMenu, index);

                pauseMenu.findViewById(R.id.continue_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        parent.removeViewAt(index);
                        parent.addView(pauseBtnFrame);

                        leftSide.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EntitiesManager.playerAttackToSide(Screen.LEFT_SIDE);
                                soundPool.play(soundId, 1, 1, 0, 0, 1);
                            }
                        });

                        rightSide.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EntitiesManager.playerAttackToSide(Screen.RIGHT_SIDE);
                                soundPool.play(soundId, 1, 1, 0, 0, 1);
                            }
                        });

                        game.resume();
                    }
                });
            }
        });

        leftSide = findViewById(R.id.left_side);
        leftSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EntitiesManager.playerAttackToSide(Screen.LEFT_SIDE);
                soundPool.play(soundId, 1, 1, 0, 0, 1);
            }
        });

        rightSide = findViewById(R.id.right_side);
        rightSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EntitiesManager.playerAttackToSide(Screen.RIGHT_SIDE);
                soundPool.play(soundId, 1, 1, 0, 0, 1);
            }
        });

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        } else {
            Toast t = Toast.makeText(this, R.string.no_sensor_detected, Toast.LENGTH_LONG);
            t.show();
        }
    }

    private void initGlobalConfig() {
        GlobalInformation.context = this;
        GlobalInformation.gameActivity = this;

        GlobalInformation.lastGameCoins = 0;
        GlobalInformation.selectedTime =
                GlobalInformation.TIME_ARRAY[GlobalInformation.SELECTED_TIME_INDEX];

        game = new Game(this);
        game.start();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int precision) {}

    @Override
    public void onSensorChanged(SensorEvent evento) {
        synchronized (this) {
            switch(evento.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    GlobalInformation.QA_HORIZONTAL_SPEED = (int) evento.values[1];
                    break;
            }
        }
    }

    protected void onResume() {
        if (!ScreenReceiver.wasScreenOn) {
            System.out.println("SCREEN TURN ON");
            game.resume();
        }
        super.onResume();
        game.resume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        boolean stoping = false;
        if (ScreenReceiver.wasScreenOn) {
            System.out.println("SCREEN TURN OFF");
            stoping = true;
            game.stop();
        }
        super.onPause();

        if (!stoping) {
            game.pause();
        }
        System.out.println("OMG2");
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("OMG");
        game.restart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        game.stop();
    }

    @Override
    public void onBackPressed() {
        if (!charging) {
            ((ViewGroup) findViewById(R.id.screen_frame_layout)).removeAllViews();
            chargeStopGame();
        }
    }

    private ProgressBar progressBar;

    public void chargeStopGame(){
        setContentView(R.layout.screen_charging);
        progressBar = findViewById(R.id.progressBar);

        new Thread(new Runnable() {
            @Override
            public void run() {
                charging = true;
                progressBar.setMax(7);
                progressBar.setProgress(0);

                game.stop(progressBar);

                game.clear();
                progressBar.setProgress(7);

                GameActivity.this.finish();
            }
        }).start();
    }

    public void endView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(game.isGameOver()) {
                    final View newV = GlobalInformation.gameActivity
                            .getLayoutInflater()
                            .inflate(R.layout.end_title, null, false);

                    ((TextView) newV.findViewById(R.id.points_field))
                            .setText(getString(R.string.points) + " " + game.getCoins());

                    ((TextView) newV.findViewById(R.id.time_field))
                            .setText(game.getCurrentTime());

                    ViewGroup frame = (ViewGroup) findViewById(R.id.screen_frame_layout);
                    frame.removeView(countDownView);
                    frame.removeView(coinsCountView);
                    frame.removeView(pauseBtnFrame);
                    frame.addView(newV);

                    GlobalInformation.lastGameCoins = game.getCoins();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                TimeUnit.SECONDS.sleep(1);
                            }
                            catch (InterruptedException e) {}

                            leftSide.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onBackPressed();
                                }
                            });

                            rightSide.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onBackPressed();
                                }
                            });
                        }
                    }).start();
                }
            }
        });
    }

    public void setCountdown(final String currentTime) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView) countDownView.findViewById(R.id.time_remaining_field))
                        .setText(currentTime);
            }
        });
    }

    public void setCoinsCount(final int coinsCount) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView) coinsCountView.findViewById(R.id.coins_field))
                        .setText("" + coinsCount);
                GlobalInformation.lastGameCoins = coinsCount;
            }
        });
    }


}