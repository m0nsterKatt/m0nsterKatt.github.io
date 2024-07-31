package com.tennis.scoreboard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import android.content.SharedPreferences;


public class SingleGameActivity extends AppCompatActivity {

    private TextView player1NameTextView;
    private TextView player2NameTextView;
    private TextView timerTextView;
    private TextView player1ScoreTextView;
    private TextView player2ScoreTextView;
    private TextView player1SetsTextView;
    private TextView player2SetsTextView;
    private View pauseResumeButton;
    private View endMatchButton;

    private Handler timerHandler = new Handler();
    private long startTime = 0L;
    private long pausedTime = 0L;
    private boolean isPaused = false;
    private int player1Score = 0;
    private int player2Score = 0;
    private int player1Sets = 0;
    private int player2Sets = 0;
    private boolean player1HasAdvantage = false;
    private boolean player2HasAdvantage = false;
    private ArrayList<String> scoreList = new ArrayList<String>() {{
        add("0");
        add("15");
        add("30");
        add("40");
        add("40+");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_game);

        player1NameTextView = findViewById(R.id.player1NameTextView);
        player2NameTextView = findViewById(R.id.player2NameTextView);
        player1ScoreTextView = findViewById(R.id.player1ScoreTextView);
        player2ScoreTextView = findViewById(R.id.player2ScoreTextView);
        timerTextView = findViewById(R.id.timerTextView);
        player1SetsTextView = findViewById(R.id.player1SetsTextView);
        player2SetsTextView = findViewById(R.id.player2SetsTextView);
        pauseResumeButton = findViewById(R.id.pauseResumeButton);
        endMatchButton = findViewById(R.id.endMatchButton);

        Intent intent = getIntent();
        String player1Name = intent.getStringExtra("PLAYER1_NAME");
        String player2Name = intent.getStringExtra("PLAYER2_NAME");

        player1NameTextView.setText(player1Name);
        player2NameTextView.setText(player2Name);

        pauseResumeButton.setVisibility(View.VISIBLE);
        endMatchButton.setVisibility(View.VISIBLE);

        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);

        pauseResumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseResumeMatch();
            }
        });

        endMatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endMatch();
            }
        });

        player1NameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player1Scored();
            }
        });

        player2NameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player2Scored();
            }
        });
    }

    private void pauseResumeMatch() {
        if (isPaused) {
            startTime = System.currentTimeMillis() - pausedTime;
            timerHandler.postDelayed(timerRunnable, 0);
            ((TextView) pauseResumeButton).setText(getResources().getString(R.string.pausar_match));
            isPaused = false;
        } else {
            pausedTime = System.currentTimeMillis() - startTime;
            timerHandler.removeCallbacks(timerRunnable);
            ((TextView) pauseResumeButton).setText(getResources().getString(R.string.reanudar_match));
            isPaused = true;
        }
    }

    private void endMatch() {
        timerHandler.removeCallbacks(timerRunnable);

        String message;
        String finPartido = getString(R.string.fin_partido);
        String empate = getString(R.string.empate);
        String congrats = getString(R.string.congrats);
        String ganador = getString(R.string.ganador);

        if (player1Sets == player2Sets) {
            message = finPartido + "\n" + empate +"\n" +
                    player1NameTextView.getText().toString() + " " + player1Sets + " - " +
                    player2Sets + " " + player2NameTextView.getText().toString() + "\n" + congrats;
        } else {
            String winner = player1Sets > player2Sets ? player1NameTextView.getText().toString() : player2NameTextView.getText().toString();
            message = finPartido + "\n" + ganador + " " + winner.toUpperCase() + "\n" +
                    player1NameTextView.getText().toString() + " " + player1Sets + " - " +
                    player2Sets + " " + player2NameTextView.getText().toString() + "\n" + congrats;
        }

        Intent intent = new Intent(SingleGameActivity.this, CommonEndMatchActivity.class);
        intent.putExtra("MESSAGE", message);
        intent.putExtra("GAME_TYPE", "single");
        intent.putExtra("PLAYER1_NAME", player1NameTextView.getText().toString());
        intent.putExtra("PLAYER2_NAME", player2NameTextView.getText().toString());
        intent.putExtra("PLAYER1_SETS", player1Sets);
        intent.putExtra("PLAYER2_SETS", player2Sets);
        intent.putExtra("IS_DRAW", player1Sets == player2Sets);
        startActivity(intent);
    }


    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            timerTextView.setText(String.format("%02d:%02d", minutes, seconds));

            timerHandler.postDelayed(this, 500);
        }
    };

    private void player1Scored() {
        if (player1Score == 3 && player2Score < 3) {
            player1Sets++;
            updateSets();
            resetScores();
            showChangeSidesMessageIfNeeded();
        } else if (player1Score == 3 && player2Score == 3) {
            if (player1HasAdvantage) {
                player1Sets++;
                updateSets();
                resetScores();
                showChangeSidesMessageIfNeeded();
                player1HasAdvantage = false;
                player2HasAdvantage = false;
            } else if (player2HasAdvantage){
                player2HasAdvantage = false;
                updateScores();
            } else {
                player1HasAdvantage = true;
                updateScores();
            }
        } else if (player1Score == 4 && player2HasAdvantage) {
            player2HasAdvantage = false;
            updateScores();
        } else {
            player1Score++;
            updateScores();
        }
    }

    private void player2Scored() {
        if (player2Score == 3 && player1Score < 3) {
            player2Sets++;
            updateSets();
            resetScores();
            showChangeSidesMessageIfNeeded();
        } else if (player2Score == 3 && player1Score == 3) {
            if (player2HasAdvantage) {
                player2Sets++;
                updateSets();
                resetScores();
                showChangeSidesMessageIfNeeded();
                player1HasAdvantage = false;
                player2HasAdvantage = false;
                updateScores();
            } else if (player1HasAdvantage){
                player1HasAdvantage = false;
                updateScores();
            } else {
                player2HasAdvantage = true;
                updateScores();
            }
        } else if (player2Score == 4 && player1HasAdvantage) {
            player1HasAdvantage = false;
            updateScores();
        } else {
            player2Score++;
            updateScores();
        }
    }

    private void updateScores() {
        player1ScoreTextView.setText(scoreList.get(player1HasAdvantage ? 4 : player1Score));
        player2ScoreTextView.setText(scoreList.get(player2HasAdvantage ? 4 : player2Score));
    }

    private void resetScores() {
        player1Score = 0;
        player2Score = 0;
        player1HasAdvantage = false;
        player2HasAdvantage = false;
        updateScores();
    }

    private void updateSets() {
        player1SetsTextView.setText(String.valueOf(player1Sets));
        player2SetsTextView.setText(String.valueOf(player2Sets));
    }

    private synchronized void showChangeSidesMessageIfNeeded() {
        if (player1Sets % 2 == 1 || player2Sets % 2 == 1) {
            Toast.makeText(this, getString(R.string.cambio), Toast.LENGTH_SHORT).show();
        }
    }
}
