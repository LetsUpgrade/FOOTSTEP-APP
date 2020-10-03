package com.example.mlapp;


import android.app.Activity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.Locale;
import android.content.Intent;

import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity   {
    TextToSpeech t1;
    EditText ed1;
    Button b1;
    Button b2;
    private TextView textview;
    private Vibrator vibrator;

    private SensorManager sensorManager;
    private Sensor proximitySensor;
    private SensorEventListener proximitySensorListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ed1=(EditText)findViewById(R.id.editText);
        b1=(Button)findViewById(R.id.button);
        b2=(Button)findViewById(R.id.button1);
        textview = findViewById(R.id.textView);
        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        proximitySensor=sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        vibrator= (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakOut();

            }
        });
        if(proximitySensor==null){
            Toast.makeText(this,"Proximity Sensor is not available",Toast.LENGTH_LONG).show();
            finish();
        }

        proximitySensorListener=new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent.values[0] < proximitySensor.getMaximumRange()) {

                    ed1.setText("Object ahead");
//                    b1.setEnabled(true);
                    speakOut();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
                    }
                    else
                    {vibrator.vibrate(500);}
                } else {
                    ed1.setText(" ");
//                    ed1.setText(sensorEvent.values[0]+" cm");
                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }

        };
        sensorManager.registerListener(proximitySensorListener,proximitySensor,2*1000*1000);


        b2.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(),DetectorActivity.class));
           /* if you want to finish the first activity then just call
            finish(); */
            }
        });


    };

    public void onPause(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        sensorManager.unregisterListener(proximitySensorListener);

        super.onPause();
    }
    private void speakOut() {
        String toSpeak = ed1.getText().toString();
        Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
    }




}

