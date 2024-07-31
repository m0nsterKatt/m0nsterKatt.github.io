package com.tennis.scoreboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class NewGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
    }

    public void startSingleGame(View view) {
        Intent intent = new Intent(this, SingleGameSetupActivity.class);
        startActivity(intent);
    }

    public void startDoubleGame(View view) {
        Intent intent = new Intent(this, DoubleGameSetupActivity.class);
        startActivity(intent);
    }
}
