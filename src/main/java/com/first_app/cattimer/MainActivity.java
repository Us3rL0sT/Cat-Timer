package com.first_app.cattimer;



import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
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
// ДЛЯ ГУГЛ ПЛЕЯ, УВЕДОМЛЕНИЕ ОБ ОТЗЫВЕ
//import hotchemi.android.rate.AppRate;
//import hotchemi.android.rate.OnClickButtonListener;
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
    private int color_value; // также экрана
    private long START_TIME_IN_MILLIS = (long) (1500 * 1000); // 1500 сек
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
    private SharedPreferences pref_display;
    private SharedPreferences pref_color;
    private SharedPreferences pref_melody;

    private float CurrentProgress = 100 * 10; // начинать с (-1)
    private float CurrentProgressRest = 100 * 10; // начинать с (-1)
    private float CurrentProgressLongRest = 100 * 10; // начинать с (-1)
    private ProgressBar progressBar;

    private Animation inAnimation;
    private Animation outAnimation;
    private Animation nullAnimation;
    private Animation fastAnimation;

    private TextView mTextViewCountDown;
    private TextView current_action;

    private Button mButtonStartPause;
    private Button mButtonStartPauseRest;
    private Button mButtonStartPauseLongRest;
    private Button mButtonReset;
    private Button mRestButtonReset;
    private Button mLongRestButtonReset;
    private Button edit_current_action;




    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;
    private boolean autostartIsOn;
    private boolean isExit;
    private boolean collapse = false;
    private boolean first_start = true;
    private boolean vibration = true;
    private boolean autostartRestIsOn = true;
    private boolean displayIsOn = true;

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

    private String Melody;

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

    Ringtone currentRingtone;















    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_main);
//        ДЛЯ ГУГЛ ПЛЕЯ, УВЕДОМЛЕНИЕ ОБ ОТЗЫВЕ
//        AppRate.with(this)
//                .setInstallDays(1) // default 10, 0 means install day.
//                .setLaunchTimes(3) // default 10
//                .setRemindInterval(1) // default 1
//                .setShowLaterButton(true) // default true
//                .setDebug(false) // default false
//                .setOnClickButtonListener(new OnClickButtonListener() { // callback listener.
//                    @Override
//                    public void onClickButton(int which) {
//                        Log.d(MainActivity.class.getName(), Integer.toString(which));
//                    }
//                })
//                .monitor();
//
//        // Show a dialog if meets conditions
//        AppRate.showRateDialogIfMeetsConditions(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height_phone = displayMetrics.heightPixels;
        width_phone = displayMetrics.widthPixels;


        mainConstraintLayout = findViewById(R.id.mainConstraintLayout);

        current_action = findViewById(R.id.current_action);

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

        deleteImage.setAlpha(0.6f);
        menu.setAlpha(0.6f);


        progressBar.setBackgroundColor(color_value);

        if (color_value == 0) {



            Drawable progressDrawable = getResources().getDrawable(R.drawable.custom_progress_not_white);

            progressDrawable.setBounds(progressBar.getProgressDrawable().getBounds());

            progressBar.setProgressDrawable(progressDrawable);
        }



        ((GifDrawable)cat_move.getDrawable()).stop();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (displayIsOn) getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            else {}

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE
        );

        onStart();




        deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cat_fall.getVisibility() == View.VISIBLE) {
                    done = 0;
                    Intent intent = getIntent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    startActivity(intent);
                    cat_sleep.setVisibility(View.INVISIBLE);
                    cat_fall.setVisibility(View.VISIBLE);

                }
                else {
                    done = 0;
                    Intent intent = getIntent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    startActivity(intent);
                }


            }
        });





    if (checkAction == 1) {
            mButtonStartPauseLongRest.setVisibility(View.INVISIBLE);
            mButtonStartPauseRest.setVisibility(View.INVISIBLE);
            mButtonStartPause.setVisibility(View.VISIBLE);
            mLongRestButtonReset.setVisibility(View.INVISIBLE);
            cat_move.setVisibility(View.INVISIBLE);
            cat_sleep.setVisibility(View.VISIBLE);
            cat_question.setVisibility(View.INVISIBLE);
            cat_pause.setVisibility(View.INVISIBLE);
            current_action.setText(R.string.work);
            arrows.setVisibility(View.VISIBLE);
            arrows_rest.setVisibility(View.INVISIBLE);
            arrows_long_rest.setVisibility(View.INVISIBLE);
            mTimeLeftInMillis = nowTime;
            updateCountDownText();

        } else if (checkAction == 2) {
            mButtonStartPauseLongRest.setVisibility(View.INVISIBLE);
            mButtonStartPauseRest.setVisibility(View.INVISIBLE);
            mButtonStartPause.setVisibility(View.VISIBLE);
            mLongRestButtonReset.setVisibility(View.INVISIBLE);
            mButtonReset.setVisibility(View.VISIBLE);
            cat_sleep.setVisibility(View.VISIBLE);
            cat_question.setVisibility(View.INVISIBLE);
            cat_pause.setVisibility(View.INVISIBLE);
            current_action.setText(R.string.work);
            arrows.setVisibility(View.VISIBLE);
            arrows_rest.setVisibility(View.INVISIBLE);
            arrows_long_rest.setVisibility(View.INVISIBLE);
            mTimeLeftInMillis = nowTime;
            CurrentProgress = 100 * 10;
            updateCountDownText();
        }
        else if (checkAction == 3) {
            mButtonStartPause.setVisibility(View.INVISIBLE);
            mButtonStartPauseLongRest.setVisibility(View.INVISIBLE);
            mButtonStartPauseRest.setVisibility(View.VISIBLE);
            mButtonReset.setVisibility(View.INVISIBLE);
            cat_move.setVisibility(View.INVISIBLE);
            cat_pause.setVisibility(View.VISIBLE);
            cat_fall.setVisibility(View.INVISIBLE);
            cat_sleep.setVisibility(View.INVISIBLE);
            current_action.setText(R.string.rest);
            arrows.setVisibility(View.INVISIBLE);
            arrows_rest.setVisibility(View.VISIBLE);
            arrows_long_rest.setVisibility(View.INVISIBLE);
            mRestLeftInMillis = nowTimeRest;
            restUpdateCountDownText();
        } else if (checkAction == 4) {
            mButtonStartPause.setVisibility(View.INVISIBLE);
            mButtonStartPauseLongRest.setVisibility(View.INVISIBLE);
            mButtonStartPauseRest.setVisibility(View.VISIBLE);
            mButtonReset.setVisibility(View.INVISIBLE);
            mRestButtonReset.setVisibility(View.VISIBLE);
            cat_sleep.setVisibility(View.INVISIBLE);
            cat_pause.setVisibility(View.VISIBLE);
            cat_fall.setVisibility(View.INVISIBLE);
            current_action.setText(R.string.rest);
            arrows.setVisibility(View.INVISIBLE);
            arrows_rest.setVisibility(View.VISIBLE);
            arrows_long_rest.setVisibility(View.INVISIBLE);
            mRestLeftInMillis = nowTimeRest;
            CurrentProgressRest = 100 * 10;
            restUpdateCountDownText();
        }
        else if (checkAction == 5) {
            mButtonStartPause.setVisibility(View.INVISIBLE);
            mButtonStartPauseRest.setVisibility(View.INVISIBLE);
            mButtonStartPauseLongRest.setVisibility(View.VISIBLE);
            mRestButtonReset.setVisibility(View.INVISIBLE);
            current_action.setText(R.string.longrest);
            arrows.setVisibility(View.INVISIBLE);
            arrows_rest.setVisibility(View.INVISIBLE);
            arrows_long_rest.setVisibility(View.VISIBLE);
            mLongRestLeftInMillis = nowTimeLongRest;
            longRestUpdateCountDownText();
            updateCountDownText();
        }
        else if (checkAction == 6) {
            mButtonStartPause.setVisibility(View.INVISIBLE);
            mButtonStartPauseRest.setVisibility(View.INVISIBLE);
            mButtonStartPauseLongRest.setVisibility(View.VISIBLE);
            mRestButtonReset.setVisibility(View.INVISIBLE);
            mLongRestButtonReset.setVisibility(View.VISIBLE);
            current_action.setText(R.string.longrest);
            arrows.setVisibility(View.INVISIBLE);
            arrows_rest.setVisibility(View.INVISIBLE);
            arrows_long_rest.setVisibility(View.VISIBLE);
            mLongRestLeftInMillis = nowTimeLongRest;
            CurrentProgressLongRest = 100 * 10;
            longRestUpdateCountDownText();
        }

        cat_sleep.setVisibility(View.VISIBLE);
        cat_fall.setVisibility(View.INVISIBLE);
        cat_move.setVisibility(View.INVISIBLE);
        mButtonReset.setVisibility(View.INVISIBLE);





        Intent iCheck = getIntent();
        if (iCheck != null) {

            Long returnLong = getIntent().getLongExtra("WORK_PERIOD", START_TIME_IN_MILLIS);

            START_TIME_IN_MILLIS = (long) (returnLong);
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

        Intent iCheckMelody = getIntent();
        if (iCheckMelody != null) {

            String returnLong = getIntent().getStringExtra("MELODY");
            Melody = returnLong;
            saveMelody();

        } else {
            Toast.makeText(MainActivity.this, "Intent is null", Toast.LENGTH_SHORT).show();
        }

        Intent iCheckDisplay = getIntent();
        if (iCheckDisplay != null) {

            boolean returnLong = getIntent().getBooleanExtra("DISPLAYON", displayIsOn);
            displayIsOn = returnLong;
            saveValueDisplay();

        } else {
            Toast.makeText(MainActivity.this, "Intent is null", Toast.LENGTH_SHORT).show();
        }

        Intent iCheckColor = getIntent();
        if (iCheckColor != null) {

            int returnLong = getIntent().getIntExtra("COLOR_VALUE", color_value);
            color_value = returnLong;
            saveValueColor();

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
                    if (mButtonReset.getVisibility() == View.INVISIBLE) {
                        clickableAnimation();
                        cat_move.startAnimation(fastAnimation);
                        cat_sleep.setVisibility(View.INVISIBLE);
                        menu.setVisibility(View.INVISIBLE);
                        menu.startAnimation(nullAnimation);


                        ((GifDrawable)cat_move.getDrawable()).start();
                        CurrentProgress = 0;
                        animationProgressBarStart();

                        startTimer();
                    } else {
                        clickableAnimation();
                        cat_move.startAnimation(fastAnimation);
                        cat_sleep.setVisibility(View.INVISIBLE);
                        menu.setVisibility(View.INVISIBLE);
                        menu.startAnimation(nullAnimation);

                        if (nowTime < 120001) {
                            CurrentProgress += 17.3;

                        }
                        START_TIME_IN_MILLIS -= 103;
                        mTimeLeftInMillis += 894;


                        ((GifDrawable)cat_move.getDrawable()).start();
                        startTimer();
                    }



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
                    if (mRestButtonReset.getVisibility() == View.INVISIBLE) {
                        clickableAnimationRest();
                        cat_sleep.startAnimation(fastAnimation);
                        cat_question.setVisibility(View.INVISIBLE);
                        menu.setVisibility(View.INVISIBLE);
                        menu.startAnimation(nullAnimation);

                        ((GifDrawable)cat_sleep.getDrawable()).start();
                        CurrentProgressRest = 0;
                        animationProgressBarStartRest();
                        restTimer();


                    } else {
                        clickableAnimationRest();
                        cat_sleep.startAnimation(fastAnimation);
                        cat_question.setVisibility(View.INVISIBLE);
                        menu.setVisibility(View.INVISIBLE);
                        menu.startAnimation(nullAnimation);

                        if (nowTimeRest < 120001)
                            CurrentProgressRest += 17.3;
                        REST_TIME_IN_MILLIS -= 103;
                        mRestLeftInMillis += 1200;

                        ((GifDrawable)cat_move.getDrawable()).start();
                        restTimer();
                    }
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



                    if (mLongRestButtonReset.getVisibility() == View.INVISIBLE) {
                        clickableAnimationLongRest();
                        menu.setVisibility(View.INVISIBLE);
                        menu.startAnimation(nullAnimation);
                        cat_sleep.startAnimation(fastAnimation);
                        cat_question.setVisibility(View.INVISIBLE);

                        ((GifDrawable)cat_sleep.getDrawable()).start();
                        CurrentProgressLongRest = 0;
                        animationProgressBarStartLongRest();
                        longRestTimer();


                    } else {
                        clickableAnimationLongRest();
                        menu.setVisibility(View.INVISIBLE);
                        menu.startAnimation(nullAnimation);
                        cat_sleep.startAnimation(fastAnimation);
                        cat_question.setVisibility(View.INVISIBLE);

                        if (nowTimeLongRest < 120001)
                            CurrentProgressLongRest += 17.3;
                        LONG_REST_TIME_IN_MILLIS -= 103;
                        mLongRestLeftInMillis += 1200;
                        Toast.makeText(MainActivity.this, "Current " + CurrentProgressLongRest, Toast.LENGTH_SHORT).show();
                        ((GifDrawable)cat_move.getDrawable()).start();
                        longRestTimer();
                    }


                }
            }
        });

        // Смена текущего действия
        edit_current_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (current_action.getText().toString().matches("Работа") || current_action.getText().toString().matches("Work") || current_action.getText().toString().matches("工作")
                        || current_action.getText().toString().matches("Arbeit") || current_action.getText().toString().matches("Посао") || current_action.getText().toString().matches("Робота")
                        || current_action.getText().toString().matches("Travail") || current_action.getText().toString().matches("仕事") || current_action.getText().toString().matches("Trabajo")) {
                    if (mButtonReset.getVisibility() == View.INVISIBLE) {
                        clickableAnimationChange();
                        mButtonStartPause.setVisibility(View.INVISIBLE);
                        mButtonStartPauseLongRest.setVisibility(View.INVISIBLE);
                        mButtonStartPauseRest.setVisibility(View.VISIBLE);
                        mButtonReset.setVisibility(View.INVISIBLE);
                        cat_move.setVisibility(View.INVISIBLE);
                        cat_pause.setVisibility(View.VISIBLE);
                        cat_fall.setVisibility(View.INVISIBLE);
                        cat_sleep.setVisibility(View.INVISIBLE);
                        current_action.setText(R.string.rest);
                        arrows.setVisibility(View.INVISIBLE);
                        arrows_rest.setVisibility(View.VISIBLE);
                        arrows_long_rest.setVisibility(View.INVISIBLE);
                        mRestLeftInMillis = nowTimeRest;
                        CurrentProgressRest = 100 * 10;
                        cat_pause.startAnimation(fastAnimation);
                        restUpdateCountDownText();
                    } else
                    if (mButtonReset.getVisibility() == View.VISIBLE) {
                        clickableAnimationChange();
                        mButtonStartPause.setVisibility(View.INVISIBLE);
                        mButtonStartPauseLongRest.setVisibility(View.INVISIBLE);
                        mButtonStartPauseRest.setVisibility(View.VISIBLE);
                        mButtonReset.setVisibility(View.INVISIBLE);
                        mRestButtonReset.setVisibility(View.VISIBLE);
                        cat_sleep.setVisibility(View.INVISIBLE);
                        cat_pause.setVisibility(View.VISIBLE);
                        cat_fall.setVisibility(View.INVISIBLE);
                        current_action.setText(R.string.rest);
                        arrows.setVisibility(View.INVISIBLE);
                        arrows_rest.setVisibility(View.VISIBLE);
                        arrows_long_rest.setVisibility(View.INVISIBLE);
                        mRestLeftInMillis = nowTimeRest;
                        CurrentProgressRest = 100 * 10;
                        cat_pause.startAnimation(fastAnimation);
                        restUpdateCountDownText();
                    }

                } else
                if (current_action.getText().toString().matches("Отдых") || current_action.getText().toString().matches("Rest") || current_action.getText().toString().matches("休息")
                        || current_action.getText().toString().matches("Erholung") || current_action.getText().toString().matches("Одмор") || current_action.getText().toString().matches("Відпочинок")
                        || current_action.getText().toString().matches("Détente") || current_action.getText().toString().matches("休息") || current_action.getText().toString().matches("Descanso")) {
                    if (mRestButtonReset.getVisibility() == View.INVISIBLE) {
                        clickableAnimationChange();
                        mButtonStartPauseRest.setVisibility(View.INVISIBLE);
                        mButtonStartPause.setVisibility(View.INVISIBLE);
                        mButtonStartPauseLongRest.setVisibility(View.VISIBLE);
                        mRestButtonReset.setVisibility(View.INVISIBLE);
                        current_action.setText(R.string.longrest);
                        arrows.setVisibility(View.INVISIBLE);
                        arrows_rest.setVisibility(View.INVISIBLE);
                        arrows_long_rest.setVisibility(View.VISIBLE);
                        mLongRestLeftInMillis = nowTimeLongRest;
                        CurrentProgressLongRest = 100 * 10;
                        cat_pause.startAnimation(fastAnimation);
                        longRestUpdateCountDownText();
                    } else
                    if (mRestButtonReset.getVisibility() == View.VISIBLE) {
                        clickableAnimationChange();
                        mButtonStartPauseRest.setVisibility(View.INVISIBLE);
                        mButtonStartPause.setVisibility(View.INVISIBLE);
                        mButtonStartPauseLongRest.setVisibility(View.VISIBLE);
                        mRestButtonReset.setVisibility(View.INVISIBLE);
                        mLongRestButtonReset.setVisibility(View.VISIBLE);
                        current_action.setText(R.string.longrest);
                        arrows.setVisibility(View.INVISIBLE);
                        arrows_rest.setVisibility(View.INVISIBLE);
                        arrows_long_rest.setVisibility(View.VISIBLE);
                        mLongRestLeftInMillis = nowTimeLongRest;
                        CurrentProgressLongRest = 100 * 10;
                        cat_pause.startAnimation(fastAnimation);
                        longRestUpdateCountDownText();
                    }

                } else if (current_action.getText().toString().matches("Долгий отдых") || current_action.getText().toString().matches("Long rest") || current_action.getText().toString().matches("长时间休息")
                        || current_action.getText().toString().matches("Lange Pause") || current_action.getText().toString().matches("Дуг одмор") || current_action.getText().toString().matches("Довгий відпочинок")
                        || current_action.getText().toString().matches("Long repos") || current_action.getText().toString().matches("長い休息") || current_action.getText().toString().matches("Largo descanso")){
                    if (mLongRestButtonReset.getVisibility() == View.INVISIBLE) {
                        clickableAnimationChange();
                        mButtonStartPauseLongRest.setVisibility(View.INVISIBLE);
                        mButtonStartPauseRest.setVisibility(View.INVISIBLE);
                        mButtonStartPause.setVisibility(View.VISIBLE);
                        mLongRestButtonReset.setVisibility(View.INVISIBLE);
                        cat_move.setVisibility(View.INVISIBLE);
                        cat_sleep.setVisibility(View.VISIBLE);
                        cat_question.setVisibility(View.INVISIBLE);
                        cat_pause.setVisibility(View.INVISIBLE);
                        current_action.setText(R.string.work);
                        arrows.setVisibility(View.VISIBLE);
                        arrows_rest.setVisibility(View.INVISIBLE);
                        arrows_long_rest.setVisibility(View.INVISIBLE);
                        mTimeLeftInMillis = nowTime;
                        CurrentProgress = 100 * 10;
                        cat_sleep.startAnimation(fastAnimation);
                        updateCountDownText();
                    } else
                    if (mLongRestButtonReset.getVisibility() == View.VISIBLE) {
                        clickableAnimationChange();
                        mButtonStartPauseLongRest.setVisibility(View.INVISIBLE);
                        mButtonStartPauseRest.setVisibility(View.INVISIBLE);
                        mButtonStartPause.setVisibility(View.VISIBLE);
                        mLongRestButtonReset.setVisibility(View.INVISIBLE);
                        mButtonReset.setVisibility(View.VISIBLE);
                        cat_sleep.setVisibility(View.VISIBLE);
                        cat_question.setVisibility(View.INVISIBLE);
                        cat_pause.setVisibility(View.INVISIBLE);
                        current_action.setText(R.string.work);
                        arrows.setVisibility(View.VISIBLE);
                        arrows_rest.setVisibility(View.INVISIBLE);
                        arrows_long_rest.setVisibility(View.INVISIBLE);
                        mTimeLeftInMillis = nowTime;
                        CurrentProgress = 100 * 10;
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
                    .setContentTitle(getApplicationContext().getString(R.string.timer_ready_to_start))
                    .setContentText(String.valueOf((START_TIME_IN_MILLIS / 1000) / 60) + ":00")
                    .addAction(R.drawable.arrows, getApplicationContext().getString(R.string.open), pendingIntent)
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
                    for (int j = 1; j < 21; j++) {
                        if (i == untilEndCount * j) {
                            f();
                            break;
                        }
                        else {
                            addStartCircles();
                            countCircles += 1;
                            countCircles();
                            break;
                        }
                    }
                }
                for (iDone = 0; iDone < done; iDone++) {
                    saveI();
                    for (int i = 1; i < 21; i++) {
                        if (iDone == untilEndCount * i) {
                            u();
                            break;
                        }
                        else {
                            replaceCircles();
                            countCircles += 1;
                            countCircles();
                            break;
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
                    if (i == untilEndCount * j) {
                        f();
                        break;
                    }
                    else {
                        addStartCircles();
                        countCircles += 1;
                        countCircles();
                        break;
                    }
                }
            }
            for (iDone = 0; iDone < done; iDone++) {
                saveI();
                for (int i = 1; i < 21; i++) {
                    if (iDone == untilEndCount * i) {
                        u();
                        break;
                    }
                    else {
                        replaceCircles();
                        countCircles += 1;
                        countCircles();
                        break;
                        }
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


        loadCheckAction();
        loadValueAutostart();
        loadValueWhenStop();
        loadValueUntilEnd();
        loadValueVibration();
        loadValueAutostartRest();
        loadMelody();
        loadValueDisplay();
        loadValueColor();
        mainConstraintLayout.setBackgroundColor(color_value);



        loadValue();
        loadValueRest();
        loadValueLongRest();



        if (current_action.getText().toString().matches("Работа") || current_action.getText().toString().matches("Work") || current_action.getText().toString().matches("工作")
                || current_action.getText().toString().matches("Arbeit") || current_action.getText().toString().matches("Посао") || current_action.getText().toString().matches("Робота")
                || current_action.getText().toString().matches("Travail") || current_action.getText().toString().matches("仕事") || current_action.getText().toString().matches("Trabajo")) {
            updateCountDownText();
        }
        if (current_action.getText().toString().matches("Отдых") || current_action.getText().toString().matches("Rest") || current_action.getText().toString().matches("休息")
                || current_action.getText().toString().matches("Erholung") || current_action.getText().toString().matches("Одмор") || current_action.getText().toString().matches("Відпочинок")
                || current_action.getText().toString().matches("Détente") || current_action.getText().toString().matches("休息") || current_action.getText().toString().matches("Descanso")) {
            restUpdateCountDownText();
            cat_sleep.setVisibility(View.INVISIBLE);
            cat_pause.setVisibility(View.VISIBLE);
        }
        if (current_action.getText().toString().matches("Долгий отдых") || current_action.getText().toString().matches("Long rest") || current_action.getText().toString().matches("长时间休息")
                || current_action.getText().toString().matches("Lange Pause") || current_action.getText().toString().matches("Дуг одмор") || current_action.getText().toString().matches("Довгий відпочинок")
                || current_action.getText().toString().matches("Long repos") || current_action.getText().toString().matches("長い休息") || current_action.getText().toString().matches("Largo descanso")) {
            longRestUpdateCountDownText();
            cat_sleep.setVisibility(View.INVISIBLE);
            cat_pause.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        Toast.makeText(this, "ON RESTART", Toast.LENGTH_SHORT).show();
        done -= 1;

//        Intent intent = getIntent();
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//        finish();
//        startActivity(intent);

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
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 100) {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                progressBar.setProgress((int)CurrentProgress, true); // установка значения

                waitForStart();


                current_action.setText(R.string.work);
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
                        CurrentProgress = 100 * 10;
                        CurrentProgressRest = 100 * 10;
                        CurrentProgressLongRest = 100 * 10;
                        LONG_REST_TIME_IN_MILLIS = nowTimeLongRest;
                        mRestButtonReset.setVisibility(View.INVISIBLE);
                        mButtonStartPauseLongRest.setVisibility(View.VISIBLE);
                        mButtonStartPauseRest.setVisibility(View.INVISIBLE);
                        mButtonStartPause.setVisibility(View.INVISIBLE);
                        mLongRestLeftInMillis = LONG_REST_TIME_IN_MILLIS;
                        playRingtone();
                        longRestUpdateCountDownText();
                        longRestTimer();
                        timerEnd();


                    } else {
                        if (vibration == true)
                            vibrator.vibrate(500);
                        CurrentProgress = 100 * 10;
                        CurrentProgressRest = 100 * 10;
                        CurrentProgressLongRest = 100 * 10;
                        START_TIME_IN_MILLIS = nowTime;
                        REST_TIME_IN_MILLIS = nowTimeRest;
                        LONG_REST_TIME_IN_MILLIS = nowTimeLongRest;
                        mButtonReset.setVisibility(View.INVISIBLE);
                        mButtonStartPauseRest.setVisibility(View.VISIBLE);
                        mButtonStartPauseLongRest.setVisibility(View.INVISIBLE);
                        mButtonStartPause.setVisibility(View.INVISIBLE);
                        mRestLeftInMillis = REST_TIME_IN_MILLIS;
                        playRingtone();
                        restUpdateCountDownText();
                        pauseTimer();
                        timerEnd();

                    }
                } else {
                    if (autostartRestIsOn == true) {
                        if (vibration == true)
                            vibrator.vibrate(500);
                        CurrentProgress = 100 * 10;
                        CurrentProgressRest = 100 * 10;
                        CurrentProgressLongRest = 100 * 10;
                        START_TIME_IN_MILLIS = nowTime;
                        REST_TIME_IN_MILLIS = nowTimeRest;
                        LONG_REST_TIME_IN_MILLIS = nowTimeLongRest;
                        mButtonReset.setVisibility(View.INVISIBLE);
                        mButtonStartPauseRest.setVisibility(View.VISIBLE);
                        mButtonStartPause.setVisibility(View.INVISIBLE);
                        mButtonStartPauseLongRest.setVisibility(View.INVISIBLE);
                        mRestLeftInMillis = REST_TIME_IN_MILLIS;
                        playRingtone();
                        restUpdateCountDownText();
                        restTimer();
                        timerEnd();
                    } else {
                        if (vibration == true)
                            vibrator.vibrate(500);
                        CurrentProgress = 100 * 10;
                        CurrentProgressRest = 100 * 10;
                        CurrentProgressLongRest = 100 * 10;
                        START_TIME_IN_MILLIS = nowTime;
                        REST_TIME_IN_MILLIS = nowTimeRest;
                        LONG_REST_TIME_IN_MILLIS = nowTimeLongRest;
                        mButtonReset.setVisibility(View.INVISIBLE);
                        mButtonStartPauseRest.setVisibility(View.VISIBLE);
                        mButtonStartPause.setVisibility(View.INVISIBLE);
                        mButtonStartPauseLongRest.setVisibility(View.INVISIBLE);
                        mRestLeftInMillis = REST_TIME_IN_MILLIS;
                        playRingtone();
                        restUpdateCountDownText();
                        pauseTimer();
                        timerEnd();


                    }
                }

            }
        }.start();




        mTimerRunning = true;
        mButtonReset.setVisibility(View.INVISIBLE);
    }

    private void restTimer() { // 5 минутный таймер
        mCountDownTimer = new CountDownTimer(mRestLeftInMillis, 100) {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTick(long restMillisUntilFinished) {
                mRestLeftInMillis = restMillisUntilFinished;
                progressBar.setProgress((int)CurrentProgressRest,true);
                waitForStartRest();
                restUpdateCountDownText();
                current_action.setText(R.string.rest);
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
                if (autostartIsOn == true) {
                    if (vibration == true)
                        vibrator.vibrate(500);
                    CurrentProgress = 100 * 10;
                    CurrentProgressRest = 100 * 10;
                    CurrentProgressLongRest = 100 * 10;
                    START_TIME_IN_MILLIS = nowTime;
                    REST_TIME_IN_MILLIS = nowTimeRest;
                    LONG_REST_TIME_IN_MILLIS = nowTimeLongRest;
                    mLongRestButtonReset.setVisibility(View.INVISIBLE);
                    mButtonStartPause.setVisibility(View.VISIBLE);
                    mButtonStartPauseLongRest.setVisibility(View.INVISIBLE);
                    mButtonStartPauseRest.setVisibility(View.INVISIBLE);
                    cat_sleep.setVisibility(View.INVISIBLE);
                    cat_move.setVisibility(View.VISIBLE);
                    mTimeLeftInMillis = nowTime;
                    ((GifDrawable)cat_move.getDrawable()).start();
                    playRingtone();
                    updateCountDownText();
                    startTimer();

                } else {
                    if (vibration == true)
                        vibrator.vibrate(500);
                    CurrentProgress = 100 * 10;
                    CurrentProgressRest = 100 * 10;
                    CurrentProgressLongRest = 100 * 10;
                    START_TIME_IN_MILLIS = nowTime;
                    REST_TIME_IN_MILLIS = nowTimeRest;
                    LONG_REST_TIME_IN_MILLIS = nowTimeLongRest;
                    mLongRestButtonReset.setVisibility(View.INVISIBLE);
                    mButtonStartPause.setVisibility(View.VISIBLE);
                    mButtonStartPauseLongRest.setVisibility(View.INVISIBLE);
                    mButtonStartPauseRest.setVisibility(View.INVISIBLE);
                    cat_sleep.setVisibility(View.INVISIBLE);
                    cat_move.setVisibility(View.VISIBLE);
                    mTimeLeftInMillis = nowTime;
                    ((GifDrawable)cat_move.getDrawable()).start();
                    playRingtone();
                    updateCountDownText();
                    pauseTimerRest();

                }

            }
        }.start();





        mTimerRunning = true;
        mRestButtonReset.setVisibility(View.INVISIBLE);
    }

    private void longRestTimer() { // 15 минутный таймер
        mCountDownTimer = new CountDownTimer(mLongRestLeftInMillis, 100) {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTick(long longRestMillisUntilFinished) {
                mLongRestLeftInMillis = longRestMillisUntilFinished;
                progressBar.setProgress((int)CurrentProgressLongRest,true); // установка значения
                waitForStartLongRest();
                longRestUpdateCountDownText();
                current_action.setText(R.string.longrest);
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
                    CurrentProgress = 100 * 10;
                    CurrentProgressRest = 100 * 10;
                    CurrentProgressLongRest = 100 * 10;
                    START_TIME_IN_MILLIS = nowTime;
                    REST_TIME_IN_MILLIS = nowTimeRest;
                    LONG_REST_TIME_IN_MILLIS = nowTimeLongRest;
                    mLongRestButtonReset.setVisibility(View.INVISIBLE);
                    mButtonStartPause.setVisibility(View.VISIBLE);
                    mButtonStartPauseLongRest.setVisibility(View.INVISIBLE);
                    mButtonStartPauseRest.setVisibility(View.INVISIBLE);
                    cat_sleep.setVisibility(View.INVISIBLE);
                    cat_move.setVisibility(View.VISIBLE);
                    mTimeLeftInMillis = nowTime;
                    ((GifDrawable)cat_move.getDrawable()).start();
                    playRingtone();
                    updateCountDownText();
                    startTimer();

                } else {
                    if (vibration == true)
                        vibrator.vibrate(500);
                    CurrentProgress = 100 * 10;
                    CurrentProgressRest = 100 * 10;
                    CurrentProgressLongRest = 100 * 10;
                    START_TIME_IN_MILLIS = nowTime;
                    REST_TIME_IN_MILLIS = nowTimeRest;
                    LONG_REST_TIME_IN_MILLIS = nowTimeLongRest;
                    mLongRestButtonReset.setVisibility(View.INVISIBLE);
                    mButtonStartPause.setVisibility(View.VISIBLE);
                    mButtonStartPauseLongRest.setVisibility(View.INVISIBLE);
                    mButtonStartPauseRest.setVisibility(View.INVISIBLE);
                    cat_sleep.setVisibility(View.INVISIBLE);
                    cat_move.setVisibility(View.VISIBLE);
                    mTimeLeftInMillis = nowTime;
                    ((GifDrawable)cat_move.getDrawable()).start();
                    playRingtone();
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
        START_TIME_IN_MILLIS += 100;
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
        REST_TIME_IN_MILLIS += 100;
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
        LONG_REST_TIME_IN_MILLIS += 100;
        menu.startAnimation(nullAnimation);
        cat_question.startAnimation(fastAnimation);
        mLongRestButtonReset.startAnimation(fastAnimation);
        arrows_long_rest.startAnimation(nullAnimation);
        visibilityLongPause();
    }

    private void resetTimer() {
        clickableAnimationReset();
        CurrentProgress = 1000; // начинать с (-1)
        mTimeLeftInMillis = nowTime;
        START_TIME_IN_MILLIS = nowTime;
        updateCountDownText();
        cat_move.setVisibility(View.INVISIBLE);
        cat_sleep.setVisibility(View.INVISIBLE);
        cat_fall.setVisibility(View.VISIBLE);
        mButtonReset.setVisibility(View.INVISIBLE);
        mButtonStartPause.setVisibility(View.VISIBLE);
        mButtonStartPauseRest.setVisibility(View.INVISIBLE);
        mButtonStartPauseLongRest.setVisibility(View.INVISIBLE);
        deleteImage.startAnimation(inAnimation);
        arrows.startAnimation(inAnimation);
        edit_current_action.setVisibility(View.VISIBLE);
        menu.startAnimation(inAnimation);
        visibilityYes();
        ((GifDrawable)cat_fall.getDrawable()).reset();
        gifTimer();
        arrows.setVisibility(View.VISIBLE);
        arrows_rest.setVisibility(View.INVISIBLE);
        arrows_long_rest.setVisibility(View.INVISIBLE);
        cat_fall.startAnimation(fastAnimation);
        animationProgressBarClose();
    }

    private void resetRestTimer() {
        clickableAnimationReset();
        CurrentProgressRest = 100 * 10; // начинать с (-1)
        mRestLeftInMillis = nowTimeRest;
        REST_TIME_IN_MILLIS = nowTimeRest;
        restUpdateCountDownText();
        cat_move.setVisibility(View.INVISIBLE);
        cat_question.setVisibility(View.INVISIBLE);
        cat_sleep.setVisibility(View.INVISIBLE);
        cat_pause.setAnimation(fastAnimation);
        mRestButtonReset.setVisibility(View.INVISIBLE);
        mButtonStartPauseRest.setVisibility(View.VISIBLE);
        mButtonStartPause.setVisibility(View.INVISIBLE);
        mButtonStartPauseLongRest.setVisibility(View.INVISIBLE);
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
        cat_pause.startAnimation(inAnimation);
        animationProgressBarClose();
    }

    private void resetLongRestTimer() {
        clickableAnimationReset();
        CurrentProgressLongRest = 100 * 10; // начинать с (-1)
        mLongRestLeftInMillis = nowTimeLongRest;
        LONG_REST_TIME_IN_MILLIS = nowTimeLongRest;
        longRestUpdateCountDownText();
        cat_move.setVisibility(View.INVISIBLE);
        cat_question.setVisibility(View.INVISIBLE);
        cat_sleep.setVisibility(View.INVISIBLE);
        cat_pause.setAnimation(fastAnimation);
        mLongRestButtonReset.setVisibility(View.INVISIBLE);
        mButtonStartPauseLongRest.setVisibility(View.VISIBLE);
        mButtonStartPauseRest.setVisibility(View.INVISIBLE);
        mButtonStartPause.setVisibility(View.INVISIBLE);
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
        cat_pause.startAnimation(inAnimation);
        animationProgressBarClose();
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















        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d : %02d", minutes, seconds);

        mTextViewCountDown.setText(timeLeftFormatted);


    }

    private void timerEnd() {

            done += 1;


            for (; iWell < done; iWell++) {
                for (int i = 1; i < 21; i++) {
                    if (iWell == untilEndCount * i) {
                        u();
                        break;
                    }
                    else {
                        replaceCircles();
                        countCircles += 1;
                        countCircles();
                        break;
                    }
                }
                break;


            }


            if (done == whenStopCount) {

                pauseTimer();
                ((GifDrawable)cat_move.getDrawable()).stop();
                cat_question.setVisibility(View.VISIBLE);
                menu.setVisibility(View.VISIBLE);
                visibilityCatMove();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(R.string.timer_end);
                builder.setTitle("Конец");
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
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
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        System.exit(0);
                    }
                });
                builder.show();

            }

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
        if ((current_action.getText().toString().matches("Работа") || current_action.getText().toString().matches("Work") || current_action.getText().toString().matches("工作")
                || current_action.getText().toString().matches("Arbeit") || current_action.getText().toString().matches("Посао") || current_action.getText().toString().matches("Робота")
                || current_action.getText().toString().matches("Travail") || current_action.getText().toString().matches("仕事") || current_action.getText().toString().matches("Trabajo"))
            && mButtonReset.getVisibility() == View.INVISIBLE) {
            checkAction = 1;
        } else if ((current_action.getText().toString().matches("Работа") || current_action.getText().toString().matches("Work") || current_action.getText().toString().matches("工作")
                || current_action.getText().toString().matches("Arbeit") || current_action.getText().toString().matches("Посао") || current_action.getText().toString().matches("Робота")
                || current_action.getText().toString().matches("Travail") || current_action.getText().toString().matches("仕事") || current_action.getText().toString().matches("Trabajo")) && mButtonReset.getVisibility() == View.VISIBLE) {
            checkAction = 2;
        }
        else if ((current_action.getText().toString().matches("Отдых") || current_action.getText().toString().matches("Rest") || current_action.getText().toString().matches("休息")
                || current_action.getText().toString().matches("Erholung") || current_action.getText().toString().matches("Одмор") || current_action.getText().toString().matches("Відпочинок")
                || current_action.getText().toString().matches("Détente") || current_action.getText().toString().matches("休息") || current_action.getText().toString().matches("Descanso")) && mRestButtonReset.getVisibility() == View.INVISIBLE) {
            checkAction = 3;
        }
        else if ((current_action.getText().toString().matches("Отдых") || current_action.getText().toString().matches("Rest") || current_action.getText().toString().matches("休息")
                || current_action.getText().toString().matches("Erholung") || current_action.getText().toString().matches("Одмор") || current_action.getText().toString().matches("Відпочинок")
                || current_action.getText().toString().matches("Détente") || current_action.getText().toString().matches("休息") || current_action.getText().toString().matches("Descanso")) && mRestButtonReset.getVisibility() == View.VISIBLE) {
            checkAction = 4;
        }
        else if ((current_action.getText().toString().matches("Долгий отдых") || current_action.getText().toString().matches("Long rest") || current_action.getText().toString().matches("长时间休息")
                || current_action.getText().toString().matches("Lange Pause") || current_action.getText().toString().matches("Дуг одмор") || current_action.getText().toString().matches("Довгий відпочинок")
                || current_action.getText().toString().matches("Long repos") || current_action.getText().toString().matches("長い休息") || current_action.getText().toString().matches("Largo descanso")) && mLongRestButtonReset.getVisibility() == View.INVISIBLE) {
            checkAction = 5;
        }
        else if ((current_action.getText().toString().matches("Долгий отдых") || current_action.getText().toString().matches("Long rest") || current_action.getText().toString().matches("长时间休息")
                || current_action.getText().toString().matches("Lange Pause") || current_action.getText().toString().matches("Дуг одмор") || current_action.getText().toString().matches("Довгий відпочинок")
                || current_action.getText().toString().matches("Long repos") || current_action.getText().toString().matches("長い休息") || current_action.getText().toString().matches("Largo descanso")) && mLongRestButtonReset.getVisibility() == View.VISIBLE) {
            checkAction = 6;
        }

        saveCheckAction();
        Intent intent = new Intent(MainActivity.this, Menu.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slidein, R.anim.slideout);
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

    private void saveValueDisplay() {
        pref_display = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor edlongrest = pref_display.edit();
        edlongrest.putString("save_display", String.valueOf(displayIsOn));
        edlongrest.apply();

    }

    private void loadValueDisplay() {
        pref_display = getPreferences(MODE_PRIVATE);
        String savedTextLongRest = pref_display.getString("save_display", String.valueOf(displayIsOn));
        displayIsOn = Boolean.valueOf(savedTextLongRest);
    }

    private void saveValueColor() {
        pref_color = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor edlongrest = pref_color.edit();
        edlongrest.putString("save_color", String.valueOf(color_value));
        edlongrest.apply();

    }

    private void loadValueColor() {
        pref_color = getPreferences(MODE_PRIVATE);
        String savedTextLongRest = pref_color.getString("save_color", String.valueOf(color_value));
        color_value = Integer.valueOf(savedTextLongRest);
    }


    private void saveMelody() {
        pref_melody = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor edlongrest = pref_melody.edit();
        edlongrest.putString("save_melody", String.valueOf(Melody));
        edlongrest.apply();

    }

    private void loadMelody() {
        pref_melody = getPreferences(MODE_PRIVATE);
        String savedTextLongRest = pref_melody.getString("save_melody", String.valueOf(Melody));
        Melody = String.valueOf(savedTextLongRest);
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
                layoutParams.setMargins(220, 0, 10, 0);
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
                layoutParams.setMargins(220, 0, 10, 0);
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

    private void playRingtone() {
        loadMelody();
        currentRingtone = RingtoneManager.getRingtone(MainActivity.this, Uri.parse(Melody));
        currentRingtone.play();
    }

    private void animationProgressBarClose() {
        mCountDownTimer = new CountDownTimer(1500, 1) {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTick(long restMillisUntilFinished) {
                for (int i = 0; CurrentProgress >= i;) {
                    CurrentProgress -= 40;
                    CurrentProgressRest -= 40;
                    CurrentProgressLongRest -= 40;
                    progressBar.setProgress((int) CurrentProgress);
                    break;
                }
            }

            @Override
            public void onFinish() {
                progressBar.setProgress(0);
                CurrentProgress = 1000;
                CurrentProgressRest = 1000;
                CurrentProgressLongRest = 1000;

            }
        }.start();
    }

    private void animationProgressBarStart() {
        mCountDownTimer = new CountDownTimer(1000, 1) {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTick(long restMillisUntilFinished) {
                for (int i = 1000; CurrentProgress <= i;) {
                    CurrentProgress += 40;
                    progressBar.setProgress((int) CurrentProgress);
                    break;
                }
            }

            @Override
            public void onFinish() {
                CurrentProgress = 1000;

            }
        }.start();
    }

    private void animationProgressBarStartRest() {
        mCountDownTimer = new CountDownTimer(1500, 1) {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTick(long restMillisUntilFinished) {
                for (int i = 1000; CurrentProgressRest <= i;) {
                    CurrentProgressRest += 40;
                    progressBar.setProgress((int) CurrentProgressRest);
                    break;
                }
            }

            @Override
            public void onFinish() {
                CurrentProgressRest = 1000;

            }
        }.start();
    }

    private void animationProgressBarStartLongRest() {
        mCountDownTimer = new CountDownTimer(1500, 1) {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTick(long longRestMillisUntilFinished) {
                for (int i = 1000; CurrentProgressLongRest <= i;) {
                    CurrentProgressLongRest += 40;
                    progressBar.setProgress((int) CurrentProgressLongRest);
                    break;
                }
            }

            @Override
            public void onFinish() {
                CurrentProgressLongRest = 1000;

            }
        }.start();
    }

    private void waitForStart(){
        new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {
                // You don't need to use this.
            }

            public void onFinish() {
                if ((seconds > 0 && minutes == 0) || (minutes > 0 && seconds == 0) || (minutes > 0 && seconds > 0)) {
                    START_TIME_IN_MILLIS -= 103;
                    CurrentProgress = (float) (CurrentProgress - (1.79 / (nowTime / 1000 / 60)));
                    updateCountDownText();
                }
            }

        }.start();
    }

    private void waitForStartRest(){
        new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {
                // You don't need to use this.
            }

            public void onFinish() {
                if ((seconds > 0 && minutes == 0) || (minutes > 0 && seconds == 0) || (minutes > 0 && seconds > 0)) {
                    REST_TIME_IN_MILLIS -= 103;
                    CurrentProgressRest = (float) (CurrentProgressRest - (2.1 / (nowTimeRest / 1000 / 60)));
                    restUpdateCountDownText();
                }
            }

        }.start();
    }

    private void waitForStartLongRest(){
        new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {
                // You don't need to use this.
            }

            public void onFinish() {
                if ((seconds > 0 && minutes == 0) || (minutes > 0 && seconds == 0) || (minutes > 0 && seconds > 0)) {
                    LONG_REST_TIME_IN_MILLIS -= 103;
                    CurrentProgressLongRest = (float) (CurrentProgressLongRest - (1.79 / (nowTimeLongRest / 1000 / 60))); // 1.9
                    longRestUpdateCountDownText();
                }
            }

        }.start();
    }

    private void rateUs(){

    }


























}




