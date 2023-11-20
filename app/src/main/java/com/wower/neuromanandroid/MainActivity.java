package com.wower.neuromanandroid;

import androidx.annotation.ArrayRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String[] scenariuszeArray = {"MOCA", "test9p"};
    int czasKlikaniaValue = 1100;
    int czasKlikaniaIncrement = 100;
    int czasPrzerwyValue = 500;
    int czasPrzerwyIncrement = 100;
    int wielkoscKursoraValue = 60;
    int wielkoscKursoraIncrement = 5;
    int cornerMin = 0;
    int cornerMax = 100;
    int cornerIncrement = 10;
    int upperLeftCornerXValue = cornerMin;
    int upperLeftCornerYValue = cornerMin;
    int lowerRightCornerXValue = cornerMax;
    int lowerRightCornerYValue = cornerMax;

    private Spinner operatorSpinner;
    private Spinner profilSpinner;
    private ArrayAdapter<String> operatorAdapter;
    private ArrayAdapter<String> profilAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);

       //OPERATOR
       View operatorLayout = findViewById(R.id.operatorLayout);
       TextView operatorLabel = operatorLayout.findViewById(R.id.layoutLabel);
       operatorLabel.setText("Operator: ");
       operatorSpinner = operatorLayout.findViewById(R.id.layoutSpinner);
       operatorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());
       operatorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       operatorSpinner.setAdapter(operatorAdapter);
       operatorAdapter.add("Adam");
       operatorAdapter.add("Ola");
       operatorAdapter.notifyDataSetChanged();
       operatorSpinner.setSelection(0); // Set the default selection to the first item
       Button operatorButton1 = operatorLayout.findViewById(R.id.layoutButton1);
       operatorButton1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               showInputDialog(operatorAdapter, operatorSpinner);
           }
       });
       Button operatorButton2 = operatorLayout.findViewById(R.id.layoutButton2);
       operatorButton2.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               deleteSelectedItem(operatorAdapter, operatorSpinner);
           }
       });

       //PROFIL
        View profilLayout = findViewById(R.id.profilLayout);
        TextView profilLabel = profilLayout.findViewById(R.id.layoutLabel);
        profilLabel.setText("Profil: ");
        profilSpinner = profilLayout.findViewById(R.id.layoutSpinner);
        profilAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());
        profilAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        profilSpinner.setAdapter(profilAdapter);
        profilAdapter.add("Domyślny");
        profilAdapter.add("Lewy-Szary");
        profilAdapter.add("Prawy");
        profilAdapter.notifyDataSetChanged();;
        profilSpinner.setSelection(0);
        Button profilButton1 = profilLayout.findViewById(R.id.layoutButton1);
        profilButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog(profilAdapter, profilSpinner);
            }
        });
        Button profilButton2 = profilLayout.findViewById(R.id.layoutButton2);
        profilButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSelectedItem(profilAdapter, profilSpinner);
            }
        });

        View czasKlikaniaLayout = findViewById(R.id.czasKlikaniaLayout);
        TextView czasKlikaniaLabel = czasKlikaniaLayout.findViewById(R.id.layoutLabel);
        czasKlikaniaLabel.setText("Czas klikania (ms) ");
        TextView czasKlikaniaNumber = czasKlikaniaLayout.findViewById(R.id.numberText);
        czasKlikaniaNumber.setText(String.valueOf(czasKlikaniaValue));
        Button czasKlikaniaIncreaseButton = czasKlikaniaLayout.findViewById(R.id.increaseButton);
        czasKlikaniaIncreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseValueAndSetText(czasKlikaniaNumber, czasKlikaniaValue, czasKlikaniaIncrement);
                czasKlikaniaValue += czasKlikaniaIncrement;
            }
        });
        Button czasKlikaniaDecreaseButton = czasKlikaniaLayout.findViewById(R.id.decreaseButton);
        czasKlikaniaDecreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(czasKlikaniaValue >= czasKlikaniaIncrement) {
                    decreaseValueAndSetText(czasKlikaniaNumber, czasKlikaniaValue, czasKlikaniaIncrement);
                    czasKlikaniaValue -= czasKlikaniaIncrement;
                }
            }
        });

        View czasPrzerwyLayout = findViewById(R.id.czasPrzerwyLayout);
        TextView czasPrzerwyLabel = czasPrzerwyLayout.findViewById(R.id.layoutLabel);
        czasPrzerwyLabel.setText("Max czas przerwy (ms) ");
        TextView czasPrzerwyNumber = czasPrzerwyLayout.findViewById(R.id.numberText);
        czasPrzerwyNumber.setText(String.valueOf(czasPrzerwyValue));
        Button czasPrzerwyIncreaseButton = czasPrzerwyLayout.findViewById(R.id.increaseButton);
        czasPrzerwyIncreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseValueAndSetText(czasPrzerwyNumber, czasPrzerwyValue, czasPrzerwyIncrement);
                czasPrzerwyValue += czasPrzerwyIncrement;
            }
        });
        Button czasPrzerwyDecreaseButton = czasPrzerwyLayout.findViewById(R.id.decreaseButton);
        czasPrzerwyDecreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(czasPrzerwyValue >= czasPrzerwyIncrement) {
                    decreaseValueAndSetText(czasPrzerwyNumber, czasPrzerwyValue, czasPrzerwyIncrement);
                    czasPrzerwyValue -= czasPrzerwyIncrement;
                }
            }
        });

        View wielkoscKursoraLayout = findViewById(R.id.wielkoscKursoraLayout);
        TextView wielkoscKursoraLabel = wielkoscKursoraLayout.findViewById(R.id.layoutLabel);
        wielkoscKursoraLabel.setText("Wielkość kursora (px) ");
        TextView wielkoscKursoraNumber = wielkoscKursoraLayout.findViewById(R.id.numberText);
        wielkoscKursoraNumber.setText(String.valueOf(wielkoscKursoraValue));
        Button wielkoscKursoraIncreaseButton = wielkoscKursoraLayout.findViewById(R.id.increaseButton);
        wielkoscKursoraIncreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseValueAndSetText(wielkoscKursoraNumber, wielkoscKursoraValue, wielkoscKursoraIncrement);
                wielkoscKursoraValue += wielkoscKursoraIncrement;
            }
        });
        Button wielkoscKursoraDecreaseButton = wielkoscKursoraLayout.findViewById(R.id.decreaseButton);
        wielkoscKursoraDecreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wielkoscKursoraValue >= wielkoscKursoraIncrement) {
                    decreaseValueAndSetText(wielkoscKursoraNumber, wielkoscKursoraValue, wielkoscKursoraIncrement);
                    wielkoscKursoraValue -= wielkoscKursoraIncrement;
                }
            }
        });

        View upperLeftCornerLayout = findViewById(R.id.upperLeftCornerLayout);
        TextView upperLeftCornerLabel = upperLeftCornerLayout.findViewById(R.id.layoutLabel);
        upperLeftCornerLabel.setText("Lewy górny róg (%) ");
        TextView upperLeftCornerNumberXText = upperLeftCornerLayout.findViewById(R.id.numberXText);
        upperLeftCornerNumberXText.setText(String.valueOf(cornerMin));
        Button upperLeftCornerIncreaseXButton = upperLeftCornerLayout.findViewById(R.id.increaseXButton);
        upperLeftCornerIncreaseXButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(upperLeftCornerXValue < cornerMax) {
                    increaseValueAndSetText(upperLeftCornerNumberXText, upperLeftCornerXValue, cornerIncrement);
                    upperLeftCornerXValue += cornerIncrement;
                }
            }
        });
        Button upperLeftCornerDecreaseXButton = upperLeftCornerLayout.findViewById(R.id.decreaseXButton);
        upperLeftCornerDecreaseXButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(upperLeftCornerXValue > cornerMin) {
                    decreaseValueAndSetText(upperLeftCornerNumberXText, upperLeftCornerXValue, cornerIncrement);
                    upperLeftCornerXValue -= cornerIncrement;
                }
            }
        });
        TextView upperLeftCornerNumberYText = upperLeftCornerLayout.findViewById(R.id.numberYText);
        upperLeftCornerNumberYText.setText(String.valueOf(cornerMin));
        Button upperLeftCornerIncreaseYButton = upperLeftCornerLayout.findViewById(R.id.increaseYButton);
        upperLeftCornerIncreaseYButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(upperLeftCornerYValue < cornerMax) {
                    increaseValueAndSetText(upperLeftCornerNumberYText, upperLeftCornerYValue, cornerIncrement);
                    upperLeftCornerYValue += cornerIncrement;
                }
            }
        });
        Button upperLeftCornerDecreaseYButton = upperLeftCornerLayout.findViewById(R.id.decreaseYButton);
        upperLeftCornerDecreaseYButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(upperLeftCornerYValue > cornerMin) {
                    decreaseValueAndSetText(upperLeftCornerNumberYText, upperLeftCornerYValue, cornerIncrement);
                    upperLeftCornerYValue -= cornerIncrement;
                }
            }
        });

        View lowerRightCornerLayout = findViewById(R.id.lowerRightCornerLayout);
        TextView lowerRightCornerLabel = lowerRightCornerLayout.findViewById(R.id.layoutLabel);
        lowerRightCornerLabel.setText("Prawy dolny róg (%) ");
        TextView lowerRightCornerNumberXText = lowerRightCornerLayout.findViewById(R.id.numberXText);
        lowerRightCornerNumberXText.setText(String.valueOf(cornerMax));
        Button lowerRightCornerIncreaseXButton = lowerRightCornerLayout.findViewById(R.id.increaseXButton);
        lowerRightCornerIncreaseXButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lowerRightCornerXValue < cornerMax) {
                    increaseValueAndSetText(lowerRightCornerNumberXText, lowerRightCornerXValue, cornerIncrement);
                    lowerRightCornerXValue += cornerIncrement;
                }
            }
        });
        Button lowerRightCornerDecreaseXButton = lowerRightCornerLayout.findViewById(R.id.decreaseXButton);
        lowerRightCornerDecreaseXButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lowerRightCornerXValue > cornerMin) {
                    decreaseValueAndSetText(lowerRightCornerNumberXText, lowerRightCornerXValue, cornerIncrement);
                    lowerRightCornerXValue -= cornerIncrement;
                }
            }
        });
        TextView lowerRightCornerNumberYText = lowerRightCornerLayout.findViewById(R.id.numberYText);
        lowerRightCornerNumberYText.setText(String.valueOf(cornerMax));
        Button lowerRightCornerIncreaseYButton = lowerRightCornerLayout.findViewById(R.id.increaseYButton);
        lowerRightCornerIncreaseYButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lowerRightCornerYValue < cornerMax) {
                    increaseValueAndSetText(lowerRightCornerNumberYText, lowerRightCornerYValue, cornerIncrement);
                    lowerRightCornerYValue += cornerIncrement;
                }
            }
        });
        Button lowerRightCornerDecreaseYButton = lowerRightCornerLayout.findViewById(R.id.decreaseYButton);
        lowerRightCornerDecreaseYButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lowerRightCornerYValue > cornerMin) {
                    decreaseValueAndSetText(lowerRightCornerNumberYText, lowerRightCornerYValue, cornerIncrement);
                    lowerRightCornerYValue -= cornerIncrement;
                }
            }
        });

        ArrayAdapter scenariuszeAdapter = new ArrayAdapter<String>(this, R.layout.listview, scenariuszeArray);
        ListView scenariuszeListView = (ListView) findViewById(R.id.scenariuszeListView);
        scenariuszeListView.setAdapter(scenariuszeAdapter);

    }

    private void increaseValueAndSetText(TextView textView, int value, int increment) {
        value += increment;
        textView.setText(String.valueOf(value));
    }

    private void decreaseValueAndSetText(TextView textView, int value, int increment) {
            value -= increment;
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