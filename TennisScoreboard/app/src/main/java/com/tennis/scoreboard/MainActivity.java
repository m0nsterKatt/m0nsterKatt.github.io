package com.tennis.scoreboard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

public class MainActivity extends AppCompatActivity {

    private View newGameButton;
    private View matchHistoryButton;
    private View settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Apply the current theme
        boolean isDarkMode = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("dark_mode", false);
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        setContentView(R.layout.activity_main);

        setContentView(R.layout.activity_main);

        newGameButton = findViewById(R.id.button_new_game);
        matchHistoryButton = findViewById(R.id.button_game_log);
        settingsButton = findViewById(R.id.button_settings);

        // Ocultar botones inicialmente
        newGameButton.setVisibility(View.INVISIBLE);
        matchHistoryButton.setVisibility(View.INVISIBLE);
        settingsButton.setVisibility(View.INVISIBLE);

        // Mostrar botones después de 2 segundos
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                newGameButton.setVisibility(View.VISIBLE);
                matchHistoryButton.setVisibility(View.VISIBLE);
                settingsButton.setVisibility(View.VISIBLE);
            }
        }, 2000);
    }

    public void openNewGame(View view) {
        Intent intent = new Intent(this, NewGameActivity.class);
        startActivity(intent);
    }

    public void openGameLog(View view) {
        Intent intent = new Intent(this, GameLogActivity.class);
        startActivity(intent);
    }

    public void openSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
