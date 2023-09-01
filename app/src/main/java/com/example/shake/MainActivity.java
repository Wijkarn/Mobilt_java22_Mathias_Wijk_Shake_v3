package com.example.shake;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    SensorManager sensorManager;
    SensorEventListener gyroListener;
    SensorEventListener accListener;
    SensorEventListener lightListener;
    Sensor lightSensor;
    FragmentManager fm;
    float lightValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.frameLayout, BlankFragment.class, null).commit();

        Button button = findViewById(R.id.button);
        button.setOnClickListener(view -> {
            accelerator();
            gyroscope();
            light();
        });
    }

    public void light() {

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        lightListener = new SensorEventListener(){

            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float newLightValue = sensorEvent.values[0];
                if (lightValue != newLightValue){
                    showToastMessage(String.valueOf(newLightValue));
                    lightValue = newLightValue;
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
        sensorManager.registerListener(lightListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void gyroscope() {
        Log.d("Wijk", "Gyroscope");
        Sensor gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        gyroListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                TextView gyroX = findViewById(R.id.gyroX);
                gyroX.setText("X: " + sensorEvent.values[0]);

                TextView gyroY = findViewById(R.id.gyroY);
                gyroY.setText("Y: " + sensorEvent.values[1]);

                TextView gyroZ = findViewById(R.id.gyroZ);
                float gyroZValue = sensorEvent.values[2];
                gyroZ.setText("Z: " + gyroZValue);
                changeImgRotation(gyroZValue);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
        sensorManager.registerListener(gyroListener, gyroSensor, SensorManager.SENSOR_DELAY_UI);
    }

    private void changeImgRotation(float newRot) {
        ImageView imgView = findViewById(R.id.imageView);
        float oldRot = imgView.getRotation();
        imgView.setRotation(oldRot + newRot * 4);
        if(newRot > 1){
            Toast.makeText(this,String.valueOf(newRot), Toast.LENGTH_LONG).show();
        }
    }

    private void accelerator() {
        Log.d("Wijk", "Accelerate");

        Sensor gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        accListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                TextView accX = findViewById(R.id.accX);
                accX.setText("X: " + sensorEvent.values[0]);

                TextView accY = findViewById(R.id.accY);
                accY.setText("Y: " + (sensorEvent.values[1] - 9.81f));

                TextView accZ = findViewById(R.id.accZ);
                accZ.setText("Z: " + sensorEvent.values[2]);

                if(sensorEvent.values[0] > 2 || sensorEvent.values[0] < -2){
                    showToastMessage("Shaken not stirred");
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
        sensorManager.registerListener(accListener, gyroSensor, SensorManager.SENSOR_DELAY_UI);
    }

    private void showToastMessage(String string) {
        Toast.makeText(this,string, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        unregisterListeners();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        unregisterListeners();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        unregisterListeners();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void unregisterListeners(){
        sensorManager.unregisterListener(accListener);
        sensorManager.unregisterListener(gyroListener);
        sensorManager.unregisterListener(lightListener);
    }
}