package com.wower.neuromanandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
/**
 * Activity class representing the board where scenarios are played.
 * This class is responsible for setting up and managing the board view for the selected scenario.
 */
public class BoardActivity extends AppCompatActivity implements BoardView.BoardViewListener {
    ImageView pointer;
    /**
     * Called when the activity is starting.
     * Initializes the board view and sets up the scenario based on the intent extras.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down
     *                           then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise it is null.
     */
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
            boardView.setPatient(badany);
            boardView.setBoardViewListener(this);

        } catch(IOException e) {
            e.printStackTrace();
        }
        pointer = findViewById(R.id.pointer);
    }
    /**
     * Callback method to handle the completion of a scenario.
     * Finishes the current activity and returns to the previous screen.
     */
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