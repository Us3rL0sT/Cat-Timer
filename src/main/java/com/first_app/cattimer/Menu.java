package com.first_app.cattimer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Menu extends MainActivity {

        private TextView work_time;
        private TextView rest_time;
        private TextView long_rest_time;

        private Button button_work_time;
        private Button button_rest_time;
        private Button button_long_rest_time;

        private ImageView title;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        title = findViewById(R.id.goBack);

        work_time = (findViewById(R.id.work_time));
        rest_time = (findViewById(R.id.rest_time));
        long_rest_time = (findViewById(R.id.long_rest_time));

        button_work_time = findViewById(R.id.button_work_time);
        button_rest_time = findViewById(R.id.button_rest_time);
        button_long_rest_time = findViewById(R.id.button_long_rest_time);

        button_work_time.getBackground().setAlpha(128);
        button_rest_time.getBackground().setAlpha(128);
        button_long_rest_time.getBackground().setAlpha(128);
        work_time.setText(String.valueOf((START_TIME_IN_MILLIS / 1000) / 60));
        rest_time.setText(String.valueOf((REST_TIME_IN_MILLIS / 1000) / 60));
        long_rest_time.setText(String.valueOf((LONG_REST_TIME_IN_MILLIS / 1000) / 60));

        button_work_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(Menu.this).create();

                dialog.setCancelable(true);

                ConstraintLayout cl = (ConstraintLayout) getLayoutInflater().inflate(R.layout.activity_work_timer, null);
                work_time_invisible.setVisibility(View.VISIBLE);
                dialog.setView(cl);
                dialog.getWindow().setDimAmount(0.5f); // 0 - нет затемнения,  1 - максимальное зтемнение
                dialog.show();
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