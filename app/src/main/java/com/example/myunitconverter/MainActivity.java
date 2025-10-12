package com.example.myunitconverter;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    private EditText inputValue;
    private Spinner spinnerFrom, spinnerTo;
    private TextView resultText;
    private Button convertBtn;

    // Simple map (unit -> factor relative to meter) for Length units example
    private final Map<String, Double> lengthFactors = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI references
        inputValue = findViewById(R.id.inputValue);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        resultText = findViewById(R.id.resultText);
        convertBtn = findViewById(R.id.convertBtn);

        // Populate unit factors (relative to meter)
        lengthFactors.put("Meter", 1.0);
        lengthFactors.put("Kilometer", 1000.0);
        lengthFactors.put("Centimeter", 0.01);
        lengthFactors.put("Millimeter", 0.001);
        lengthFactors.put("Mile", 1609.344);
        lengthFactors.put("Yard", 0.9144);
        lengthFactors.put("Foot", 0.3048);
        lengthFactors.put("Inch", 0.0254);

        // Create adapter and attach to spinners
        List<String> units = new ArrayList<>(lengthFactors.keySet());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);

        // Set defaults (optional)
        spinnerFrom.setSelection(0);
        spinnerTo.setSelection(1);

        convertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doConversion();
            }
        });
    }

    private void doConversion() {
        String valStr = inputValue.getText().toString().trim();
        if (valStr.isEmpty()) {
            Toast.makeText(this, "Enter a value", Toast.LENGTH_SHORT).show();
            return;
        }

        double input;
        try {
            input = Double.parseDouble(valStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number", Toast.LENGTH_SHORT).show();
            return;
        }

        String from = spinnerFrom.getSelectedItem().toString();
        String to = spinnerTo.getSelectedItem().toString();

        Double fromFactor = lengthFactors.get(from);
        Double toFactor = lengthFactors.get(to);

        if (fromFactor == null || toFactor == null) {
            resultText.setText("Conversion not available");
            return;
        }

        // Convert: input * fromFactor (to meters) / toFactor (to target)
        double result = input * fromFactor / toFactor;

        // Format result: show up to 6 decimal places (trim trailing zeros)
        String resultStr = String.format(Locale.getDefault(), "%.6f", result).replaceAll("\\.?0+$", "");
        resultText.setText(resultStr + " " + to);
    }
}
