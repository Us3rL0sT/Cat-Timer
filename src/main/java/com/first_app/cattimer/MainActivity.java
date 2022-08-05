package com.first_app.cattimer;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {


    private int checkAction;
    private int seconds;
    private int minutes;
    private long START_TIME_IN_MILLIS = 1500 * 1000; // 1500 сек
    private long REST_TIME_IN_MILLIS = 300 * 1000; // 300 сек
    private long LONG_REST_TIME_IN_MILLIS = 900 * 1000; // 900 сек


    private SharedPreferences pref;
    private SharedPreferences prefrest;
    private SharedPreferences preflongrest;
    private SharedPreferences check;

    private float CurrentProgress = 99; // начинать с (-1)
    private float CurrentProgressRest = 100; // начинать с (-1)
    private float CurrentProgressLongRest = 100; // начинать с (-1)
    private ProgressBar progressBar;

    private Animation inAnimation;
    private Animation outAnimation;
    private Animation nullAnimation;
    private Animation fastAnimation;
    private Animation fastexitAnimation;

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
    private ImageView stick;
    private ImageView stick1;
    private ImageView stick2;
    private ImageView stick3;
    private ImageView stick4;
    private ImageView stick5;
    private ImageView stick6;
    private ImageView stick7;
    private ImageView stick_do;
    private ImageView stick_do1;
    private ImageView stick_do2;
    private ImageView stick_do3;
    private ImageView stick_do4;
    private ImageView stick_do5;
    private ImageView stick_do6;
    private ImageView stick_do7;
    private ImageView menu;

    private byte done = 0;










    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        current_action = findViewById(R.id.current_action);

        mTextViewCountDown = findViewById(R.id.text_view_countdown);

        mButtonStartPause = findViewById(R.id.button_start_pause); // кнопка начала отсчета
        mButtonStartPauseRest = findViewById(R.id.button_start_pause_rest); // кнопка начала отсчета отдыха
        mButtonStartPauseLongRest = findViewById(R.id.button_start_pause_longrest); // кнопка начала отсчета длинного отдыха
        mButtonReset = findViewById(R.id.button_restart); // кнопка рестарта
        mRestButtonReset = findViewById(R.id.button_rest_restart);
        mLongRestButtonReset = findViewById(R.id.button_long_rest_restart);
        edit_current_action = findViewById(R.id.edit_current_action);

        inAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_in);
        outAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_out);
        nullAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_null);
        fastAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_fast);
        fastexitAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_fastexit);

        progressBar = findViewById(R.id.progressBar);

        cat_move = (GifImageView) findViewById(R.id.cat_move);
        cat_fall = (GifImageView) findViewById(R.id.cat_fall);
        cat_sleep = (GifImageView) findViewById(R.id.cat_sleep);
        cat_question = (GifImageView) findViewById(R.id.cat_question);
        cat_pause = (GifImageView) findViewById(R.id.cat_pause);

        arrows = findViewById(R.id.arrows);
        stick = findViewById(R.id.stick);
        stick1 = findViewById(R.id.stick1);
        stick2 = findViewById(R.id.stick2);
        stick3 = findViewById(R.id.stick3);
        stick4 = findViewById(R.id.stick4);
        stick5 = findViewById(R.id.stick5);
        stick6 = findViewById(R.id.stick6);
        stick7 = findViewById(R.id.stick7);
        stick_do = findViewById(R.id.stick_do);
        stick_do1 = findViewById(R.id.stick_do1);
        stick_do2 = findViewById(R.id.stick_do2);
        stick_do3 = findViewById(R.id.stick_do3);
        stick_do4 = findViewById(R.id.stick_do4);
        stick_do5 = findViewById(R.id.stick_do5);
        stick_do6 = findViewById(R.id.stick_do6);
        stick_do7 = findViewById(R.id.stick_do7);
        menu = findViewById(R.id.menu_icon);

        ((GifDrawable)cat_move.getDrawable()).stop(); // кот не бежит с самого начала, без нажатия на кнопку старт

        onStart();

    if (checkAction == 1) {
            mButtonStartPauseLongRest.setVisibility(View.INVISIBLE);
            mButtonStartPause.setVisibility(View.VISIBLE);
            mLongRestButtonReset.setVisibility(View.INVISIBLE);
    //                        mButtonReset.setVisibility(View.VISIBLE);
            cat_move.setVisibility(View.INVISIBLE);
            cat_sleep.setVisibility(View.VISIBLE);
            cat_question.setVisibility(View.INVISIBLE);
            cat_pause.setVisibility(View.INVISIBLE);
            current_action.setText("Работа");
            arrows.setX(-5);
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
            arrows.setX(-5);
            mTimeLeftInMillis = nowTime;
            CurrentProgress = 99;
            updateCountDownText();
        }
        else if (checkAction == 3) {
            mButtonStartPause.setVisibility(View.INVISIBLE);
            mButtonStartPauseRest.setVisibility(View.VISIBLE);
            mButtonReset.setVisibility(View.INVISIBLE);
    //                        mRestButtonReset.setVisibility(View.VISIBLE);
            cat_move.setVisibility(View.INVISIBLE);
            cat_pause.setVisibility(View.VISIBLE);
            cat_fall.setVisibility(View.INVISIBLE);
            cat_sleep.setVisibility(View.INVISIBLE);
            current_action.setText("Отдых");
            arrows.setX(0);
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
            arrows.setX(0);
            mRestLeftInMillis = nowTimeRest;
            CurrentProgress = 99;
            restUpdateCountDownText();
        }
        else if (checkAction == 5) {
            mButtonStartPauseRest.setVisibility(View.INVISIBLE);
            mButtonStartPauseLongRest.setVisibility(View.VISIBLE);
            mRestButtonReset.setVisibility(View.INVISIBLE);
            current_action.setText("Долгий отдых");
            arrows.setX(-90);
            mLongRestLeftInMillis = nowTimeLongRest;
            longRestUpdateCountDownText();
            updateCountDownText();
    } else if (checkAction == 6) {
            mButtonStartPauseRest.setVisibility(View.INVISIBLE);
            mButtonStartPauseLongRest.setVisibility(View.VISIBLE);
            mRestButtonReset.setVisibility(View.INVISIBLE);
            mLongRestButtonReset.setVisibility(View.VISIBLE);
            current_action.setText("Долгий отдых");
            arrows.setX(-90);
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
                    cat_move.startAnimation(fastAnimation);
                    cat_sleep.setVisibility(View.INVISIBLE);
                    menu.setVisibility(View.INVISIBLE);
                    menu.startAnimation(nullAnimation);
                    arrows.setVisibility(View.INVISIBLE);
                    arrows.startAnimation(nullAnimation);
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
                arrows.startAnimation(outAnimation);
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
                    cat_sleep.startAnimation(fastAnimation);
                    cat_question.setVisibility(View.INVISIBLE);
                    menu.setVisibility(View.INVISIBLE);
                    menu.startAnimation(nullAnimation);
                    arrows.setVisibility(View.INVISIBLE);
                    arrows.startAnimation(nullAnimation);
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
                arrows.startAnimation(outAnimation);
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
                    menu.setVisibility(View.INVISIBLE);
                    menu.startAnimation(nullAnimation);
                    cat_sleep.startAnimation(fastAnimation);
                    cat_question.setVisibility(View.INVISIBLE);
                    arrows.setVisibility(View.INVISIBLE);
                    arrows.startAnimation(nullAnimation);
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
                        mButtonStartPause.setVisibility(View.INVISIBLE);
                        mButtonStartPauseRest.setVisibility(View.VISIBLE);
                        mButtonReset.setVisibility(View.INVISIBLE);
                        cat_move.setVisibility(View.INVISIBLE);
                        cat_pause.setVisibility(View.VISIBLE);
                        cat_fall.setVisibility(View.INVISIBLE);
                        cat_sleep.setVisibility(View.INVISIBLE);
                        current_action.setText("Отдых");
                        arrows.setX(404);
                        mRestLeftInMillis = nowTimeRest;
                        restUpdateCountDownText();
                    } else
                    if (mButtonReset.getVisibility() == View.VISIBLE) {
                        mButtonStartPause.setVisibility(View.INVISIBLE);
                        mButtonStartPauseRest.setVisibility(View.VISIBLE);
                        mButtonReset.setVisibility(View.INVISIBLE);
                        mRestButtonReset.setVisibility(View.VISIBLE);
                        cat_sleep.setVisibility(View.INVISIBLE);
                        cat_pause.setVisibility(View.VISIBLE);
                        cat_fall.setVisibility(View.INVISIBLE);
                        current_action.setText("Отдых");
                        arrows.setX(404);
                        mRestLeftInMillis = nowTimeRest;
                        CurrentProgress = 99;
                        restUpdateCountDownText();
                    }

                } else
                if (current_action.getText().toString().matches("Отдых")) {
                    if (mRestButtonReset.getVisibility() == View.INVISIBLE) {
                        mButtonStartPauseRest.setVisibility(View.INVISIBLE);
                        mButtonStartPauseLongRest.setVisibility(View.VISIBLE);
                        mRestButtonReset.setVisibility(View.INVISIBLE);
                        current_action.setText("Долгий отдых");
                        arrows.setX(404 - 90);
                        mLongRestLeftInMillis = nowTimeLongRest;
                        longRestUpdateCountDownText();
                    } else
                    if (mRestButtonReset.getVisibility() == View.VISIBLE) {
                        mButtonStartPauseRest.setVisibility(View.INVISIBLE);
                        mButtonStartPauseLongRest.setVisibility(View.VISIBLE);
                        mRestButtonReset.setVisibility(View.INVISIBLE);
                        mLongRestButtonReset.setVisibility(View.VISIBLE);
                        current_action.setText("Долгий отдых");
                        arrows.setX(404 - 90);
                        mLongRestLeftInMillis = nowTimeLongRest;
                        CurrentProgress = 99;
                        longRestUpdateCountDownText();
                    }

                } else if (current_action.getText().toString().matches("Долгий отдых")){
                    if (mLongRestButtonReset.getVisibility() == View.INVISIBLE) {
                        mButtonStartPauseLongRest.setVisibility(View.INVISIBLE);
                        mButtonStartPause.setVisibility(View.VISIBLE);
                        mLongRestButtonReset.setVisibility(View.INVISIBLE);
                        cat_move.setVisibility(View.INVISIBLE);
                        cat_sleep.setVisibility(View.VISIBLE);
                        cat_question.setVisibility(View.INVISIBLE);
                        cat_pause.setVisibility(View.INVISIBLE);
                        current_action.setText("Работа");
                        arrows.setX(404);
                        mTimeLeftInMillis = nowTime;
                        updateCountDownText();
                    } else
                    if (mLongRestButtonReset.getVisibility() == View.VISIBLE) {
                        mButtonStartPauseLongRest.setVisibility(View.INVISIBLE);
                        mButtonStartPause.setVisibility(View.VISIBLE);
                        mLongRestButtonReset.setVisibility(View.INVISIBLE);
                        mButtonReset.setVisibility(View.VISIBLE);
                        cat_sleep.setVisibility(View.VISIBLE);
                        cat_question.setVisibility(View.INVISIBLE);
                        cat_pause.setVisibility(View.INVISIBLE);
                        current_action.setText("Работа");
                        arrows.setX(404);
                        mTimeLeftInMillis = nowTime;
                        CurrentProgress = 99;
                        updateCountDownText();
                    }

                }
            }
        });

        View.OnClickListener goMenu = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        updateCountDownText();


    } // ONCREATE

    @Override
    protected void onStart() {
        super.onStart();
        loadCheckAction();
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

        menu.startAnimation(inAnimation);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        onStart();
    }

    private void startTimer() { // 25 минутный таймер
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                progressBar.setProgress((int)CurrentProgress); // установка значения

                if (START_TIME_IN_MILLIS > 0) {
                    START_TIME_IN_MILLIS -= 1000;
                    CurrentProgress = (float) (CurrentProgress - (1.666666 / (nowTime / 1000 / 60)));
                }
                updateCountDownText();
                current_action.setText("Работа");
                cat_sleep.setVisibility(View.INVISIBLE);
                cat_move.setVisibility(View.VISIBLE);
                arrows.setX(398);
                visibilityStart();

            }

            @Override
            public void onFinish() {
                CurrentProgress = 99;
                START_TIME_IN_MILLIS = nowTime;
                mButtonReset.setVisibility(View.INVISIBLE);

                mButtonStartPauseRest.setVisibility(View.VISIBLE);
                mButtonStartPause.setVisibility(View.INVISIBLE);
                mRestLeftInMillis = REST_TIME_IN_MILLIS;
                restUpdateCountDownText();
                restTimer();


            }
        }.start();




        mTimerRunning = true;
        mButtonReset.setVisibility(View.INVISIBLE);
    }

    private void restTimer() { // 5 минутный таймер
        mCountDownTimer = new CountDownTimer(mRestLeftInMillis, 1000) {
            @Override
            public void onTick(long restMillisUntilFinished) {
                mRestLeftInMillis = restMillisUntilFinished;
                progressBar.setProgress((int)CurrentProgressRest);
                if (REST_TIME_IN_MILLIS > 0) {
                    REST_TIME_IN_MILLIS -= 1000;
                    CurrentProgressRest = (float) (CurrentProgressRest - (1.666666 / (nowTimeRest / 1000 / 60)));
                }
                restUpdateCountDownText();
                current_action.setText("Отдых");
                cat_sleep.setVisibility(View.VISIBLE);
                cat_move.setVisibility(View.INVISIBLE);
                cat_question.setVisibility(View.INVISIBLE);
                mRestButtonReset.setVisibility(View.INVISIBLE);
                arrows.setX(404);
            }

            @Override
            public void onFinish() {
                CurrentProgress = 99;
                REST_TIME_IN_MILLIS = nowTimeRest;
                mRestButtonReset.setVisibility(View.INVISIBLE);
                mButtonStartPauseLongRest.setVisibility(View.VISIBLE);
                mButtonStartPauseRest.setVisibility(View.INVISIBLE);
                mLongRestLeftInMillis = LONG_REST_TIME_IN_MILLIS;
                longRestUpdateCountDownText();
                longRestTimer();
            }
        }.start();





        mTimerRunning = true;
        mRestButtonReset.setVisibility(View.INVISIBLE);
    }

    private void longRestTimer() { // 15 минутный таймер
        mCountDownTimer = new CountDownTimer(mLongRestLeftInMillis, 1000) {
            @Override
            public void onTick(long longRestMillisUntilFinished) {
                mLongRestLeftInMillis = longRestMillisUntilFinished;
                progressBar.setProgress((int)CurrentProgressLongRest); // установка значения
                if (LONG_REST_TIME_IN_MILLIS > 0) {
                    LONG_REST_TIME_IN_MILLIS -= 1000;
                    CurrentProgressLongRest = (float) (CurrentProgressLongRest - (1.666666 / (nowTimeLongRest / 1000 / 60)));
                }
                longRestUpdateCountDownText();
                current_action.setText("Долгий отдых");
                cat_sleep.setVisibility(View.VISIBLE);
                cat_move.setVisibility(View.INVISIBLE);
                cat_question.setVisibility(View.INVISIBLE);
                mLongRestButtonReset.setVisibility(View.INVISIBLE);


                arrows.setX(404 - 90);
            }

            @Override
            public void onFinish() {
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

            }
        }.start();





        mTimerRunning = true;
        mLongRestButtonReset.setVisibility(View.INVISIBLE);
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mButtonReset.setVisibility(View.VISIBLE);
        cat_sleep.setVisibility(View.VISIBLE);
        cat_move.setVisibility(View.INVISIBLE);
        arrows.setVisibility(View.INVISIBLE);
        edit_current_action.setVisibility(View.INVISIBLE);
        if (seconds == 0 && minutes == 0) {
            done -= 1;
        }
        START_TIME_IN_MILLIS += 1000;
        menu.startAnimation(nullAnimation);
        arrows.startAnimation(nullAnimation);

    }

    private void pauseTimerRest() {
        mCountDownTimer.cancel();

        mTimerRunning = false;
        cat_move.setVisibility(View.INVISIBLE);
        cat_sleep.setVisibility(View.INVISIBLE);
        arrows.setVisibility(View.INVISIBLE);
        edit_current_action.setVisibility(View.INVISIBLE);
        REST_TIME_IN_MILLIS += 1000;
        menu.startAnimation(nullAnimation);
        arrows.startAnimation(nullAnimation);
        cat_question.startAnimation(fastAnimation);
        mRestButtonReset.startAnimation(fastAnimation);
        visibilityPause();
    }

    private void pauseTimerLongRest() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mLongRestButtonReset.setVisibility(View.VISIBLE);
        cat_sleep.setVisibility(View.INVISIBLE);
        cat_move.setVisibility(View.INVISIBLE);
        arrows.setX(404 - 90);
        arrows.setVisibility(View.INVISIBLE);
        edit_current_action.setVisibility(View.INVISIBLE);
        LONG_REST_TIME_IN_MILLIS += 1000;
        menu.startAnimation(nullAnimation);
        cat_question.startAnimation(fastAnimation);
        mLongRestButtonReset.startAnimation(fastAnimation);
        arrows.startAnimation(nullAnimation);
        visibilityLongPause();
    }

    private void resetTimer() {

        CurrentProgress = 99; // начинать с (-1)
        mTimeLeftInMillis = nowTime;
        START_TIME_IN_MILLIS = nowTime;
        updateCountDownText();
        cat_move.setVisibility(View.INVISIBLE);
        cat_sleep.setVisibility(View.INVISIBLE);
        cat_fall.setVisibility(View.VISIBLE);
        mButtonReset.setVisibility(View.INVISIBLE);
        mButtonStartPause.setVisibility(View.VISIBLE);
        arrows.startAnimation(inAnimation);
        edit_current_action.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
        menu.startAnimation(inAnimation);
        visibilityYes();
        ((GifDrawable)cat_fall.getDrawable()).reset();
        gifTimer();
        arrows.setVisibility(View.VISIBLE);
    }

    private void resetRestTimer() {

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
        menu.startAnimation(inAnimation);
        progressBar.setProgress(0);
        visibilityYes();
        arrows.setVisibility(View.VISIBLE);
        edit_current_action.setVisibility(View.VISIBLE);
        ((GifDrawable)cat_fall.getDrawable()).reset();
        cat_pause.setVisibility(View.VISIBLE);

    }

    private void resetLongRestTimer() {

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
        menu.startAnimation(inAnimation);
        progressBar.setProgress(0);
        visibilityYes();
        arrows.setVisibility(View.VISIBLE);
        edit_current_action.setVisibility(View.VISIBLE);
        ((GifDrawable)cat_fall.getDrawable()).reset();
        cat_pause.setVisibility(View.VISIBLE);
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

    private void visibilityStart(){
        new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {
                // You don't need to use this.
            }

            public void onFinish() {
                menu.setVisibility(View.INVISIBLE);
                arrows.setVisibility(View.INVISIBLE);
            }

        }.start();
    }


    private void updateCountDownText() {
         minutes = (int) (mTimeLeftInMillis / 1000) / 60;
         seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        if (seconds == 0 && minutes == 0) {
            done += 1;
        }




        if (done == 1) {
            stick.setVisibility(View.INVISIBLE);
            stick_do.setVisibility(View.VISIBLE);

        }
        else if (done == 2){
            stick1.setVisibility(View.INVISIBLE);
            stick_do1.setVisibility(View.VISIBLE);
        }

        else if (done == 3) {
            stick2.setVisibility(View.INVISIBLE);
            stick_do2.setVisibility(View.VISIBLE);
        }

        else if (done == 4) {
            stick3.setVisibility(View.INVISIBLE);
            stick_do3.setVisibility(View.VISIBLE);
        }

        else if (done == 5) {
            stick4.setVisibility(View.INVISIBLE);
            stick_do4.setVisibility(View.VISIBLE);
        }

        else if (done == 6) {
           stick5.setVisibility(View.INVISIBLE);
           stick_do5.setVisibility(View.VISIBLE);
       }

        else if (done == 7) {
            stick6.setVisibility(View.INVISIBLE);
            stick_do6.setVisibility(View.VISIBLE);
        }


        else if (done == 8) {
            stick7.setVisibility(View.INVISIBLE);
            stick_do7.setVisibility(View.VISIBLE);
            pauseTimer();
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Вы хорошо поработали сегодня. Поздравляем! Желаете начать сначала?");
            builder.setTitle("Конец");
            builder.setCancelable(false);
            builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    stick.setVisibility(View.VISIBLE);
                    stick_do.setVisibility(View.INVISIBLE);
                    stick1.setVisibility(View.VISIBLE);
                    stick_do1.setVisibility(View.INVISIBLE);
                    stick2.setVisibility(View.VISIBLE);
                    stick_do2.setVisibility(View.INVISIBLE);
                    stick3.setVisibility(View.VISIBLE);
                    stick_do3.setVisibility(View.INVISIBLE);
                    stick4.setVisibility(View.VISIBLE);
                    stick_do4.setVisibility(View.INVISIBLE);
                    stick5.setVisibility(View.VISIBLE);
                    stick_do5.setVisibility(View.INVISIBLE);
                    stick6.setVisibility(View.VISIBLE);
                    stick_do6.setVisibility(View.INVISIBLE);
                    stick7.setVisibility(View.VISIBLE);
                    stick_do7.setVisibility(View.INVISIBLE);
                    done = 0;

                    dialogInterface.cancel();
                    resetTimer();

                }
            });
            builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    stick7.setVisibility(View.VISIBLE);
                    stick_do7.setVisibility(View.INVISIBLE);
                    done = 6;
                    dialogInterface.cancel();
                }
            });
            builder.show();
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







}




