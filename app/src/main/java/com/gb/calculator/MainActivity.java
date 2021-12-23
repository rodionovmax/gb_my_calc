package com.gb.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "myCustomLogger: ";
    private final int[] numericButtons = {
            R.id.key_0, R.id.key_1, R.id.key_2, R.id.key_3, R.id.key_4,
            R.id.key_5, R.id.key_6, R.id.key_7, R.id.key_8, R.id.key_9
    };
    private final int[] operandButtons = {
            R.id.key_divide, R.id.key_multiply, R.id.key_plus, R.id.key_minus
    };
    private TextView display;
    private boolean lastNumeric = false;
    private boolean lastDot = false;

    public static final String DISPLAY = "DISPLAY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_constraint);

        this.display = findViewById(R.id.display);

        onDigit();
        onOperator();
        onClear();
        onDecimal();
        onChangeSign();
        onPercent();
        onEqual();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(DISPLAY, display.getText().toString());
        Log.d(TAG, "onSaveInstanceState: " + display.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String display_output = savedInstanceState.getString(DISPLAY);
        Log.d(TAG, "onRestoreInstanceState: " + display_output);
        display.setText(display_output);
    }


    /**
     * Set a listener for numeric buttons
     */
    private void onDigit() {
        View.OnClickListener listener = view -> {
            Button button = (Button) view;
            display.append(button.getText().toString());
            lastNumeric = true;
        };

        for (int id : numericButtons) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    /**
     * Set a listener for operand buttons
     */
    private void onOperator() {
        View.OnClickListener listener = view -> {
            Button button = (Button) view;

            if (lastNumeric && !isOperatorAdded(display.getText().toString())) {
                display.append(button.getText().toString());
                lastNumeric = false;
                lastDot = false;
            }
        };

        for (int id : operandButtons) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    /*
    * Verify if operator has been added or not
    */
    private boolean isOperatorAdded(String value) {
        if (value.startsWith("-")) {
            Log.d(TAG, "not added");
            return false;
        } else if (value.contains("÷") || value.contains("×") || value.contains("+") || value.contains("-")) {
            Log.d(TAG, "added");
            return true;
        } else {
            Log.d(TAG, "not. default case");
            return false;
        }

    }

    /**
    * Set a listener for AC button
    */
    private void onClear() {
        Button clearBtn = findViewById(R.id.key_ac);
        clearBtn.setOnClickListener(view -> {
            display.setText("");
            lastNumeric = false;
            lastDot = false;
        });
    }

    /*
    * Set a listener for decimal button
    */
    private void onDecimal() {
        Button decimalBtn = findViewById(R.id.key_decimal);
        decimalBtn.setOnClickListener(view -> {
            if (lastNumeric && !lastDot) {
                display.append(decimalBtn.getText().toString());
                lastNumeric = false;
                lastDot = true;
            }
        });
    }

    // TODO: implement later
    private void onEqual() {
        Button equalBtn = findViewById(R.id.key_equal);
        equalBtn.setOnClickListener(view -> {
            if (lastNumeric && isOperatorAdded(display.getText().toString())) {

                // Show a toast message as a placeholder
                Toast.makeText(
                        MainActivity.this,
                        getResources().getString(R.string.toast_calculating_results_msg),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    // TODO: implement later
    private void onPercent() {

    }

    // TODO: implement later
    private void onChangeSign() {

    }


}

