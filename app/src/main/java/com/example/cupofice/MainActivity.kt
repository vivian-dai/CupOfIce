package com.example.cupofice

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import kotlin.math.abs

class MainActivity : AppCompatActivity() {
    private lateinit var sensorManager: SensorManager
    private lateinit var sensor: Sensor

    private lateinit var listener: LinearAccelListener
    private lateinit var player: SoundPool

    var sounds: IntArray = intArrayOf(0, 0, 0, 0, 0, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)

        val textView = findViewById<TextView>(R.id.text)

        // TODO: load in 6 different sounds then play on shake
        player = SoundPool.Builder().setMaxStreams(6).setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        ).build()

        sounds[0] = player.load(this@MainActivity, R.raw.shake1, 1)
        sounds[1] = player.load(this, R.raw.shake2, 1)
        sounds[2] = player.load(this, R.raw.shake3, 1)
        sounds[3] = player.load(this, R.raw.shake4, 1)
        sounds[4] = player.load(baseContext, R.raw.shake5, 1)
        sounds[5] = player.load(baseContext, R.raw.shake6, 1)

        val ok = player.load(this, R.raw.bruh, 1)

        player.setOnLoadCompleteListener(object : SoundPool.OnLoadCompleteListener {
            override fun onLoadComplete(player: SoundPool, sampleId: Int, status: Int) {
                player.play(ok, 1F, 1F, 0, 0, 1F)
                Toast.makeText(this@MainActivity, "Playing sound. . . .", Toast.LENGTH_SHORT).show()
            }
        })


        listener = LinearAccelListener(textView, sounds, player)
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
//        player = null
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

    class LinearAccelListener(textView: TextView, sArr: IntArray, p: SoundPool) : SensorEventListener {
        private var tView: TextView
        private var accels: FloatArray = floatArrayOf(0F, 0F, 0F)
        private var pool: SoundPool
        private var sounds: IntArray

        init {
            tView = textView
            pool = p
            sounds = sArr
        }

        override fun onSensorChanged(event: SensorEvent) {
            accels[0] = event.values[0]
            accels[1] = event.values[1]
            accels[2] = event.values[2]

            val sound = findPrimary(accels[0], accels[1], accels[2])
            playSound(sound)

            tView.text = findPrimary(accels[0], accels[1], accels[2]).toString()

        }

        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
            // yay do nothing
        }

        fun playSound(sound: Int) {
            pool.play(sounds[sound - 1], 1F, 1F, 0, 0, 1F)
//            Toast.makeText(this@MainActivity, "Playing sound. . . .", Toast.LENGTH_SHORT).show()
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