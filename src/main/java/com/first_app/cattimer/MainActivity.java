package com.first_app.cattimer;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.Locale;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageButton;
import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {

    private float CurrentProgress = 100;
    private ProgressBar progressBar;

    private static final long START_TIME_IN_MILLIS = 100000; // 1500000

    private TextView mTextViewCountDown;
    private Button mButtonStartPause;
    private Button mButtonReset;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;

    private GifImageView cat_move;
    private GifImageView cat_fall;










    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewCountDown = findViewById(R.id.text_view_countdown);

        mButtonStartPause = findViewById(R.id.button_start_pause); // кнопка начала отсчета
        mButtonReset = findViewById(R.id.button_restart);

        progressBar = findViewById(R.id.progressBar);

        cat_move = (GifImageView) findViewById(R.id.cat_move);
        cat_fall = (GifImageView) findViewById(R.id.cat_fall);
        ((GifDrawable)cat_move.getDrawable()).stop(); // кот не бежит с самого начала, без нажатия на кнопку старт







        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cat_move.setVisibility(View.VISIBLE);
                cat_fall.setVisibility(View.INVISIBLE);

                if (mTimerRunning) {
                    ((GifDrawable)cat_move.getDrawable()).stop();
                    pauseTimer();
                } else {
                    cat_move.setVisibility(View.VISIBLE);
                    ((GifDrawable)cat_move.getDrawable()).start();
                    startTimer();
                }
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        updateCountDownText();
    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                progressBar.setProgress((int)CurrentProgress); // установка значения
                CurrentProgress -= 1; // значение прогресс бара
                progressBar.setMax(100);
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
//                mButtonStartPause.setText("Start");
                mButtonStartPause.setVisibility(View.INVISIBLE);
                mButtonReset.setVisibility(View.VISIBLE);
            }
        }.start();



        mTimerRunning = true;
//        mButtonStartPause.setText("pause");
        mButtonReset.setVisibility(View.INVISIBLE);
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
//        mButtonStartPause.setText("Start");
        mButtonReset.setVisibility(View.VISIBLE);
    }

    private void resetTimer() {

        CurrentProgress = 100;
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
        cat_move.setVisibility(View.INVISIBLE);
        cat_fall.setVisibility(View.VISIBLE);

        mButtonReset.setVisibility(View.INVISIBLE);
        mButtonStartPause.setVisibility(View.VISIBLE);
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



}




