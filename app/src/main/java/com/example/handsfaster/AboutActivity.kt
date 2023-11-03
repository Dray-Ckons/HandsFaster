package com.example.handsfaster

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class AboutActivity : AppCompatActivity(), SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null
    private var accelerometerValuesTextView: TextView? = null
    private var shakeCount = 0
    private var lastXAxisValue = 0f
    private var previousXAxisValue = 0f

    private lateinit var shakeCountTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        // Inicializar el sensor del acelerómetro
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        // Asignar los TextView recién agregados
        accelerometerValuesTextView = findViewById(R.id.accelerometerValues)
        shakeCountTextView = findViewById(R.id.sacude_s_n)
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.let { sensor ->
            sensorManager?.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No necesitas implementar nada aquí
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val xAxis = event.values[0]
            val yAxis = event.values[1]
            val zAxis = event.values[2]

            // Verificar si hay un cambio repentino en xAxis
            if ((previousXAxisValue > 8 && xAxis <= 8) || (previousXAxisValue < -8 && xAxis >= -8)) {
                shakeCount++
                shakeCountTextView.text = "El celular se ha agitado: $shakeCount Veces"
            }

            // Actualizar el TextView con los valores del acelerómetro
            accelerometerValuesTextView?.text = "Valores del acelerómetro:\nX: $xAxis\nY: $yAxis\nZ: $zAxis"

            // Actualizar los valores previos de xAxis
            previousXAxisValue = xAxis
        }
    }
}
