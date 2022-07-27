package com.first_app.cattimer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentContainer;

import android.content.Intent;
import android.media.Image;
import android.opengl.Visibility;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Menu extends MainActivity {

        private boolean isShade;

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
        private View check_shade;

        private ConstraintLayout maximizedContainer;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        title = findViewById(R.id.goBack);

        shade = findViewById(R.id.shade);
//        check_shade = findViewById(R.id.check_shade);
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
        work_time_invisible.setText(String.valueOf((START_TIME_IN_MILLIS / 1000) / 60));
        rest_time_invisible.setText(String.valueOf((REST_TIME_IN_MILLIS / 1000) / 60));
        long_rest_time_invisible.setText(String.valueOf((LONG_REST_TIME_IN_MILLIS / 1000) / 60));
        work_time_invisible = (EditText)findViewById(R.id.work_time_invisible);
        rest_time_invisible = (EditText)findViewById(R.id.rest_time_invisible);
        long_rest_time_invisible = (EditText)findViewById(R.id.long_rest_time_invisible);
        work_time_invisible.setInputType(InputType.TYPE_CLASS_NUMBER);
        rest_time_invisible.setInputType(InputType.TYPE_CLASS_NUMBER);
        long_rest_time_invisible.setInputType(InputType.TYPE_CLASS_NUMBER);


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
            }
        });

        shade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shade.setVisibility(View.INVISIBLE);
                work_time_invisible.setVisibility(View.INVISIBLE);
                rest_time_invisible.setVisibility(View.INVISIBLE);
                long_rest_time_invisible.setVisibility(View.INVISIBLE);
                button_work_time.setClickable(true);
                button_rest_time.setClickable(true);
                button_long_rest_time.setClickable(true);
            }
        });

        View.OnClickListener goBack = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, MainActivity.class);
                startActivity(intent);
            }
        };
        title.setOnClickListener(goBack);


    }


}