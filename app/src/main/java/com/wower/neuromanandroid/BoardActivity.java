package com.wower.neuromanandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.io.IOException;
import java.io.InputStream;

public class BoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        try {
            XmlPullParserHandlerTest9p parser = new XmlPullParserHandlerTest9p();
            InputStream inputStream = getAssets().open("moca.xml");

            Scenario scenario = parser.parse(inputStream);
            BoardView boardView = findViewById(R.id.boardView);
            boardView.setScenario(scenario);

        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}