package com.tennis.scoreboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class SingleGameSetupActivity extends AppCompatActivity {

    private EditText player1NameEditText;
    private EditText player2NameEditText;
    private Button startSingleGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_game_setup);

        player1NameEditText = findViewById(R.id.player1NameEditText);
        player2NameEditText = findViewById(R.id.player2NameEditText);
        startSingleGameButton = findViewById(R.id.startSingleGameButton);

        startSingleGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String player1Name = player1NameEditText.getText().toString();
                String player2Name = player2NameEditText.getText().toString();

                Intent intent = new Intent(SingleGameSetupActivity.this, SingleGameActivity.class);
                intent.putExtra("PLAYER1_NAME", player1Name);
                intent.putExtra("PLAYER2_NAME", player2Name);
                startActivity(intent);
            }
        });
    }
}
