package com.gb.calculator;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "myCustomLogger: ";

    private final int[] numericButtons = {
            R.id.key_0, R.id.key_1, R.id.key_2, R.id.key_3, R.id.key_4,
            R.id.key_5, R.id.key_6, R.id.key_7, R.id.key_8, R.id.key_9
    };
    private final int[] operandButtons = {
            R.id.key_divide, R.id.key_multiply, R.id.key_plus, R.id.key_minus
    };
    private TextView tvDisplay;
    private boolean lastNumeric = false;
    private boolean lastDot = false;

    public static final String DISPLAY = "DISPLAY";
    private Display display = new Display();

    private Button settingsBtn;
    public static final String MODE = "isDarkModeOn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_constraint);

        this.tvDisplay = findViewById(R.id.display);

        onDigit();
        onOperator();
        onClear();
        onDecimal();
        onChangeSign();
        onPercent();
        onEqual();

        settingsBtn = findViewById(R.id.settings_button);
        settingsBtn.setOnClickListener(this);

        // Get MODE in shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        final boolean isDarkModeOn = sharedPreferences.getBoolean(MODE, false);

        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
//        Option 1. Save value as String
//        outState.putString(DISPLAY, tvDisplay.getText().toString());

//        Option 2. Save as Parcelable
        display.setDisplay(tvDisplay.getText().toString());
        outState.putParcelable(DISPLAY, display);
        Log.d(TAG, "onSaveInstanceState: " + display.getDisplay());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
//        Option 1. Get display output as String
//        String display_output = savedInstanceState.getString(DISPLAY);

//        Option 2. Get display output as Parcelable
        Display display_output = savedInstanceState.getParcelable(DISPLAY);
        Log.d(TAG, "onRestoreInstanceState: " + display_output.getDisplay());
        tvDisplay.setText(display_output.getDisplay());
    }

    @Override
    public void onClick(View view) {
        Intent settingsIntent = new Intent(this, Settings.class);
        startActivity(settingsIntent);
    }


    /**
     * Set a listener for numeric buttons
     */
    private void onDigit() {
        View.OnClickListener listener = view -> {
            Button button = (Button) view;
            tvDisplay.append(button.getText().toString());
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

            if (lastNumeric && !isOperatorAdded(tvDisplay.getText().toString())) {
                tvDisplay.append(button.getText().toString());
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
            tvDisplay.setText("");
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
                tvDisplay.append(decimalBtn.getText().toString());
                lastNumeric = false;
                lastDot = true;
            }
        });
    }

    private void onEqual() {
        Button equalBtn = findViewById(R.id.key_equal);
        equalBtn.setOnClickListener(view -> {
            if (lastNumeric) {
                String displayValue = tvDisplay.getText().toString();

                String prefix = "";

                try {
                    if (displayValue.startsWith("-")) {
                        prefix = "-";
                        displayValue = displayValue.substring(1);
                    }

                    if (displayValue.contains("-")) {
                        String[] splitValue = displayValue.split("-");

                        String one = splitValue[0];
                        String two = splitValue[1];

                        if (!prefix.isEmpty()) {
                            one = prefix + one;
                        }

                        double res = Double.parseDouble(one) - Double.parseDouble(two);
                        String resultToDisplay = Double.toString(res);
                        tvDisplay.setText(removeZeroAfterDot(resultToDisplay));
                    } else if (displayValue.contains("+")) {
                        String[] splitValue = displayValue.split("\\+");

                        String one = splitValue[0];
                        String two = splitValue[1];

                        if (!prefix.isEmpty()) {
                            one = prefix + one;
                        }

                        double res = Double.parseDouble(one) + Double.parseDouble(two);
                        String resultToDisplay = Double.toString(res);
                        tvDisplay.setText(removeZeroAfterDot(resultToDisplay));
                    } else if (displayValue.contains("×")) {
                        String[] splitValue = displayValue.split("×");

                        String one = splitValue[0];
                        String two = splitValue[1];

                        if (!prefix.isEmpty()) {
                            one = prefix + one;
                        }

                        double res = Double.parseDouble(one) * Double.parseDouble(two);
                        String resultToDisplay = Double.toString(res);
                        tvDisplay.setText(removeZeroAfterDot(resultToDisplay));
                    } else if (displayValue.contains("÷")) {
                        String[] splitValue = displayValue.split("÷");

                        String one = splitValue[0];
                        String two = splitValue[1];

                        if (!prefix.isEmpty()) {
                            one = prefix + one;
                        }

                        double res = Double.parseDouble(one) / Double.parseDouble(two);
                        String resultToDisplay = Double.toString(res);
                        tvDisplay.setText(removeZeroAfterDot(resultToDisplay));
                    }
                } catch (ArithmeticException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private String removeZeroAfterDot(String result) {
        String value = result;
        if (result.contains(".0")) {
            value = result.substring(0, result.length() - 2);
        }
        return value;
    }

    private void onPercent() {
        Button percentBtn = findViewById(R.id.key_percent);
        percentBtn.setOnClickListener(view -> {
            if (lastNumeric && !isOperatorAdded(tvDisplay.getText().toString())) {
                String value = tvDisplay.getText().toString();
                double parsedValue = Double.parseDouble(value);
                parsedValue = parsedValue / 100;
                String valueToString = Double.toString(parsedValue);
                tvDisplay.setText(valueToString);
            }
        });
    }

    private void onChangeSign() {
        Button changeSignBtn = findViewById(R.id.key_plus_minus);
        changeSignBtn.setOnClickListener(view -> {
            String displayValue = tvDisplay.getText().toString();
            String changedValue;
            if (displayValue.startsWith("-") && !isOperatorAdded(tvDisplay.getText().toString())) {
                changedValue = displayValue.substring(1);
                tvDisplay.setText(changedValue);
            } else if (!displayValue.startsWith("-") && !isOperatorAdded(tvDisplay.getText().toString())) {
                changedValue = "-" + displayValue;
                tvDisplay.setText(changedValue);
            }
        });
    }

}

