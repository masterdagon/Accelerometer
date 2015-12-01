package com.example.muggi.accelerometer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager accSensorManager;
    private Sensor senAccelerometer;
    private long lastUpdate = 0;
    public TextView showX_acc, showY_acc, showZ_acc,type;
    public Button button;
    private Boolean state = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        showX_acc = (TextView) findViewById(R.id.x_acc);
        showY_acc = (TextView) findViewById(R.id.y_acc);
        showZ_acc = (TextView) findViewById(R.id.z_acc);
        button = (Button) findViewById(R.id.button);
        type = (TextView)findViewById(R.id.type);

        accSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = accSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        accSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void buttonClick(View view) {
        if (state) {
            state = false;
            accSensorManager.unregisterListener(this);
            senAccelerometer = accSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
            accSensorManager.registerListener(this, senAccelerometer, accSensorManager.SENSOR_DELAY_NORMAL);
            button.setText(R.string.gravityOff);
        } else {
            state = true;
            accSensorManager.unregisterListener(this);
            senAccelerometer = accSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            accSensorManager.registerListener(this, senAccelerometer, accSensorManager.SENSOR_DELAY_NORMAL);
            button.setText(R.string.gravityOn);
        }
        makeToast("Gravity is " + state);
    }

    public void makeToast(String info) {
        Context context = getApplicationContext();
        CharSequence text = info;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float x = 0;
        float y = 0;
        float z = 0;
        Sensor mySensor = sensorEvent.sensor;
        type.setText(mySensor.getName());
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            x = sensorEvent.values[0];
            y = sensorEvent.values[1];
            z = sensorEvent.values[2];
        }else if (mySensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            x = sensorEvent.values[0];
            y = sensorEvent.values[1];
            z = sensorEvent.values[2];
        }
            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 300) {
                lastUpdate = curTime;

                if (x > 0.25 || x < 0.25) {
                    showX_acc.setText(String.valueOf(x).substring(0,4));
                } else {
                    showX_acc.setText(String.valueOf(0));
                }

                if (y > 0.25 || y < 0.25) {
                    showY_acc.setText(String.valueOf(y).substring(0,4));
                } else {
                    showY_acc.setText(String.valueOf(0));
                }
                if (z > 0.25 || z < 0.25) {
                    showZ_acc.setText(String.valueOf(z).substring(0,4));
                } else {
                    showZ_acc.setText(String.valueOf(0));
                }

            }


    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        accSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
            accSensorManager.registerListener(this, senAccelerometer, accSensorManager.SENSOR_DELAY_NORMAL);
    }
}
