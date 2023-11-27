package com.example.handsfaster

import android.content.Context
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity(), View.OnTouchListener, SensorEventListener {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var mediaPlayerVictoria: MediaPlayer
    private lateinit var mediaPlayerDerrota: MediaPlayer
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var gestureTextView: TextView
    private lateinit var scoreTextView: TextView
    private lateinit var topScoreTextView: TextView
    private lateinit var NivelDeDificultadTextView: TextView
    private lateinit var bienMalImageView: ImageView

    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null
    private var shakeCount = 0
    private var lastXAxisValue = 0f
    private var previousXAxisValue = 0f

    private var playbackSpeed = 1.0f
    private var count = 0
    private var delay: Long = 5000
    private var score = 0
    private var requiredGesture = ""
    private lateinit var gestureDetector: GestureDetector
    private var mistakeCount = 0
    private var dificultad = ""

    companion object {
        const val TOP_SCORE_KEY = "top_score"
        lateinit var mainActivity: MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar el sensor del acelerómetro
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        // Inicializar los MediaPlayers y otros elementos
        mediaPlayer = MediaPlayer.create(this, R.raw.musica_de_fondo)
        mediaPlayer.isLooping = true
        mediaPlayerVictoria = MediaPlayer.create(this, R.raw.sonido_victoria)
        mediaPlayerDerrota = MediaPlayer.create(this, R.raw.sonido_derrota)
        bienMalImageView = findViewById(R.id.bien_mal)

        // Obtener las SharedPreferences
        sharedPreferences = getSharedPreferences("HandsFasterPrefs", Context.MODE_PRIVATE)

        // Obtener el puntaje más alto y mostrarlo en el TextView
        topScoreTextView = findViewById(R.id.topScoreTextView)
        val topScore = sharedPreferences.getInt(TOP_SCORE_KEY, 0)
        topScoreTextView.text = "Top Score: $topScore"
        NivelDeDificultadTextView = findViewById(R.id.NivelDeDificultadTextView)




        // Inicializar el listener onTouch para la detección de toques
        val view = findViewById<View>(R.id.frameLayout)
        view.setOnTouchListener(this)

        // Implementación de la lógica del Gesture Detector en pantalla
        gestureTextView = findViewById(R.id.gestureTextView)
        scoreTextView = findViewById(R.id.scoreTextView)



        // Inicializar el detector de gestos
        gestureDetector = GestureDetector(this, GestureListener())

        // Implementación del sensor de acelerómetro
        accelerometer?.let { sensor ->
            sensorManager?.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }

        val musicVolume = sharedPreferences.getInt("musicVolume", 50)
        val effectsVolume = sharedPreferences.getInt("effectsVolume", 50)

// Configurar volumen de MediaPlayer según preferencias
        setVolume(mediaPlayer, musicVolume)
        setVolume(mediaPlayerVictoria, effectsVolume)
        setVolume(mediaPlayerDerrota, effectsVolume)


// Actualiza el TextView textViewMusicVolumeNum con el valor del volumen de música
        val textViewMusicVolumeNum = findViewById<TextView>(R.id.textViewMusicVolumeNum)
        textViewMusicVolumeNum.text = musicVolume.toString()

// Actualiza el TextView textViewEffectsVolumeNum con el valor del volumen de efectos
        val textViewEffectsVolumeNum = findViewById<TextView>(R.id.textViewEffectsVolumeNum)
        textViewEffectsVolumeNum.text = effectsVolume.toString()



        dificultad = sharedPreferences.getString("selectedDifficulty", "Gradual") ?: "Gradual"
        println(dificultad)
        NivelDeDificultadTextView.text = dificultad

        // Inicializar el detector de gestos
        gestureDetector = GestureDetector(this, GestureListener())

        // Implementación del sensor de acelerómetro
        accelerometer?.let { sensor ->
            sensorManager?.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }

        // En la función onCreate
        playbackSpeed = sharedPreferences.getFloat(AjustesActivity.VELOCITY_KEY, 1.0f)
        delay = sharedPreferences.getLong(AjustesActivity.IMPRESION_KEY, 5000)

        val speedHandler = Handler()

        // En la función onCreate
        speedHandler.postDelayed(object : Runnable {
            override fun run() {
                if (count < 8 && dificultad == "Gradual") {
                    playbackSpeed += 0.05f
                    mediaPlayer.playbackParams = mediaPlayer.playbackParams.setSpeed(playbackSpeed)

                    speedHandler.postDelayed(this, 15000)
                }
            }
        }, 15000)



        val handler = Handler()
        val runnable = object : Runnable {
            override fun run() {
                bienMalImageView.setImageResource(R.drawable.imagen_inicial)
                val random = Random().nextInt(4)
                requiredGesture = when (random) {
                    0 -> "Agitar"
                    1 -> "Un toque"
                    2 -> "Mantener el toque"
                    else -> "Desliza a la Izquierda"
                }
                gestureTextView.text = requiredGesture
                count++

                if (delay >= 1500 && dificultad == "Gradual") {
                    if (count % 3 == 0) {
                        delay = (delay - 500).toLong()
                    }
                }

                val halfDelay = delay / 2

                val secondHandler = Handler()
                val secondRunnable = object : Runnable {
                    override fun run() {
                        gestureTextView.text = ""
                    }
                }
                secondHandler.postDelayed(secondRunnable, halfDelay)

                handler.postDelayed(this, delay)
            }
        }
        handler.post(runnable)

        // Inicializar el detector de gestos
        gestureDetector = GestureDetector(this, GestureListener())
        // Implementación del sensor de acelerómetro
        accelerometer?.let { sensor ->
            sensorManager?.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    //////////////////////// Gesture Detector ////////////////

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        return true
    }

    inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            showToast("Un toque")
            checkGesture("Un toque")
            return true
        }

        override fun onLongPress(e: MotionEvent) {
            showToast("Mantener el toque")
            checkGesture("Mantener el toque")
        }

        override fun onFling(
            e1: MotionEvent?, e2: MotionEvent,
            velocityX: Float, velocityY: Float
        ): Boolean {
            showToast("Desliza a la Izquierda")
            checkGesture("Desliza a la Izquierda")
            return true
        }

        override fun onScroll(
            e1: MotionEvent?, e2: MotionEvent,
            distanceX: Float, distanceY: Float
        ): Boolean {
            when (requiredGesture) {
                "Un toque" -> {
                    if (distanceY < 0) {
                        showToast("Un toque")
                        checkGesture("Un toque")
                    }
                }
                "Mantener el toque" -> {
                    if (distanceX > 0) {
                        showToast("Mantener el toque")
                        checkGesture("Mantener el toque")
                    }
                }
                "Desliza a la Izquierda" -> {
                    if (distanceX < 0) {
                        showToast("Desliza a la Izquierda")
                        checkGesture("Desliza a la Izquierda")
                    }
                }
            }
            return true
        }
    }


    /////////////////// Verificador de Gestos y Puntaje ////////////////////////

    private fun checkGesture(gesture: String) {
        if (gesture == requiredGesture) {
            score++
            bienMalImageView.setImageResource(R.drawable.imagen_bien)
            mediaPlayerVictoria.start()
        } else {
            mistakeCount++
            bienMalImageView.setImageResource(R.drawable.imagen_mal)
            score -= 2

            if (mistakeCount >= 5) {
                Toast.makeText(this, "Has cometido 5 errores. Reiniciando...", Toast.LENGTH_SHORT).show()
                val intent = intent
                finish()
                startActivity(intent)
            }
            if (score < 0) {
                score = 0
                mediaPlayerDerrota.start()
            } else {
                mediaPlayerDerrota.start()
            }
        }
        scoreTextView.text = "Score: $score"

        val topScore = sharedPreferences.getInt(TOP_SCORE_KEY, 0)
        if (score > topScore) {
            bienMalImageView.setImageResource(R.drawable.imagen_record)
            with(sharedPreferences.edit()) {
                putInt(TOP_SCORE_KEY, score)
                apply()
            }
        }

        val newTopScore = sharedPreferences.getInt(TOP_SCORE_KEY, 0)
        topScoreTextView.text = "Top Score: $newTopScore"
    }

    /////////////// funciones adicionales mediaplayer /////////////

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No necesitas implementar nada aquí
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val xAxis = event.values[0]

            if ((previousXAxisValue > 8 && xAxis <= 8) || (previousXAxisValue < -8 && xAxis >= -8)) {
                if (requiredGesture == "Agitar") {
                    score++
                    bienMalImageView.setImageResource(R.drawable.imagen_bien)
                    mediaPlayerVictoria.start()
                    showToast("Agito Bien")
                    scoreTextView.text = "Score: $score"

                    val topScore = sharedPreferences.getInt(TOP_SCORE_KEY, 0)
                    if (score > topScore) {
                        bienMalImageView.setImageResource(R.drawable.imagen_record)
                        with(sharedPreferences.edit()) {
                            putInt(TOP_SCORE_KEY, score)
                            apply()
                        }
                    }

                    val newTopScore = sharedPreferences.getInt(TOP_SCORE_KEY, 0)
                    topScoreTextView.text = "Top Score: $newTopScore"
                } else if (score < 0) {
                    score = 0
                    mediaPlayerDerrota.start()
                } else {
                    mistakeCount++
                    bienMalImageView.setImageResource(R.drawable.imagen_mal)
                    score -= 2
                    showToast("agito mal")
                    mediaPlayerDerrota.start()
                    scoreTextView.text = "Score: $score"
                }
                showToast("agito")
            }
            previousXAxisValue = xAxis
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setVolume(mediaPlayer: MediaPlayer, volume: Int) {
        val volumeFloat = volume / 100f
        mediaPlayer.setVolume(volumeFloat, volumeFloat)
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer.start()
        accelerometer?.let { sensor ->
            sensorManager?.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.pause()
        sensorManager?.unregisterListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        mediaPlayerVictoria.release()
        mediaPlayerDerrota.release()
    }
}






