package com.example.cupofice

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    lateinit var sensorManager: SensorManager;
    lateinit var sensor: Sensor;

    lateinit var listener: LinearAccelListener;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager;
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        val textViewX = findViewById<TextView>(R.id.x_text);
        val textViewY = findViewById<TextView>(R.id.y_text);
        val textViewZ = findViewById<TextView>(R.id.z_text);

        listener = LinearAccelListener(arrayOf(textViewX, textViewY, textViewZ));

    }

    override fun onResume() {
        super.onResume();
//        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        // ???? literally why does this not work when this is here
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(listener);
    }

    class LinearAccelListener(textViewsA: Array<TextView>) : SensorEventListener {
        lateinit var textViews: Array<TextView>;
        lateinit var accels: FloatArray;

        init {
            textViews = textViewsA;
            accels = floatArrayOf(0F, 0F, 0F);
        }

        override fun onSensorChanged(event: SensorEvent) {
            accels[0] = event.values[0];
            accels[1] = event.values[1];
            accels[2] = event.values[2];

            textViews[0].text = accels[0].toString();
            textViews[1].text = accels[1].toString();
            textViews[2].text = accels[2].toString();
        }

        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
            TODO("Not yet implemented")
            // yay
        }
    }

}