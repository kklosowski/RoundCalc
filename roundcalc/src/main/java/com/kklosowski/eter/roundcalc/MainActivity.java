package com.kklosowski.eter.roundcalc;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;


public class MainActivity extends AppCompatActivity implements
        DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


         mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                        //Log.d(TAG, "onConnected: " + connectionHint);
                        // Now you can use the Data Layer API
                    }
                    @Override
                    public void onConnectionSuspended(int cause) {
                        //Log.d(TAG, "onConnectionSuspended: " + cause);
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                       // Log.d(TAG, "onConnectionFailed: " + result);
                    }
                })
                // Request access only to the Wearable API
                .addApi(Wearable.API)
                .build();


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
        int styleID = 0;
        Context context = getApplicationContext();
        SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        switch (view.getId()){
            case R.id.LBImageButton:{
                colorBar.setColor(ContextCompat.getColor(context, R.color.lb500));
                colorBackground = ContextCompat.getColor(context, R.color.lb100);
                styleID = 1;
                break;
            }
            case R.id.DPImageButton:{
                colorBar.setColor(ContextCompat.getColor(context, R.color.dp500));
                colorBackground = ContextCompat.getColor(context, R.color.dp75);
                styleID = 2;
                break;
            }
            case R.id.RImageButton:{
                colorBar.setColor(ContextCompat.getColor(context, R.color.r500));
                colorBackground = ContextCompat.getColor(context, R.color.r75);
                styleID = 3;
                break;
            }
            case R.id.GImageButton:{
                colorBar.setColor(ContextCompat.getColor(context, R.color.g500));
                colorBackground = ContextCompat.getColor(context, R.color.g75);
                styleID = 4;
                break;
            }
        }

        editor.putInt(getString(R.string.sharedPreferenceKey1), colorBar.getColor());
        editor.putInt(getString(R.string.sharedPreferenceKey2), colorBackground);
        editor.commit();

        changeStyle(styleID);


        getSupportActionBar().setBackgroundDrawable(colorBar);
        backgroundRelativeLayout.setBackgroundColor(colorBackground);

        //Toast.makeText(MainActivity.this, "Changes will be visible after restarting wear app.", Toast.LENGTH_SHORT).show();

        backgroundRelativeLayout.invalidate();
    }

    private void changeStyle(int styleID) {
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/styleID");
        putDataMapReq.getDataMap().putInt("styleID", styleID);
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest().setUrgent();

        Toast.makeText(MainActivity.this, "changeStyle()", Toast.LENGTH_SHORT).show();
        
        Wearable.DataApi.putDataItem(mGoogleApiClient, putDataReq)
                .setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                    @Override
                    public void onResult(@NonNull DataApi.DataItemResult dataItemResult) {
                        Log.v("x", "onResult");
                        if(!dataItemResult.getStatus().isSuccess()){
                            Toast.makeText(MainActivity.this, "Failed to change color.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Color will be changed shortly", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        PendingResult<DataApi.DataItemResult> pendingResult =
                Wearable.DataApi.putDataItem(mGoogleApiClient, putDataReq);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {

    }
}
