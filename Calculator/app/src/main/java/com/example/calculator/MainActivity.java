package com.example.calculator;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Boolean exitCalc = false;
    String answer = "";
    String tmp;
    int bracket = 0;

    TextView mathText;
    TextView resultField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mathText = findViewById(R.id.math_operation_text);
        resultField = findViewById(R.id.result);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("OPERATION", mathText.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mathText.setText(savedInstanceState.getString("OPERATION"));
    }

    public void onNumberClick(View view) {
        if (exitCalc) {
            exitCalc = false;
            resultField.setText("");
        }
        Button button = (Button) view;
        mathText.append(button.getText());
    }

    @SuppressLint("SetTextI18n")
    public void onOperationClick(View view) {
        Button button = (Button) view;
        String op = button.getText().toString();
        if (op.equals("AC") || op.equals("C") || op.equals("=") || op.equals("(") || op.equals(")"))
            switch (op) {
                case "C":
                    tmp = mathText.getText().toString();
                    if (tmp.length() != 0) {
                        tmp = tmp.substring(0, tmp.length() - 1);
                        mathText.setText(tmp);
                        int countBracket = 0;
                        for (char element : tmp.toCharArray())
                            if (element == '(')
                                countBracket++;
                        bracket = countBracket;
                    }
                    break;
                case "AC":
                    mathText.setText("");
                    resultField.setText("");
                    bracket = 0;
                    break;
                case "(":
                    mathText.append(button.getText());
                    bracket++;
                    break;
                case ")":
                    if (bracket != 0) {
                        mathText.append(button.getText());
                        bracket--;
                    }
                    break;
                case "=":
                    if (mathText.getText().toString().length() != 0) {
                        answer = Calculator.searchResult(mathText.getText().toString());
                        try {
                            double doubleAnswer = Double.parseDouble(answer);
                            long longAnswer = (long) doubleAnswer;
                            if (longAnswer - doubleAnswer == 0)
                                resultField.setText(String.valueOf(longAnswer));
                            else
                                resultField.setText(String.valueOf(doubleAnswer));
                            mathText.setText("");
                            answer = "";
                            exitCalc = true;
                            bracket = 0;
                            break;
                        } catch (Exception ex) {
                            resultField.setText("Error!");
                        }
                    }
            }
        else
            mathText.append(button.getText());
    }
}