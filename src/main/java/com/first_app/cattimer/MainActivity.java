package com.first_app.cattimer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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

    private static final long START_TIME_IN_MILLIS = 3 * 1000; // 1500 сек
    private static final long REST_TIME_IN_MILLIS = 3 * 1000; // 300 сек
    private static final long LONG_REST_TIME_IN_MILLIS = 3 * 1000; // 900 сек

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

        progressBar = findViewById(R.id.progressBar);

        cat_move = (GifImageView) findViewById(R.id.cat_move);
        cat_fall = (GifImageView) findViewById(R.id.cat_fall);
        cat_sleep = (GifImageView) findViewById(R.id.cat_sleep);
        cat_question = (GifImageView) findViewById(R.id.cat_question);

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

        ((GifDrawable)cat_move.getDrawable()).stop(); // кот не бежит с самого начала, без нажатия на кнопку старт







        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat_sleep.setVisibility(View.INVISIBLE);
                cat_move.setVisibility(View.VISIBLE);
                cat_fall.setVisibility(View.INVISIBLE);
                arrows.setVisibility(View.INVISIBLE);
                edit_current_action.setVisibility(View.INVISIBLE);

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
                arrows.setVisibility(View.INVISIBLE);
                edit_current_action.setVisibility(View.INVISIBLE);

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
                arrows.setVisibility(View.INVISIBLE);
                edit_current_action.setVisibility(View.INVISIBLE);

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
                    if (mButtonReset.getVisibility() == View.INVISIBLE) {
                        mButtonStartPause.setVisibility(View.INVISIBLE);
                        mButtonStartPauseRest.setVisibility(View.VISIBLE);
                        mButtonReset.setVisibility(View.INVISIBLE);
                        mRestButtonReset.setVisibility(View.VISIBLE);
                        cat_move.setVisibility(View.INVISIBLE);
                        cat_sleep.setVisibility(View.VISIBLE);
                        current_action.setText("Отдых");
                        arrows.setX(404);
                        mRestLeftInMillis = REST_TIME_IN_MILLIS;
                        restUpdateCountDownText();
                    } else
                    if (mButtonReset.getVisibility() == View.VISIBLE) {
                        mButtonStartPause.setVisibility(View.INVISIBLE);
                        mButtonStartPauseRest.setVisibility(View.VISIBLE);
                        mButtonReset.setVisibility(View.INVISIBLE);
                        mRestButtonReset.setVisibility(View.VISIBLE);
                        cat_sleep.setVisibility(View.INVISIBLE);
                        cat_question.setVisibility(View.VISIBLE);
                        current_action.setText("Отдых");
                        arrows.setX(404);
                        mRestLeftInMillis = REST_TIME_IN_MILLIS;
                        restUpdateCountDownText();
                    }

                } else
                if (current_action.getText().toString().matches("Отдых")) {
                    if (mRestButtonReset.getVisibility() == View.INVISIBLE) {
                        mButtonStartPauseRest.setVisibility(View.INVISIBLE);
                        mButtonStartPauseLongRest.setVisibility(View.VISIBLE);
                        mRestButtonReset.setVisibility(View.INVISIBLE);
                        mLongRestButtonReset.setVisibility(View.VISIBLE);
                        current_action.setText("Долгий отдых");
                        arrows.setX(404 - 90);
                        mLongRestLeftInMillis = LONG_REST_TIME_IN_MILLIS;
                        longRestUpdateCountDownText();
                    } else
                    if (mRestButtonReset.getVisibility() == View.VISIBLE) {
                        mButtonStartPauseRest.setVisibility(View.INVISIBLE);
                        mButtonStartPauseLongRest.setVisibility(View.VISIBLE);
                        mRestButtonReset.setVisibility(View.INVISIBLE);
                        mLongRestButtonReset.setVisibility(View.VISIBLE);
                        current_action.setText("Долгий отдых");
                        arrows.setX(404 - 90);
                        mLongRestLeftInMillis = LONG_REST_TIME_IN_MILLIS;
                        longRestUpdateCountDownText();
                    }

                } else if (current_action.getText().toString().matches("Долгий отдых")){
                    if (mLongRestButtonReset.getVisibility() == View.INVISIBLE) {
                        mButtonStartPauseLongRest.setVisibility(View.INVISIBLE);
                        mButtonStartPause.setVisibility(View.VISIBLE);
                        mLongRestButtonReset.setVisibility(View.INVISIBLE);
                        mButtonReset.setVisibility(View.VISIBLE);
                        cat_move.setVisibility(View.VISIBLE);
                        cat_sleep.setVisibility(View.INVISIBLE);
                        current_action.setText("Работа");
                        arrows.setX(404);
                        mTimeLeftInMillis = START_TIME_IN_MILLIS;
                        updateCountDownText();
                    } else
                    if (mLongRestButtonReset.getVisibility() == View.VISIBLE) {
                        mButtonStartPauseLongRest.setVisibility(View.INVISIBLE);
                        mButtonStartPause.setVisibility(View.VISIBLE);
                        mLongRestButtonReset.setVisibility(View.INVISIBLE);
                        mButtonReset.setVisibility(View.VISIBLE);
                        cat_sleep.setVisibility(View.VISIBLE);
                        cat_question.setVisibility(View.INVISIBLE);
                        current_action.setText("Работа");
                        arrows.setX(404);
                        mTimeLeftInMillis = START_TIME_IN_MILLIS;
                        updateCountDownText();
                    }

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
                CurrentProgressLongRest -= 1; // значение прогресс бара
                longRestUpdateCountDownText();
                current_action.setText("Долгий отдых");
                cat_sleep.setVisibility(View.VISIBLE);
                cat_move.setVisibility(View.INVISIBLE);


                arrows.setX(404 - 90);
            }

            @Override
            public void onFinish() {
                mLongRestButtonReset.setVisibility(View.INVISIBLE);
                mButtonStartPause.setVisibility(View.VISIBLE);
                mButtonStartPauseLongRest.setVisibility(View.INVISIBLE);
                cat_sleep.setVisibility(View.INVISIBLE);
                cat_move.setVisibility(View.VISIBLE);
                mTimeLeftInMillis = START_TIME_IN_MILLIS;
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
        arrows.setVisibility(View.VISIBLE);
        edit_current_action.setVisibility(View.VISIBLE);

    }

    private void pauseTimerRest() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mRestButtonReset.setVisibility(View.VISIBLE);
        cat_move.setVisibility(View.INVISIBLE);
        cat_sleep.setVisibility(View.INVISIBLE);
        cat_question.setVisibility(View.VISIBLE);
        arrows.setVisibility(View.VISIBLE);
        edit_current_action.setVisibility(View.VISIBLE);
    }

    private void pauseTimerLongRest() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mLongRestButtonReset.setVisibility(View.VISIBLE);
        cat_sleep.setVisibility(View.INVISIBLE);
        cat_move.setVisibility(View.INVISIBLE);
        cat_question.setVisibility(View.VISIBLE);
        arrows.setX(404 - 90);
        arrows.setVisibility(View.VISIBLE);
        edit_current_action.setVisibility(View.VISIBLE);
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
        if (minutes == 0 && seconds == 0) {
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
            seconds = 3;


            builder.setMessage("Вы хорошо поработали сегодня. Поздравляем! Желаете начать сначала?");
            builder.setTitle("Конец");
            builder.setCancelable(true);
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
                    dialogInterface.cancel();
                }
            });
            builder.show();
        }








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




