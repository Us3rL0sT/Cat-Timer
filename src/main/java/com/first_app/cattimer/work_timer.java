package com.first_app.cattimer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class work_timer extends Menu {


    private TextView work_time_invisible;
    private TextView rest_time_invisible;
    private TextView long_rest_time_invisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_timer);

        work_time_invisible = findViewById(R.id.work_time_invisible);
        rest_time_invisible = findViewById(R.id.rest_time_invisible);
        long_rest_time_invisible = findViewById(R.id.long_rest_time_invisible);

        work_time_invisible.setText(String.valueOf((START_TIME_IN_MILLIS / 1000) / 60));
        rest_time_invisible.setText(String.valueOf((REST_TIME_IN_MILLIS / 1000) / 60));
        long_rest_time_invisible.setText(String.valueOf((LONG_REST_TIME_IN_MILLIS / 1000) / 60));

        work_time_invisible.setVisibility(View.VISIBLE);

        System.out.println("ВЦЫФВФЫВфцццц");
    }
}