package com.first_app.cattimer;


import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

public class Menu extends MainActivity {

        private long START_TIME_IN_MILLIS = 1500 * 1000;
        private long REST_TIME_IN_MILLIS = 300 * 1000; // 300 сек
        private long LONG_REST_TIME_IN_MILLIS = 900 * 1000;

        private SharedPreferences pref;
        private SharedPreferences prefrest;
        private SharedPreferences preflongrest;

        private TextView work_time;
        private TextView rest_time;
        private TextView long_rest_time;
        private EditText work_time_invisible;
        private TextView rest_time_invisible;
        private TextView long_rest_time_invisible;

        private Button button_work_time;
        private Button button_rest_time;
        private Button button_long_rest_time;

        private ImageView title;

        private View shade;

        private ConstraintLayout maximizedContainer;

        private ScrollView SW;

        private boolean isBlockedScrollView;





    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        SW = findViewById(R.id.SW);

        title = findViewById(R.id.goBack);

        shade = findViewById(R.id.shade);
        maximizedContainer = findViewById(R.id.maximized_container);

        work_time = (findViewById(R.id.work_time));
        rest_time = (findViewById(R.id.rest_time));
        long_rest_time = (findViewById(R.id.long_rest_time));
        work_time_invisible = findViewById(R.id.work_time_invisible);
        rest_time_invisible = findViewById(R.id.rest_time_invisible);
        long_rest_time_invisible = findViewById(R.id.long_rest_time_invisible);

        button_work_time = findViewById(R.id.button_work_time);
        button_rest_time = findViewById(R.id.button_rest_time);
        button_long_rest_time = findViewById(R.id.button_long_rest_time);

        button_work_time.getBackground().setAlpha(128);
        button_rest_time.getBackground().setAlpha(128);
        button_long_rest_time.getBackground().setAlpha(128);
        work_time.setText(String.valueOf((START_TIME_IN_MILLIS / 1000) / 60));
        rest_time.setText(String.valueOf((REST_TIME_IN_MILLIS / 1000) / 60));
        long_rest_time.setText(String.valueOf((LONG_REST_TIME_IN_MILLIS / 1000) / 60));

        work_time_invisible = (EditText)findViewById(R.id.work_time_invisible);
        rest_time_invisible = (EditText)findViewById(R.id.rest_time_invisible);
        long_rest_time_invisible = (EditText)findViewById(R.id.long_rest_time_invisible);
        work_time_invisible.setInputType(InputType.TYPE_CLASS_NUMBER);
        rest_time_invisible.setInputType(InputType.TYPE_CLASS_NUMBER);
        long_rest_time_invisible.setInputType(InputType.TYPE_CLASS_NUMBER);




        loadValue();
        loadValueRest();
        loadValueLongRest();


        work_time_invisible.setText(String.valueOf((START_TIME_IN_MILLIS / 1000) / 60));
        rest_time_invisible.setText(String.valueOf((REST_TIME_IN_MILLIS / 1000) / 60));
        long_rest_time_invisible.setText(String.valueOf((LONG_REST_TIME_IN_MILLIS / 1000) / 60));

        button_work_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maximizedContainer.setVisibility(View.VISIBLE);
                work_time_invisible.setVisibility(View.VISIBLE);
                shade.setVisibility(View.VISIBLE);
                rest_time_invisible.setVisibility(View.INVISIBLE);
                long_rest_time_invisible.setVisibility(View.INVISIBLE);
                button_work_time.setClickable(false);
                button_rest_time.setClickable(false);
                button_long_rest_time.setClickable(false);
                isBlockedScrollView = true;
                SW.smoothScrollTo(0, 0);
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
                button_work_time.setClickable(false);
                button_rest_time.setClickable(false);
                button_long_rest_time.setClickable(false);
                isBlockedScrollView = true;
                SW.smoothScrollTo(0, 0);
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
                button_work_time.setClickable(false);
                button_rest_time.setClickable(false);
                button_long_rest_time.setClickable(false);
                isBlockedScrollView = true;
                SW.smoothScrollTo(0, 0);
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
                button_work_time.setClickable(true);
                button_rest_time.setClickable(true);
                button_long_rest_time.setClickable(true);
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
            }
        };
        title.setOnClickListener(goBack);










    }

    private void sendValue() {
        Intent a = new Intent(Menu.this, MainActivity.class);
        a.putExtra("WORK_PERIOD", START_TIME_IN_MILLIS);
        a.putExtra("REST_PERIOD", REST_TIME_IN_MILLIS);
        a.putExtra("LONG_REST_PERIOD", LONG_REST_TIME_IN_MILLIS);
        startActivity(a);
        this.finish();
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

    public static void hideKeyboard( Activity activity ) {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE );
        View f = activity.getCurrentFocus();
        if( null != f && null != f.getWindowToken() && EditText.class.isAssignableFrom( f.getClass() ) )
            imm.hideSoftInputFromWindow( f.getWindowToken(), 0 );
        else
            activity.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN );
    }


}