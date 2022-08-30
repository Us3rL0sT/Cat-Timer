package com.first_app.cattimer;


import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class Menu extends MainActivity {

        private long START_TIME_IN_MILLIS = 1500 * 1000;
        private long REST_TIME_IN_MILLIS = 300 * 1000; // 300 сек
        private long LONG_REST_TIME_IN_MILLIS = 900 * 1000;

        private short whenStopCount = 8;
        private short untilEndCount = 4;

        private SharedPreferences pref;
        private SharedPreferences prefrest;
        private SharedPreferences preflongrest;
        private SharedPreferences prefautostart;
        private SharedPreferences whenstop;
        private SharedPreferences untilend;

        private TextView work_time;
        private TextView rest_time;
        private TextView long_rest_time;
        private EditText work_time_invisible;
        private TextView rest_time_invisible;
        private TextView long_rest_time_invisible;

        private TextView until_end_invisible;
        private TextView when_stop;
        private TextView until_end;
        private TextView when_stop_invisible;

        private Button button_work_time;
        private Button button_rest_time;
        private Button button_long_rest_time;
        private Button button_color;
        private Button button_autostart_done;
        private Button button_autostart_notdone;
        private Button button_whenstop;
        private Button button_until_end;
        private Button button_autostart_background;
        private Button button_whenstop_background;

        private ImageView title;
        private ImageView autostart_done;
        private ImageView autostart_notdone;
        private ImageView underline1;
        private ImageView underline2;
        private ImageView plus;
        private ImageView minus;
        private ImageView plus_rest;
        private ImageView minus_rest;
        private ImageView plus_long_rest;
        private ImageView minus_long_rest;
        private ImageView plus_whenstop;
        private ImageView plus_until_end;
        private ImageView minus_whenstop;
        private ImageView minus_until_end;

        private View shade;

        private ConstraintLayout maximizedContainer;

        private ScrollView SW;

        private boolean isBlockedScrollView;
        private boolean autostartIsOn = true;

        private Animation inAnimation;
        private Animation outAnimation;
        private Animation nullAnimation;
        private Animation fastAnimation;





    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        inAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_in);
        outAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_out);
        nullAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_null);
        fastAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_fast);

        button_autostart_done = findViewById(R.id.button_autostart_done);
        button_whenstop_background = findViewById(R.id.button_whenstop_background);
        button_autostart_background = findViewById(R.id.button_autostart_background);
        button_autostart_notdone = findViewById(R.id.button_autostart_notdone);
        autostart_done = findViewById(R.id.autostart_done);
        autostart_notdone = findViewById(R.id.autostart_notdone);
        underline1 = findViewById(R.id.underline1);
        underline2 = findViewById(R.id.underline2);
        plus = findViewById(R.id.plus);
        minus = findViewById(R.id.minus);
        plus_rest = findViewById(R.id.plus_rest);
        minus_rest = findViewById(R.id.minus_rest);
        plus_long_rest = findViewById(R.id.plus_long_rest);
        minus_long_rest = findViewById(R.id.minus_long_rest);
        plus_whenstop = findViewById(R.id.plus_whenstop);
        plus_until_end = findViewById(R.id.plus_until_end);
        minus_whenstop = findViewById(R.id.minus_whenstop);
        minus_until_end = findViewById(R.id.minus_until_end);

        SW = findViewById(R.id.SW);

        title = findViewById(R.id.goBack);

        shade = findViewById(R.id.shade);
        maximizedContainer = findViewById(R.id.maximized_container);

        work_time = (findViewById(R.id.work_time));
        rest_time = (findViewById(R.id.rest_time));
        long_rest_time = (findViewById(R.id.long_rest_time));
        work_time_invisible = findViewById(R.id.work_time_invisible);
        rest_time_invisible = findViewById(R.id.rest_time_invisible);
        until_end_invisible = findViewById(R.id.until_end_invisible);
        when_stop_invisible = findViewById(R.id.when_stop_invisible);
        long_rest_time_invisible = findViewById(R.id.long_rest_time_invisible);
        when_stop = findViewById(R.id.when_stop);
        until_end = findViewById(R.id.until_end);


        button_work_time = findViewById(R.id.button_work_time);
        button_rest_time = findViewById(R.id.button_rest_time);
        button_long_rest_time = findViewById(R.id.button_long_rest_time);
        button_color = findViewById(R.id.button_color);
        button_whenstop = findViewById(R.id.button_whenstop);
        button_until_end = findViewById(R.id.button_until_end);

        button_whenstop.getBackground().setAlpha(128);
        button_until_end.getBackground().setAlpha(128);
        button_work_time.getBackground().setAlpha(128);
        button_rest_time.getBackground().setAlpha(128);
        button_long_rest_time.getBackground().setAlpha(128);
        button_color.getBackground().setAlpha(128);
        work_time.setText(String.valueOf((START_TIME_IN_MILLIS / 1000) / 60));
        rest_time.setText(String.valueOf((REST_TIME_IN_MILLIS / 1000) / 60));
        long_rest_time.setText(String.valueOf((LONG_REST_TIME_IN_MILLIS / 1000) / 60));
        when_stop.setText(String.valueOf(whenStopCount));
        until_end.setText(String.valueOf(untilEndCount));


        work_time_invisible.setInputType(InputType.TYPE_CLASS_NUMBER);
        rest_time_invisible.setInputType(InputType.TYPE_CLASS_NUMBER);
        long_rest_time_invisible.setInputType(InputType.TYPE_CLASS_NUMBER);
        when_stop_invisible.setInputType(InputType.TYPE_CLASS_NUMBER);
        until_end_invisible.setInputType(InputType.TYPE_CLASS_NUMBER);




        loadValue();
        loadValueRest();
        loadValueLongRest();
        loadValueAutostart();
        loadValueWhenStop();
        loadValueUntilEnd();


        if (autostartIsOn == true) {
            underline1.setVisibility(View.VISIBLE);
            underline2.setVisibility(View.INVISIBLE);
        } else {
            underline2.setVisibility(View.VISIBLE);
            underline1.setVisibility(View.INVISIBLE);
        }


        work_time_invisible.setText(String.valueOf((START_TIME_IN_MILLIS / 1000) / 60));
        rest_time_invisible.setText(String.valueOf((REST_TIME_IN_MILLIS / 1000) / 60));
        long_rest_time_invisible.setText(String.valueOf((LONG_REST_TIME_IN_MILLIS / 1000) / 60));
        when_stop_invisible.setText(String.valueOf(whenStopCount));
        until_end_invisible.setText(String.valueOf(untilEndCount));


        button_whenstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maximizedContainer.setVisibility(View.VISIBLE);
                shade.setVisibility(View.VISIBLE);
                when_stop_invisible.setVisibility(View.VISIBLE);
                plus_whenstop.setVisibility(View.VISIBLE);
                minus_whenstop.setVisibility(View.VISIBLE);
                button_whenstop.setClickable(false);
                button_until_end.setClickable(false);
                button_color.setClickable(false);
                button_autostart_done.setClickable(false);
                button_autostart_notdone.setClickable(false);
                button_autostart_background.setClickable(false);
                button_whenstop_background.setClickable(false);
                isBlockedScrollView = true;
                SW.smoothScrollTo(0, 1400);
            }
        });

        plus_whenstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whenStopCount = (Short.valueOf(when_stop_invisible.getText().toString()));
                if (whenStopCount < 20) {
                    whenStopCount += 1;
                }
                when_stop.setText(String.valueOf(whenStopCount));
                when_stop_invisible.setText(String.valueOf(whenStopCount));
                saveValueWhenStop();
            }
        });

        minus_whenstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whenStopCount = (Short.valueOf(when_stop_invisible.getText().toString()));
                if (whenStopCount > 1) {
                    whenStopCount -= 1;
                }
                when_stop.setText(String.valueOf(whenStopCount));
                when_stop_invisible.setText(String.valueOf(whenStopCount));
                saveValueWhenStop();
            }
        });

        button_until_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maximizedContainer.setVisibility(View.VISIBLE);
                shade.setVisibility(View.VISIBLE);
                until_end_invisible.setVisibility(View.VISIBLE);
                plus_until_end.setVisibility(View.VISIBLE);
                minus_until_end.setVisibility(View.VISIBLE);
                button_until_end.setClickable(false);
                button_whenstop.setClickable(false);
                button_color.setClickable(false);
                button_autostart_done.setClickable(false);
                button_autostart_notdone.setClickable(false);
                button_autostart_background.setClickable(false);
                button_whenstop_background.setClickable(false);
                isBlockedScrollView = true;
                SW.smoothScrollTo(0, 1400);
            }
        });

        plus_until_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                untilEndCount = (Short.valueOf(until_end_invisible.getText().toString()));
                if (untilEndCount < 20) {
                    untilEndCount += 1;
                }
                until_end.setText(String.valueOf(untilEndCount));
                until_end_invisible.setText(String.valueOf(untilEndCount));
                saveValueUntilEnd();
            }
        });

        minus_until_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                untilEndCount = (Short.valueOf(until_end_invisible.getText().toString()));
                if (untilEndCount > 1) {
                    untilEndCount -= 1;
                }
                until_end.setText(String.valueOf(untilEndCount));
                until_end_invisible.setText(String.valueOf(untilEndCount));
                saveValueUntilEnd();
            }
        });

        button_work_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maximizedContainer.setVisibility(View.VISIBLE);
                work_time_invisible.setVisibility(View.VISIBLE);
                shade.setVisibility(View.VISIBLE);
                rest_time_invisible.setVisibility(View.INVISIBLE);
                long_rest_time_invisible.setVisibility(View.INVISIBLE);
                plus.setVisibility(View.VISIBLE);
                minus.setVisibility(View.VISIBLE);
                button_autostart_background.setClickable(false);
                button_work_time.setClickable(false);
                button_rest_time.setClickable(false);
                button_long_rest_time.setClickable(false);
                button_color.setClickable(false);
                button_autostart_done.setClickable(false);
                button_autostart_notdone.setClickable(false);
                isBlockedScrollView = true;
                SW.smoothScrollTo(0, 0);
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                START_TIME_IN_MILLIS = Long.valueOf(work_time_invisible.getText().toString()) * 60 * 1000;
                if (START_TIME_IN_MILLIS < 5940000) {
                    START_TIME_IN_MILLIS += 60000;
                }
                work_time.setText(String.valueOf((START_TIME_IN_MILLIS / 1000) / 60));
                work_time_invisible.setText(String.valueOf((START_TIME_IN_MILLIS / 1000) / 60));
                saveValue();
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                START_TIME_IN_MILLIS = Long.valueOf(work_time_invisible.getText().toString()) * 60 * 1000;
                if (START_TIME_IN_MILLIS > 60000) {
                    START_TIME_IN_MILLIS -= 60000;
                }
                work_time.setText(String.valueOf((START_TIME_IN_MILLIS / 1000) / 60));
                work_time_invisible.setText(String.valueOf((START_TIME_IN_MILLIS / 1000) / 60));
                saveValue();
            }
        });



        button_rest_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maximizedContainer.setVisibility(View.VISIBLE);
                rest_time_invisible.setVisibility(View.VISIBLE);
                shade.setVisibility(View.VISIBLE);
                work_time_invisible.setVisibility(View.INVISIBLE);
                long_rest_time_invisible.setVisibility(View.INVISIBLE);
                plus_rest.setVisibility(View.VISIBLE);
                minus_rest.setVisibility(View.VISIBLE);
                button_autostart_background.setClickable(false);
                button_work_time.setClickable(false);
                button_rest_time.setClickable(false);
                button_long_rest_time.setClickable(false);
                button_color.setClickable(false);
                button_autostart_done.setClickable(false);
                button_autostart_notdone.setClickable(false);
                isBlockedScrollView = true;
                SW.smoothScrollTo(0, 0);
            }
        });

        plus_rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                REST_TIME_IN_MILLIS = Long.valueOf(rest_time_invisible.getText().toString()) * 60 * 1000;
                if (REST_TIME_IN_MILLIS < 5940000) {
                    REST_TIME_IN_MILLIS += 60000;
                }
                rest_time.setText(String.valueOf((REST_TIME_IN_MILLIS / 1000) / 60));
                rest_time_invisible.setText(String.valueOf((REST_TIME_IN_MILLIS / 1000) / 60));
                saveValueRest();
            }
        });

        minus_rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                REST_TIME_IN_MILLIS = Long.valueOf(rest_time_invisible.getText().toString()) * 60 * 1000;
                if (REST_TIME_IN_MILLIS > 60000) {
                    REST_TIME_IN_MILLIS -= 60000;
                }
                rest_time.setText(String.valueOf((REST_TIME_IN_MILLIS / 1000) / 60));
                rest_time_invisible.setText(String.valueOf((REST_TIME_IN_MILLIS / 1000) / 60));
                saveValueRest();
            }
        });



        button_long_rest_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maximizedContainer.setVisibility(View.VISIBLE);
                long_rest_time_invisible.setVisibility(View.VISIBLE);
                work_time_invisible.setVisibility(View.INVISIBLE);
                rest_time_invisible.setVisibility(View.INVISIBLE);
                shade.setVisibility(View.VISIBLE);
                plus_long_rest.setVisibility(View.VISIBLE);
                minus_long_rest.setVisibility(View.VISIBLE);
                button_autostart_background.setClickable(false);
                button_work_time.setClickable(false);
                button_rest_time.setClickable(false);
                button_long_rest_time.setClickable(false);
                button_color.setClickable(false);
                button_autostart_done.setClickable(false);
                button_autostart_notdone.setClickable(false);
                isBlockedScrollView = true;
                SW.smoothScrollTo(0, 0);
            }
        });

        plus_long_rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LONG_REST_TIME_IN_MILLIS = Long.valueOf(long_rest_time_invisible.getText().toString()) * 60 * 1000;
                if (LONG_REST_TIME_IN_MILLIS < 5940000) {
                    LONG_REST_TIME_IN_MILLIS += 60000;
                }
                long_rest_time.setText(String.valueOf((LONG_REST_TIME_IN_MILLIS / 1000) / 60));
                long_rest_time_invisible.setText(String.valueOf((LONG_REST_TIME_IN_MILLIS / 1000) / 60));
                saveValueLongRest();
            }
        });

        minus_long_rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LONG_REST_TIME_IN_MILLIS = Long.valueOf(long_rest_time_invisible.getText().toString()) * 60 * 1000;
                if (LONG_REST_TIME_IN_MILLIS > 60000) {
                    LONG_REST_TIME_IN_MILLIS -= 60000;
                }
                long_rest_time.setText(String.valueOf((LONG_REST_TIME_IN_MILLIS / 1000) / 60));
                long_rest_time_invisible.setText(String.valueOf((LONG_REST_TIME_IN_MILLIS / 1000) / 60));
                saveValueLongRest();
            }
        });

        shade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(Menu.this);
                isBlockedScrollView = false;
                shade.setVisibility(View.INVISIBLE);
                work_time_invisible.setVisibility(View.INVISIBLE);
                rest_time_invisible.setVisibility(View.INVISIBLE);
                long_rest_time_invisible.setVisibility(View.INVISIBLE);
                plus.setVisibility(View.INVISIBLE);
                minus.setVisibility(View.INVISIBLE);
                plus_rest.setVisibility(View.INVISIBLE);
                minus_rest.setVisibility(View.INVISIBLE);
                plus_long_rest.setVisibility(View.INVISIBLE);
                minus_long_rest.setVisibility(View.INVISIBLE);
                when_stop_invisible.setVisibility(View.INVISIBLE);
                until_end_invisible.setVisibility(View.INVISIBLE);
                plus_whenstop.setVisibility(View.INVISIBLE);
                plus_until_end.setVisibility(View.INVISIBLE);
                minus_whenstop.setVisibility(View.INVISIBLE);
                minus_until_end.setVisibility(View.INVISIBLE);
                button_autostart_background.setClickable(true);
                button_whenstop_background.setClickable(true);
                button_autostart_done.setClickable(true);
                button_autostart_notdone.setClickable(true);
                button_whenstop.setClickable(true);
                button_until_end.setClickable(true);
                button_work_time.setClickable(true);
                button_rest_time.setClickable(true);
                button_long_rest_time.setClickable(true);
                button_color.setClickable(true);
                if (Long.valueOf(work_time_invisible.getText().toString()) < 100) {
                    START_TIME_IN_MILLIS = Long.valueOf(work_time_invisible.getText().toString()) * 60 * 1000;
                    work_time.setText(String.valueOf((START_TIME_IN_MILLIS / 1000) / 60));
                    saveValue();
                }
                if (Long.valueOf(rest_time_invisible.getText().toString()) < 100) {
                    REST_TIME_IN_MILLIS = Long.valueOf(rest_time_invisible.getText().toString()) * 60 * 1000;
                    rest_time.setText(String.valueOf((REST_TIME_IN_MILLIS / 1000) / 60));
                    saveValueRest();
                }
                if (Long.valueOf(long_rest_time_invisible.getText().toString()) < 100) {
                    LONG_REST_TIME_IN_MILLIS = Long.valueOf(long_rest_time_invisible.getText().toString()) * 60 * 1000;
                    long_rest_time.setText(String.valueOf((LONG_REST_TIME_IN_MILLIS / 1000) / 60));
                    saveValueLongRest();
                }
                if (Long.valueOf(when_stop_invisible.getText().toString()) < 21) {
                    whenStopCount = Short.valueOf(when_stop_invisible.getText().toString());
                    when_stop.setText(String.valueOf(whenStopCount));
                    saveValueWhenStop();
                }
                if (Long.valueOf(until_end_invisible.getText().toString()) < 21) {
                    untilEndCount = Short.valueOf(until_end_invisible.getText().toString());
                    until_end.setText(String.valueOf(untilEndCount));
                    saveValueUntilEnd();
                }

            }
        });

        SW.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                return isBlockedScrollView;
            }
        });

        View.OnClickListener goBack = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Menu.this, MainActivity.class);
                startActivity(i);
                finish();
                sendValue();
                saveValueAutostart();
            }
        };
        title.setOnClickListener(goBack);




        // AUTOSTART
        button_autostart_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autostartIsOn = true;
                underline1.startAnimation(inAnimation);
                underline2.setVisibility(View.INVISIBLE);
                button_autostart_done.setClickable(false);
                button_autostart_notdone.setClickable(false);
                autostartAnimationFixDone();
            }
        });

        button_autostart_notdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autostartIsOn = false;
                underline1.setVisibility(View.INVISIBLE);
                button_autostart_done.setClickable(false);
                button_autostart_notdone.setClickable(false);
                underline2.startAnimation(inAnimation);
                autostartAnimationFixNotDone();
            }
        });








    }

    private void autostartAnimationFixDone(){
        new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {
                // You don't need to use this.
            }

            public void onFinish() {

                button_autostart_notdone.setClickable(true);
                underline1.setVisibility(View.VISIBLE);
                underline2.setVisibility(View.INVISIBLE);
            }

        }.start();
    }

    private void autostartAnimationFixNotDone(){
        new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {
                // You don't need to use this.
            }

            public void onFinish() {

                button_autostart_done.setClickable(true);
                underline2.setVisibility(View.VISIBLE);
                underline1.setVisibility(View.INVISIBLE);
            }

        }.start();
    }

    private void sendValue() {
        Intent a = new Intent(Menu.this, MainActivity.class);
        a.putExtra("WORK_PERIOD", START_TIME_IN_MILLIS);
        a.putExtra("REST_PERIOD", REST_TIME_IN_MILLIS);
        a.putExtra("LONG_REST_PERIOD", LONG_REST_TIME_IN_MILLIS);
        a.putExtra("AUTOSTART_CHECK", autostartIsOn);
        a.putExtra("WHEN_STOP", whenStopCount);
        a.putExtra("UNTIL_END", untilEndCount);
        startActivity(a);
        this.finish();
    }

    private void saveValueAutostart() {
        prefautostart = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor edq = pref.edit();
        edq.putBoolean("autostart_key", autostartIsOn);

        edq.apply();
    }

    private void loadValueAutostart() {
        prefautostart = getPreferences(MODE_PRIVATE);
        boolean savedTextAutostart = pref.getBoolean("autostart_key", false);
        autostartIsOn = savedTextAutostart;
    }


    private void saveValue() {
        pref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = pref.edit();
        ed.putString("save_key", String.valueOf((START_TIME_IN_MILLIS / 1000) / 60));

        ed.apply();
    }

    private void loadValue() {
        pref = getPreferences(MODE_PRIVATE);
        String savedText = pref.getString("save_key", String.valueOf((START_TIME_IN_MILLIS / 1000) / 60));
        START_TIME_IN_MILLIS = Long.valueOf(savedText);
        START_TIME_IN_MILLIS = START_TIME_IN_MILLIS * 60 * 1000;
        work_time.setText(String.valueOf(savedText));
        work_time_invisible.setText(String.valueOf(savedText));
    }

    private void saveValueRest() {
        prefrest = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor edrest = prefrest.edit();
        edrest.putString("save_keyrest", String.valueOf((REST_TIME_IN_MILLIS / 1000) / 60));
        edrest.apply();
    }

    private void loadValueRest() {
        prefrest = getPreferences(MODE_PRIVATE);
        String savedTextRest = prefrest.getString("save_keyrest", String.valueOf((REST_TIME_IN_MILLIS / 1000) / 60));
        REST_TIME_IN_MILLIS = Long.valueOf(savedTextRest);
        REST_TIME_IN_MILLIS = REST_TIME_IN_MILLIS * 60 * 1000;
        rest_time.setText(String.valueOf(savedTextRest));
        rest_time_invisible.setText(String.valueOf(savedTextRest));

    }

    private void saveValueLongRest() {
        preflongrest = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor edlongrest = preflongrest.edit();
        edlongrest.putString("save_keylongrest", String.valueOf((LONG_REST_TIME_IN_MILLIS / 1000) / 60));
        edlongrest.apply();

    }

    private void loadValueLongRest() {
        preflongrest = getPreferences(MODE_PRIVATE);
        String savedTextLongRest = preflongrest.getString("save_keylongrest", String.valueOf((LONG_REST_TIME_IN_MILLIS / 1000) / 60));
        LONG_REST_TIME_IN_MILLIS = Long.valueOf(savedTextLongRest);
        LONG_REST_TIME_IN_MILLIS = LONG_REST_TIME_IN_MILLIS * 60 * 1000;
        long_rest_time.setText(String.valueOf(savedTextLongRest));
        long_rest_time_invisible.setText(String.valueOf(savedTextLongRest));
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
        when_stop.setText(String.valueOf(savedTextLongRest));
        when_stop_invisible.setText(String.valueOf(savedTextLongRest));
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
        until_end.setText(String.valueOf(savedTextLongRest));
        until_end_invisible.setText(String.valueOf(savedTextLongRest));
    }

    public static void hideKeyboard( Activity activity ) {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE );
        View f = activity.getCurrentFocus();
        if( null != f && null != f.getWindowToken() && EditText.class.isAssignableFrom( f.getClass() ) )
            imm.hideSoftInputFromWindow( f.getWindowToken(), 0 );
        else
            activity.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN );
    }


}