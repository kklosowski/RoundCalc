package com.example.eter.roundcalc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.CircledImageView;
import android.support.wearable.view.WatchViewStub;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.udojava.evalex.Expression;

public class MainActivity extends WearableActivity {


    private Bitmap calcMaskBitmap;
    private CircledImageView calcGridImageView;
    private TextView varOneTextView, varTwoTextView, operatorTextView, resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {


                varOneTextView = (TextView) findViewById(R.id.varOneTextView);
                varTwoTextView = (TextView) findViewById(R.id.varTwoTextView);
                operatorTextView = (TextView) findViewById(R.id.operatorTextView);
                resultTextView = (TextView) findViewById(R.id.resultTextView);

                calcMaskBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.calc_mask);
                calcGridImageView = (CircledImageView) findViewById(R.id.calc_grid);



                calcGridImageView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {

                        long color = getColor(motionEvent.getX(), motionEvent.getY());
                        String sVarOne = varOneTextView.getText().toString();
                        String sVarTwo = varTwoTextView.getText().toString();
                        String sOperator = operatorTextView.getText().toString();
                        String sResult;
                        Character cPressed;


                        switch (motionEvent.getAction()) {
                            case MotionEvent.ACTION_DOWN: {

                                //<editor-fold desc="ColorSwitch">
                                switch (Integer.valueOf(String.valueOf(color))) {

                                    case 0xffeeeeee:
                                        cPressed = '1';
                                        break;

                                    case 0xffdddddd:
                                        cPressed = '2';
                                        break;

                                    case 0xffcccccc:
                                        cPressed = '3';
                                        break;

                                    case 0xffbbbbbb:
                                        cPressed = '4';
                                        break;

                                    case 0xffaaaaaa:
                                        cPressed = '5';
                                        break;

                                    case 0xff999999:
                                        cPressed = '6';
                                        break;

                                    case 0xff888888:
                                        cPressed = '7';
                                        break;

                                    case 0xff777777:
                                        cPressed = '8';
                                        break;

                                    case 0xff666666:
                                        cPressed = '9';
                                        break;

                                    case 0xff555555:
                                        cPressed = '0';
                                        break;

                                    case 0xff00ff00:
                                        cPressed = 'c';
                                        break;

                                    case 0xff00ffff:
                                        cPressed = '+';
                                        break;

                                    case 0xffff00ff:
                                        cPressed = '-';
                                        break;

                                    case 0xffffff00:
                                        cPressed = '*';
                                        break;

                                    case 0xffff0000:
                                        cPressed = '÷';
                                        break;

                                    case 0xff0000ff:
                                        cPressed = '=';
                                        break;

                                    default:
                                        cPressed = 'e';
                                        break;
                                }
                                //</editor-fold>

                                switch (cPressed)
                                {
                                    case '+':
                                    case '-':
                                    case '÷':
                                    case '*':{
                                        if(!sVarOne.isEmpty() && sVarTwo.isEmpty()) sOperator = Character.toString(cPressed);
                                        else if(!sVarOne.isEmpty() && !sVarTwo.isEmpty() && !sOperator.isEmpty()){
                                            sVarOne = calculate(sVarOne, sVarTwo, sOperator);
                                            sVarTwo = "";
                                            sOperator = Character.toString(cPressed);
                                        }
                                        break;
                                    }
                                    case 'c':{
                                        if(!sVarTwo.isEmpty()) sVarTwo = removeLastChar(sVarTwo);
                                        else if(!sOperator.isEmpty()) sOperator = "";
                                        else if(!sVarOne.isEmpty()) sVarOne = removeLastChar(sVarOne);

                                        break;
                                    }
                                    case '=': {
                                        if (!sVarOne.isEmpty() && !sVarTwo.isEmpty() && !sOperator.isEmpty()) {
                                            sVarOne = calculate(sVarOne, sVarTwo, sOperator);
                                            sVarTwo = "";
                                            sOperator = "";
                                        }
                                        break;
                                    }
                                    case 'e':
                                        break;
                                    default: {
                                        if (sOperator.isEmpty()) {
                                            if (sVarOne.isEmpty() && cPressed != '0')
                                                sVarOne += cPressed;
                                            else if (sVarOne.length() == 1 && sVarOne.charAt(0) == '0')
                                                sVarOne = Character.toString(cPressed);
                                            else sVarOne += cPressed;
                                        } else {
                                            if (sVarTwo.isEmpty()) sVarTwo += cPressed;
                                            else if (!sVarTwo.isEmpty() && sVarTwo.charAt(0) == '0')
                                                sVarTwo = Character.toString(cPressed);
                                            else sVarTwo += cPressed;
                                        }
                                        break;
                                    }
                                }

                                if(!sVarOne.isEmpty() && !sVarTwo.isEmpty() && !sOperator.isEmpty()) sResult = calculate(sVarOne, sVarTwo, sOperator);
                                else sResult = "";

                                varOneTextView.setText(sVarOne);
                                varTwoTextView.setText(sVarTwo);
                                resultTextView.setText(sResult);
                                operatorTextView.setText(sOperator);

                                findViewById(R.id.mainLinearLayout).invalidate();

                                break;
                            }
                        }

                        return false;
                    }
                });

            }
        });

    }


    private boolean isOperator(char c){
        return (c =='+' || c == '-' || c == '*' || c == '/');
    }

    private String calculate(String varOne, String varTwo, String operator){

        if(operator.charAt(0) == '÷') operator = "/";

        String sExpression = (varOne + operator + varTwo);
        if(operator.charAt(0) == '/' && varTwo.charAt(0) == '0') {
            Toast.makeText(MainActivity.this, "Never divide by 0!", Toast.LENGTH_SHORT).show();
            return varOne;
        }

        Expression expression = new Expression(sExpression);
        expression.setPrecision(6);
        return expression.eval().toPlainString();
    }


    private static String removeLastChar(String str) {
        return str.substring(0,str.length()-1);
    }

    private long getColor(float xTouch, float yTouch){

        int xMask = (int)(xTouch * (calcMaskBitmap.getHeight() / (double)calcGridImageView.getHeight()));
        int yMask = (int)(yTouch * (calcMaskBitmap.getHeight() / (double)calcGridImageView.getWidth()));

        return  calcMaskBitmap.getPixel(xMask,yMask);
    }

}
