package com.example.mlapp;


import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.Locale;

import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity  implements SensorEventListener {
    TextToSpeech t1;
    EditText ed1;
    Button b1;
    private TextView textview;
    private SensorManager sensorManager;
    private Sensor proximitySensor;
    private Boolean isProximitySensorAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ed1=(EditText)findViewById(R.id.editText);
        b1=(Button)findViewById(R.id.button);
        textview = findViewById(R.id.textView);
        sensorManager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);

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
                String toSpeak = ed1.getText().toString();
                Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        if(sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)!=null)
        {
            proximitySensor=sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            isProximitySensorAvailable=true;

        } else
        {
            textview.setText("proximity sensor is not avaliable");
            isProximitySensorAvailable=false;
        }

    }

    public void onPause(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        if(isProximitySensorAvailable)
        {
            sensorManager.unregisterListener(this);
        }
        super.onPause();
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        textview.setText(sensorEvent.values[0]+" cm");
//

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(isProximitySensorAvailable)
        {
            sensorManager.registerListener(this,proximitySensor,sensorManager.SENSOR_DELAY_NORMAL);
        }
    }


}

