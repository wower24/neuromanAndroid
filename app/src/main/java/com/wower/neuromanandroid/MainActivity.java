package com.wower.neuromanandroid;

import androidx.annotation.ArrayRes;
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

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    String[] scenariuszeArray = {"MOCA", "test9p"};
    AtomicInteger czasKlikaniaValue = new AtomicInteger(1100);
    int czasKlikaniaIncrement = 100;
    AtomicInteger czasPrzerwyValue = new AtomicInteger(500);
    int czasPrzerwyIncrement = 100;
    AtomicInteger wielkoscKursoraValue = new AtomicInteger(60);
    int wielkoscKursoraIncrement = 5;
    int cornerMin = 0;
    int cornerMax = 100;
    int cornerIncrement = 10;
    AtomicInteger upperLeftCornerXValue = new AtomicInteger(cornerMin);
    AtomicInteger upperLeftCornerYValue = new AtomicInteger(cornerMin);
    AtomicInteger lowerRightCornerXValue = new AtomicInteger(cornerMax);
    AtomicInteger lowerRightCornerYValue = new AtomicInteger(cornerMax);
    int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupOperatorSpinner();
        setupProfilSpinner();
        setupScenariosListViewAndCheckbox();
        setupCzasKlikaniaLayout();
        setupCzasPrzerwyLayout();
        setupWielkoscKursoraLayout();
        setupUpperLeftCornerLayout();
        setupLowerRightCornerLayout();
    }

    private void setupOperatorSpinner() {
        View operatorLayout = findViewById(R.id.operatorLayout);
        TextView label = operatorLayout.findViewById(R.id.layoutLabel);
        label.setText("Operator: ");
        Spinner operatorSpinner = operatorLayout.findViewById(R.id.layoutSpinner);
        ArrayAdapter<String> operatorAdapter = setupSpinner(operatorSpinner, R.array.operator_items);
        operatorAdapter.add("Adam");
        operatorAdapter.add("Ola");
        operatorAdapter.notifyDataSetChanged();
        operatorSpinner.setSelection(0);

        setupSpinnerButtons(findViewById(R.id.operatorLayout), operatorAdapter, operatorSpinner);
    }

    private void setupProfilSpinner() {
        View profilLayout = findViewById(R.id.profilLayout);
        TextView label = profilLayout.findViewById(R.id.layoutLabel);
        label.setText("Profil: ");
        Spinner profilSpinner = profilLayout.findViewById(R.id.layoutSpinner);
        ArrayAdapter<String> profilAdapter = setupSpinner(profilSpinner, R.array.profil_items);
        profilAdapter.add("Domyślny");
        profilAdapter.add("Lewy-Szary");
        profilAdapter.add("Prawy");
        profilAdapter.notifyDataSetChanged();
        profilSpinner.setSelection(0);

        setupSpinnerButtons(findViewById(R.id.profilLayout), profilAdapter, profilSpinner);
    }

    private ArrayAdapter<String> setupSpinner(Spinner spinner, @ArrayRes int itemsArrayId) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        return adapter;
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
                if(wykonanoCheckBox.isChecked()) {
                    if(!selectedScenario.equals(null)) {
                        startScenario(selectedScenario[0]);
                    }
                }
                else {
                    showCheckboxWarning();
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
    }

    private void showCheckboxWarning() {
        // Display a warning dialog when the checkbox is not checked
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please check the checkbox.")
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
                upperLeftCornerXValue,
                upperLeftCornerYValue
        );
    }

    private void setupLowerRightCornerLayout() {
        setupCornerLayout(
                R.id.lowerRightCornerLayout,
                "Prawy dolny róg (%) ",
                lowerRightCornerXValue,
                lowerRightCornerYValue
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
}