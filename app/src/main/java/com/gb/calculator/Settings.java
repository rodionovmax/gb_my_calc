package com.gb.calculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    public static final String MODE = "isDarkModeOn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);



        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        RadioButton light_mode_radio = (RadioButton) findViewById(R.id.radio_light_mode);
        RadioButton dark_mode_radio = (RadioButton) findViewById(R.id.radio_dark_mode);
        Button apply = (Button) findViewById(R.id.apply_btn);

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final boolean isDarkModeOn = sharedPreferences.getBoolean(MODE, false);

        // Change MODE by clicking on radio buttons
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id) {
                    case R.id.radio_light_mode:
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        editor.putBoolean(MODE, false);
                        editor.apply();
                        break;
                    case R.id.radio_dark_mode:
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        editor.putBoolean(MODE, true);
                        editor.apply();
                        break;
                }
            }
        });

        // Send intent with MODE to main
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent main = new Intent();
                main.putExtra(MainActivity.MODE, isDarkModeOn);
                setResult(RESULT_OK, main);
                finish();
            }
        });

        if (isDarkModeOn) {
            dark_mode_radio.setChecked(true);
        } else {
            light_mode_radio.setChecked(true);
        }



    }
}
