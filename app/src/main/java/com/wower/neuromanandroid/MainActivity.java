package com.wower.neuromanandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class MainActivity extends AppCompatActivity {
    String[] scenariuszeArray = {"MOCA", "test9p"};
    AtomicInteger czasKlikaniaValue = new AtomicInteger(1100);
    int czasKlikaniaIncrement = 100;
    AtomicInteger czasPrzerwyValue = new AtomicInteger(500);
    int czasPrzerwyIncrement = 100;
    AtomicInteger wielkoscKursoraValue = new AtomicInteger(55);
    int wielkoscKursoraIncrement = 5;
    int cornerIncrement = 10;
    AtomicInteger minX = new AtomicInteger(0);
    AtomicInteger minY = new AtomicInteger(0);
    AtomicInteger maxX = new AtomicInteger(100);
    AtomicInteger maxY = new AtomicInteger(100);
    int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Map<String, List<?>> parsedLists;

        try {
            XmlPullParserHandlerMainScreen parser = new XmlPullParserHandlerMainScreen();
            InputStream inputStream = getAssets().open("neuroman.xml");

            parsedLists = parser.parse(inputStream);

            List<Profile> profiles = (List<Profile>)parsedLists.get("profiles");
            List<String> profileNames = new ArrayList<>();

            for(Profile profile: profiles) {
                profileNames.add(profile.name);
            }

            ArrayAdapter<String> operatorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, (List<String>)parsedLists.get("operatorNames"));
            ArrayAdapter<String> profilAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, profileNames);

            setupOperatorSpinner(operatorAdapter);
            setupProfilSpinner(profilAdapter, profiles);

        } catch(IOException e) {
            e.printStackTrace();
        }

        setupScenariosListViewAndCheckbox();
        setupCzasKlikaniaLayout();
        setupCzasPrzerwyLayout();
        setupWielkoscKursoraLayout();
        setupUpperLeftCornerLayout();
        setupLowerRightCornerLayout();
        setupFinishButton();
    }

    private void setupOperatorSpinner(ArrayAdapter<String> operatorAdapter) {
        View operatorLayout = findViewById(R.id.operatorLayout);
        TextView label = operatorLayout.findViewById(R.id.layoutLabel);
        label.setText("Operator: ");
        Spinner operatorSpinner = operatorLayout.findViewById(R.id.layoutSpinner);
        operatorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        operatorSpinner.setAdapter(operatorAdapter);
        setupSpinnerButtons(findViewById(R.id.operatorLayout), operatorAdapter, operatorSpinner);
    }

    private void setupProfilSpinner(ArrayAdapter<String> profilAdapter, List<Profile> profiles) {
        View profilLayout = findViewById(R.id.profilLayout);
        TextView label = profilLayout.findViewById(R.id.layoutLabel);
        label.setText("Profil: ");
        Spinner profilSpinner = profilLayout.findViewById(R.id.layoutSpinner);
        profilAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        profilSpinner.setAdapter(profilAdapter);
        setupSpinnerButtons(findViewById(R.id.profilLayout), profilAdapter, profilSpinner);

        profilSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Profile selectedProfile = profiles.get(position);
                updateValues(selectedProfile);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    void updateValues(Profile selectedProfile) {
        czasKlikaniaValue.set(selectedProfile.getDuration());
        czasPrzerwyValue.set(selectedProfile.getPatience());
        wielkoscKursoraValue.set(selectedProfile.getRadius());
        minX.set(selectedProfile.getMinX());
        minY.set(selectedProfile.getMinY());
        maxX.set(selectedProfile.getMaxX());
        maxY.set(selectedProfile.getMaxY());

        updateValueLayout(R.id.czasKlikaniaLayout, czasKlikaniaValue);
        updateValueLayout(R.id.czasPrzerwyLayout, czasPrzerwyValue);
        updateValueLayout(R.id.wielkoscKursoraLayout, wielkoscKursoraValue);
        updateXYValueLayout(R.id.upperLeftCornerLayout, minX, minY);
        updateXYValueLayout(R.id.lowerRightCornerLayout, maxX, maxY);
    }

    private void updateValueLayout(int layoutId, AtomicInteger value) {
        View layout = findViewById(layoutId);
        TextView textView = layout.findViewById(R.id.numberText);
        textView.setText(String.valueOf(value));
    }

    private void updateXYValueLayout(int layoutId, AtomicInteger valueX, AtomicInteger valueY) {
        View layout = findViewById(layoutId);
        TextView textViewX = layout.findViewById(R.id.numberXText);
        textViewX.setText(String.valueOf(valueX));
        TextView textViewY = layout.findViewById(R.id.numberYText);
        textViewY.setText(String.valueOf(valueY));
    }

    private void setupSpinnerButtons(View layout, final ArrayAdapter<String> adapter, final Spinner spinner) {
        Button button1 = layout.findViewById(R.id.layoutButton1);
        Button button2 = layout.findViewById(R.id.layoutButton2);

        button1.setOnClickListener(v -> showInputDialog(adapter, spinner));

        button2.setOnClickListener(v -> deleteSelectedItem(adapter, spinner));
    }

    private void setupScenariosListViewAndCheckbox() {
        String selectedScenario[] = {null};
        CheckBox wykonanoCheckBox = findViewById(R.id.wykonanoCheckBox);
        EditText badanyName = findViewById(R.id.badany);
        ArrayAdapter scenariuszeAdapter = new ArrayAdapter<String>(this, R.layout.listview, scenariuszeArray);
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
                    startScenario(selectedScenario[0]);
                }
            }
        });
    }

    private void startScenario(String scenarioName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Starting " + scenarioName + " scenario...")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, do nothing or handle it as needed
                    }
                });
        // Create and show the dialog
        builder.create().show();
        try {
            XmlPullParserHandlerTest9p parser = new XmlPullParserHandlerTest9p();
            InputStream inputStream = getAssets().open("test9p.xml");

            Scenario scen = parser.parse(inputStream);

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

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

    private void setupCzasKlikaniaLayout() {
        setupValueLayout(
                R.id.czasKlikaniaLayout,
                "Czas klikania (ms) ",
                czasKlikaniaValue,
                czasKlikaniaIncrement
        );
    }

    private void setupCzasPrzerwyLayout() {
        setupValueLayout(
                R.id.czasPrzerwyLayout,
                "Max czas przerwy (ms) ",
                czasPrzerwyValue,
                czasPrzerwyIncrement
        );
    }

    private void setupWielkoscKursoraLayout() {
        setupValueLayout(
                R.id.wielkoscKursoraLayout,
                "Wielkość kursora (px) ",
                wielkoscKursoraValue,
                wielkoscKursoraIncrement
        );
    }

    private void setupUpperLeftCornerLayout() {
        setupCornerLayout(
                R.id.upperLeftCornerLayout,
                "Lewy górny róg (%) ",
                minX,
                minY
        );
    }

    private void setupLowerRightCornerLayout() {
        setupCornerLayout(
                R.id.lowerRightCornerLayout,
                "Prawy dolny róg (%) ",
                maxX,
                maxY
        );
    }

    private void setupValueLayout(int layoutId, String labelText, AtomicInteger value, int increment) {
        View layout = findViewById(layoutId);
        TextView label = layout.findViewById(R.id.layoutLabel);
        label.setText(labelText);
        TextView textView = layout.findViewById(R.id.numberText);
        textView.setText(String.valueOf(value));

        Button increaseButton = layout.findViewById(R.id.increaseButton);
        Button decreaseButton = layout.findViewById(R.id.decreaseButton);

        setupClickListener(value, increment, textView, increaseButton, decreaseButton);
    }

    private void setupCornerLayout(int layoutId, String labelText, AtomicInteger valueX, AtomicInteger valueY) {
        View layout = findViewById(layoutId);
        TextView label = layout.findViewById(R.id.layoutLabel);
        label.setText(labelText);

        TextView textViewX = layout.findViewById(R.id.numberXText);
        TextView textViewY = layout.findViewById(R.id.numberYText);
        textViewX.setText(String.valueOf(valueX));
        textViewY.setText(String.valueOf(valueY));

        Button increaseXButton = layout.findViewById(R.id.increaseXButton);
        Button decreaseXButton = layout.findViewById(R.id.decreaseXButton);
        Button increaseYButton = layout.findViewById(R.id.increaseYButton);
        Button decreaseYButton = layout.findViewById(R.id.decreaseYButton);

        setupClickListener(valueX, cornerIncrement, textViewX, increaseXButton, decreaseXButton);
        setupClickListener(valueY, cornerIncrement, textViewY, increaseYButton, decreaseYButton);
    }

    private void setupClickListener(AtomicInteger value, int increment, TextView textView, Button increaseButton, Button decreaseButton) {
        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseValueAndSetText(textView, value, increment);
            }
        });

        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (value.get() >= increment) {
                    decreaseValueAndSetText(textView, value, increment);
                }
            }
        });
    }

    private void increaseValueAndSetText(TextView textView, AtomicInteger value, int increment) {
        value.addAndGet(increment);
        textView.setText(String.valueOf(value));
    }

    private void decreaseValueAndSetText(TextView textView, AtomicInteger value, int increment) {
        value.addAndGet(-increment);
        textView.setText(String.valueOf(value));
    }

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