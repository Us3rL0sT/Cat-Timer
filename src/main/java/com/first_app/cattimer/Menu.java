package com.first_app.cattimer;


import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.text.InputType;
import android.util.DisplayMetrics;
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

import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.jaredrummler.android.colorpicker.ColorShape;


public class Menu extends MainActivity implements ColorPickerDialogListener {

    private long START_TIME_IN_MILLIS = 1500 * 1000;
    private long REST_TIME_IN_MILLIS = 300 * 1000; // 300 сек
    private long LONG_REST_TIME_IN_MILLIS = 900 * 1000;

    private int height_phone; // экрана
    private int width_phone; // также экрана
    private int color_value;
    private ColorDrawable button_color_value_drawable;
    private int button_color_value;
    private static final int firstId = 1;

    private short whenStopCount = 8;
    private short untilEndCount = 4;

    private SharedPreferences pref;
    private SharedPreferences prefrest;
    private SharedPreferences preflongrest;
    private SharedPreferences prefautostart;
    private SharedPreferences prefvibration;
    private SharedPreferences prefautostartrest;
    private SharedPreferences prefnotification;
    private SharedPreferences prefdisplay;
    private SharedPreferences prefmelody;
    private SharedPreferences whenstop;
    private SharedPreferences untilend;
    private SharedPreferences prefcolor;

    private TextView work_time;
    private TextView rest_time;
    private TextView long_rest_time;
    private EditText work_time_invisible;
    private TextView rest_time_invisible;
    private TextView long_rest_time_invisible;
    private TextView work_time_text;
    private TextView rest_time_text;
    private TextView long_rest_time_text;
    private TextView whenstop_text;
    private TextView untilend_text;
    private TextView until_end_action_name;
    private TextView when_stop_action_name;
    private TextView melody_text;

    private TextView until_end_invisible;
    private TextView when_stop;
    private TextView until_end;
    private TextView when_stop_invisible;

    private Button button_work_time;
    private Button button_rest_time;
    private Button button_long_rest_time;
    private Button button_color;
    private Button button_autostart_done;
    private Button button_vibration_done;
    private Button button_autostartrest_done;
    private Button button_notification_done;
    private Button button_display_done;
    private Button button_autostart_notdone;
    private Button button_vibration_notdone;
    private Button button_autostartrest_notdone;
    private Button button_notification_notdone;
    private Button button_display_notdone;
    private Button button_whenstop;
    private Button button_until_end;
    private Button button_autostart_background;
    private Button button_vibration_background;
    private Button button_autostartrest_background;
    private Button button_whenstop_background;
    private Button button_display_background;
    private Button button_sounds;
    private Button button_how_to_use;
    private Button button_rate_us;
    private Button button_contact_us;


    private ImageView title;
    private ImageView autostart_done;
    private ImageView notification_done;
    private ImageView vibration_done;
    private ImageView autostartrest_done;
    private ImageView autostart_notdone;
    private ImageView vibration_notdone;
    private ImageView autostartrest_notdone;
    private ImageView notification_notdone;
    private ImageView display_done;
    private ImageView display_notdone;
    private ImageView underline1;
    private ImageView underline2;
    private ImageView underline3;
    private ImageView underline4;
    private ImageView underline5;
    private ImageView underline6;
    private ImageView underline7;
    private ImageView underline8;
    private ImageView underline9;
    private ImageView underline10;
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
    private ConstraintLayout screen;

    private ScrollView SW;

    private boolean isBlockedScrollView;
    private boolean autostartIsOn = false;
    private boolean vibration = true;
    private boolean autostartrestIsON = true;
    private boolean notificationIsOn = true;
    private boolean displayIsOn = true;

    private Animation inAnimation;
    private Animation outAnimation;
    private Animation fastExit;
    private Animation nullAnimation;
    private Animation fastAnimation;

    private Vibrator vibrator;

    private String Melody;
    private Ringtone currentRingtone;
    private Uri uri;
    private String valueUri;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height_phone = displayMetrics.heightPixels;
        width_phone = displayMetrics.widthPixels;

        if (height_phone == 2400) {
            button_color.setX(-300);
        }

        inAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_in);
        outAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_out);
        fastExit = AnimationUtils.loadAnimation(this, R.anim.alpha_fastexit);
        nullAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_null);
        fastAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_fast);
        final Animation animTranslate = AnimationUtils.loadAnimation(this, R.anim.anim_translate);
        final Animation animTranslateToLeft = AnimationUtils.loadAnimation(this, R.anim.anim_translate_to_left);


        button_autostart_done = findViewById(R.id.button_autostart_done);
        button_autostart_notdone = findViewById(R.id.button_autostart_notdone);

        button_vibration_done = findViewById(R.id.button_vibration_done);
        button_vibration_notdone = findViewById(R.id.button_vibration_notdone);

        button_autostartrest_done = findViewById(R.id.button_autostartrest_done);
        button_autostartrest_notdone = findViewById(R.id.button_autostartrest_notdone);

        button_notification_done = findViewById(R.id.button_notification_done);
        button_notification_notdone = findViewById(R.id.button_notification_notdone);

        button_display_done = findViewById(R.id.button_display_done);
        button_display_notdone = findViewById(R.id.button_display_notdone);

        button_whenstop_background = findViewById(R.id.button_whenstop_background);
        button_autostart_background = findViewById(R.id.button_autostart_background);
        button_vibration_background = findViewById(R.id.button_vibration_background);
        button_autostartrest_background = findViewById(R.id.button_autostartrest_background);
        button_display_background = findViewById(R.id.button_display_background);
        button_how_to_use = findViewById(R.id.button_how_to_use);
        button_rate_us = findViewById(R.id.button_rate_us);
        button_contact_us = findViewById(R.id.button_contact_us);

        autostart_done = findViewById(R.id.autostart_done);
        autostart_notdone = findViewById(R.id.autostart_notdone);

        notification_done = findViewById(R.id.notification_done);
        notification_notdone = findViewById(R.id.notification_notdone);

        vibration_done = findViewById(R.id.vibration_done);
        vibration_notdone = findViewById(R.id.vibration_notdone);

        autostartrest_done = findViewById(R.id.autostartrest_done);
        autostartrest_notdone = findViewById(R.id.autostartrest_notdone);


        display_done = findViewById(R.id.display_done);
        display_notdone = findViewById(R.id.display_notdone);

        underline1 = findViewById(R.id.underline1);
        underline2 = findViewById(R.id.underline2);
        underline3 = findViewById(R.id.underline3);
        underline4 = findViewById(R.id.underline4);
        underline5 = findViewById(R.id.underline5);
        underline6 = findViewById(R.id.underline6);
        underline7 = findViewById(R.id.underline7);
        underline8 = findViewById(R.id.underline8);
        underline9 = findViewById(R.id.underline9);
        underline10 = findViewById(R.id.underline10);
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
        until_end_action_name = findViewById(R.id.until_end_action_name);
        when_stop_action_name = findViewById(R.id.when_stop_action_name);
        button_sounds = findViewById(R.id.button_sounds);

        SW = findViewById(R.id.SW);



        title = findViewById(R.id.goBack);

        shade = findViewById(R.id.shade);
        maximizedContainer = findViewById(R.id.maximized_container);
        screen = findViewById(R.id.screen);

        work_time = (findViewById(R.id.work_time));
        rest_time = (findViewById(R.id.rest_time));
        long_rest_time = (findViewById(R.id.long_rest_time));
        work_time_invisible = findViewById(R.id.work_time_invisible);
        rest_time_invisible = findViewById(R.id.rest_time_invisible);
        until_end_invisible = findViewById(R.id.until_end_invisible);
        when_stop_invisible = findViewById(R.id.when_stop_invisible);
        long_rest_time_invisible = findViewById(R.id.long_rest_time_invisible);
        work_time_text = findViewById(R.id.work_time_text);
        rest_time_text = findViewById(R.id.rest_time_text);
        long_rest_time_text = findViewById(R.id.long_rest_time_text);
        whenstop_text = findViewById(R.id.whenstop_text);
        untilend_text = findViewById(R.id.whenstop_text);
        when_stop = findViewById(R.id.when_stop);
        until_end = findViewById(R.id.until_end);
        melody_text = findViewById(R.id.melody_text);


        button_work_time = findViewById(R.id.button_work_time);
        button_rest_time = findViewById(R.id.button_rest_time);
        button_long_rest_time = findViewById(R.id.button_long_rest_time);
        button_color = findViewById(R.id.button_color);
        button_whenstop = findViewById(R.id.button_whenstop);
        button_until_end = findViewById(R.id.button_until_end);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);



        button_work_time.getBackground().setAlpha(255);
        button_rest_time.getBackground().setAlpha(255);
        button_long_rest_time.getBackground().setAlpha(255);
        button_color.getBackground().setAlpha(255);
        button_how_to_use.getBackground().setAlpha(255);
        title.setAlpha(0.5f);
        autostart_done.setAlpha(0.85f);
        vibration_done.setAlpha(0.85f);
        autostartrest_done.setAlpha(0.85f);
        notification_done.setAlpha(0.85f);
        autostart_notdone.setAlpha(0.85f);
        vibration_notdone.setAlpha(0.85f);
        autostartrest_notdone.setAlpha(0.85f);
        notification_notdone.setAlpha(0.85f);
        button_autostart_done.setAlpha(0.7f);
        button_vibration_done.setAlpha(0.7f);
        button_autostartrest_done.setAlpha(0.7f);
        button_notification_done.setAlpha(0.7f);
        button_display_done.setAlpha(0.7f);
        button_autostart_notdone.setAlpha(0.7f);
        button_vibration_notdone.setAlpha(0.7f);
        button_autostartrest_notdone.setAlpha(0.7f);
        button_notification_notdone.setAlpha(0.7f);
        work_time.setText(String.valueOf((START_TIME_IN_MILLIS / 1000) / 60));
        rest_time.setText(String.valueOf((REST_TIME_IN_MILLIS / 1000) / 60));
        long_rest_time.setText(String.valueOf((LONG_REST_TIME_IN_MILLIS / 1000) / 60));
        when_stop.setText(String.valueOf(whenStopCount));
        until_end.setText(String.valueOf(untilEndCount));
        button_sounds.setStateListAnimator(null);


        work_time_invisible.setInputType(InputType.TYPE_CLASS_NUMBER);
        rest_time_invisible.setInputType(InputType.TYPE_CLASS_NUMBER);
        long_rest_time_invisible.setInputType(InputType.TYPE_CLASS_NUMBER);
        when_stop_invisible.setInputType(InputType.TYPE_CLASS_NUMBER);
        until_end_invisible.setInputType(InputType.TYPE_CLASS_NUMBER);

        String debugInfo = "\nSystem-info:";
        debugInfo += "\n OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
        debugInfo += "\n OS API Level: " + android.os.Build.VERSION.RELEASE + "(" + android.os.Build.VERSION.SDK_INT + ")";
        debugInfo += "\n Device: " + android.os.Build.DEVICE;
        debugInfo += "\n Model (and Product): " + android.os.Build.MODEL + " (" + android.os.Build.PRODUCT + ")";




        loadValue();
        loadValueRest();
        loadValueLongRest();
        loadValueAutostart();
        loadValueVibration();
        loadValueWhenStop();
        loadValueUntilEnd();
        loadValueAutostartRest();
        loadValueNotification();
        loadMelody();
        loadValueDisplay();
        loadValueColor();
        if (color_value != 123)
            onColorSelected(firstId, color_value);



        if (autostartIsOn == true) {
            underline1.setVisibility(View.VISIBLE);
            underline2.setVisibility(View.INVISIBLE);
        } else {
            underline2.setVisibility(View.VISIBLE);
            underline1.setVisibility(View.INVISIBLE);
        }

        if (vibration == true) {
            underline3.setVisibility(View.VISIBLE);
            underline4.setVisibility(View.INVISIBLE);
        } else {
            underline4.setVisibility(View.VISIBLE);
            underline3.setVisibility(View.INVISIBLE);
        }

        if (autostartrestIsON == true) {
            underline5.setVisibility(View.VISIBLE);
            underline6.setVisibility(View.INVISIBLE);
        } else {
            underline6.setVisibility(View.VISIBLE);
            underline5.setVisibility(View.INVISIBLE);
        }

        if (notificationIsOn == true) {
            underline7.setVisibility(View.VISIBLE);
            underline8.setVisibility(View.INVISIBLE);
        } else {
            underline8.setVisibility(View.VISIBLE);
            underline7.setVisibility(View.INVISIBLE);
        }

        if (displayIsOn == true) {
            underline9.setVisibility(View.VISIBLE);
            underline10.setVisibility(View.INVISIBLE);
        } else {
            underline10.setVisibility(View.VISIBLE);
            underline9.setVisibility(View.INVISIBLE);
        }


        work_time_invisible.setText(String.valueOf((START_TIME_IN_MILLIS / 1000) / 60));
        rest_time_invisible.setText(String.valueOf((REST_TIME_IN_MILLIS / 1000) / 60));
        long_rest_time_invisible.setText(String.valueOf((LONG_REST_TIME_IN_MILLIS / 1000) / 60));
        when_stop_invisible.setText(String.valueOf(whenStopCount));
        until_end_invisible.setText(String.valueOf(untilEndCount));

        button_sounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickRingtone();
                if (Melody == ""){
                    melody_text.setText("");
                }
                else {
                    melody_text.setText(Melody);
                }

            }
        });


        button_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createColorPickerDialog(firstId);
            }
        });


        button_whenstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                maximizedContainer.setVisibility(View.VISIBLE);
                when_stop_invisible.startAnimation(fastAnimation);
                plus_whenstop.startAnimation(fastAnimation);
                minus_whenstop.startAnimation(fastAnimation);
                whenstop_text.startAnimation(fastAnimation);
                shade.setVisibility(View.VISIBLE);
                when_stop_invisible.setVisibility(View.VISIBLE);
                plus_whenstop.setVisibility(View.VISIBLE);
                minus_whenstop.setVisibility(View.VISIBLE);
                whenstop_text.setText(R.string.whenstop);
                whenstop_text.setVisibility(View.VISIBLE);
                button_whenstop.setClickable(false);
                button_until_end.setClickable(false);
                button_color.setClickable(false);
                button_autostart_done.setClickable(false);
                button_vibration_done.setClickable(false);
                button_vibration_background.setClickable(false);
                button_autostartrest_background.setClickable(false);
                button_autostart_notdone.setClickable(false);
                button_vibration_notdone.setClickable(false);
                button_autostart_background.setClickable(false);
                button_whenstop_background.setClickable(false);
                button_autostartrest_done.setClickable(false);
                button_autostartrest_notdone.setClickable(false);
                isBlockedScrollView = true;



                button_color_value_drawable = (ColorDrawable) button_whenstop.getBackground();
                button_color_value = button_color_value_drawable.getColor();


                button_whenstop.startAnimation(fastExit);
                button_until_end.startAnimation(fastExit);
                button_whenstop.setVisibility(View.INVISIBLE);
                button_until_end.setVisibility(View.INVISIBLE);
                when_stop_action_name.setAlpha(0.2f);
                until_end_action_name.setAlpha(0.2f);
                until_end.setAlpha(0.2f);
                when_stop.setAlpha(0.2f);

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
                until_end_invisible.startAnimation(fastAnimation);
                plus_until_end.startAnimation(fastAnimation);
                minus_until_end.startAnimation(fastAnimation);
                untilend_text.startAnimation(fastAnimation);
                shade.setVisibility(View.VISIBLE);
                until_end_invisible.setVisibility(View.VISIBLE);
                plus_until_end.setVisibility(View.VISIBLE);
                minus_until_end.setVisibility(View.VISIBLE);
                whenstop_text.setText(R.string.until_end);
                untilend_text.setVisibility(View.VISIBLE);
                button_until_end.setClickable(false);
                button_whenstop.setClickable(false);
                button_color.setClickable(false);
                button_autostart_done.setClickable(false);
                button_vibration_done.setClickable(false);
                button_autostart_notdone.setClickable(false);
                button_autostart_background.setClickable(false);
                button_vibration_background.setClickable(false);
                button_autostartrest_background.setClickable(false);
                button_whenstop_background.setClickable(false);
                button_autostartrest_done.setClickable(false);
                button_autostartrest_notdone.setClickable(false);
                isBlockedScrollView = true;

                button_color_value_drawable = (ColorDrawable) button_whenstop.getBackground();
                button_color_value = button_color_value_drawable.getColor();



                button_whenstop.startAnimation(fastExit);
                button_until_end.startAnimation(fastExit);
                button_whenstop.setVisibility(View.INVISIBLE);
                button_until_end.setVisibility(View.INVISIBLE);

                until_end_action_name.setAlpha(0.2f);
                when_stop_action_name.setAlpha(0.2f);
                when_stop.setAlpha(0.2f);
                until_end.setAlpha(0.2f);
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
                work_time_invisible.startAnimation(fastAnimation);
                plus.startAnimation(fastAnimation);
                minus.startAnimation(fastAnimation);
                work_time_text.startAnimation(fastAnimation);
                work_time_invisible.setVisibility(View.VISIBLE);
                shade.setVisibility(View.VISIBLE);
                rest_time_invisible.setVisibility(View.INVISIBLE);
                long_rest_time_invisible.setVisibility(View.INVISIBLE);
                plus.setVisibility(View.VISIBLE);
                minus.setVisibility(View.VISIBLE);
                work_time_text.setText(R.string.work);
                work_time_text.setVisibility(View.VISIBLE);
                button_autostart_background.setClickable(false);
                button_vibration_background.setClickable(false);
                button_work_time.setClickable(false);
                button_rest_time.setClickable(false);
                button_long_rest_time.setClickable(false);
                button_color.setClickable(false);
                button_autostart_done.setClickable(false);
                button_vibration_done.setClickable(false);
                button_autostart_notdone.setClickable(false);
                isBlockedScrollView = true;
                SW.smoothScrollTo(0, 0);
                button_color.getBackground().setAlpha(10);
                button_work_time.getBackground().setAlpha(254);
                button_rest_time.getBackground().setAlpha(254);
                button_long_rest_time.getBackground().setAlpha(254);
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
                rest_time_invisible.startAnimation(fastAnimation);
                plus_rest.startAnimation(fastAnimation);
                minus_rest.startAnimation(fastAnimation);
                rest_time_text.startAnimation(fastAnimation);
                rest_time_invisible.setVisibility(View.VISIBLE);
                shade.setVisibility(View.VISIBLE);
                work_time_invisible.setVisibility(View.INVISIBLE);
                long_rest_time_invisible.setVisibility(View.INVISIBLE);
                plus_rest.setVisibility(View.VISIBLE);
                minus_rest.setVisibility(View.VISIBLE);
                rest_time_text.setText(R.string.rest);
                rest_time_text.setVisibility(View.VISIBLE);
                button_autostart_background.setClickable(false);
                button_vibration_background.setClickable(false);
                button_work_time.setClickable(false);
                button_rest_time.setClickable(false);
                button_long_rest_time.setClickable(false);
                button_color.setClickable(false);
                button_autostart_done.setClickable(false);
                button_vibration_done.setClickable(false);
                button_autostart_notdone.setClickable(false);
                isBlockedScrollView = true;
                SW.smoothScrollTo(0, 0);
                button_color.getBackground().setAlpha(10);
                button_work_time.getBackground().setAlpha(254);
                button_rest_time.getBackground().setAlpha(254);
                button_long_rest_time.getBackground().setAlpha(254);
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
                long_rest_time_invisible.startAnimation(fastAnimation);
                plus_long_rest.startAnimation(fastAnimation);
                minus_long_rest.startAnimation(fastAnimation);
                long_rest_time_text.startAnimation(fastAnimation);
                long_rest_time_invisible.setVisibility(View.VISIBLE);
                work_time_invisible.setVisibility(View.INVISIBLE);
                rest_time_invisible.setVisibility(View.INVISIBLE);
                shade.setVisibility(View.VISIBLE);
                plus_long_rest.setVisibility(View.VISIBLE);
                minus_long_rest.setVisibility(View.VISIBLE);
                long_rest_time_text.setText(R.string.longrest);
                long_rest_time_text.setVisibility(View.VISIBLE);
                button_autostart_background.setClickable(false);
                button_vibration_background.setClickable(false);
                button_work_time.setClickable(false);
                button_rest_time.setClickable(false);
                button_long_rest_time.setClickable(false);
                button_color.setClickable(false);
                button_autostart_done.setClickable(false);
                button_vibration_done.setClickable(false);
                button_autostart_notdone.setClickable(false);
                isBlockedScrollView = true;
                SW.smoothScrollTo(0, 0);
                button_color.getBackground().setAlpha(10);
                button_work_time.getBackground().setAlpha(254);
                button_rest_time.getBackground().setAlpha(254);
                button_long_rest_time.getBackground().setAlpha(254);
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



                button_whenstop.setVisibility(View.VISIBLE);
                button_until_end.setVisibility(View.VISIBLE);


                button_rest_time.getBackground().setAlpha(255);
                button_long_rest_time.getBackground().setAlpha(255);
                button_work_time.getBackground().setAlpha(255);

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
                work_time_text.setVisibility(View.INVISIBLE);
                rest_time_text.setVisibility(View.INVISIBLE);
                long_rest_time_text.setVisibility(View.INVISIBLE);
                whenstop_text.setVisibility(View.INVISIBLE);
                untilend_text.setVisibility(View.INVISIBLE);
                button_autostart_background.setClickable(true);
                button_autostartrest_background.setClickable(true);
                button_vibration_background.setClickable(true);
                button_whenstop_background.setClickable(true);
                button_autostart_done.setClickable(true);
                button_vibration_done.setClickable(true);
                button_autostart_notdone.setClickable(true);
                button_whenstop.setClickable(true);
                button_until_end.setClickable(true);
                button_work_time.setClickable(true);
                button_rest_time.setClickable(true);
                button_long_rest_time.setClickable(true);
                button_color.setClickable(true);
                button_autostartrest_done.setClickable(true);
                button_autostartrest_notdone.setClickable(true);



                button_color.getBackground().setAlpha(255);





                until_end_action_name.setAlpha(1.0f);
                when_stop_action_name.setAlpha(1.0f);

                when_stop.setAlpha(1.0f);
                until_end.setAlpha(1.0f);
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
                saveValueVibration();
                saveValueAutostartRest();
                saveValueNotification();
                saveMelody();
                saveValueDisplay();
                saveValueColor();
                overridePendingTransition(R.anim.go_back_out, R.anim.go_back_in);

            }
        };
        title.setOnClickListener(goBack);


        // AUTOSTART
        button_autostart_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animTranslateToLeft);
                autostart_done.startAnimation(animTranslateToLeft);
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
                view.startAnimation(animTranslate);
                autostart_notdone.startAnimation(animTranslate);
                autostartIsOn = false;
                underline1.setVisibility(View.INVISIBLE);
                button_autostart_done.setClickable(false);
                button_autostart_notdone.setClickable(false);
                underline2.startAnimation(inAnimation);
                autostartAnimationFixNotDone();
            }
        });

        // VIBRATION
        button_vibration_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animTranslateToLeft);
                vibration_done.startAnimation(animTranslateToLeft);
                vibrator.vibrate(300);
                vibration = true;
                underline3.startAnimation(inAnimation);
                underline4.setVisibility(View.INVISIBLE);
                button_vibration_done.setClickable(false);
                button_vibration_notdone.setClickable(false);
                vibrationAnimationFixDone();
            }
        });

        button_vibration_notdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animTranslate);
                vibration_notdone.startAnimation(animTranslate);
                vibration = false;
                underline3.setVisibility(View.INVISIBLE);
                button_vibration_done.setClickable(false);
                button_vibration_notdone.setClickable(false);
                underline4.startAnimation(inAnimation);
                vibrationAnimationFixNotDone();
            }
        });

        // AUTOSTART REST
        button_autostartrest_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animTranslateToLeft);
                autostartrest_done.startAnimation(animTranslateToLeft);
                autostartrestIsON = true;
                underline5.startAnimation(inAnimation);
                underline6.setVisibility(View.INVISIBLE);
                button_autostartrest_done.setClickable(false);
                button_autostartrest_notdone.setClickable(false);
                autostartrestAnimationFixDone();
            }
        });

        button_autostartrest_notdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animTranslate);
                autostartrest_notdone.startAnimation(animTranslate);
                autostartrestIsON = false;
                underline5.setVisibility(View.INVISIBLE);
                button_autostartrest_done.setClickable(false);
                button_autostartrest_notdone.setClickable(false);
                underline6.startAnimation(inAnimation);
                autostartrestAnimationFixNotDone();
            }
        });

        // NOTIFICATION
        button_notification_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animTranslateToLeft);
                notification_done.startAnimation(animTranslateToLeft);
                notificationIsOn = true;
                underline7.startAnimation(inAnimation);
                underline8.setVisibility(View.INVISIBLE);
                button_notification_done.setClickable(false);
                button_notification_notdone.setClickable(false);
                notificationAnimationFixDone();
                // заход в настройки уведомлений
                Intent intent = new Intent();
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("app_package", getPackageName());
                intent.putExtra("app_uid", getApplicationInfo().uid);
                intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());
                startActivity(intent);
                //
            }
        });

        button_notification_notdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animTranslate);
                notification_notdone.startAnimation(animTranslate);
                notificationIsOn = false;
                underline7.setVisibility(View.INVISIBLE);
                button_notification_done.setClickable(false);
                button_notification_notdone.setClickable(false);
                underline8.startAnimation(inAnimation);
                notificationAnimationFixNotDone();
                // заход в настройки уведомлений
                Intent intent = new Intent();
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("app_package", getPackageName());
                intent.putExtra("app_uid", getApplicationInfo().uid);
                intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());
                startActivity(intent);
                //
            }
        });

        // DISPLAY
        button_display_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animTranslateToLeft);
                display_done.startAnimation(animTranslateToLeft);
                displayIsOn = true;
                underline9.startAnimation(inAnimation);
                underline10.setVisibility(View.INVISIBLE);
                button_display_done.setClickable(false);
                button_display_notdone.setClickable(false);
                displayAnimationFixDone();
            }
        });

        button_display_notdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animTranslate);
                display_notdone.startAnimation(animTranslate);
                displayIsOn = false;
                underline9.setVisibility(View.INVISIBLE);
                button_display_done.setClickable(false);
                button_display_notdone.setClickable(false);
                underline10.startAnimation(inAnimation);
                displayAnimationFixNotDone();
            }
        });

        button_rate_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Menu.this, R.string.thank_you, Toast.LENGTH_SHORT).show();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://cat.drru.agconnect.link/nA4b"));
                startActivity(browserIntent);

            }
        });


        String finalDebugInfo = debugInfo;
        button_contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType(ClipDescription.MIMETYPE_TEXT_PLAIN);
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"mr.goldman.co@gmail.com"});
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT,"What do you want to report?");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, finalDebugInfo);
                startActivity(Intent.createChooser(intent,"Send Email"));
            }
        });


    }
    // ONCREATE

    private void autostartAnimationFixDone() {
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

    private void autostartAnimationFixNotDone() {
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

    private void vibrationAnimationFixDone() {
        new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {
                // You don't need to use this.
            }

            public void onFinish() {

                button_vibration_notdone.setClickable(true);
                underline3.setVisibility(View.VISIBLE);
                underline4.setVisibility(View.INVISIBLE);
            }

        }.start();
    }

    private void vibrationAnimationFixNotDone() {
        new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {
                // You don't need to use this.
            }

            public void onFinish() {

                button_vibration_done.setClickable(true);
                underline4.setVisibility(View.VISIBLE);
                underline3.setVisibility(View.INVISIBLE);
            }

        }.start();
    }

    private void autostartrestAnimationFixDone() {
        new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {
                // You don't need to use this.
            }

            public void onFinish() {

                button_autostartrest_notdone.setClickable(true);
                underline5.setVisibility(View.VISIBLE);
                underline6.setVisibility(View.INVISIBLE);
            }

        }.start();
    }

    private void autostartrestAnimationFixNotDone() {
        new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {
                // You don't need to use this.
            }

            public void onFinish() {

                button_autostartrest_done.setClickable(true);
                underline6.setVisibility(View.VISIBLE);
                underline5.setVisibility(View.INVISIBLE);
            }

        }.start();
    }

    private void notificationAnimationFixDone() {
        new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {
                // You don't need to use this.
            }

            public void onFinish() {

                button_notification_notdone.setClickable(true);
                underline7.setVisibility(View.VISIBLE);
                underline8.setVisibility(View.INVISIBLE);
            }

        }.start();
    }

    private void notificationAnimationFixNotDone() {
        new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {
                // You don't need to use this.
            }

            public void onFinish() {

                button_notification_done.setClickable(true);
                underline8.setVisibility(View.VISIBLE);
                underline7.setVisibility(View.INVISIBLE);
            }

        }.start();
    }

    private void displayAnimationFixDone() {
        new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {
                // You don't need to use this.
            }

            public void onFinish() {

                button_display_notdone.setClickable(true);
                underline9.setVisibility(View.VISIBLE);
                underline10.setVisibility(View.INVISIBLE);
            }

        }.start();
    }

    private void displayAnimationFixNotDone() {
        new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {
                // You don't need to use this.
            }

            public void onFinish() {

                button_display_done.setClickable(true);
                underline10.setVisibility(View.VISIBLE);
                underline9.setVisibility(View.INVISIBLE);
            }

        }.start();
    }


    private void sendValue() {
        Intent a = new Intent(Menu.this, MainActivity.class);
        a.putExtra("WORK_PERIOD", START_TIME_IN_MILLIS);
        a.putExtra("REST_PERIOD", REST_TIME_IN_MILLIS);
        a.putExtra("LONG_REST_PERIOD", LONG_REST_TIME_IN_MILLIS);
        a.putExtra("AUTOSTART_CHECK", autostartIsOn);
        a.putExtra("VIBRATION", vibration);
        a.putExtra("WHEN_STOP", whenStopCount);
        a.putExtra("UNTIL_END", untilEndCount);
        a.putExtra("AUTOSTARTREST", autostartrestIsON);
        a.putExtra("NOTIFICATIONCHOICE", notificationIsOn);
        a.putExtra("MELODY", valueUri);
        a.putExtra("DISPLAYON", displayIsOn);
        a.putExtra("COLOR_VALUE", color_value);
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
        if (autostartIsOn) {
            underline1.setVisibility(View.VISIBLE);
            underline2.setVisibility(View.INVISIBLE);
        } else {
            underline2.setVisibility(View.VISIBLE);
            underline1.setVisibility(View.INVISIBLE);
        }
    }

    private void saveValueVibration() {
        prefvibration = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor edq = pref.edit();
        edq.putBoolean("vibration_key", vibration);

        edq.apply();
    }

    private void loadValueVibration() {
        prefvibration = getPreferences(MODE_PRIVATE);
        boolean savedTextVibration = pref.getBoolean("vibration_key", true);
        vibration = savedTextVibration;
        if (vibration) {
            underline3.setVisibility(View.VISIBLE);
            underline4.setVisibility(View.INVISIBLE);
        } else {
            underline4.setVisibility(View.VISIBLE);
            underline3.setVisibility(View.INVISIBLE);
        }
    }

    private void saveValueAutostartRest() {
        prefautostartrest = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor edq = pref.edit();
        edq.putBoolean("autostartrest_key", autostartrestIsON);

        edq.apply();
    }

    private void loadValueAutostartRest() {
        prefautostartrest = getPreferences(MODE_PRIVATE);
        boolean savedTextAutostartRest = pref.getBoolean("autostartrest_key", true);
        autostartrestIsON = savedTextAutostartRest;
        if (autostartrestIsON) {
            underline5.setVisibility(View.VISIBLE);
            underline6.setVisibility(View.INVISIBLE);
        } else {
            underline6.setVisibility(View.VISIBLE);
            underline5.setVisibility(View.INVISIBLE);
        }
    }

    private void saveValueNotification() {
        prefnotification = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor edq = pref.edit();
        edq.putBoolean("notification_key", notificationIsOn);

        edq.apply();
    }

    private void loadValueNotification() {
        prefnotification = getPreferences(MODE_PRIVATE);
        boolean savedTextNotification = pref.getBoolean("notification_key", true);
        notificationIsOn = savedTextNotification;
        if (notificationIsOn) {
            underline7.setVisibility(View.VISIBLE);
            underline8.setVisibility(View.INVISIBLE);
        } else {
            underline8.setVisibility(View.VISIBLE);
            underline7.setVisibility(View.INVISIBLE);
        }
    }

    private void saveMelody() {
        prefmelody = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor edq = pref.edit();
        edq.putString("melody_key", Melody);

        edq.apply();
    }

    private void loadMelody() {
        prefmelody = getPreferences(MODE_PRIVATE);
        String savedTextMelody = pref.getString("melody_key", "");
        Melody = savedTextMelody;

        if (Melody == "")
            melody_text.setText("Выберите мелодию!");
        else
            melody_text.setText(Melody);

    }

    private void saveValueDisplay() {
        prefdisplay = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor edq = pref.edit();
        edq.putBoolean("display_key", displayIsOn);

        edq.apply();
    }

    private void loadValueDisplay() {
        prefdisplay = getPreferences(MODE_PRIVATE);
        boolean savedTextNotification = pref.getBoolean("display_key", true);
        displayIsOn = savedTextNotification;
        if (displayIsOn) {

            underline9.setVisibility(View.VISIBLE);
            underline10.setVisibility(View.INVISIBLE);
        } else {
            underline10.setVisibility(View.VISIBLE);
            underline9.setVisibility(View.INVISIBLE);
        }
    }

    private void saveValueColor() {
        prefcolor = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor edq = pref.edit();
        edq.putInt("color_key", color_value);

        edq.apply();
    }

    private void loadValueColor() {
        prefcolor = getPreferences(MODE_PRIVATE);
        int savedTextNotification = prefcolor.getInt("color_key",  123);
        color_value = savedTextNotification;
    }


    private void saveValue() {
        pref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = prefcolor.edit();
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





    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View f = activity.getCurrentFocus();
        if (null != f && null != f.getWindowToken() && EditText.class.isAssignableFrom(f.getClass()))
            imm.hideSoftInputFromWindow(f.getWindowToken(), 0);
        else
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void pickRingtone() {

        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Выбор мелодии");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        saveMelody();
        startActivityForResult(intent, 1);
        saveMelody();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case 1:
                if(resultCode == RESULT_OK) {
                    uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                    saveMelody();
                    valueUri = uri.toString();
                    saveMelody();
                    if (uri != null) {
                        currentRingtone = RingtoneManager.getRingtone(getApplicationContext(), uri);
                        saveMelody();
                        Melody = currentRingtone.getTitle(this);
                        saveMelody();
                        if (Melody == ""){
                            melody_text.setText("");
                            Toast.makeText(this, "нет звука", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            melody_text.setText(Melody);
                            Toast.makeText(this, "звук " + Melody, Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                break;
        }
    }


    @Override
    public void onColorSelected(int dialogId, int color) {
        color_value = color;
        saveValueColor();

        screen.setBackgroundColor(color);


        button_work_time.setAlpha(0.3f);
        button_rest_time.setAlpha(0.3f);
        button_long_rest_time.setAlpha(0.3f);
        button_whenstop.setAlpha(0.3f);
        button_until_end.setAlpha(0.3f);
        button_how_to_use.setAlpha(0.3f);
        button_rate_us.setAlpha(0.3f);
        button_contact_us.setAlpha(0.3f);




    }

    @Override
    public void onDialogDismissed(int dialogId) {

    }

    private void createColorPickerDialog(int id) {
        ColorPickerDialog.Builder builder = ColorPickerDialog.newBuilder();
        builder.setColor(Color.RED);
        builder.setDialogType(ColorPickerDialog.TYPE_PRESETS);
        builder.setAllowCustom(true);
        builder.setAllowPresets(true);
        builder.setColorShape(ColorShape.CIRCLE);
        builder.setDialogId(id);
        builder.show(this);
        saveValueColor();
// полный список атрибутов класса ColorPickerDialog смотрите ниже
    }






















}