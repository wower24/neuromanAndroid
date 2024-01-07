package com.wower.neuromanandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

public class BoardActivity extends AppCompatActivity implements BoardView.BoardViewListener {
    ImageView pointer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        Intent intent = getIntent();
        String scenarioName = intent.getStringExtra("SCENARIO_NAME");
        String fileName = scenarioName + ".xml";
        String badany = intent.getStringExtra("BADANY");
        try {
            XmlPullParserHandlerScenario parser = new XmlPullParserHandlerScenario();
            InputStream inputStream = getAssets().open(fileName);

            Scenario scenario = parser.parse(inputStream);
            BoardView boardView = findViewById(R.id.boardView);
            boardView.setScenario(scenario);
            boardView.setBadany(badany);

        } catch(IOException e) {
            e.printStackTrace();
        }
        pointer = findViewById(R.id.pointer);
    }
    @Override
    public void onScenarioCompleted() {
        finish();
    }

    public void movePointer(float x, float y) {
        pointer.setVisibility(View.VISIBLE);
        pointer.setX(x - (pointer.getWidth() / 2));
        pointer.setY(y - (pointer.getHeight() / 2));
    }

    public void hidePointer() {
        pointer.setVisibility(View.INVISIBLE);
    }
}