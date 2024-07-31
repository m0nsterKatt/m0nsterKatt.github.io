package com.tennis.scoreboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DoubleGameSetupActivity extends AppCompatActivity {

    private EditText team1NameEditText;
    private EditText team1Player1EditText;
    private EditText team1Player2EditText;
    private EditText team2NameEditText;
    private EditText team2Player1EditText;
    private EditText team2Player2EditText;
    private Button startDoubleGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_double_game_setup);

        team1NameEditText = findViewById(R.id.team1NameEditText);
        team1Player1EditText = findViewById(R.id.team1Player1EditText);
        team1Player2EditText = findViewById(R.id.team1Player2EditText);
        team2NameEditText = findViewById(R.id.team2NameEditText);
        team2Player1EditText = findViewById(R.id.team2Player1EditText);
        team2Player2EditText = findViewById(R.id.team2Player2EditText);
        startDoubleGameButton = findViewById(R.id.startDoubleGameButton);

        startDoubleGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String team1Name = team1NameEditText.getText().toString();
                String team1Player1 = team1Player1EditText.getText().toString();
                String team1Player2 = team1Player2EditText.getText().toString();
                String team2Name = team2NameEditText.getText().toString();
                String team2Player1 = team2Player1EditText.getText().toString();
                String team2Player2 = team2Player2EditText.getText().toString();

                if (team1Player1.isEmpty() || team1Player2.isEmpty() || team2Player1.isEmpty() || team2Player2.isEmpty()) {
                    Toast.makeText(DoubleGameSetupActivity.this, "Todos los campos de jugador son obligatorios", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(DoubleGameSetupActivity.this, DoubleGameActivity.class);
                    intent.putExtra("TEAM1_NAME", team1Name);
                    intent.putExtra("TEAM1_PLAYER1", team1Player1);
                    intent.putExtra("TEAM1_PLAYER2", team1Player2);
                    intent.putExtra("TEAM2_NAME", team2Name);
                    intent.putExtra("TEAM2_PLAYER1", team2Player1);
                    intent.putExtra("TEAM2_PLAYER2", team2Player2);
                    startActivity(intent);
                }
            }
        });
    }
}
