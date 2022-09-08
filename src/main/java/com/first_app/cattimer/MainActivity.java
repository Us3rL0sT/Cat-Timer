package com.first_app.cattimer;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout mainConstraintLayout;

    private short iDone = 1;
    private short iWell;
    private short whenStopCount = 8;
    private short untilEndCount = 4;
    private short countCircles = 0;
    private int checkAction = 0;
    private int seconds;
    private int minutes;
    private int height_phone; // экрана
    private int width_phone; // также экрана
    private int whenspace;
    private long START_TIME_IN_MILLIS = 1500 * 1000; // 1500 сек
    private long REST_TIME_IN_MILLIS = 300 * 1000; // 300 сек
    private long LONG_REST_TIME_IN_MILLIS = 900 * 1000; // 900 сек

    LinearLayout circles;
    LinearLayout circles_replace;


    private SharedPreferences pref;
    private SharedPreferences prefrest;
    private SharedPreferences preflongrest;
    private SharedPreferences check;
    private SharedPreferences autostart;
    private SharedPreferences whenstop;
    private SharedPreferences untilend;
    private SharedPreferences savedone;
    private SharedPreferences savei;
    private SharedPreferences save_exit;
    private SharedPreferences pref_vibration;
    private SharedPreferences pref_autostartrest;

    private float CurrentProgress = 99; // начинать с (-1)
    private float CurrentProgressRest = 99; // начинать с (-1)
    private float CurrentProgressLongRest = 99; // начинать с (-1)
    private ProgressBar progressBar;

    private Animation inAnimation;
    private Animation outAnimation;
    private Animation nullAnimation;
    private Animation fastAnimation;

    private TextView mTextViewCountDown;
    private TextView current_action;
    private TextView openthemenu;

    private Button mButtonStartPause;
    private Button mButtonStartPauseRest;
    private Button mButtonStartPauseLongRest;
    private Button mButtonReset;
    private Button mRestButtonReset;
    private Button mLongRestButtonReset;
    private Button edit_current_action;
    private Button addImage;


    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;
    private boolean autostartIsOn;
    private boolean isExit;
    private boolean collapse = false;
    private boolean first_start = true;
    private boolean vibration = true;
    private boolean autostartRestIsOn = true;

    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private long mRestLeftInMillis = REST_TIME_IN_MILLIS;
    private long mLongRestLeftInMillis = LONG_REST_TIME_IN_MILLIS;

    long nowTime;
    long nowTimeRest;
    long nowTimeLongRest;

    private GifImageView cat_move;
    private GifImageView cat_fall;
    private GifImageView cat_sleep;
    private GifImageView cat_question;
    private GifImageView cat_pause;



    private ImageView arrows;
    private ImageView arrows_rest;
    private ImageView arrows_long_rest;
    private ImageView deleteImage;
    private ImageView menu;

    private byte done = 1;

    private static final int NOTIFY_ID = 101;

    // Идентификатор канала

    private static final int PRIMARY_FOREGROUND_NOTIF_SERVICE_ID = 122;

    private Vibrator vibrator;













    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height_phone = displayMetrics.heightPixels;
        width_phone = displayMetrics.widthPixels;


        mainConstraintLayout = findViewById(R.id.mainConstraintLayout);

        current_action = findViewById(R.id.current_action);
        openthemenu = findViewById(R.id.openthemenu);

        mTextViewCountDown = findViewById(R.id.text_view_countdown);

        circles = findViewById(R.id.circles);
        circles_replace = findViewById(R.id.circles_replace);

        mButtonStartPause = findViewById(R.id.button_start_pause); // кнопка начала отсчета
        mButtonStartPauseRest = findViewById(R.id.button_start_pause_rest); // кнопка начала отсчета отдыха
        mButtonStartPauseLongRest = findViewById(R.id.button_start_pause_longrest); // кнопка начала отсчета длинного отдыха
        mButtonReset = findViewById(R.id.button_restart); // кнопка рестарта
        mRestButtonReset = findViewById(R.id.button_rest_restart);
        mLongRestButtonReset = findViewById(R.id.button_long_rest_restart);
        edit_current_action = findViewById(R.id.edit_current_action);
        addImage = findViewById(R.id.addImage);
        deleteImage = findViewById(R.id.deleteImage);

        inAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_in);
        outAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_out);
        nullAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_null);
        fastAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_fast);

        progressBar = findViewById(R.id.progressBar);

        cat_move = (GifImageView) findViewById(R.id.cat_move);
        cat_fall = (GifImageView) findViewById(R.id.cat_fall);
        cat_sleep = (GifImageView) findViewById(R.id.cat_sleep);
        cat_question = (GifImageView) findViewById(R.id.cat_question);
        cat_pause = (GifImageView) findViewById(R.id.cat_pause);

        arrows = findViewById(R.id.arrows);
        arrows_rest = findViewById(R.id.arrows_rest);
        arrows_long_rest = findViewById(R.id.arrows_long_rest);

        menu = findViewById(R.id.menu_icon);

        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);



        ((GifDrawable)cat_move.getDrawable()).stop();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        onStart();
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                done += 1;

                for (; iWell < done; iWell++) {
                    if (iWell == untilEndCount) {
                        u();
                    } else if (iWell == untilEndCount * 2) {
                        u();
                    } else if (iWell == untilEndCount * 3) {
                        u();
                    }else if (iWell == untilEndCount * 4) {
                        u();
                    }else if (iWell == untilEndCount * 5) {
                        u();
                    }else if (iWell == untilEndCount * 6) {
                        u();
                    }else if (iWell == untilEndCount * 7) {
                        u();
                    }else if (iWell == untilEndCount * 8) {
                        u();
                    }else if (iWell == untilEndCount * 9) {
                        u();
                    }else if (iWell == untilEndCount * 10) {
                        u();
                    }else if (iWell == untilEndCount * 11) {
                        u();
                    }else if (iWell == untilEndCount * 12) {
                        u();
                    }else if (iWell == untilEndCount * 13) {
                        u();
                    }else if (iWell == untilEndCount * 14) {
                        u();
                    }else if (iWell == untilEndCount * 15) {
                        u();
                    }else if (iWell == untilEndCount * 16) {
                        u();
                    }else if (iWell == untilEndCount * 17) {
                        u();
                    }else if (iWell == untilEndCount * 18) {
                        u();
                    }else if (iWell == untilEndCount * 19) {
                        u();
                    }else if (iWell == untilEndCount * 20) {
                        u();
                    }
                    else {
                        replaceCircles();
                        countCircles += 1;
                        countCircles();
                    }


                }

                Toast.makeText(MainActivity.this, "iWell: " + iWell, Toast.LENGTH_SHORT).show();


            }
        });

        deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                done = 0;
                Intent intent = getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                startActivity(intent);
            }
        });

    if (checkAction == 1) {
            mButtonStartPauseLongRest.setVisibility(View.INVISIBLE);
            mButtonStartPause.setVisibility(View.VISIBLE);
            mLongRestButtonReset.setVisibility(View.INVISIBLE);
            cat_move.setVisibility(View.INVISIBLE);
            cat_sleep.setVisibility(View.VISIBLE);
            cat_question.setVisibility(View.INVISIBLE);
            cat_pause.setVisibility(View.INVISIBLE);
            current_action.setText("Работа");
            arrows.setVisibility(View.VISIBLE);
            arrows_rest.setVisibility(View.INVISIBLE);
            arrows_long_rest.setVisibility(View.INVISIBLE);
            mTimeLeftInMillis = nowTime;
            updateCountDownText();

        } else if (checkAction == 2) {
            mButtonStartPauseLongRest.setVisibility(View.INVISIBLE);
            mButtonStartPause.setVisibility(View.VISIBLE);
            mLongRestButtonReset.setVisibility(View.INVISIBLE);
            mButtonReset.setVisibility(View.VISIBLE);
            cat_sleep.setVisibility(View.VISIBLE);
            cat_question.setVisibility(View.INVISIBLE);
            cat_pause.setVisibility(View.INVISIBLE);
            current_action.setText("Работа");
            arrows.setVisibility(View.VISIBLE);
            arrows_rest.setVisibility(View.INVISIBLE);
            arrows_long_rest.setVisibility(View.INVISIBLE);
            mTimeLeftInMillis = nowTime;
            CurrentProgress = 99;
            updateCountDownText();
        }
        else if (checkAction == 3) {
            mButtonStartPause.setVisibility(View.INVISIBLE);
            mButtonStartPauseRest.setVisibility(View.VISIBLE);
            mButtonReset.setVisibility(View.INVISIBLE);
            cat_move.setVisibility(View.INVISIBLE);
            cat_pause.setVisibility(View.VISIBLE);
            cat_fall.setVisibility(View.INVISIBLE);
            cat_sleep.setVisibility(View.INVISIBLE);
            current_action.setText("Отдых");
            arrows.setVisibility(View.INVISIBLE);
            arrows_rest.setVisibility(View.VISIBLE);
            arrows_long_rest.setVisibility(View.INVISIBLE);
            mRestLeftInMillis = nowTimeRest;
            restUpdateCountDownText();
        } else if (checkAction == 4) {
            mButtonStartPause.setVisibility(View.INVISIBLE);
            mButtonStartPauseRest.setVisibility(View.VISIBLE);
            mButtonReset.setVisibility(View.INVISIBLE);
            mRestButtonReset.setVisibility(View.VISIBLE);
            cat_sleep.setVisibility(View.INVISIBLE);
            cat_pause.setVisibility(View.VISIBLE);
            cat_fall.setVisibility(View.INVISIBLE);
            current_action.setText("Отдых");
            arrows.setVisibility(View.INVISIBLE);
            arrows_rest.setVisibility(View.VISIBLE);
            arrows_long_rest.setVisibility(View.INVISIBLE);
            mRestLeftInMillis = nowTimeRest;
            CurrentProgress = 99;
            restUpdateCountDownText();
        }
        else if (checkAction == 5) {
            mButtonStartPauseRest.setVisibility(View.INVISIBLE);
            mButtonStartPauseLongRest.setVisibility(View.VISIBLE);
            mRestButtonReset.setVisibility(View.INVISIBLE);
            current_action.setText("Долгий отдых");
            arrows.setVisibility(View.INVISIBLE);
            arrows_rest.setVisibility(View.INVISIBLE);
            arrows_long_rest.setVisibility(View.VISIBLE);
            mLongRestLeftInMillis = nowTimeLongRest;
            longRestUpdateCountDownText();
            updateCountDownText();
        }
        else if (checkAction == 6) {
            mButtonStartPauseRest.setVisibility(View.INVISIBLE);
            mButtonStartPauseLongRest.setVisibility(View.VISIBLE);
            mRestButtonReset.setVisibility(View.INVISIBLE);
            mLongRestButtonReset.setVisibility(View.VISIBLE);
            current_action.setText("Долгий отдых");
            arrows.setVisibility(View.INVISIBLE);
            arrows_rest.setVisibility(View.INVISIBLE);
            arrows_long_rest.setVisibility(View.VISIBLE);
            mLongRestLeftInMillis = nowTimeLongRest;
            CurrentProgress = 99;
            longRestUpdateCountDownText();
        }

        cat_sleep.setVisibility(View.VISIBLE);
        cat_fall.setVisibility(View.INVISIBLE);
        cat_move.setVisibility(View.INVISIBLE);
        mButtonReset.setVisibility(View.INVISIBLE);





        Intent iCheck = getIntent();
        if (iCheck != null) {

            Long returnLong = getIntent().getLongExtra("WORK_PERIOD", START_TIME_IN_MILLIS);

            START_TIME_IN_MILLIS = returnLong;
            mTimeLeftInMillis = START_TIME_IN_MILLIS;
            nowTime = START_TIME_IN_MILLIS;
            saveValue();



        } else {
            Toast.makeText(MainActivity.this, "Intent is null", Toast.LENGTH_SHORT).show();
        }


        Intent iCheckRest = getIntent();
        if (iCheckRest != null) {

            Long returnLong = getIntent().getLongExtra("REST_PERIOD", REST_TIME_IN_MILLIS);


            REST_TIME_IN_MILLIS = returnLong;
            mRestLeftInMillis = REST_TIME_IN_MILLIS;
            nowTimeRest = REST_TIME_IN_MILLIS;
            saveValueRest();


        } else {
            Toast.makeText(MainActivity.this, "Intent is null", Toast.LENGTH_SHORT).show();
        }

        Intent iCheckLongRest = getIntent();
        if (iCheckLongRest != null) {

            Long returnLong = getIntent().getLongExtra("LONG_REST_PERIOD", LONG_REST_TIME_IN_MILLIS);


            LONG_REST_TIME_IN_MILLIS = returnLong;
            mLongRestLeftInMillis = LONG_REST_TIME_IN_MILLIS;
            nowTimeLongRest = LONG_REST_TIME_IN_MILLIS;
            saveValueLongRest();

        } else {
            Toast.makeText(MainActivity.this, "Intent is null", Toast.LENGTH_SHORT).show();
        }

        Intent iCheckAutostart = getIntent();
        if (iCheckAutostart != null) {

            Boolean returnLong = getIntent().getBooleanExtra("AUTOSTART_CHECK", autostartIsOn);


            autostartIsOn = returnLong;
            saveValueAutostart();
        } else {
            Toast.makeText(MainActivity.this, "Intent is null", Toast.LENGTH_SHORT).show();
        }

        Intent iCheckWhenStop = getIntent();
        if (iCheckWhenStop != null) {

            short returnLong = getIntent().getShortExtra("WHEN_STOP", whenStopCount);
            whenStopCount = returnLong;
            saveValueWhenStop();

        } else {
            Toast.makeText(MainActivity.this, "Intent is null", Toast.LENGTH_SHORT).show();
        }
        Intent iCheckUntilEnd = getIntent();
        if (iCheckUntilEnd != null) {

            short returnLong = getIntent().getShortExtra("UNTIL_END", untilEndCount);
            untilEndCount = returnLong;
            saveValueUntilEnd();

        } else {
            Toast.makeText(MainActivity.this, "Intent is null", Toast.LENGTH_SHORT).show();
        }

        Intent iCheckVibration = getIntent();
        if (iCheckVibration != null) {

            boolean returnLong = getIntent().getBooleanExtra("VIBRATION", vibration);
            vibration = returnLong;
            saveValueVibration();

        } else {
            Toast.makeText(MainActivity.this, "Intent is null", Toast.LENGTH_SHORT).show();
        }

        Intent iCheckAutostartRest = getIntent();
        if (iCheckAutostartRest != null) {

            boolean returnLong = getIntent().getBooleanExtra("AUTOSTARTREST", autostartRestIsOn);
            autostartRestIsOn = returnLong;
            saveValueAutostartRest();

        } else {
            Toast.makeText(MainActivity.this, "Intent is null", Toast.LENGTH_SHORT).show();
        }


        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat_sleep.setVisibility(View.INVISIBLE);
                cat_fall.setVisibility(View.INVISIBLE);
                arrows.startAnimation(outAnimation);
                edit_current_action.setVisibility(View.INVISIBLE);
                menu.startAnimation(outAnimation);
                if (mTimerRunning) {
                    menu.setVisibility(View.INVISIBLE);
                    menu.startAnimation(nullAnimation);
                    ((GifDrawable)cat_move.getDrawable()).stop();
                    visibilityNo();
                    pauseTimer();
                } else {
                    clickableAnimation();
                    cat_move.startAnimation(fastAnimation);
                    cat_sleep.setVisibility(View.INVISIBLE);
                    menu.setVisibility(View.INVISIBLE);
                    menu.startAnimation(nullAnimation);


                    ((GifDrawable)cat_move.getDrawable()).start();
                    CurrentProgress = (float) (CurrentProgress + (1.666666 / (nowTime / 1000 / 60)));
                    startTimer();


                }
            }
        });

        mButtonStartPauseRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cat_sleep.setVisibility(View.VISIBLE);
                cat_fall.setVisibility(View.INVISIBLE);
                cat_pause.setVisibility(View.INVISIBLE);
                arrows_rest.startAnimation(outAnimation);
                edit_current_action.setVisibility(View.INVISIBLE);
                menu.startAnimation(outAnimation);
                cat_question.setVisibility(View.INVISIBLE);

                visibilityNo();
                if (mTimerRunning) {
                    menu.setVisibility(View.INVISIBLE);
                    menu.startAnimation(nullAnimation);
                    ((GifDrawable)cat_sleep.getDrawable()).stop();
                    pauseTimerRest();
                } else {
                    clickableAnimationRest();
                    cat_sleep.startAnimation(fastAnimation);
                    cat_question.setVisibility(View.INVISIBLE);
                    menu.setVisibility(View.INVISIBLE);
                    menu.startAnimation(nullAnimation);

                    ((GifDrawable)cat_sleep.getDrawable()).start();
                    CurrentProgressRest = (float) (CurrentProgressRest + (1.666666 / (nowTimeRest / 1000 / 60)));
                    restTimer();


                }
            }
        });

        mButtonStartPauseLongRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cat_sleep.setVisibility(View.VISIBLE);
                cat_fall.setVisibility(View.INVISIBLE);
                arrows_long_rest.startAnimation(outAnimation);
                cat_pause.setVisibility(View.INVISIBLE);
                edit_current_action.setVisibility(View.INVISIBLE);
                menu.startAnimation(outAnimation);
                visibilityNo();

                if (mTimerRunning) {
                    menu.setVisibility(View.INVISIBLE);
                    menu.startAnimation(nullAnimation);
                    ((GifDrawable)cat_sleep.getDrawable()).stop();
                    pauseTimerLongRest();
                } else {
                    clickableAnimationLongRest();
                    menu.setVisibility(View.INVISIBLE);
                    menu.startAnimation(nullAnimation);
                    cat_sleep.startAnimation(fastAnimation);
                    cat_question.setVisibility(View.INVISIBLE);

                    ((GifDrawable)cat_sleep.getDrawable()).start();
                    CurrentProgressLongRest = (float) (CurrentProgressLongRest + (1.666666 / (nowTimeLongRest / 1000 / 60)));
                    longRestTimer();


                }
            }
        });

        // Смена текущего действия
        edit_current_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (current_action.getText().toString().matches("Работа")) {
                    if (mButtonReset.getVisibility() == View.INVISIBLE) {
                        clickableAnimationChange();
                        mButtonStartPause.setVisibility(View.INVISIBLE);
                        mButtonStartPauseRest.setVisibility(View.VISIBLE);
                        mButtonReset.setVisibility(View.INVISIBLE);
                        cat_move.setVisibility(View.INVISIBLE);
                        cat_pause.setVisibility(View.VISIBLE);
                        cat_fall.setVisibility(View.INVISIBLE);
                        cat_sleep.setVisibility(View.INVISIBLE);
                        current_action.setText("Отдых");
                        arrows.setVisibility(View.INVISIBLE);
                        arrows_rest.setVisibility(View.VISIBLE);
                        arrows_long_rest.setVisibility(View.INVISIBLE);
                        mRestLeftInMillis = nowTimeRest;
                        cat_pause.startAnimation(fastAnimation);
                        restUpdateCountDownText();
                    } else
                    if (mButtonReset.getVisibility() == View.VISIBLE) {
                        clickableAnimationChange();
                        mButtonStartPause.setVisibility(View.INVISIBLE);
                        mButtonStartPauseRest.setVisibility(View.VISIBLE);
                        mButtonReset.setVisibility(View.INVISIBLE);
                        mRestButtonReset.setVisibility(View.VISIBLE);
                        cat_sleep.setVisibility(View.INVISIBLE);
                        cat_pause.setVisibility(View.VISIBLE);
                        cat_fall.setVisibility(View.INVISIBLE);
                        current_action.setText("Отдых");
                        arrows.setVisibility(View.INVISIBLE);
                        arrows_rest.setVisibility(View.VISIBLE);
                        arrows_long_rest.setVisibility(View.INVISIBLE);
                        mRestLeftInMillis = nowTimeRest;
                        CurrentProgress = 99;
                        cat_pause.startAnimation(fastAnimation);
                        restUpdateCountDownText();
                    }

                } else
                if (current_action.getText().toString().matches("Отдых")) {
                    if (mRestButtonReset.getVisibility() == View.INVISIBLE) {
                        clickableAnimationChange();
                        mButtonStartPauseRest.setVisibility(View.INVISIBLE);
                        mButtonStartPauseLongRest.setVisibility(View.VISIBLE);
                        mRestButtonReset.setVisibility(View.INVISIBLE);
                        current_action.setText("Долгий отдых");
                        arrows.setVisibility(View.INVISIBLE);
                        arrows_rest.setVisibility(View.INVISIBLE);
                        arrows_long_rest.setVisibility(View.VISIBLE);
                        mLongRestLeftInMillis = nowTimeLongRest;
                        cat_pause.startAnimation(fastAnimation);
                        longRestUpdateCountDownText();
                    } else
                    if (mRestButtonReset.getVisibility() == View.VISIBLE) {
                        clickableAnimationChange();
                        mButtonStartPauseRest.setVisibility(View.INVISIBLE);
                        mButtonStartPauseLongRest.setVisibility(View.VISIBLE);
                        mRestButtonReset.setVisibility(View.INVISIBLE);
                        mLongRestButtonReset.setVisibility(View.VISIBLE);
                        current_action.setText("Долгий отдых");
                        arrows.setVisibility(View.INVISIBLE);
                        arrows_rest.setVisibility(View.INVISIBLE);
                        arrows_long_rest.setVisibility(View.VISIBLE);
                        mLongRestLeftInMillis = nowTimeLongRest;
                        CurrentProgress = 99;
                        cat_pause.startAnimation(fastAnimation);
                        longRestUpdateCountDownText();
                    }

                } else if (current_action.getText().toString().matches("Долгий отдых")){
                    if (mLongRestButtonReset.getVisibility() == View.INVISIBLE) {
                        clickableAnimationChange();
                        mButtonStartPauseLongRest.setVisibility(View.INVISIBLE);
                        mButtonStartPause.setVisibility(View.VISIBLE);
                        mLongRestButtonReset.setVisibility(View.INVISIBLE);
                        cat_move.setVisibility(View.INVISIBLE);
                        cat_sleep.setVisibility(View.VISIBLE);
                        cat_question.setVisibility(View.INVISIBLE);
                        cat_pause.setVisibility(View.INVISIBLE);
                        current_action.setText("Работа");
                        arrows.setVisibility(View.VISIBLE);
                        arrows_rest.setVisibility(View.INVISIBLE);
                        arrows_long_rest.setVisibility(View.INVISIBLE);
                        mTimeLeftInMillis = nowTime;
                        cat_sleep.startAnimation(fastAnimation);
                        updateCountDownText();
                    } else
                    if (mLongRestButtonReset.getVisibility() == View.VISIBLE) {
                        clickableAnimationChange();
                        mButtonStartPauseLongRest.setVisibility(View.INVISIBLE);
                        mButtonStartPause.setVisibility(View.VISIBLE);
                        mLongRestButtonReset.setVisibility(View.INVISIBLE);
                        mButtonReset.setVisibility(View.VISIBLE);
                        cat_sleep.setVisibility(View.VISIBLE);
                        cat_question.setVisibility(View.INVISIBLE);
                        cat_pause.setVisibility(View.INVISIBLE);
                        current_action.setText("Работа");
                        arrows.setVisibility(View.VISIBLE);
                        arrows_rest.setVisibility(View.INVISIBLE);
                        arrows_long_rest.setVisibility(View.INVISIBLE);
                        mTimeLeftInMillis = nowTime;
                        CurrentProgress = 99;
                        cat_sleep.startAnimation(fastAnimation);
                        updateCountDownText();
                    }

                }
            }
        });

        View.OnClickListener goMenu = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               isExit = true;
               saveExit();
               saveI();
               sendValue();
               onPause();



            }
        };
        menu.setOnClickListener(goMenu);


        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        mRestButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetRestTimer();
            }
        });

        mLongRestButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetLongRestTimer();
            }
        });


        showNotification();


        updateCountDownText();




















    } // ONCREATE


    private void showNotification() {

//Notification Channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String id = "_channel_01";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = new NotificationChannel(id, "notification", importance);
            mChannel.enableLights(true);

            Intent notificationIntent = new Intent(MainActivity.this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this,
                    0, notificationIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            Notification notification = new Notification.Builder(getApplicationContext(), id)
                    .setSmallIcon(R.drawable.arrows)
                    .setContentTitle("Таймер готов к старту!")
                    .setContentText(String.valueOf((START_TIME_IN_MILLIS / 1000) / 60) + ":00")
                    .addAction(R.drawable.arrows, "Открыть", pendingIntent)
                    .build();

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(mChannel);
                mNotificationManager.notify(PRIMARY_FOREGROUND_NOTIF_SERVICE_ID, notification);
            }

            startForeground(PRIMARY_FOREGROUND_NOTIF_SERVICE_ID, notification);
        }
    }





    private void startForeground(int primaryForegroundNotifServiceId, Notification notification) {
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadExit();

        if (collapse == false) {
            if (isExit == true) {
                loadI();
                loadDone();
                for (int i = 0; i < whenStopCount; i++) {
                    if (i == untilEndCount)
                        f();
                    else if (i == untilEndCount * 2)
                        f();
                    else if (i == untilEndCount * 3)
                        f();
                    else if (i == untilEndCount * 4)
                        f();
                    else if (i == untilEndCount * 5)
                        f();
                    else if (i == untilEndCount * 6)
                        f();
                    else if (i == untilEndCount * 7)
                        f();
                    else if (i == untilEndCount * 8)
                        f();
                    else if (i == untilEndCount * 9)
                        f();
                    else if (i == untilEndCount * 10)
                        f();
                    else if (i == untilEndCount * 11)
                        f();
                    else if (i == untilEndCount * 12)
                        f();
                    else if (i == untilEndCount * 13)
                        f();
                    else if (i == untilEndCount * 14)
                        f();
                    else if (i == untilEndCount * 15)
                        f();
                    else if (i == untilEndCount * 16)
                        f();
                    else if (i == untilEndCount * 17)
                        f();
                    else if (i == untilEndCount * 18)
                        f();
                    else if (i == untilEndCount * 19)
                        f();
                    else if (i == untilEndCount * 20)
                        f();

                    else {
                        addStartCircles();
                        countCircles += 1;
                        countCircles();
                    }
                }
                for (iDone = 0; iDone < done; iDone++) {
                    saveI();
                    if (iDone == untilEndCount) {
                        u();
                    } else if (iDone == untilEndCount * 2) {
                        u();
                    } else if (iDone == untilEndCount * 3) {
                        u();
                    }else if (iDone == untilEndCount * 4) {
                        u();
                    }else if (iDone == untilEndCount * 5) {
                        u();
                    }else if (iDone == untilEndCount * 6) {
                        u();
                    }else if (iDone == untilEndCount * 7) {
                        u();
                    }else if (iDone == untilEndCount * 8) {
                        u();
                    }else if (iDone == untilEndCount * 9) {
                        u();
                    }else if (iDone == untilEndCount * 10) {
                        u();
                    }else if (iDone == untilEndCount * 11) {
                        u();
                    }else if (iDone == untilEndCount * 12) {
                        u();
                    }else if (iDone == untilEndCount * 13) {
                        u();
                    }else if (iDone == untilEndCount * 14) {
                        u();
                    }else if (iDone == untilEndCount * 15) {
                        u();
                    }else if (iDone == untilEndCount * 16) {
                        u();
                    }else if (iDone == untilEndCount * 17) {
                        u();
                    }else if (iDone == untilEndCount * 18) {
                        u();
                    }else if (iDone == untilEndCount * 19) {
                        u();
                    }else if (iDone == untilEndCount * 20) {
                        u();
                    }
                    else {
                        replaceCircles();
                        countCircles += 1;
                        countCircles();
                    }

                }
            }
        }
        else {
                done = 0;
                iDone = 0;

                loadI();
                loadDone();
                for (int i = 0; i < whenStopCount; i++) {
                    if (i == untilEndCount)
                        f();
                    else if (i == untilEndCount * 2)
                        f();
                    else if (i == untilEndCount * 3)
                        f();
                    else if (i == untilEndCount * 4)
                        f();
                    else if (i == untilEndCount * 5)
                        f();
                    else if (i == untilEndCount * 6)
                        f();
                    else if (i == untilEndCount * 7)
                        f();
                    else if (i == untilEndCount * 8)
                        f();
                    else if (i == untilEndCount * 9)
                        f();
                    else if (i == untilEndCount * 10)
                        f();
                    else if (i == untilEndCount * 11)
                        f();
                    else if (i == untilEndCount * 12)
                        f();
                    else if (i == untilEndCount * 13)
                        f();
                    else if (i == untilEndCount * 14)
                        f();
                    else if (i == untilEndCount * 15)
                        f();
                    else if (i == untilEndCount * 16)
                        f();
                    else if (i == untilEndCount * 17)
                        f();
                    else if (i == untilEndCount * 18)
                        f();
                    else if (i == untilEndCount * 19)
                        f();
                    else if (i == untilEndCount * 20)
                        f();

                    else {
                        addStartCircles();
                        countCircles += 1;
                        countCircles();
                    }
                }
                for (iDone = 0; iDone < done; iDone++) {
                    saveI();
                    if (iDone == untilEndCount) {
                        u();
                    } else if (iDone == untilEndCount * 2) {
                        u();
                    } else if (iDone == untilEndCount * 3) {
                        u();
                    }else if (iDone == untilEndCount * 4) {
                        u();
                    }else if (iDone == untilEndCount * 5) {
                        u();
                    }else if (iDone == untilEndCount * 6) {
                        u();
                    }else if (iDone == untilEndCount * 7) {
                        u();
                    }else if (iDone == untilEndCount * 8) {
                        u();
                    }else if (iDone == untilEndCount * 9) {
                        u();
                    }else if (iDone == untilEndCount * 10) {
                        u();
                    }else if (iDone == untilEndCount * 11) {
                        u();
                    }else if (iDone == untilEndCount * 12) {
                        u();
                    }else if (iDone == untilEndCount * 13) {
                        u();
                    }else if (iDone == untilEndCount * 14) {
                        u();
                    }else if (iDone == untilEndCount * 15) {
                        u();
                    }else if (iDone == untilEndCount * 16) {
                        u();
                    }else if (iDone == untilEndCount * 17) {
                        u();
                    }else if (iDone == untilEndCount * 18) {
                        u();
                    }else if (iDone == untilEndCount * 19) {
                        u();
                    }else if (iDone == untilEndCount * 20) {
                        u();
                    }
                    else {
                        replaceCircles();
                        countCircles += 1;
                        countCircles();
                    }
            }






        }
        collapse = true;

        }








    @Override
    protected void onStart() {
        super.onStart();

        if (first_start == true) {
            if (collapse == false) {
                if (isExit == true) {
                    loadI();
                    loadDone();
                    for (int i = 0; i < whenStopCount; i++) {
                        for (int j = 1; j < 21; j++) {
                            if (i == untilEndCount * j)
                                f();
                            else {
                                addStartCircles();
                                countCircles += 1;
                                countCircles();
                            }
                        }


                    }
                    for (iDone = 0; iDone < done; iDone++) {
                        saveI();

                        for (int j = 1; j < 21; j++) {
                            if (iDone == untilEndCount * j)
                                f();
                            else {
                                replaceCircles();
                                countCircles += 1;
                                countCircles();
                            }
                        }

                    }
                }
            }
            else {
                done = 0;
                iDone = 0;

                loadI();
                loadDone();
                for (int i = 0; i < whenStopCount; i++) {
                    for (int j = 1; j < 21; j++) {
                        if (i == untilEndCount * j)
                            f();
                        else {
                            addStartCircles();
                            countCircles += 1;
                            countCircles();
                        }
                    }
                }
                for (iDone = 0; iDone < done; iDone++) {
                    saveI();

                    for (int j = 1; j < 21; j++) {
                        if (iDone == untilEndCount * j)
                            f();
                        else {
                            replaceCircles();
                            countCircles += 1;
                            countCircles();
                        }
                    }
                }






            }
            collapse = true;
            first_start = false;


        }

        Toast.makeText(MainActivity.this, "HEIGHT: " + height_phone + " WIDTH: " + width_phone, Toast.LENGTH_SHORT).show();

        loadCheckAction();
        loadValueAutostart();
        loadValueWhenStop();
        loadValueUntilEnd();
        loadValueVibration();
        loadValueAutostartRest();



        loadValue();
        loadValueRest();
        loadValueLongRest();



        if (current_action.getText().toString().matches("Работа")) {
            updateCountDownText();
        }
        if (current_action.getText().toString().matches("Отдых")) {
            restUpdateCountDownText();
            cat_sleep.setVisibility(View.INVISIBLE);
            cat_pause.setVisibility(View.VISIBLE);
        }
        if (current_action.getText().toString().matches("Долгий отдых")) {
            longRestUpdateCountDownText();
            cat_sleep.setVisibility(View.INVISIBLE);
            cat_pause.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        collapse = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveDone();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isExit = true;
        saveExit();
    }

    private void startTimer() { // 25 минутный таймер
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                progressBar.setProgress((int)CurrentProgress, true); // установка значения

                if (START_TIME_IN_MILLIS > 0) {
                    START_TIME_IN_MILLIS -= 1000;
                    CurrentProgress = (float) (CurrentProgress - (1.699996 / (nowTime / 1000 / 60)));
                }
                updateCountDownText();
                current_action.setText("Работа");
                cat_sleep.setVisibility(View.INVISIBLE);
                cat_move.setVisibility(View.VISIBLE);
                arrows.setVisibility(View.INVISIBLE);
                arrows_rest.setVisibility(View.INVISIBLE);
                arrows_long_rest.setVisibility(View.INVISIBLE);
                deleteImage.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFinish() {

                if (done == untilEndCount) {
                    if (autostartRestIsOn == true) {
                        if (vibration == true)
                            vibrator.vibrate(500);
                        CurrentProgress = 99;
                        REST_TIME_IN_MILLIS = nowTimeRest;
                        mRestButtonReset.setVisibility(View.INVISIBLE);
                        mButtonStartPauseLongRest.setVisibility(View.VISIBLE);
                        mButtonStartPauseRest.setVisibility(View.INVISIBLE);
                        mLongRestLeftInMillis = LONG_REST_TIME_IN_MILLIS;
                        longRestUpdateCountDownText();
                        longRestTimer();

                    } else {
                        if (vibration == true)
                            vibrator.vibrate(500);
                        CurrentProgress = 99;
                        REST_TIME_IN_MILLIS = nowTimeRest;
                        mRestButtonReset.setVisibility(View.INVISIBLE);
                        mButtonStartPauseLongRest.setVisibility(View.VISIBLE);
                        mButtonStartPauseRest.setVisibility(View.INVISIBLE);
                        mLongRestLeftInMillis = LONG_REST_TIME_IN_MILLIS;
                        longRestUpdateCountDownText();
                        longRestTimer();
                        pauseTimerLongRest();

                    }
                } else {
                    if (autostartRestIsOn == true) {
                        if (vibration == true)
                            vibrator.vibrate(500);
                        CurrentProgress = 99;
                        START_TIME_IN_MILLIS = nowTime;
                        mButtonReset.setVisibility(View.INVISIBLE);
                        mButtonStartPauseRest.setVisibility(View.VISIBLE);
                        mButtonStartPause.setVisibility(View.INVISIBLE);
                        mRestLeftInMillis = REST_TIME_IN_MILLIS;
                        restUpdateCountDownText();
                        restTimer();
                        Toast.makeText(MainActivity.this, "autostart: " + autostartIsOn, Toast.LENGTH_SHORT).show();
                    } else {
                        if (vibration == true)
                            vibrator.vibrate(500);
                        CurrentProgress = 99;
                        START_TIME_IN_MILLIS = nowTime;
                        mButtonReset.setVisibility(View.INVISIBLE);
                        mButtonStartPauseRest.setVisibility(View.VISIBLE);
                        mButtonStartPause.setVisibility(View.INVISIBLE);
                        mRestLeftInMillis = REST_TIME_IN_MILLIS;
                        restUpdateCountDownText();
                        pauseTimer();

                    }
                }

            }
        }.start();




        mTimerRunning = true;
        mButtonReset.setVisibility(View.INVISIBLE);
    }

    private void restTimer() { // 5 минутный таймер
        mCountDownTimer = new CountDownTimer(mRestLeftInMillis, 1000) {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTick(long restMillisUntilFinished) {
                mRestLeftInMillis = restMillisUntilFinished;
                progressBar.setProgress((int)CurrentProgressRest,true);
                if (REST_TIME_IN_MILLIS > 0) {
                    REST_TIME_IN_MILLIS -= 1000;
                    CurrentProgressRest = (float) (CurrentProgressRest - (1.699996 / (nowTimeRest / 1000 / 60)));
                }
                restUpdateCountDownText();
                current_action.setText("Отдых");
                cat_question.startAnimation(nullAnimation);
                cat_sleep.setVisibility(View.VISIBLE);
                cat_move.setVisibility(View.INVISIBLE);
                cat_question.setVisibility(View.INVISIBLE);
                mRestButtonReset.setVisibility(View.INVISIBLE);
                arrows.setVisibility(View.INVISIBLE);
                arrows_rest.setVisibility(View.INVISIBLE);
                arrows_long_rest.setVisibility(View.INVISIBLE);
                deleteImage.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFinish() {
                if (autostartRestIsOn == true) {
                    if (vibration == true)
                        vibrator.vibrate(500);
                    CurrentProgress = 99;
                    LONG_REST_TIME_IN_MILLIS = nowTimeRest;
                    mLongRestButtonReset.setVisibility(View.INVISIBLE);
                    mButtonStartPause.setVisibility(View.VISIBLE);
                    mButtonStartPauseLongRest.setVisibility(View.INVISIBLE);
                    cat_sleep.setVisibility(View.INVISIBLE);
                    cat_move.setVisibility(View.VISIBLE);
                    mTimeLeftInMillis = nowTime;
                    ((GifDrawable)cat_move.getDrawable()).start();
                    updateCountDownText();
                    startTimer();
                } else {
                    if (vibration == true)
                        vibrator.vibrate(500);
                    CurrentProgress = 99;
                    LONG_REST_TIME_IN_MILLIS = nowTimeRest;
                    mLongRestButtonReset.setVisibility(View.INVISIBLE);
                    mButtonStartPause.setVisibility(View.VISIBLE);
                    mButtonStartPauseLongRest.setVisibility(View.INVISIBLE);
                    cat_sleep.setVisibility(View.INVISIBLE);
                    cat_move.setVisibility(View.VISIBLE);
                    mTimeLeftInMillis = nowTime;
                    ((GifDrawable)cat_move.getDrawable()).start();
                    updateCountDownText();
                    pauseTimerRest();
                }

            }
        }.start();





        mTimerRunning = true;
        mRestButtonReset.setVisibility(View.INVISIBLE);
    }

    private void longRestTimer() { // 15 минутный таймер
        mCountDownTimer = new CountDownTimer(mLongRestLeftInMillis, 1000) {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTick(long longRestMillisUntilFinished) {
                mLongRestLeftInMillis = longRestMillisUntilFinished;
                progressBar.setProgress((int)CurrentProgressLongRest,true); // установка значения
                if (LONG_REST_TIME_IN_MILLIS > 0) {
                    LONG_REST_TIME_IN_MILLIS -= 1000;
                    CurrentProgressLongRest = (float) (CurrentProgressLongRest - (1.699996 / (nowTimeLongRest / 1000 / 60)));
                }
                longRestUpdateCountDownText();
                current_action.setText("Долгий отдых");
                cat_sleep.setVisibility(View.VISIBLE);
                cat_move.setVisibility(View.INVISIBLE);
                cat_question.setVisibility(View.INVISIBLE);
                mLongRestButtonReset.setVisibility(View.INVISIBLE);
                arrows.setVisibility(View.INVISIBLE);
                arrows_rest.setVisibility(View.INVISIBLE);
                arrows_long_rest.setVisibility(View.INVISIBLE);
                deleteImage.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFinish() {
                if (autostartIsOn == true) {
                    if (vibration == true)
                        vibrator.vibrate(500);
                    CurrentProgress = 99;
                    LONG_REST_TIME_IN_MILLIS = nowTimeRest;
                    mLongRestButtonReset.setVisibility(View.INVISIBLE);
                    mButtonStartPause.setVisibility(View.VISIBLE);
                    mButtonStartPauseLongRest.setVisibility(View.INVISIBLE);
                    cat_sleep.setVisibility(View.INVISIBLE);
                    cat_move.setVisibility(View.VISIBLE);
                    mTimeLeftInMillis = nowTime;
                    ((GifDrawable)cat_move.getDrawable()).start();
                    updateCountDownText();
                    startTimer();
                } else {
                    if (vibration == true)
                        vibrator.vibrate(500);
                    CurrentProgress = 99;
                    LONG_REST_TIME_IN_MILLIS = nowTimeRest;
                    mLongRestButtonReset.setVisibility(View.INVISIBLE);
                    mButtonStartPause.setVisibility(View.VISIBLE);
                    mButtonStartPauseLongRest.setVisibility(View.INVISIBLE);
                    cat_sleep.setVisibility(View.INVISIBLE);
                    cat_move.setVisibility(View.VISIBLE);
                    mTimeLeftInMillis = nowTime;
                    ((GifDrawable)cat_move.getDrawable()).start();
                    updateCountDownText();
                    pauseTimerLongRest();
                }
            }
        }.start();





        mTimerRunning = true;
        mLongRestButtonReset.setVisibility(View.INVISIBLE);
    }

    private void pauseTimer() {
        clickableAnimation();
        if (mCountDownTimer != null) mCountDownTimer.cancel();
        mTimerRunning = false;
        mButtonReset.setVisibility(View.VISIBLE);
        cat_sleep.startAnimation(fastAnimation);
        cat_move.setVisibility(View.INVISIBLE);
        arrows.setVisibility(View.INVISIBLE);
        edit_current_action.setVisibility(View.INVISIBLE);
        if (seconds == 0 && minutes == 0) {
            done -= 1;
        }
        START_TIME_IN_MILLIS += 1000;
        menu.startAnimation(nullAnimation);
        arrows.startAnimation(nullAnimation);
        cat_sleep.setVisibility(View.VISIBLE);

    }

    private void pauseTimerRest() {
        clickableAnimationRest();
        if (mCountDownTimer != null) mCountDownTimer.cancel();
        mTimerRunning = false;
        cat_move.setVisibility(View.INVISIBLE);
        cat_sleep.setVisibility(View.INVISIBLE);
        arrows_rest.setVisibility(View.INVISIBLE);
        edit_current_action.setVisibility(View.INVISIBLE);
        REST_TIME_IN_MILLIS += 1000;
        menu.startAnimation(nullAnimation);
        arrows_rest.startAnimation(nullAnimation);
        cat_question.startAnimation(fastAnimation);
        mRestButtonReset.startAnimation(fastAnimation);
        visibilityPause();
    }

    private void pauseTimerLongRest() {
        clickableAnimationLongRest();
        if (mCountDownTimer != null) mCountDownTimer.cancel();
        mTimerRunning = false;
        mLongRestButtonReset.setVisibility(View.VISIBLE);
        cat_sleep.setVisibility(View.INVISIBLE);
        cat_move.setVisibility(View.INVISIBLE);
        arrows_long_rest.setVisibility(View.INVISIBLE);
        edit_current_action.setVisibility(View.INVISIBLE);
        LONG_REST_TIME_IN_MILLIS += 1000;
        menu.startAnimation(nullAnimation);
        cat_question.startAnimation(fastAnimation);
        mLongRestButtonReset.startAnimation(fastAnimation);
        arrows_long_rest.startAnimation(nullAnimation);
        visibilityLongPause();
    }

    private void resetTimer() {
        clickableAnimationReset();
        CurrentProgress = 99; // начинать с (-1)
        mTimeLeftInMillis = nowTime;
        START_TIME_IN_MILLIS = nowTime;
        updateCountDownText();
        cat_move.setVisibility(View.INVISIBLE);
        cat_sleep.setVisibility(View.INVISIBLE);
        cat_fall.setVisibility(View.VISIBLE);
        mButtonReset.setVisibility(View.INVISIBLE);
        mButtonStartPause.setVisibility(View.VISIBLE);
        deleteImage.startAnimation(inAnimation);
        arrows.startAnimation(inAnimation);
        edit_current_action.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
        menu.startAnimation(inAnimation);
        visibilityYes();
        ((GifDrawable)cat_fall.getDrawable()).reset();
        gifTimer();
        arrows.setVisibility(View.VISIBLE);
        arrows_rest.setVisibility(View.INVISIBLE);
        arrows_long_rest.setVisibility(View.INVISIBLE);
        cat_fall.startAnimation(fastAnimation);
    }

    private void resetRestTimer() {
        clickableAnimationReset();
        CurrentProgressRest = 99; // начинать с (-1)
        mRestLeftInMillis = nowTimeRest;
        REST_TIME_IN_MILLIS = nowTimeRest;
        restUpdateCountDownText();
        cat_move.setVisibility(View.INVISIBLE);
        cat_question.setVisibility(View.INVISIBLE);
        cat_sleep.setVisibility(View.INVISIBLE);
        cat_pause.setAnimation(fastAnimation);
        mRestButtonReset.setVisibility(View.INVISIBLE);
        mButtonStartPauseRest.setVisibility(View.VISIBLE);
        deleteImage.startAnimation(inAnimation);
        arrows_rest.startAnimation(inAnimation);
        menu.startAnimation(inAnimation);
        progressBar.setProgress(0);
        visibilityYes();
        edit_current_action.setVisibility(View.VISIBLE);
        ((GifDrawable)cat_fall.getDrawable()).reset();
        cat_pause.setVisibility(View.VISIBLE);
        arrows.setVisibility(View.INVISIBLE);
        arrows_rest.setVisibility(View.VISIBLE);
        arrows_long_rest.setVisibility(View.INVISIBLE);
        cat_pause.startAnimation(fastAnimation);
    }

    private void resetLongRestTimer() {
        clickableAnimationReset();
        CurrentProgressLongRest = 99; // начинать с (-1)
        mLongRestLeftInMillis = nowTimeLongRest;
        LONG_REST_TIME_IN_MILLIS = nowTimeLongRest;
        longRestUpdateCountDownText();
        cat_move.setVisibility(View.INVISIBLE);
        cat_question.setVisibility(View.INVISIBLE);
        cat_sleep.setVisibility(View.INVISIBLE);
        cat_pause.setAnimation(fastAnimation);
        mLongRestButtonReset.setVisibility(View.INVISIBLE);
        mButtonStartPauseLongRest.setVisibility(View.VISIBLE);
        deleteImage.startAnimation(inAnimation);
        arrows_long_rest.startAnimation(inAnimation);
        menu.startAnimation(inAnimation);
        progressBar.setProgress(0);
        visibilityYes();
        edit_current_action.setVisibility(View.VISIBLE);
        ((GifDrawable)cat_fall.getDrawable()).reset();
        cat_pause.setVisibility(View.VISIBLE);
        arrows.setVisibility(View.INVISIBLE);
        arrows_rest.setVisibility(View.INVISIBLE);
        arrows_long_rest.setVisibility(View.VISIBLE);
        cat_pause.startAnimation(fastAnimation);
    }

    private void gifTimer() {
        new CountDownTimer(1190, 1190) {

            public void onTick(long millisUntilFinished) {
                // You don't need to use this.
            }

            public void onFinish() {
                ((GifDrawable)cat_fall.getDrawable()).stop();
            }

        }.start();
    }

    private void visibilityNo(){
        new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {
                // You don't need to use this.
            }

            public void onFinish() {
                menu.setVisibility(View.INVISIBLE);
            }

        }.start();
    }

    private void visibilityYes(){
        new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {
                // You don't need to use this.
            }

            public void onFinish() {
                menu.setVisibility(View.VISIBLE);
                deleteImage.setVisibility(View.VISIBLE);
            }

        }.start();
    }

    private void visibilityPause(){
        new CountDownTimer(400, 100) {

            public void onTick(long millisUntilFinished) {
                // You don't need to use this.
            }

            public void onFinish() {
                cat_question.setVisibility(View.VISIBLE);
                mRestButtonReset.setVisibility(View.VISIBLE);
                cat_sleep.setVisibility(View.INVISIBLE);
            }

        }.start();
    }

    private void visibilityLongPause(){
        new CountDownTimer(400, 100) {

            public void onTick(long millisUntilFinished) {
                // You don't need to use this.
            }

            public void onFinish() {
                cat_question.setVisibility(View.VISIBLE);
                mLongRestButtonReset.setVisibility(View.VISIBLE);
            }

        }.start();
    }

    private void visibilityOpenTheMenuText(){
        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                // You don't need to use this.
            }

            public void onFinish() {
                openthemenu.setVisibility(View.INVISIBLE);
            }

        }.start();
    }

    private void visibilityCatMove(){
        new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {
                // You don't need to use this.
            }

            public void onFinish() {
                cat_move.setVisibility(View.INVISIBLE);
            }

        }.start();
    }


    private void clickableAnimation(){
        mButtonStartPause.setClickable(false);
        cat_sleep.setClickable(false);
        new CountDownTimer(800, 1) {

            public void onTick(long millisUntilFinished) {
                // You don't need to use this.
            }

            public void onFinish() {
                mButtonStartPause.setClickable(true);
                cat_sleep.setClickable(true);
            }

        }.start();
    }

    private void clickableAnimationRest(){
        mButtonStartPauseRest.setClickable(false);
        cat_question.setClickable(false);
        new CountDownTimer(800, 1) {

            public void onTick(long millisUntilFinished) {
                // You don't need to use this.
            }

            public void onFinish() {
                mButtonStartPauseRest.setClickable(true);
                cat_question.setClickable(true);
            }

        }.start();
    }

    private void clickableAnimationLongRest(){
        mButtonStartPauseLongRest.setClickable(false);
        cat_question.setClickable(false);
        new CountDownTimer(800, 1) {

            public void onTick(long millisUntilFinished) {
                // You don't need to use this.
            }

            public void onFinish() {
                mButtonStartPauseLongRest.setClickable(true);
                cat_question.setClickable(true);
            }

        }.start();
    }

    private void clickableAnimationReset(){
        edit_current_action.setClickable(false);
        new CountDownTimer(800, 1) {

            public void onTick(long millisUntilFinished) {
                // You don't need to use this.
            }

            public void onFinish() {
                edit_current_action.setClickable(true);
            }

        }.start();
    }



    private void clickableAnimationChange(){
        edit_current_action.setClickable(false);
        new CountDownTimer(500, 1) {

            public void onTick(long millisUntilFinished) {
                // You don't need to use this.
            }

            public void onFinish() {
                edit_current_action.setClickable(true);
            }

        }.start();
    }

    private void updateCountDownText() {
         minutes = (int) (mTimeLeftInMillis / 1000) / 60;
         seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        if (seconds == 0 && minutes == 0) {


            done += 1;

            for (; iWell < done; iWell++) {
                if (iWell == untilEndCount) {
                    u();
                } else if (iWell == untilEndCount * 2) {
                    u();
                } else if (iWell == untilEndCount * 3) {
                    u();
                }else if (iWell == untilEndCount * 4) {
                    u();
                }else if (iWell == untilEndCount * 5) {
                    u();
                }else if (iWell == untilEndCount * 6) {
                    u();
                }else if (iWell == untilEndCount * 7) {
                    u();
                }else if (iWell == untilEndCount * 8) {
                    u();
                }else if (iWell == untilEndCount * 9) {
                    u();
                }else if (iWell == untilEndCount * 10) {
                    u();
                }else if (iWell == untilEndCount * 11) {
                    u();
                }else if (iWell == untilEndCount * 12) {
                    u();
                }else if (iWell == untilEndCount * 13) {
                    u();
                }else if (iWell == untilEndCount * 14) {
                    u();
                }else if (iWell == untilEndCount * 15) {
                    u();
                }else if (iWell == untilEndCount * 16) {
                    u();
                }else if (iWell == untilEndCount * 17) {
                    u();
                }else if (iWell == untilEndCount * 18) {
                    u();
                }else if (iWell == untilEndCount * 19) {
                    u();
                }else if (iWell == untilEndCount * 20) {
                    u();
                }
                else {
                    replaceCircles();
                    countCircles += 1;
                    countCircles();
                }


            }


            if (done == whenStopCount) {

                pauseTimer();
                ((GifDrawable)cat_move.getDrawable()).stop();
                cat_question.setVisibility(View.VISIBLE);
                menu.setVisibility(View.VISIBLE);
                visibilityCatMove();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Вы хорошо поработали сегодня. Поздравляем! Желаете начать сначала?");
                builder.setTitle("Конец");
                builder.setCancelable(false);
                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        done = 0;
                        Intent intent = getIntent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        finish();
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        System.exit(0);
                    }
                });
                builder.show();

            }
        }











        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d : %02d", minutes, seconds);

        mTextViewCountDown.setText(timeLeftFormatted);


    }

    private void restUpdateCountDownText() {
         minutes = (int) (mRestLeftInMillis / 1000) / 60;
         seconds = (int) (mRestLeftInMillis / 1000) % 60;

        String timeRestLeftFormatted = String.format(Locale.getDefault(), "%02d : %02d", minutes, seconds);

        mTextViewCountDown.setText(timeRestLeftFormatted);
    }

    private void longRestUpdateCountDownText() {
         minutes = (int) (mLongRestLeftInMillis / 1000) / 60;
         seconds = (int) (mLongRestLeftInMillis / 1000) % 60;


        String timeRestLeftFormatted = String.format(Locale.getDefault(), "%02d : %02d", minutes, seconds);

        mTextViewCountDown.setText(timeRestLeftFormatted);
    }

    private void sendValue(){
        if (current_action.getText().toString().matches("Работа") && mButtonReset.getVisibility() == View.INVISIBLE) {
            checkAction = 1;
        } else if (current_action.getText().toString().matches("Работа") && mButtonReset.getVisibility() == View.VISIBLE) {
            checkAction = 2;
        }
        else if (current_action.getText().toString().matches("Отдых") && mRestButtonReset.getVisibility() == View.INVISIBLE) {
            checkAction = 3;
        }
        else if (current_action.getText().toString().matches("Отдых") && mRestButtonReset.getVisibility() == View.VISIBLE) {
            checkAction = 4;
        }
        else if (current_action.getText().toString().matches("Долгий отдых") && mLongRestButtonReset.getVisibility() == View.INVISIBLE) {
            checkAction = 5;
        }
        else if (current_action.getText().toString().matches("Долгий отдых") && mLongRestButtonReset.getVisibility() == View.VISIBLE) {
            checkAction = 6;
        }

        saveCheckAction();
        Intent intent = new Intent(MainActivity.this, Menu.class);
        startActivity(intent);
        this.finish();
    }




    private void saveValue() {
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor ed = pref.edit();
        ed.putLong("save_time", nowTime);
        ed.apply();
    }

    private void loadValue() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        START_TIME_IN_MILLIS = pref.getLong("save_time", 0);
        if (START_TIME_IN_MILLIS == 0) {
            START_TIME_IN_MILLIS = 1500 * 1000;
        }

        nowTime = START_TIME_IN_MILLIS;
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
    }

    private void saveValueRest() {
        prefrest = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edrest = prefrest.edit();
        edrest.putLong("save_keyrest", nowTimeRest);
        edrest.apply();
    }

    private void loadValueRest() {
        SharedPreferences prefrest = PreferenceManager.getDefaultSharedPreferences(this);
        REST_TIME_IN_MILLIS = prefrest.getLong("save_keyrest", 0);
        if (REST_TIME_IN_MILLIS == 0) {
            REST_TIME_IN_MILLIS = 300 * 1000;
        }
        nowTimeRest = REST_TIME_IN_MILLIS;
        mRestLeftInMillis = REST_TIME_IN_MILLIS;
        restUpdateCountDownText();

    }

    private void saveValueLongRest() {
        preflongrest = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edlongrest = preflongrest.edit();
        edlongrest.putLong("save_keylongrest", nowTimeLongRest);
        edlongrest.apply();

    }

    private void loadValueLongRest() {
        SharedPreferences preflongrest = PreferenceManager.getDefaultSharedPreferences(this);
        LONG_REST_TIME_IN_MILLIS = preflongrest.getLong("save_keylongrest", 0);
        if (LONG_REST_TIME_IN_MILLIS == 0) {
            LONG_REST_TIME_IN_MILLIS = 900 * 1000;
        }
        nowTimeLongRest = LONG_REST_TIME_IN_MILLIS;
        mLongRestLeftInMillis = LONG_REST_TIME_IN_MILLIS;
        longRestUpdateCountDownText();
    }

    private void saveCheckAction(){
        check = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor sp = check.edit();
        sp.putInt("check_action", checkAction);
        sp.apply();
    }

    private void loadCheckAction() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        checkAction = sp.getInt("check_action", 0);
        longRestUpdateCountDownText();
    }

    private void saveValueAutostart() {
        autostart = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor edq = autostart.edit();
        edq.putBoolean("autostart_key", autostartIsOn);

        edq.apply();
    }

    private void loadValueAutostart() {
        autostart = getPreferences(MODE_PRIVATE);
        boolean savedTextAutostart = autostart.getBoolean("autostart_key", false);
        autostartIsOn = savedTextAutostart;
    }

    private void saveValueWhenStop() {
        whenstop = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor edlongrest = whenstop.edit();
        edlongrest.putString("save_whenstop", String.valueOf(whenStopCount));
        edlongrest.apply();

    }

    private void loadValueWhenStop() {
        whenstop = getPreferences(MODE_PRIVATE);
        String savedTextLongRest = whenstop.getString("save_whenstop", String.valueOf(whenStopCount));
        whenStopCount = Short.valueOf(savedTextLongRest);
    }

    private void saveValueUntilEnd() {
        untilend = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor edlongrest = untilend.edit();
        edlongrest.putString("save_untilend", String.valueOf(untilEndCount));
        edlongrest.apply();

    }

    private void loadValueUntilEnd() {
        untilend = getPreferences(MODE_PRIVATE);
        String savedTextLongRest = untilend.getString("save_untilend", String.valueOf(untilEndCount));
        untilEndCount = Short.valueOf(savedTextLongRest);
    }

    private void saveDone() {
        savedone = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor edlongrest = savedone.edit();
        edlongrest.putString("save_done", String.valueOf(done));
        edlongrest.apply();

    }

    private void loadDone() {
        savedone = getPreferences(MODE_PRIVATE);
        String savedTextLongRest = savedone.getString("save_done", String.valueOf(done));
        done = Byte.valueOf(savedTextLongRest);

    }

    private void saveI() {
        savei = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor edlongrest = savei.edit();
        edlongrest.putString("save_iDone", String.valueOf(iDone));
        edlongrest.apply();

    }

    private void loadI() {
        savei = getPreferences(MODE_PRIVATE);
        String savedTextLongRest = savei.getString("save_iDone", String.valueOf(iDone));
        done = Byte.valueOf(savedTextLongRest);

    }

    private void saveExit() {
        save_exit = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor edlongrest = save_exit.edit();
        edlongrest.putString("save_exit", String.valueOf(isExit));
        edlongrest.apply();

    }

    private void loadExit() {
        save_exit = getPreferences(MODE_PRIVATE);
        String savedTextLongRest = save_exit.getString("save_exit", String.valueOf(isExit));
        isExit = Boolean.valueOf(savedTextLongRest);

    }

    private void saveValueVibration() {
        pref_vibration = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor edlongrest = pref_vibration.edit();
        edlongrest.putString("save_vibration", String.valueOf(vibration));
        edlongrest.apply();

    }

    private void loadValueVibration() {
        pref_vibration = getPreferences(MODE_PRIVATE);
        String savedTextLongRest = pref_vibration.getString("save_vibration", String.valueOf(vibration));
        vibration = Boolean.valueOf(savedTextLongRest);
    }

    private void saveValueAutostartRest() {
        pref_autostartrest = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor edlongrest = pref_autostartrest.edit();
        edlongrest.putString("save_autostartrest", String.valueOf(autostartRestIsOn));
        edlongrest.apply();

    }

    private void loadValueAutostartRest() {
        pref_autostartrest = getPreferences(MODE_PRIVATE);
        String savedTextLongRest = pref_autostartrest.getString("save_autostartrest", String.valueOf(autostartRestIsOn));
        autostartRestIsOn = Boolean.valueOf(savedTextLongRest);
    }




    private void addStartCircles() {
        ImageView imageView = new ImageView(MainActivity.this);
        imageView.setImageResource(R.drawable.stick);
        if (whenStopCount <= 8) {
            if (width_phone == 720)
                addView(imageView, 30, 30);
            if (width_phone == 1080)
                addView(imageView, 50, 50);
            if (width_phone == 1440)
                addView(imageView, 70, 70);
            if (width_phone == 2160)
                addView(imageView, 70, 70);
        } else if (whenStopCount > 8 && whenStopCount <= 12) {
            if (width_phone == 720)
                addView(imageView, 15, 15);
            if (width_phone == 1080)
                addView(imageView, 25, 25);
            if (width_phone == 1440)
                addView(imageView, 35, 35);
            if (width_phone == 2160)
                addView(imageView, 35, 35);
        } else if (whenStopCount > 12 && whenStopCount <= 16) {
            if (width_phone == 720)
                addView(imageView, 10, 10);
            if (width_phone == 1080)
                addView(imageView, 15, 15);
            if (width_phone == 1440)
                addView(imageView, 25, 25);
            if (width_phone == 2160)
                addView(imageView, 25, 25);
        }
        else if (whenStopCount > 16 && whenStopCount <= 20) {
            if (width_phone == 720)
                addView(imageView, 3, 3);
            if (width_phone == 1080)
                addView(imageView, 10, 10);
            if (width_phone == 1440)
                addView(imageView, 15, 15);
            if (width_phone == 2160)
                addView(imageView, 15, 15);
        }

    }

    private void addStartCirclesSpace() {
        ImageView imageView = new ImageView(MainActivity.this);
        imageView.setImageResource(R.drawable.stick);
        if (whenStopCount <= 8) {
            if (width_phone == 720)
                addViewSpace(imageView, 30, 30);
            if (width_phone == 1080)
                addViewSpace(imageView, 50, 50);
            if (width_phone == 1440)
                addViewSpace(imageView, 70, 70);
            if (width_phone == 2160)
                addViewSpace(imageView, 70, 70);
        } else if (whenStopCount > 8 && whenStopCount <= 12) {
            if (width_phone == 720)
                addViewSpace(imageView, 15, 15);
            if (width_phone == 1080)
                addViewSpace(imageView, 25, 25);
            if (width_phone == 1440)
                addViewSpace(imageView, 35, 35);
            if (width_phone == 2160)
                addViewSpace(imageView, 35, 35);
        } else if (whenStopCount > 12 && whenStopCount <= 16) {
            if (width_phone == 720)
                addViewSpace(imageView, 10, 10);
            if (width_phone == 1080)
                addViewSpace(imageView, 15, 15);
            if (width_phone == 1440)
                addViewSpace(imageView, 25, 25);
            if (width_phone == 2160)
                addViewSpace(imageView, 25, 25);
        }
        else if (whenStopCount > 16 && whenStopCount <= 20) {
            if (width_phone == 720)
                addViewSpace(imageView, 3, 3);
            if (width_phone == 1080)
                addViewSpace(imageView, 10, 10);
            if (width_phone == 1440)
                addViewSpace(imageView, 15, 15);
            if (width_phone == 2160)
                addViewSpace(imageView, 15, 15);
        }

    }

    private void addStartBigSpace() {
        ImageView imageView = new ImageView(MainActivity.this);
        imageView.setImageResource(R.drawable.stick);


        // тут ничего изменять не надо
        if (whenStopCount <= 8) {
            if (width_phone == 720)
                addViewBigSpace(imageView, 30, 30);
            if (width_phone == 1080)
                addViewBigSpace(imageView, 50, 50);
            if (width_phone == 1440)
                addViewBigSpace(imageView, 70, 70);
            if (width_phone == 2160)
                addViewBigSpace(imageView, 70, 70);
        } else if (whenStopCount > 8 && whenStopCount <= 12) {
            if (width_phone == 720)
                addViewBigSpace(imageView, 15, 15);
            if (width_phone == 1080)
                addViewBigSpace(imageView, 25, 25);
            if (width_phone == 1440)
                addViewBigSpace(imageView, 35, 35);
            if (width_phone == 2160)
                addViewBigSpace(imageView, 35, 35);
        } else if (whenStopCount > 12 && whenStopCount <= 16) {
            if (width_phone == 720)
                addViewBigSpace(imageView, 10, 10);
            if (width_phone == 1080)
                addViewBigSpace(imageView, 15, 15);
            if (width_phone == 1440)
                addViewBigSpace(imageView, 25, 25);
            if (width_phone == 2160)
                addViewBigSpace(imageView, 25, 25);
        }
        else if (whenStopCount > 16 && whenStopCount <= 20) {
            if (width_phone == 720)
                addViewBigSpace(imageView, 3, 3);
            if (width_phone == 1080)
                addViewBigSpace(imageView, 10, 10);
            if (width_phone == 1440)
                addViewBigSpace(imageView, 15, 15);
            if (width_phone == 2160)
                addViewBigSpace(imageView, 15, 15);
        }

    }

    private void replaceCircles(){
        ImageView imageView = new ImageView(MainActivity.this);
        imageView.setImageResource(R.drawable.stick_do);
        if (whenStopCount <= 8) {
            if (width_phone == 720)
                replaceView(imageView, 30, 30);
            if (width_phone == 1080)
                replaceView(imageView, 50, 50);
            if (width_phone == 1440)
                replaceView(imageView, 70, 70);
            if (width_phone == 2160)
                replaceView(imageView, 70, 70);
        } else if (whenStopCount > 8 && whenStopCount <= 12) {
            if (width_phone == 720)
                replaceView(imageView, 15, 15);
            if (width_phone == 1080)
                replaceView(imageView, 25, 25);
            if (width_phone == 1440)
                replaceView(imageView, 35, 35);
            if (width_phone == 2160)
                replaceView(imageView, 35, 35);
        } else if (whenStopCount > 12 && whenStopCount <= 16) {
            if (width_phone == 720)
                replaceView(imageView, 10, 10);
            if (width_phone == 1080)
                replaceView(imageView, 15, 15);
            if (width_phone == 1440)
                replaceView(imageView, 25, 25);
            if (width_phone == 2160)
                replaceView(imageView, 25, 25);
        }
        else if (whenStopCount > 16 && whenStopCount <= 20) {
            if (width_phone == 720)
                replaceView(imageView, 3, 3);
            if (width_phone == 1080)
                replaceView(imageView, 10, 10);
            if (width_phone == 1440)
                replaceView(imageView, 15, 15);
            if (width_phone == 2160)
                replaceView(imageView, 15, 15);
        }

    }

    private void addStartCirclesSpaceReplace() {
        ImageView imageView = new ImageView(MainActivity.this);
        imageView.setImageResource(R.drawable.stick_do);
        if (whenStopCount <= 8) {
            if (width_phone == 720)
                addViewSpaceReplace(imageView, 30, 30);
            if (width_phone == 1080)
                addViewSpaceReplace(imageView, 50, 50);
            if (width_phone == 1440)
                addViewSpaceReplace(imageView, 70, 70);
            if (width_phone == 2160)
                addViewSpaceReplace(imageView, 70, 70);
        } else if (whenStopCount > 8 && whenStopCount <= 12) {
            if (width_phone == 720)
                addViewSpaceReplace(imageView, 15, 15);
            if (width_phone == 1080)
                addViewSpaceReplace(imageView, 25, 25);
            if (width_phone == 1440)
                addViewSpaceReplace(imageView, 35, 35);
            if (width_phone == 2160)
                addViewSpaceReplace(imageView, 35, 35);
        } else if (whenStopCount > 12 && whenStopCount <= 16) {
            if (width_phone == 720)
                addViewSpaceReplace(imageView, 10, 10);
            if (width_phone == 1080)
                addViewSpaceReplace(imageView, 15, 15);
            if (width_phone == 1440)
                addViewSpaceReplace(imageView, 25, 25);
            if (width_phone == 2160)
                addViewSpaceReplace(imageView, 25, 25);
        }
        else if (whenStopCount > 16 && whenStopCount <= 20) {
            if (width_phone == 720)
                addViewSpaceReplace(imageView, 3, 3);
            if (width_phone == 1080)
                addViewSpaceReplace(imageView, 10, 10);
            if (width_phone == 1440)
                addViewSpaceReplace(imageView, 15, 15);
            if (width_phone == 2160)
                addViewSpaceReplace(imageView, 15, 15);
        }
    }








    private void addView(ImageView imageView, int width, int height) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        layoutParams.setMargins(10, 0, 10, 0);
        imageView.setLayoutParams(layoutParams);

        circles.addView(imageView);

    }



    private void replaceView(ImageView imageView, int width, int height) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        layoutParams.setMargins(10, 0, 10, 0);
        imageView.setLayoutParams(layoutParams);

        circles_replace.addView(imageView);

    }



    // Пропуск незакрашенных
    private void addViewSpace(ImageView imageView, int width, int height) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);

        if (width_phone == 720) {

            if (untilEndCount == 1)
                layoutParams.setMargins(130, 0, 10, 0);
            if (untilEndCount == 1 && whenStopCount > 4 && whenStopCount <= 8)
                layoutParams.setMargins(25, 0, 10, 0);
            if (untilEndCount == 1 && whenStopCount > 8 && whenStopCount <= 12)
                layoutParams.setMargins(20, 0, 10, 0);
            if (untilEndCount == 1 && whenStopCount > 12 && whenStopCount <= 16)
                layoutParams.setMargins(15, 0, 10, 0);
            if (untilEndCount == 1 && whenStopCount > 16 && whenStopCount <= 20)
                layoutParams.setMargins(16, 0, 10, 0);

            if (untilEndCount == 2)
                layoutParams.setMargins(45, 0, 10, 0);
            if (untilEndCount == 2 && whenStopCount > 8)
                layoutParams.setMargins(30, 0, 10, 0);
            if (untilEndCount == 2 && whenStopCount > 14)
                layoutParams.setMargins(20, 0, 10, 0);

            if (untilEndCount == 3)
                layoutParams.setMargins(250, 0, 10, 0);
            if (untilEndCount == 3 && whenStopCount > 6 && whenStopCount <= 8)
                layoutParams.setMargins(60, 0, 10, 0);
            if (untilEndCount == 3 && whenStopCount >= 9 && whenStopCount <= 12)
                layoutParams.setMargins(40, 0, 10, 0);
            if (whenStopCount > 12 && whenStopCount <= 15 && untilEndCount == 3) {
                layoutParams.setMargins(30, 0, 10, 0);
            }
            if (whenStopCount >= 16 && whenStopCount <= 20 && untilEndCount == 3) {
                layoutParams.setMargins(25, 0, 10, 0);
            }



            if (untilEndCount == 4)
                layoutParams.setMargins(120, 0, 10, 0);
            if (whenStopCount > 8 && whenStopCount <= 12 && untilEndCount == 4) {
                layoutParams.setMargins(70, 0, 10, 0);
            }
            if (whenStopCount > 12 && whenStopCount <= 16 && untilEndCount == 4) {
                layoutParams.setMargins(20, 0, 10, 0);
            }
            if (whenStopCount > 16 && whenStopCount <= 20 && untilEndCount == 4) {
                layoutParams.setMargins(15, 0, 10, 0);
            }

            for (int i = 5; i < 21; i++) {
                if (untilEndCount == i)
                    layoutParams.setMargins(120, 0, 10, 0);
                if (whenStopCount > 8 && whenStopCount <= 12 && untilEndCount == i) {
                    layoutParams.setMargins(50, 0, 10, 0);
                }
                if (whenStopCount > 12 && whenStopCount <= 16 && untilEndCount == i) {
                    layoutParams.setMargins(20, 0, 10, 0);
                }
                if (whenStopCount > 16 && whenStopCount <= 20 && untilEndCount == i) {
                    layoutParams.setMargins(15, 0, 10, 0);
                }
            }
        }

        if (width_phone == 1080) {

            if (untilEndCount == 1)
                layoutParams.setMargins(130, 0, 10, 0);
            if (untilEndCount == 1 && whenStopCount > 4 && whenStopCount <= 8)
                layoutParams.setMargins(30, 0, 10, 0);
            if (untilEndCount == 1 && whenStopCount > 8 && whenStopCount <= 12)
                layoutParams.setMargins(25, 0, 10, 0);
            if (untilEndCount == 1 && whenStopCount > 12 && whenStopCount <= 16)
                layoutParams.setMargins(22, 0, 10, 0);
            if (untilEndCount == 1 && whenStopCount > 16 && whenStopCount <= 20)
                layoutParams.setMargins(20, 0, 10, 0);

            if (untilEndCount == 2)
                layoutParams.setMargins(45, 0, 10, 0);
            if (untilEndCount == 2 && whenStopCount > 8)
                layoutParams.setMargins(30, 0, 10, 0);
            if (untilEndCount == 2 && whenStopCount > 14)
                layoutParams.setMargins(20, 0, 10, 0);

            if (untilEndCount == 3)
                layoutParams.setMargins(250, 0, 10, 0);
            if (untilEndCount == 3 && whenStopCount > 6 && whenStopCount <= 8)
                layoutParams.setMargins(60, 0, 10, 0);
            if (untilEndCount == 3 && whenStopCount >= 9 && whenStopCount <= 12)
                layoutParams.setMargins(40, 0, 10, 0);
            if (whenStopCount > 12 && whenStopCount <= 15 && untilEndCount == 3) {
                layoutParams.setMargins(30, 0, 10, 0);
            }
            if (whenStopCount >= 16 && whenStopCount <= 20 && untilEndCount == 3) {
                layoutParams.setMargins(25, 0, 10, 0);
            }


            if (untilEndCount == 4)
                layoutParams.setMargins(170, 0, 10, 0);
            if (whenStopCount > 8 && whenStopCount <= 12 && untilEndCount == 4) {
                layoutParams.setMargins(70, 0, 10, 0);
            }
            if (whenStopCount > 12 && whenStopCount <= 16 && untilEndCount == 4) {
                layoutParams.setMargins(45, 0, 10, 0);
            }
            if (whenStopCount > 16 && whenStopCount <= 20 && untilEndCount == 4) {
                layoutParams.setMargins(40, 0, 10, 0);
            }

            for (int i = 5; i < 21; i++) {
                if (untilEndCount == i)
                    layoutParams.setMargins(170, 0, 10, 0);
                if (whenStopCount > 8 && whenStopCount <= 12 && untilEndCount == i) {
                    layoutParams.setMargins(100, 0, 10, 0);
                }
                if (whenStopCount > 12 && whenStopCount <= 16 && untilEndCount == i) {
                    layoutParams.setMargins(60, 0, 10, 0);
                }
                if (whenStopCount > 16 && whenStopCount <= 20 && untilEndCount == i) {
                    layoutParams.setMargins(70, 0, 10, 0);
                }
            }
        }


        if (width_phone == 1080 && height_phone == 1794) {

            if (untilEndCount == 1)
                layoutParams.setMargins(130, 0, 10, 0);
            if (untilEndCount == 1 && whenStopCount > 4 && whenStopCount <= 8)
                layoutParams.setMargins(25, 0, 10, 0);
            if (untilEndCount == 1 && whenStopCount > 8 && whenStopCount <= 12)
                layoutParams.setMargins(20, 0, 10, 0);
            if (untilEndCount == 1 && whenStopCount > 12 && whenStopCount <= 16)
                layoutParams.setMargins(15, 0, 10, 0);
            if (untilEndCount == 1 && whenStopCount > 16 && whenStopCount <= 20)
                layoutParams.setMargins(16, 0, 10, 0);

            if (untilEndCount == 2)
                layoutParams.setMargins(45, 0, 10, 0);
            if (untilEndCount == 2 && whenStopCount > 8)
                layoutParams.setMargins(30, 0, 10, 0);
            if (untilEndCount == 2 && whenStopCount > 14)
                layoutParams.setMargins(20, 0, 10, 0);

            if (untilEndCount == 3)
                layoutParams.setMargins(250, 0, 10, 0);
            if (untilEndCount == 3 && whenStopCount > 6 && whenStopCount <= 8)
                layoutParams.setMargins(60, 0, 10, 0);
            if (untilEndCount == 3 && whenStopCount >= 9 && whenStopCount <= 12)
                layoutParams.setMargins(40, 0, 10, 0);
            if (whenStopCount > 12 && whenStopCount <= 15 && untilEndCount == 3) {
                layoutParams.setMargins(30, 0, 10, 0);
            }
            if (whenStopCount >= 16 && whenStopCount <= 20 && untilEndCount == 3) {
                layoutParams.setMargins(25, 0, 10, 0);
            }



            if (untilEndCount == 4)
                layoutParams.setMargins(120, 0, 10, 0);
            if (whenStopCount > 8 && whenStopCount <= 12 && untilEndCount == 4) {
                layoutParams.setMargins(50, 0, 10, 0);
            }
            if (whenStopCount > 12 && whenStopCount <= 16 && untilEndCount == 4) {
                layoutParams.setMargins(45, 0, 10, 0);
            }
            if (whenStopCount > 16 && whenStopCount <= 20 && untilEndCount == 4) {
                layoutParams.setMargins(40, 0, 10, 0);
            }


            for (int i = 5; i < 21; i++) {
                if (untilEndCount == i)
                    layoutParams.setMargins(170, 0, 10, 0);
                if (whenStopCount > 8 && whenStopCount <= 12 && untilEndCount == i) {
                    layoutParams.setMargins(70, 0, 10, 0);
                }
                if (whenStopCount > 12 && whenStopCount <= 16 && untilEndCount == i) {
                    layoutParams.setMargins(45, 0, 10, 0);
                }
                if (whenStopCount > 16 && whenStopCount <= 20 && untilEndCount == i) {
                    layoutParams.setMargins(40, 0, 10, 0);
                }
            }
        }


        if (width_phone == 1440){
            if (untilEndCount == 1)
                layoutParams.setMargins(170, 0, 10, 0);
            if (untilEndCount == 1 && whenStopCount > 4 && whenStopCount <= 8)
                layoutParams.setMargins(30, 0, 10, 0);
            if (untilEndCount == 1 && whenStopCount > 8 && whenStopCount <= 12)
                layoutParams.setMargins(30, 0, 10, 0);
            if (untilEndCount == 1 && whenStopCount > 12 && whenStopCount <= 16)
                layoutParams.setMargins(20, 0, 10, 0);
            if (untilEndCount == 1 && whenStopCount > 16 && whenStopCount <= 20)
                layoutParams.setMargins(17, 0, 10, 0);

            if (untilEndCount == 2)
                layoutParams.setMargins(60, 0, 10, 0);
            if (untilEndCount == 2 && whenStopCount > 8)
                layoutParams.setMargins(50, 0, 10, 0);
            if (untilEndCount == 2 && whenStopCount > 14)
                layoutParams.setMargins(30, 0, 10, 0);

            if (untilEndCount == 3)
                layoutParams.setMargins(330, 0, 10, 0);
            if (untilEndCount == 3 && whenStopCount > 6 && whenStopCount <= 8)
                layoutParams.setMargins(100, 0, 10, 0);
            if (untilEndCount == 3 && whenStopCount >= 9 && whenStopCount <= 12)
                layoutParams.setMargins(80, 0, 10, 0);
            if (whenStopCount > 12 && whenStopCount <= 15 && untilEndCount == 3) {
                layoutParams.setMargins(60, 0, 10, 0);
            }
            if (whenStopCount >= 16 && whenStopCount <= 20 && untilEndCount == 3) {
                layoutParams.setMargins(30, 0, 10, 0);
            }



            if (untilEndCount == 4)
                layoutParams.setMargins(170, 0, 10, 0);
            if (whenStopCount > 8 && whenStopCount <= 12 && untilEndCount == 4) {
                layoutParams.setMargins(110, 0, 10, 0);
            }
            if (whenStopCount > 12 && whenStopCount <= 16 && untilEndCount == 4) {
                layoutParams.setMargins(68, 0, 10, 0);
            }
            if (whenStopCount > 16 && whenStopCount <= 20 && untilEndCount == 4) {
                layoutParams.setMargins(58, 0, 10, 0);
            }

            for (int i = 5; i < 21; i++) {
                if (untilEndCount == i)
                    layoutParams.setMargins(170, 0, 10, 0);
                if (whenStopCount > 8 && whenStopCount <= 12 && untilEndCount == i) {
                    layoutParams.setMargins(110, 0, 10, 0);
                }
                if (whenStopCount > 12 && whenStopCount <= 16 && untilEndCount == i) {
                    layoutParams.setMargins(68, 0, 10, 0);
                }
                if (whenStopCount > 16 && whenStopCount <= 20 && untilEndCount == i) {
                    layoutParams.setMargins(58, 0, 10, 0);
                }
            }
        }

        if (width_phone == 2160) {
            if (untilEndCount == 1)
                layoutParams.setMargins(200, 0, 10, 0);
            if (untilEndCount == 1 && whenStopCount > 4 && whenStopCount <= 8)
                layoutParams.setMargins(50, 0, 10, 0);
            if (untilEndCount == 1 && whenStopCount > 8 && whenStopCount <= 12)
                layoutParams.setMargins(35, 0, 10, 0);
            if (untilEndCount == 1 && whenStopCount > 12 && whenStopCount <= 16)
                layoutParams.setMargins(25, 0, 10, 0);
            if (untilEndCount == 1 && whenStopCount > 16 && whenStopCount <= 20)
                layoutParams.setMargins(22, 0, 10, 0);

            if (untilEndCount == 2)
                layoutParams.setMargins(80, 0, 10, 0);
            if (untilEndCount == 2 && whenStopCount > 8)
                layoutParams.setMargins(70, 0, 10, 0);
            if (untilEndCount == 2 && whenStopCount > 14)
                layoutParams.setMargins(40, 0, 10, 0);

            if (untilEndCount == 3)
                layoutParams.setMargins(370, 0, 10, 0);
            if (untilEndCount == 3 && whenStopCount > 6 && whenStopCount <= 8)
                layoutParams.setMargins(100, 0, 10, 0);
            if (untilEndCount == 3 && whenStopCount >= 9 && whenStopCount <= 12)
                layoutParams.setMargins(90, 0, 10, 0);
            if (whenStopCount > 12 && whenStopCount <= 15 && untilEndCount == 3) {
                layoutParams.setMargins(70, 0, 10, 0);
            }
            if (whenStopCount >= 16 && whenStopCount <= 20 && untilEndCount == 3) {
                layoutParams.setMargins(50, 0, 10, 0);
            }


            if (untilEndCount == 4)
                layoutParams.setMargins(200, 0, 10, 0);
            if (whenStopCount > 8 && whenStopCount <= 12 && untilEndCount == 4) {
                layoutParams.setMargins(170, 0, 10, 0);
            }
            if (whenStopCount > 12 && whenStopCount <= 16 && untilEndCount == 4) {
                layoutParams.setMargins(90, 0, 10, 0);
            }
            if (whenStopCount > 16 && whenStopCount <= 20 && untilEndCount == 4) {
                layoutParams.setMargins(75, 0, 10, 0);
            }

            for (int i = 5; i < 21; i++) {
                if (untilEndCount == i)
                    layoutParams.setMargins(170, 0, 10, 0);
                if (whenStopCount > 8 && whenStopCount <= 12 && untilEndCount == i) {
                    layoutParams.setMargins(110, 0, 10, 0);
                }
                if (whenStopCount > 12 && whenStopCount <= 16 && untilEndCount == i) {
                    layoutParams.setMargins(68, 0, 10, 0);
                }
                if (whenStopCount > 16 && whenStopCount <= 20 && untilEndCount == i) {
                    layoutParams.setMargins(58, 0, 10, 0);
                }
            }
        }


        imageView.setLayoutParams(layoutParams);

        circles.addView(imageView);

    }

    private void addViewBigSpace(ImageView imageView, int width, int height) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);

        if (width_phone == 720) {
            layoutParams.setMargins(1400, 0, 10, 0);
            if (whenStopCount > 8 && whenStopCount <= 12) {
                layoutParams.setMargins(1400, 0, 10, 0);
            }
            if (whenStopCount > 12 && whenStopCount <= 16) {
                layoutParams.setMargins(1000, 0, 10, 0);
            }
            if (whenStopCount > 16 && whenStopCount <= 20) {
                layoutParams.setMargins(1000, 0, 10, 0);
            }
        }

        if (width_phone == 1080) {
            layoutParams.setMargins(1400, 0, 10, 0);
            if (whenStopCount > 8 && whenStopCount <= 12) {
                layoutParams.setMargins(1400, 0, 10, 0);
            }
            if (whenStopCount > 12 && whenStopCount <= 16) {
                layoutParams.setMargins(1000, 0, 10, 0);
            }
            if (whenStopCount > 16 && whenStopCount <= 20) {
                layoutParams.setMargins(1000, 0, 10, 0);
            }
        }


        if (width_phone == 1440){
            if (whenStopCount > 0 && whenStopCount <= 8)
                layoutParams.setMargins(1400, 0, 10, 0);
            if (whenStopCount > 8 && whenStopCount <= 12) {
                layoutParams.setMargins(1000, 0, 10, 0);
            }
            if (whenStopCount > 12 && whenStopCount <= 16) {
                layoutParams.setMargins(1000, 0, 10, 0);
            }
            if (whenStopCount > 16 && whenStopCount <= 20) {
                layoutParams.setMargins(1000, 0, 10, 0);
            }
        }

        if (width_phone == 2160){
            if (whenStopCount > 0 && whenStopCount <= 8)
                layoutParams.setMargins(1400, 0, 10, 0);
            if (whenStopCount > 8 && whenStopCount <= 12) {
                layoutParams.setMargins(1000, 0, 10, 0);
            }
            if (whenStopCount > 12 && whenStopCount <= 16) {
                layoutParams.setMargins(1000, 0, 10, 0);
            }
            if (whenStopCount > 16 && whenStopCount <= 20) {
                layoutParams.setMargins(1000, 0, 10, 0);
            }
        }

        imageView.setLayoutParams(layoutParams);

        circles.addView(imageView);

    }


    //
    private void addViewSpaceReplace(ImageView imageView, int width, int height) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);

        if (width_phone == 720) {

            if (untilEndCount == 1)
                layoutParams.setMargins(130, 0, 10, 0);
            if (untilEndCount == 1 && whenStopCount > 4 && whenStopCount <= 8)
                layoutParams.setMargins(25, 0, 10, 0);
            if (untilEndCount == 1 && whenStopCount > 8 && whenStopCount <= 12)
                layoutParams.setMargins(20, 0, 10, 0);
            if (untilEndCount == 1 && whenStopCount > 12 && whenStopCount <= 16)
                layoutParams.setMargins(15, 0, 10, 0);
            if (untilEndCount == 1 && whenStopCount > 16 && whenStopCount <= 20)
                layoutParams.setMargins(16, 0, 10, 0);

            if (untilEndCount == 2)
                layoutParams.setMargins(45, 0, 10, 0);
            if (untilEndCount == 2 && whenStopCount > 8)
                layoutParams.setMargins(30, 0, 10, 0);
            if (untilEndCount == 2 && whenStopCount > 14)
                layoutParams.setMargins(20, 0, 10, 0);

            if (untilEndCount == 3)
                layoutParams.setMargins(250, 0, 10, 0);
            if (untilEndCount == 3 && whenStopCount > 6 && whenStopCount <= 8)
                layoutParams.setMargins(60, 0, 10, 0);
            if (untilEndCount == 3 && whenStopCount >= 9 && whenStopCount <= 12)
                layoutParams.setMargins(40, 0, 10, 0);
            if (whenStopCount > 12 && whenStopCount <= 15 && untilEndCount == 3) {
                layoutParams.setMargins(30, 0, 10, 0);
            }
            if (whenStopCount >= 16 && whenStopCount <= 20 && untilEndCount == 3) {
                layoutParams.setMargins(25, 0, 10, 0);
            }



            if (untilEndCount == 4)
                layoutParams.setMargins(120, 0, 10, 0);
            if (whenStopCount > 8 && whenStopCount <= 12 && untilEndCount == 4) {
                layoutParams.setMargins(70, 0, 10, 0);
            }
            if (whenStopCount > 12 && whenStopCount <= 16 && untilEndCount == 4) {
                layoutParams.setMargins(20, 0, 10, 0);
            }
            if (whenStopCount > 16 && whenStopCount <= 20 && untilEndCount == 4) {
                layoutParams.setMargins(15, 0, 10, 0);
            }

            for (int i = 5; i < 21; i++) {
                if (untilEndCount == i)
                    layoutParams.setMargins(120, 0, 10, 0);
                if (whenStopCount > 8 && whenStopCount <= 12 && untilEndCount == i) {
                    layoutParams.setMargins(50, 0, 10, 0);
                }
                if (whenStopCount > 12 && whenStopCount <= 16 && untilEndCount == i) {
                    layoutParams.setMargins(20, 0, 10, 0);
                }
                if (whenStopCount > 16 && whenStopCount <= 20 && untilEndCount == i) {
                    layoutParams.setMargins(15, 0, 10, 0);
                }
            }
        }

        if (width_phone == 1080) {

            if (untilEndCount == 1)
                layoutParams.setMargins(130, 0, 10, 0);
            if (untilEndCount == 1 && whenStopCount > 4 && whenStopCount <= 8)
                layoutParams.setMargins(30, 0, 10, 0);
            if (untilEndCount == 1 && whenStopCount > 8 && whenStopCount <= 12)
                layoutParams.setMargins(25, 0, 10, 0);
            if (untilEndCount == 1 && whenStopCount > 12 && whenStopCount <= 16)
                layoutParams.setMargins(22, 0, 10, 0);
            if (untilEndCount == 1 && whenStopCount > 16 && whenStopCount <= 20)
                layoutParams.setMargins(20, 0, 10, 0);

            if (untilEndCount == 2)
                layoutParams.setMargins(45, 0, 10, 0);
            if (untilEndCount == 2 && whenStopCount > 8)
                layoutParams.setMargins(30, 0, 10, 0);
            if (untilEndCount == 2 && whenStopCount > 14)
                layoutParams.setMargins(20, 0, 10, 0);

            if (untilEndCount == 3)
                layoutParams.setMargins(250, 0, 10, 0);
            if (untilEndCount == 3 && whenStopCount > 6 && whenStopCount <= 8)
                layoutParams.setMargins(60, 0, 10, 0);
            if (untilEndCount == 3 && whenStopCount >= 9 && whenStopCount <= 12)
                layoutParams.setMargins(40, 0, 10, 0);
            if (whenStopCount > 12 && whenStopCount <= 15 && untilEndCount == 3) {
                layoutParams.setMargins(30, 0, 10, 0);
            }
            if (whenStopCount >= 16 && whenStopCount <= 20 && untilEndCount == 3) {
                layoutParams.setMargins(25, 0, 10, 0);
            }


            if (untilEndCount == 4)
                layoutParams.setMargins(170, 0, 10, 0);
            if (whenStopCount > 8 && whenStopCount <= 12 && untilEndCount == 4) {
                layoutParams.setMargins(70, 0, 10, 0);
            }
            if (whenStopCount > 12 && whenStopCount <= 16 && untilEndCount == 4) {
                layoutParams.setMargins(45, 0, 10, 0);
            }
            if (whenStopCount > 16 && whenStopCount <= 20 && untilEndCount == 4) {
                layoutParams.setMargins(40, 0, 10, 0);
            }

            for (int i = 5; i < 21; i++) {
                if (untilEndCount == i)
                    layoutParams.setMargins(170, 0, 10, 0);
                if (whenStopCount > 8 && whenStopCount <= 12 && untilEndCount == i) {
                    layoutParams.setMargins(100, 0, 10, 0);
                }
                if (whenStopCount > 12 && whenStopCount <= 16 && untilEndCount == i) {
                    layoutParams.setMargins(60, 0, 10, 0);
                }
                if (whenStopCount > 16 && whenStopCount <= 20 && untilEndCount == i) {
                    layoutParams.setMargins(70, 0, 10, 0);
                }
            }
        }


        if (width_phone == 1080 && height_phone == 1794) {

            if (untilEndCount == 1)
                layoutParams.setMargins(130, 0, 10, 0);
            if (untilEndCount == 1 && whenStopCount > 4 && whenStopCount <= 8)
                layoutParams.setMargins(25, 0, 10, 0);
            if (untilEndCount == 1 && whenStopCount > 8 && whenStopCount <= 12)
                layoutParams.setMargins(20, 0, 10, 0);
            if (untilEndCount == 1 && whenStopCount > 12 && whenStopCount <= 16)
                layoutParams.setMargins(15, 0, 10, 0);
            if (untilEndCount == 1 && whenStopCount > 16 && whenStopCount <= 20)
                layoutParams.setMargins(16, 0, 10, 0);

            if (untilEndCount == 2)
                layoutParams.setMargins(45, 0, 10, 0);
            if (untilEndCount == 2 && whenStopCount > 8)
                layoutParams.setMargins(30, 0, 10, 0);
            if (untilEndCount == 2 && whenStopCount > 14)
                layoutParams.setMargins(20, 0, 10, 0);

            if (untilEndCount == 3)
                layoutParams.setMargins(250, 0, 10, 0);
            if (untilEndCount == 3 && whenStopCount > 6 && whenStopCount <= 8)
                layoutParams.setMargins(60, 0, 10, 0);
            if (untilEndCount == 3 && whenStopCount >= 9 && whenStopCount <= 12)
                layoutParams.setMargins(40, 0, 10, 0);
            if (whenStopCount > 12 && whenStopCount <= 15 && untilEndCount == 3) {
                layoutParams.setMargins(30, 0, 10, 0);
            }
            if (whenStopCount >= 16 && whenStopCount <= 20 && untilEndCount == 3) {
                layoutParams.setMargins(25, 0, 10, 0);
            }



            if (untilEndCount == 4)
                layoutParams.setMargins(120, 0, 10, 0);
            if (whenStopCount > 8 && whenStopCount <= 12 && untilEndCount == 4) {
                layoutParams.setMargins(50, 0, 10, 0);
            }
            if (whenStopCount > 12 && whenStopCount <= 16 && untilEndCount == 4) {
                layoutParams.setMargins(45, 0, 10, 0);
            }
            if (whenStopCount > 16 && whenStopCount <= 20 && untilEndCount == 4) {
                layoutParams.setMargins(40, 0, 10, 0);
            }


            for (int i = 5; i < 21; i++) {
                if (untilEndCount == i)
                    layoutParams.setMargins(170, 0, 10, 0);
                if (whenStopCount > 8 && whenStopCount <= 12 && untilEndCount == i) {
                    layoutParams.setMargins(70, 0, 10, 0);
                }
                if (whenStopCount > 12 && whenStopCount <= 16 && untilEndCount == i) {
                    layoutParams.setMargins(45, 0, 10, 0);
                }
                if (whenStopCount > 16 && whenStopCount <= 20 && untilEndCount == i) {
                    layoutParams.setMargins(40, 0, 10, 0);
                }
            }
        }


        if (width_phone == 1440){
            if (untilEndCount == 1)
                layoutParams.setMargins(170, 0, 10, 0);
            if (untilEndCount == 1 && whenStopCount > 4 && whenStopCount <= 8)
                layoutParams.setMargins(30, 0, 10, 0);
            if (untilEndCount == 1 && whenStopCount > 8 && whenStopCount <= 12)
                layoutParams.setMargins(30, 0, 10, 0);
            if (untilEndCount == 1 && whenStopCount > 12 && whenStopCount <= 16)
                layoutParams.setMargins(20, 0, 10, 0);
            if (untilEndCount == 1 && whenStopCount > 16 && whenStopCount <= 20)
                layoutParams.setMargins(17, 0, 10, 0);

            if (untilEndCount == 2)
                layoutParams.setMargins(60, 0, 10, 0);
            if (untilEndCount == 2 && whenStopCount > 8)
                layoutParams.setMargins(50, 0, 10, 0);
            if (untilEndCount == 2 && whenStopCount > 14)
                layoutParams.setMargins(30, 0, 10, 0);

            if (untilEndCount == 3)
                layoutParams.setMargins(330, 0, 10, 0);
            if (untilEndCount == 3 && whenStopCount > 6 && whenStopCount <= 8)
                layoutParams.setMargins(100, 0, 10, 0);
            if (untilEndCount == 3 && whenStopCount >= 9 && whenStopCount <= 12)
                layoutParams.setMargins(80, 0, 10, 0);
            if (whenStopCount > 12 && whenStopCount <= 15 && untilEndCount == 3) {
                layoutParams.setMargins(60, 0, 10, 0);
            }
            if (whenStopCount >= 16 && whenStopCount <= 20 && untilEndCount == 3) {
                layoutParams.setMargins(30, 0, 10, 0);
            }



            if (untilEndCount == 4)
                layoutParams.setMargins(170, 0, 10, 0);
            if (whenStopCount > 8 && whenStopCount <= 12 && untilEndCount == 4) {
                layoutParams.setMargins(110, 0, 10, 0);
            }
            if (whenStopCount > 12 && whenStopCount <= 16 && untilEndCount == 4) {
                layoutParams.setMargins(68, 0, 10, 0);
            }
            if (whenStopCount > 16 && whenStopCount <= 20 && untilEndCount == 4) {
                layoutParams.setMargins(58, 0, 10, 0);
            }

            for (int i = 5; i < 21; i++) {
                if (untilEndCount == i)
                    layoutParams.setMargins(170, 0, 10, 0);
                if (whenStopCount > 8 && whenStopCount <= 12 && untilEndCount == i) {
                    layoutParams.setMargins(110, 0, 10, 0);
                }
                if (whenStopCount > 12 && whenStopCount <= 16 && untilEndCount == i) {
                    layoutParams.setMargins(68, 0, 10, 0);
                }
                if (whenStopCount > 16 && whenStopCount <= 20 && untilEndCount == i) {
                    layoutParams.setMargins(58, 0, 10, 0);
                }
            }
        }

        if (width_phone == 2160) {
            if (untilEndCount == 1)
                layoutParams.setMargins(200, 0, 10, 0);
            if (untilEndCount == 1 && whenStopCount > 4 && whenStopCount <= 8)
                layoutParams.setMargins(50, 0, 10, 0);
            if (untilEndCount == 1 && whenStopCount > 8 && whenStopCount <= 12)
                layoutParams.setMargins(35, 0, 10, 0);
            if (untilEndCount == 1 && whenStopCount > 12 && whenStopCount <= 16)
                layoutParams.setMargins(25, 0, 10, 0);
            if (untilEndCount == 1 && whenStopCount > 16 && whenStopCount <= 20)
                layoutParams.setMargins(22, 0, 10, 0);

            if (untilEndCount == 2)
                layoutParams.setMargins(80, 0, 10, 0);
            if (untilEndCount == 2 && whenStopCount > 8)
                layoutParams.setMargins(70, 0, 10, 0);
            if (untilEndCount == 2 && whenStopCount > 14)
                layoutParams.setMargins(40, 0, 10, 0);

            if (untilEndCount == 3)
                layoutParams.setMargins(370, 0, 10, 0);
            if (untilEndCount == 3 && whenStopCount > 6 && whenStopCount <= 8)
                layoutParams.setMargins(100, 0, 10, 0);
            if (untilEndCount == 3 && whenStopCount >= 9 && whenStopCount <= 12)
                layoutParams.setMargins(90, 0, 10, 0);
            if (whenStopCount > 12 && whenStopCount <= 15 && untilEndCount == 3) {
                layoutParams.setMargins(70, 0, 10, 0);
            }
            if (whenStopCount >= 16 && whenStopCount <= 20 && untilEndCount == 3) {
                layoutParams.setMargins(50, 0, 10, 0);
            }


            if (untilEndCount == 4)
                layoutParams.setMargins(200, 0, 10, 0);
            if (whenStopCount > 8 && whenStopCount <= 12 && untilEndCount == 4) {
                layoutParams.setMargins(170, 0, 10, 0);
            }
            if (whenStopCount > 12 && whenStopCount <= 16 && untilEndCount == 4) {
                layoutParams.setMargins(90, 0, 10, 0);
            }
            if (whenStopCount > 16 && whenStopCount <= 20 && untilEndCount == 4) {
                layoutParams.setMargins(75, 0, 10, 0);
            }

            for (int i = 5; i < 21; i++) {
                if (untilEndCount == i)
                    layoutParams.setMargins(170, 0, 10, 0);
                if (whenStopCount > 8 && whenStopCount <= 12 && untilEndCount == i) {
                    layoutParams.setMargins(110, 0, 10, 0);
                }
                if (whenStopCount > 12 && whenStopCount <= 16 && untilEndCount == i) {
                    layoutParams.setMargins(68, 0, 10, 0);
                }
                if (whenStopCount > 16 && whenStopCount <= 20 && untilEndCount == i) {
                    layoutParams.setMargins(58, 0, 10, 0);
                }
            }
        }
        imageView.setLayoutParams(layoutParams);

        circles_replace.addView(imageView);

    }


    private void countCircles(){
        if (countCircles == whenStopCount) {
            addStartBigSpace();
            countCircles = 0;

        }
    }

    private void f(){
        addStartCirclesSpace();
        countCircles += 1;
        countCircles();
    }

    private void u() {
        addStartCirclesSpaceReplace();
        countCircles += 1;
        countCircles();
    }

    private void c() {

    }




























}




