package com.wower.neuromanandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

public class BoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        Intent intent = getIntent();
        String scenarioName = intent.getStringExtra("SCENARIO_NAME");
        String fileName = scenarioName + ".xml";
        try {
            XmlPullParserHandlerTest9p parser = new XmlPullParserHandlerTest9p();
            InputStream inputStream = getAssets().open(fileName);

            Scenario scenario = parser.parse(inputStream);
            BoardView boardView = findViewById(R.id.boardView);
            boardView.setScenario(scenario);

        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}