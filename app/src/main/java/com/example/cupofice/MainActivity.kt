package com.example.cupofice

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlin.math.abs

class MainActivity : AppCompatActivity() {
    private lateinit var sensorManager: SensorManager
    private lateinit var sensor: Sensor

    private lateinit var listener: LinearAccelListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)

        val textView = findViewById<TextView>(R.id.text)

        listener = LinearAccelListener(textView)
        // TODO: load in 6 different sounds then play on shake

    }

    override fun onResume() {
        super.onResume()
        sensorManager.unregisterListener(listener)
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(listener)
    }

    class LinearAccelListener(textView: TextView) : SensorEventListener {
        private var tView: TextView
        private var accels: FloatArray = floatArrayOf(0F, 0F, 0F)

        init {
            tView = textView
        }

        override fun onSensorChanged(event: SensorEvent) {
            accels[0] = event.values[0]
            accels[1] = event.values[1]
            accels[2] = event.values[2]

            tView.text = findPrimary(accels[0], accels[1], accels[2]).toString()
        }

        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
            // yay do nothing
        }
    }

}

fun findPrimary(a: Float, b: Float, c: Float): Int {
    var ret: Int
    if (abs(a) > abs(b) && abs(a) > abs(c)) {
        ret = 1
        if(a < 0) {
            ret++
        }
    } else  if(abs(b) > abs(a) && abs(b) > abs(c)) {
        ret = 3
        if(b < 0) {
            ret++
        }
    } else {
        ret = 5
        if (c < 0) {
            ret++
        }
    }
    return ret
}