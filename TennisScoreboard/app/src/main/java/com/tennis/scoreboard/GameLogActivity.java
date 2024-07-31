package com.tennis.scoreboard;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;

public class GameLogActivity extends AppCompatActivity {

    private ListView gameLogListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_log);

        gameLogListView = findViewById(R.id.gameLogListView);

        SharedPreferences prefs = getSharedPreferences("GameLog", MODE_PRIVATE);
        String gameLog = prefs.getString("log", "");

        if (!gameLog.isEmpty()) {
            String[] games = gameLog.split("\n");
            ArrayList<String> gameList = new ArrayList<>();
            Collections.addAll(gameList, games);

            GameLogAdapter adapter = new GameLogAdapter(this, gameList);
            gameLogListView.setAdapter(adapter);
        }
    }
}
