package com.tennis.scoreboard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import android.content.SharedPreferences;


public class DoubleGameActivity extends AppCompatActivity {

    private TextView team1NameTextView;
    private TextView team1Player1TextView;
    private TextView team1Player2TextView;
    private TextView team2NameTextView;
    private TextView team2Player1TextView;
    private TextView team2Player2TextView;
    private TextView timerTextView;
    private TextView team1ScoreTextView;
    private TextView team2ScoreTextView;
    private TextView team1SetsTextView;
    private TextView team2SetsTextView;
    private View pauseResumeButton;
    private View endMatchButton;

    private Handler timerHandler = new Handler();
    private long startTime = 0L;
    private long pausedTime = 0L;
    private boolean isPaused = false;
    private int team1Score = 0;
    private int team2Score = 0;
    private int team1Sets = 0;
    private int team2Sets = 0;
    private ArrayList<String> scoreList = new ArrayList<String>() {{
        add("0");
        add("15");
        add("30");
        add("40");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_double_game);

        team1NameTextView = findViewById(R.id.team1NameTextView);
        team1Player1TextView = findViewById(R.id.team1Player1TextView);
        team1Player2TextView = findViewById(R.id.team1Player2TextView);
        team2NameTextView = findViewById(R.id.team2NameTextView);
        team2Player1TextView = findViewById(R.id.team2Player1TextView);
        team2Player2TextView = findViewById(R.id.team2Player2TextView);
        timerTextView = findViewById(R.id.timerTextView);
        team1ScoreTextView = findViewById(R.id.team1ScoreTextView);
        team2ScoreTextView = findViewById(R.id.team2ScoreTextView);
        team1SetsTextView = findViewById(R.id.team1SetsTextView);
        team2SetsTextView = findViewById(R.id.team2SetsTextView);
        pauseResumeButton = findViewById(R.id.pauseResumeButton);
        endMatchButton = findViewById(R.id.endMatchButton);

        Intent intent = getIntent();
        String team1Name = intent.getStringExtra("TEAM1_NAME");
        String team1Player1 = intent.getStringExtra("TEAM1_PLAYER1");
        String team1Player2 = intent.getStringExtra("TEAM1_PLAYER2");
        String team2Name = intent.getStringExtra("TEAM2_NAME");
        String team2Player1 = intent.getStringExtra("TEAM2_PLAYER1");
        String team2Player2 = intent.getStringExtra("TEAM2_PLAYER2");

        team1NameTextView.setText(team1Name);
        team1Player1TextView.setText(team1Player1);
        team1Player2TextView.setText(team1Player2);
        team2NameTextView.setText(team2Name);
        team2Player1TextView.setText(team2Player1);
        team2Player2TextView.setText(team2Player2);

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

        findViewById(R.id.team1Layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                team1Scored();
            }
        });

        findViewById(R.id.team2Layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                team2Scored();
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

        if (team1Sets == team2Sets) {
            message = finPartido + "\n" + empate + "\n" +
                    team1NameTextView.getText().toString() + " " + team1Sets + " - " +
                    team2Sets + " " + team2NameTextView.getText().toString() + "\n" + congrats;
        } else {
            String winner = team1Sets > team2Sets ? team1NameTextView.getText().toString() : team2NameTextView.getText().toString();
            message = finPartido + "\n" + ganador + " " + winner.toUpperCase() + "\n" +
                    team1NameTextView.getText().toString() + " " + team1Sets + " - " +
                    team2Sets + " " + team2NameTextView.getText().toString() + "\n" + congrats;
        }

        Intent intent = new Intent(DoubleGameActivity.this, CommonEndMatchActivity.class);
        intent.putExtra("MESSAGE", message);
        intent.putExtra("GAME_TYPE", "double");
        intent.putExtra("TEAM1_NAME", team1NameTextView.getText().toString());
        intent.putExtra("TEAM2_NAME", team2NameTextView.getText().toString());
        intent.putExtra("TEAM1_SETS", team1Sets);
        intent.putExtra("TEAM2_SETS", team2Sets);
        intent.putExtra("IS_DRAW", team1Sets == team2Sets);
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

    private void team1Scored() {
        if (team1Score == 3 && team2Score < 3) {
            team1Sets++;
            updateSets();
            resetScores();
            showChangeSidesMessageIfNeeded();
        } else if (team1Score == 3 && team2Score == 3) {
            team1Sets++;
            updateSets();
            resetScores();
            showChangeSidesMessageIfNeeded();
        } else if (team1Score == 2 && team2Score == 3) {
            team1Score = 3;
            updateScores();
            showGoldenPointMessage();
        } else {
            team1Score++;
            updateScores();
        }
    }

    private void team2Scored() {
        if (team2Score == 3 && team1Score < 3) {
            team2Sets++;
            updateSets();
            resetScores();
            showChangeSidesMessageIfNeeded();
        } else if (team2Score == 3 && team1Score == 3) {
            team2Sets++;
            updateSets();
            resetScores();
            showChangeSidesMessageIfNeeded();
        } else if (team1Score == 3 && team2Score == 2) {
            team2Score = 3;
            updateScores();
            showGoldenPointMessage();
        } else {
            team2Score++;
            updateScores();
        }
    }

    private void showGoldenPointMessage() {
        Toast.makeText(this, getString(R.string.ventaja), Toast.LENGTH_SHORT).show();
        team1Score = team2Score = 3; // El siguiente punto gana el set
    }

    private void updateScores() {
        team1ScoreTextView.setText(scoreList.get(team1Score));
        team2ScoreTextView.setText(scoreList.get(team2Score));
    }

    private void resetScores() {
        team1Score = 0;
        team2Score = 0;
        updateScores();
    }

    private void updateSets() {
        team1SetsTextView.setText(String.valueOf(team1Sets));
        team2SetsTextView.setText(String.valueOf(team2Sets));
    }

    private synchronized void showChangeSidesMessageIfNeeded() {
        if ((team1Sets + team2Sets) % 2 == 1) {
            Toast.makeText(this, getString(R.string.cambio), Toast.LENGTH_SHORT).show();
//            swapSides();
        }
    }

//    private void swapSides() {
//        CharSequence tempName = team1NameTextView.getText();
//        CharSequence tempPlayer1 = team1Player1TextView.getText();
//        CharSequence tempPlayer2 = team1Player2TextView.getText();
//        CharSequence tempScore = team1ScoreTextView.getText();
//        CharSequence tempSets = team1SetsTextView.getText();
//
//        team1NameTextView.setText(team2NameTextView.getText());
//        team1Player1TextView.setText(team2Player1TextView.getText());
//        team1Player2TextView.setText(team2Player2TextView.getText());
//        team1ScoreTextView.setText(team2ScoreTextView.getText());
//        team1SetsTextView.setText(team2SetsTextView.getText());
//
//        team2NameTextView.setText(tempName);
//        team2Player1TextView.setText(tempPlayer1);
//        team2Player2TextView.setText(tempPlayer2);
//        team2ScoreTextView.setText(tempScore);
//        team2SetsTextView.setText(tempSets);
//    }
}
