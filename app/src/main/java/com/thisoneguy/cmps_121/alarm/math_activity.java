package com.thisoneguy.cmps_121.alarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class math_activity extends AppCompatActivity {
    private EditText solution;
    private Button check, randomize;
    private TextView valueOne, valueTwo, status, operation;

    private int num1, num2, sol;
    private char op;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //returns to main activity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(math_activity.this, MainActivity.class));
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        init();
    }

    private void init() {
        valueOne = (TextView)findViewById(R.id.value_One);
        valueTwo = (TextView)findViewById(R.id.value_Two);
        operation = (TextView)findViewById(R.id.operation);
        status = (TextView)findViewById(R.id.status);

        randomize = (Button)findViewById(R.id.randomize);
        check = (Button)findViewById(R.id.check);

        solution = (EditText)findViewById(R.id.solution);

        generateEquation();
    }

    private void generateEquation() {
        generateNums();
        generateOperation();

        valueOne.setText(Integer.toString(num1));
        valueTwo.setText(Integer.toString(num2));
        operation.setText(Character.toString(op));
    }

    private void generateNums() {
        num1 = num2 = 0;

        Random random = new Random();
        num1 = random.nextInt(10);
        num2 = random.nextInt(10);
    }

    private void generateOperation() {
        op = '?';

        Random random = new Random();
        switch (random.nextInt(3)) {
            case 0:
                op = '+';
                break;
            case 1:
                op = '-';
                break;
            case 2:
                op = 'x';
                break;
            default:
                op = '?';
        }
    }

    private void solveProblem() {
        sol = 0;

        switch (op) {
            case '+':
                sol = num1 + num2;
                break;
            case '-':
                sol = num1 - num2;
                break;
            case 'x':
                sol = num1 * num2;
        }
    }

    public void randomize(View view) {
        generateEquation();
    }

    public void checkSolution(View view) {
        solveProblem();

        if(!TextUtils.isEmpty(solution.getText())) {
            if (sol == Integer.parseInt(solution.getText().toString())) {
                Log.d("DEBUG", "Correct!");
                status.setText("Status: Correct");
            } else {
                Log.d("DEBUG", "Wrong!");
                status.setText("Status: Incorrect!");
            }
        }
    }

}
