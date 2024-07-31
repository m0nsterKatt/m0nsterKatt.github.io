package com.tennis.scoreboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import android.media.MediaPlayer;
import java.util.Date;
import java.util.Locale;

public class CommonEndMatchActivity extends AppCompatActivity {

    private String player1Name;
    private String player2Name;
    private String team1Name;
    private String team2Name;
    private int player1Sets;
    private int player2Sets;
    private int team1Sets;
    private int team2Sets;
    private boolean isDraw;
    private String gameType;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_end_match);

        String message = getIntent().getStringExtra("MESSAGE");
        gameType = getIntent().getStringExtra("GAME_TYPE");

        TextView endMatchMessage = findViewById(R.id.endMatchMessage);
        endMatchMessage.setText(message);

        findViewById(R.id.buttonMainMenu).setOnClickListener(v -> {
            if (gameType.equals("single")) {
                player1Name = getIntent().getStringExtra("PLAYER1_NAME");
                player2Name = getIntent().getStringExtra("PLAYER2_NAME");
                player1Sets = getIntent().getIntExtra("PLAYER1_SETS", 0);
                player2Sets = getIntent().getIntExtra("PLAYER2_SETS", 0);
                isDraw = getIntent().getBooleanExtra("IS_DRAW", false);
                saveSingleGameLog(player1Name, player2Name, player1Sets, player2Sets, isDraw);
            } else if (gameType.equals("double")) {
                team1Name = getIntent().getStringExtra("TEAM1_NAME");
                team2Name = getIntent().getStringExtra("TEAM2_NAME");
                team1Sets = getIntent().getIntExtra("TEAM1_SETS", 0);
                team2Sets = getIntent().getIntExtra("TEAM2_SETS", 0);
                isDraw = getIntent().getBooleanExtra("IS_DRAW", false);
                saveDoubleGameLog(team1Name, team2Name, team1Sets, team2Sets, isDraw);
            }
            Intent intent = new Intent(CommonEndMatchActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        if (!isDraw) {
            mediaPlayer = MediaPlayer.create(this, R.raw.win_sound);
            mediaPlayer.start();
        }

    }

    private void saveSingleGameLog(String player1, String player2, int player1Sets, int player2Sets, boolean isDraw) {
        SharedPreferences prefs = getSharedPreferences("GameLog", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String currentTime = new SimpleDateFormat("dd/MM/yy - HH:mm -> ", Locale.getDefault()).format(new Date());

        String gameLog = prefs.getString("log", "");
        String[] games = gameLog.split("\n");

        StringBuilder newLog = new StringBuilder();

        String result = isDraw ? "Empate" : (player1Sets > player2Sets ? player1 : player2);
        newLog.append(currentTime).append(player1).append(" (").append(player1Sets).append(") - ")
                .append("(").append(player2Sets).append(") ").append(player2).append("\n");

        int limit = Math.min(games.length, 12);
        for (int i = 0; i < limit; i++) {
            newLog.append(games[i]).append("\n");
        }

        editor.putString("log", newLog.toString().trim());
        editor.apply();
    }

    private void saveDoubleGameLog(String team1, String team2, int team1Sets, int team2Sets, boolean isDraw) {
        SharedPreferences prefs = getSharedPreferences("GameLog", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String currentTime = new SimpleDateFormat("dd/MM/yy - HH:mm -> ", Locale.getDefault()).format(new Date());

        String gameLog = prefs.getString("log", "");
        String[] games = gameLog.split("\n");

        StringBuilder newLog = new StringBuilder();

        String result = isDraw ? "Empate" : (team1Sets > team2Sets ? team1 : team2);
        newLog.append(currentTime).append(team1).append(" (").append(team1Sets).append(") - ")
                .append("(").append(team2Sets).append(") ").append(team2).append("\n");

        int limit = Math.min(games.length, 12);
        for (int i = 0; i < limit; i++) {
            newLog.append(games[i]).append("\n");
        }

        editor.putString("log", newLog.toString().trim());
        editor.apply();
    }
}
