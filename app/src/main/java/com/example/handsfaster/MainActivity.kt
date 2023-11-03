package com.example.handsfaster

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import java.util.*

class MainActivity : AppCompatActivity(), View.OnTouchListener {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var mediaPlayerVictoria: MediaPlayer
    private lateinit var mediaPlayerDerrota: MediaPlayer
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var gestureTextView: TextView
    private lateinit var scoreTextView: TextView
    private var playbackSpeed = 1.0f
    private var count = 0
    private var delay: Long = 5000
    private var score = 0 // Variable global de score
    private var requiredGesture = "" // Variable para el gesto requerido
    private lateinit var gestureDetector: GestureDetector
    private var mistakeCount = 0 // Variable para contar la cantidad de errores

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar los MediaPlayers
        mediaPlayer = MediaPlayer.create(this, R.raw.musica_de_fondo)
        mediaPlayer.isLooping = true
        mediaPlayer.start()
        mediaPlayerVictoria = MediaPlayer.create(this, R.raw.sonido_victoria)
        mediaPlayerDerrota = MediaPlayer.create(this, R.raw.sonido_derrota)

        // Obtener el volumen de las SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val musicVolume = sharedPreferences.getInt("music_volume", 50)
        val effectsVolume = sharedPreferences.getInt("effects_volume", 50)

        // Ajustar el volumen de los MediaPlayers
        setVolume(mediaPlayer, musicVolume)
        setVolume(mediaPlayerVictoria, effectsVolume)
        setVolume(mediaPlayerDerrota, effectsVolume)

        // Establecer el listener onTouch para la detección de toques
        val view = findViewById<View>(R.id.frameLayout)
        view.setOnTouchListener(this)

        // Implementación de la lógica del segundo código
        gestureTextView = findViewById(R.id.gestureTextView)
        scoreTextView = findViewById(R.id.scoreTextView)

        val handler = Handler()
        val runnable = object : Runnable {
            override fun run() {
                val random = Random().nextInt(5)
                requiredGesture = when (random) {
                    0 -> "onScroll"
                    1 -> "onSingleTapUp"
                    2 -> "onLongPress"
                    3 -> "onFling"
                    else -> "onDragRight"
                }
                gestureTextView.text = requiredGesture
                count++
                if (delay >= 1000) {
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

        // Incrementar la velocidad cada 10 segundos
        val speedHandler = Handler()
        speedHandler.postDelayed(object : Runnable {
            override fun run() {
                if (count < 8) {
                    playbackSpeed = (playbackSpeed + 0.05f)
                    mediaPlayer.playbackParams = mediaPlayer.playbackParams.setSpeed(playbackSpeed)
                    count++
                    speedHandler.postDelayed(this, 15000)
                }
            }
        }, 10000)

        // Inicializar el detector de gestos
        gestureDetector = GestureDetector(this, GestureListener())
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        return true
    }

    inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            showToast("onSingleTapUp")
            checkGesture("onSingleTapUp")
            return true
        }

        override fun onLongPress(e: MotionEvent) {
            showToast("onLongPress")
            checkGesture("onLongPress")
        }

        override fun onFling(
            e1: MotionEvent?, e2: MotionEvent,
            velocityX: Float, velocityY: Float
        ): Boolean {
            showToast("onFling")
            checkGesture("onFling")
            return true
        }

        override fun onScroll(
            e1: MotionEvent?, e2: MotionEvent,
            distanceX: Float, distanceY: Float
        ): Boolean {
            when (requiredGesture) {
                "onScroll" -> {
                    if (distanceY > 0) {
                        showToast("onScroll")
                        checkGesture("onScroll")
                    }
                }
                "onSingleTapUp" -> {
                    if (distanceY < 0) {
                        showToast("onSingleTapUp")
                        checkGesture("onSingleTapUp")
                    }
                }
                "onLongPress" -> {
                    if (distanceX > 0) {
                        showToast("onLongPress")
                        checkGesture("onLongPress")
                    }
                }
                "onFling" -> {
                    if (distanceX < 0) {
                        showToast("onFling")
                        checkGesture("onFling")
                    }
                }
                "onDragRight" -> {
                    if (distanceX > 0) {
                        showToast("onDragRight")
                        checkGesture("onDragRight")
                    }
                }
            }
            return true
        }
    }

    private fun checkGesture(gesture: String) {
        if (gesture == requiredGesture) {
            score++ // Incrementa el score en 1 si el gesto es correcto
            mediaPlayerVictoria.start() // Reproduce el sonido de victoria
        } else {
            mistakeCount++ // Aumenta el contador de errores
            score -= 2 // Decrementa el score en 2 si el gesto es incorrecto

            if (mistakeCount >= 5) {
                Toast.makeText(this, "Has cometido 5 errores. Reiniciando...", Toast.LENGTH_SHORT).show()
                val intent = intent
                finish()
                startActivity(intent) // Reiniciar la actividad
            }
            if (score < 0) {
                // Asegúrate de que el score nunca sea negativo
                score = 0

                mediaPlayerDerrota.start() // Reproduce el sonido de derrota

            } else {
                mediaPlayerDerrota.start() // Reproduce el sonido de derrota
            }
        }
        // Actualiza el valor de scoreTextView
        scoreTextView.text = "Score: $score"
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer.start()
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        mediaPlayerVictoria.release()
        mediaPlayerDerrota.release()
    }

    private fun setVolume(mediaPlayer: MediaPlayer, volume: Int) {
        val volumeFloat = volume / 100.0f
        mediaPlayer.setVolume(volumeFloat, volumeFloat)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}










