package com.wower.neuromanandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * Main activity class for the application.
 * This class is responsible for the initial user interface where the user can select scenarios and other settings.
 */
public class MainActivity extends AppCompatActivity {
    /**
     * Array of scenario names available in the application.
     */
    String[] scenarios = {"MOCA", "test9p"};
    /**
     * Strings to store the patient's name and the operator's name.
     */
    String patient;
    String operator;
    /**
     * Selected position in the list view.
     */
    int selectedPosition = -1;
    /**
     * Called when the activity is starting.
     * This is where most initialization should go: calling setContentView(int) to inflate
     * the activity's UI, using findViewById(int) to programmatically interact with widgets in the UI,
     * setting up various listeners, and initializing other components.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down
     *                           then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Map<String, List<?>> parsedLists;

        try {
            XmlPullParserHandlerMainScreen parser = new XmlPullParserHandlerMainScreen();
            InputStream inputStream = getAssets().open("neuroman.xml");

            parsedLists = parser.parse(inputStream);

            ArrayAdapter<String> operatorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, (List<String>)parsedLists.get("operatorNames"));

            setupOperatorSpinner(operatorAdapter);

        } catch(IOException e) {
            e.printStackTrace();
        }

        setupScenariosListViewAndCheckbox();
        setupCalibrationButton();
        setupFinishButton();
    }
    /**
     * Sets up the spinner for selecting the operator.
     *
     * @param operatorAdapter The adapter containing the list of operator names.
     */
    private void setupOperatorSpinner(ArrayAdapter<String> operatorAdapter) {
        View operatorLayout = findViewById(R.id.operatorLayout);
        TextView label = operatorLayout.findViewById(R.id.layoutLabel);
        label.setText("Operator: ");
        Spinner operatorSpinner = operatorLayout.findViewById(R.id.layoutSpinner);
        operatorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        operatorSpinner.setAdapter(operatorAdapter);
        setupSpinnerButtons(findViewById(R.id.operatorLayout), operatorAdapter, operatorSpinner);
    }

    /**
     * Sets up the buttons for the spinner.
     *
     * @param layout The layout containing the spinner and buttons.
     * @param adapter The adapter for the spinner.
     * @param spinner The spinner to which the buttons are associated.
     */
    private void setupSpinnerButtons(View layout, final ArrayAdapter<String> adapter, final Spinner spinner) {
        Button button1 = layout.findViewById(R.id.layoutButton1);
        Button button2 = layout.findViewById(R.id.layoutButton2);

        button1.setOnClickListener(v -> showInputDialog(adapter, spinner));

        button2.setOnClickListener(v -> deleteSelectedItem(adapter, spinner));
    }
    /**
     * Sets up the list view and checkbox for scenario selection.
     */
    private void setupScenariosListViewAndCheckbox() {
        String selectedScenario[] = {null};
        CheckBox wykonanoCheckBox = findViewById(R.id.wykonanoCheckBox);
        EditText badanyName = findViewById(R.id.badany);
        ArrayAdapter scenariuszeAdapter = new ArrayAdapter<String>(this, R.layout.listview, scenarios);
        ListView scenariuszeListView = findViewById(R.id.scenariuszeListView);
        scenariuszeListView.setAdapter(scenariuszeAdapter);
        scenariuszeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (selectedPosition != -1) {
                    View previousView = scenariuszeListView.getChildAt(selectedPosition - scenariuszeListView.getFirstVisiblePosition());
                    if (previousView != null) {
                        previousView.setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.white));
                    }
                }

                // Set the background color for the clicked item
                view.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.selectedColor));

                // Update the selected position
                selectedPosition = position;
                selectedScenario[0] = (String)parent.getItemAtPosition(selectedPosition);
            }
        });
        Button startScenarioButton = findViewById(R.id.startButton);
        startScenarioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!wykonanoCheckBox.isChecked()) {
                    showCheckboxWarning("Zaznacz wykonanie testu CSR-R!");

                } else if(badanyName.getText().toString().equals("")) {
                    showCheckboxWarning("Najpierw określ badanego!");
                } else if(selectedScenario[0] == null) {
                    showCheckboxWarning("Najpierw wybierz scenariusz!");
                }
                else {
                    patient = badanyName.getText().toString();
                    startScenario(selectedScenario[0]);
                }
            }
        });
    }
    /**
     * Sets up the calibration button.
     */
    private void setupCalibrationButton() {
        Button calibrationButton = findViewById(R.id.kalibracjaButton);
        calibrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScenario("test9p");
            }
        });
    }
    /**
     * Starts a selected scenario.
     *
     * @param scenarioName The name of the scenario to start.
     */
    private void startScenario(String scenarioName) {
        Intent intent = new Intent(this, BoardActivity.class);
        intent.putExtra("SCENARIO_NAME", scenarioName.toLowerCase());
        intent.putExtra("BADANY", patient);
        intent.putExtra("OPERATOR", operator);
        startActivity(intent);
    }
    /**
     * Displays a warning dialog with a specified message.
     *
     * @param message The message to be displayed in the warning dialog.
     */
    private void showCheckboxWarning(String message) {
        // Display a warning dialog when the checkbox is not checked
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, do nothing or handle it as needed
                    }
                });
        // Create and show the dialog
        builder.create().show();
    }
    /**
     * Shows an input dialog for adding a new item to the spinner.
     *
     * @param adapter The adapter associated with the spinner.
     * @param spinner The spinner to which the new item will be added.
     */
    private void showInputDialog(final ArrayAdapter<String> adapter, final Spinner spinner) {
        final EditText inputText = new EditText(this);
        inputText.setHint("Wprowadź nazwę");

        new android.app.AlertDialog.Builder(this)
                .setTitle("Dodaj nowy element")
                .setView(inputText)
                .setPositiveButton("OK", (dialog, whichButton) -> {
                    String newItem = inputText.getText().toString().trim();
                    if (!newItem.isEmpty()) {
                        adapter.add(newItem);
                        adapter.notifyDataSetChanged();
                        spinner.setSelection(adapter.getPosition(newItem));
                    }
                })
                .setNegativeButton("Anuluj", null)
                .show();
    }
    /**
     * Deletes the selected item from the spinner.
     *
     * @param adapter The adapter associated with the spinner.
     * @param spinner The spinner from which the selected item will be deleted.
     */
    private void deleteSelectedItem(final ArrayAdapter<String> adapter, final Spinner spinner) {
        int selectedPosition = spinner.getSelectedItemPosition();
        if (selectedPosition != Spinner.INVALID_POSITION) {
            adapter.remove(adapter.getItem(selectedPosition));
            adapter.notifyDataSetChanged();
            if (selectedPosition < adapter.getCount()) {
                spinner.setSelection(selectedPosition);
            } else if (adapter.getCount() > 0) {
                spinner.setSelection(adapter.getCount() - 1);
            }
        }
    }
    /**
     * Sets up the finish button to close the activity.
     */
    private void setupFinishButton() {
        Button koniecButton = findViewById(R.id.koniecButton);

        koniecButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close the current activity
                finish();
            }
        });
    }
}