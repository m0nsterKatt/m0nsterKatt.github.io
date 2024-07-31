package com.tennis.scoreboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class SelectServerActivity extends AppCompatActivity {

    private RadioGroup serverOptions;
    private RadioButton randomOption;
    private String[] players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_server);

        serverOptions = findViewById(R.id.serverOptions);
        //randomOption = findViewById(R.id.randomOption);

        Intent intent = getIntent();
        players = intent.getStringArrayExtra("PLAYERS");

        for (String player : players) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(player);
            serverOptions.addView(radioButton);
        }
    }

    public void startMatch(View view) {
        int selectedId = serverOptions.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Por favor, selecciona un servidor.", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedButton = findViewById(selectedId);
        String server = selectedButton.getText().toString();

        if (randomOption.isChecked()) {
            Random random = new Random();
            server = players[random.nextInt(players.length)];
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra("SERVER", server);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
