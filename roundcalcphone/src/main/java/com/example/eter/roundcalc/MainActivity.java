package com.example.eter.roundcalc;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout backgroundRelativeLayout = (RelativeLayout) findViewById(R.id.backgroundRelativeLayout);

        SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        int iColorPrimary = sharedPref.getInt(getString(R.string.sharedPreferenceKey1), ContextCompat.getColor(getApplicationContext(), R.color.lb500));
        int iColorSecondary = sharedPref.getInt(getString(R.string.sharedPreferenceKey2), ContextCompat.getColor(getApplicationContext(), R.color.lb100));

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(iColorPrimary));
        backgroundRelativeLayout.setBackgroundColor(iColorSecondary);

    }

    public void changeColor(View view){

        RelativeLayout backgroundRelativeLayout = (RelativeLayout) findViewById(R.id.backgroundRelativeLayout);
        ColorDrawable colorBar = new ColorDrawable();
        int colorBackground = 0;
        int iBackgroundID = 0;
        Context context = getApplicationContext();
        SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        switch (view.getId()){
            case R.id.LBImageButton:{
                colorBar.setColor(ContextCompat.getColor(context, R.color.lb500));
                colorBackground = ContextCompat.getColor(context, R.color.lb100);
                iBackgroundID = 1;
                break;
            }
            case R.id.DPImageButton:{
                colorBar.setColor(ContextCompat.getColor(context, R.color.dp500));
                colorBackground = ContextCompat.getColor(context, R.color.dp75);
                iBackgroundID = 2;
                break;
            }
            case R.id.RImageButton:{
                colorBar.setColor(ContextCompat.getColor(context, R.color.r500));
                colorBackground = ContextCompat.getColor(context, R.color.r75);
                iBackgroundID = 3;
                break;
            }
            case R.id.GImageButton:{
                colorBar.setColor(ContextCompat.getColor(context, R.color.g500));
                colorBackground = ContextCompat.getColor(context, R.color.g75);
                iBackgroundID = 4;
                break;
            }
        }

        editor.putInt(getString(R.string.sharedPreferenceKey1), colorBar.getColor());
        editor.putInt(getString(R.string.sharedPreferenceKey2), colorBackground);
        editor.putInt(getString(R.string.sharedPreferenceKey3), iBackgroundID);
        editor.commit();

        getSupportActionBar().setBackgroundDrawable(colorBar);
        backgroundRelativeLayout.setBackgroundColor(colorBackground);
        backgroundRelativeLayout.invalidate();
    }
}
