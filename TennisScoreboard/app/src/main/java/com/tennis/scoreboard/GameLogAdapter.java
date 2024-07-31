package com.tennis.scoreboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class GameLogAdapter extends ArrayAdapter<String> {

    public GameLogAdapter(Context context, ArrayList<String> games) {
        super(context, 0, games);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String game = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_game_log, parent, false);
        }

        TextView gameLogItemText = convertView.findViewById(R.id.gameLogItemText);
        gameLogItemText.setText(game);

        return convertView;
    }
}
