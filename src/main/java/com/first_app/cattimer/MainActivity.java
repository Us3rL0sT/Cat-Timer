package com.first_app.cattimer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {

    private float CurrentProgress = 30; // начинать с (-1)
    private float CurrentProgressRest = 30; // начинать с (-1)
    private float CurrentProgressLongRest = 30; // начинать с (-1)
    private ProgressBar progressBar;

    private static final long START_TIME_IN_MILLIS = 10 * 1000; // 1500 сек
    private static final long REST_TIME_IN_MILLIS = 5 * 1000; // 300 сек
    private static final long LONG_REST_TIME_IN_MILLIS = 15 * 1000; // 900 сек

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

    private GifImageView cat_move;
    private GifImageView cat_fall;
    private GifImageView cat_sleep;
    private GifImageView cat_question;



    private ImageView arrows;
    private float defaultArrows;










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

        progressBar = findViewById(R.id.progressBar);

        cat_move = (GifImageView) findViewById(R.id.cat_move);
        cat_fall = (GifImageView) findViewById(R.id.cat_fall);
        cat_sleep = (GifImageView) findViewById(R.id.cat_sleep);
        cat_question = (GifImageView) findViewById(R.id.cat_question);

        arrows = findViewById(R.id.arrows);

        ((GifDrawable)cat_move.getDrawable()).stop(); // кот не бежит с самого начала, без нажатия на кнопку старт







        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat_sleep.setVisibility(View.INVISIBLE);
                cat_move.setVisibility(View.VISIBLE);
                cat_fall.setVisibility(View.INVISIBLE);

                if (mTimerRunning) {
                    ((GifDrawable)cat_move.getDrawable()).stop();
                    pauseTimer();
                } else {
                    cat_move.setVisibility(View.VISIBLE);
                    ((GifDrawable)cat_move.getDrawable()).start();
                    CurrentProgress += 1;
                    startTimer();


                }
            }
        });

        mButtonStartPauseRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cat_sleep.setVisibility(View.VISIBLE);
                cat_fall.setVisibility(View.INVISIBLE);

                if (mTimerRunning) {
                    ((GifDrawable)cat_sleep.getDrawable()).stop();
                    pauseTimerRest();
                } else {
                    cat_sleep.setVisibility(View.VISIBLE);
                    cat_question.setVisibility(View.INVISIBLE);
                    ((GifDrawable)cat_sleep.getDrawable()).start();
                    CurrentProgressRest += 1;
                    restTimer();


                }
            }
        });

        mButtonStartPauseLongRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cat_sleep.setVisibility(View.VISIBLE);
                cat_fall.setVisibility(View.INVISIBLE);

                if (mTimerRunning) {
                    ((GifDrawable)cat_sleep.getDrawable()).stop();
                    pauseTimerLongRest();
                } else {
                    cat_sleep.setVisibility(View.VISIBLE);
                    cat_question.setVisibility(View.INVISIBLE);
                    ((GifDrawable)cat_sleep.getDrawable()).start();
                    CurrentProgressLongRest += 1;
                    longRestTimer();


                }
            }
        });

        // Смена текущего действия
        edit_current_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (current_action.getText().toString().matches("Работа")) {
                    mButtonStartPause.setVisibility(View.INVISIBLE);
                    mButtonStartPauseRest.setVisibility(View.VISIBLE);
                    current_action.setText("Отдых");
                    arrows.setX(404);
                    restUpdateCountDownText();
                } else
                if (current_action.getText().toString().matches("Отдых")) {
                    mButtonStartPauseRest.setVisibility(View.INVISIBLE);
                    mButtonStartPauseLongRest.setVisibility(View.VISIBLE);
                    current_action.setText("Долгий отдых");
                    arrows.setX(404 - 90);
                    longRestUpdateCountDownText();
                } else if (current_action.getText().toString().matches("Долгий отдых")){
                    mButtonStartPauseLongRest.setVisibility(View.INVISIBLE);
                    mButtonStartPause.setVisibility(View.VISIBLE);
                    current_action.setText("Работа");
                    arrows.setX(404);
                    updateCountDownText();
                }
            }
        });


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
    }


    private void startTimer() { // 25 минутный таймер
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                progressBar.setProgress((int)CurrentProgress); // установка значения
                CurrentProgress -= 1; // значение прогресс бара
                updateCountDownText();
                current_action.setText("Работа");
                arrows.setX(404);
            }

            @Override
            public void onFinish() {

                mButtonReset.setVisibility(View.INVISIBLE);
                mButtonStartPauseRest.setVisibility(View.VISIBLE);
                mButtonStartPause.setVisibility(View.INVISIBLE);
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
                CurrentProgressRest -= 1;
                restUpdateCountDownText();
                current_action.setText("Отдых");
                cat_sleep.setVisibility(View.VISIBLE);
                cat_move.setVisibility(View.INVISIBLE);
                arrows.setX(404);
            }

            @Override
            public void onFinish() {
                mRestButtonReset.setVisibility(View.INVISIBLE);
                mButtonStartPauseLongRest.setVisibility(View.VISIBLE);
                mButtonStartPauseRest.setVisibility(View.INVISIBLE);
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
                CurrentProgressLongRest -= 1; // значение прогресс бара
                longRestUpdateCountDownText();
                current_action.setText("Долгий отдых");
                cat_sleep.setVisibility(View.VISIBLE);
                cat_move.setVisibility(View.INVISIBLE);


                arrows.setX(404 - 90);
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                ((GifDrawable)cat_sleep.getDrawable()).stop();
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

    }

    private void pauseTimerRest() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mRestButtonReset.setVisibility(View.VISIBLE);
        cat_move.setVisibility(View.INVISIBLE);
        cat_sleep.setVisibility(View.INVISIBLE);
        cat_question.setVisibility(View.VISIBLE);

    }

    private void pauseTimerLongRest() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mLongRestButtonReset.setVisibility(View.VISIBLE);
        cat_sleep.setVisibility(View.INVISIBLE);
        cat_move.setVisibility(View.INVISIBLE);
        cat_question.setVisibility(View.VISIBLE);
        arrows.setX(404 - 90);
    }

    private void resetTimer() {

        CurrentProgress = 60; // начинать с (-1)
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
        cat_move.setVisibility(View.INVISIBLE);
        cat_sleep.setVisibility(View.INVISIBLE);
        cat_fall.setVisibility(View.VISIBLE);
        mButtonReset.setVisibility(View.INVISIBLE);
        mButtonStartPause.setVisibility(View.VISIBLE);
        ((GifDrawable)cat_fall.getDrawable()).reset();
        gifTimer();

    }

    private void resetRestTimer() {

        CurrentProgress = 30; // начинать с (-1)
        mRestLeftInMillis = REST_TIME_IN_MILLIS;
        restUpdateCountDownText();
        cat_move.setVisibility(View.INVISIBLE);
        cat_fall.setVisibility(View.VISIBLE);
        cat_sleep.setVisibility(View.INVISIBLE);
        mRestButtonReset.setVisibility(View.INVISIBLE);
        mButtonStartPauseRest.setVisibility(View.VISIBLE);
        ((GifDrawable)cat_fall.getDrawable()).reset();
        gifTimer();

    }

    private void resetLongRestTimer() {

        CurrentProgress = 25; // начинать с (-1)
        mLongRestLeftInMillis = LONG_REST_TIME_IN_MILLIS;
        longRestUpdateCountDownText();
        cat_move.setVisibility(View.INVISIBLE);
        cat_fall.setVisibility(View.VISIBLE);
        cat_sleep.setVisibility(View.INVISIBLE);
        mLongRestButtonReset.setVisibility(View.INVISIBLE);
        mButtonStartPauseLongRest.setVisibility(View.VISIBLE);
        ((GifDrawable)cat_fall.getDrawable()).reset();
        gifTimer();

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



    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d : %02d", minutes, seconds);

        mTextViewCountDown.setText(timeLeftFormatted);
    }

    private void restUpdateCountDownText() {
        int minutes = (int) (mRestLeftInMillis / 1000) / 60;
        int seconds = (int) (mRestLeftInMillis / 1000) % 60;

        String timeRestLeftFormatted = String.format(Locale.getDefault(), "%02d : %02d", minutes, seconds);

        mTextViewCountDown.setText(timeRestLeftFormatted);
    }

    private void longRestUpdateCountDownText() {
        int minutes = (int) (mLongRestLeftInMillis / 1000) / 60;
        int seconds = (int) (mLongRestLeftInMillis / 1000) % 60;

        String timeRestLeftFormatted = String.format(Locale.getDefault(), "%02d : %02d", minutes, seconds);

        mTextViewCountDown.setText(timeRestLeftFormatted);
    }


//    private void showRestGif() {
//
//        if (isRest == true) {
//            final Handler handler = new Handler(Looper.getMainLooper());
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    cat_sleep.setVisibility(View.VISIBLE);
//                    cat_move.setVisibility(View.INVISIBLE);
//                    current_action.setText("Пауза");
//                }
//            }, 5000);
//        } else
//            return;
//
//    }







}




