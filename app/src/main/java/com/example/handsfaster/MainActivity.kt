package com.example.handsfaster

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import java.util.*

class MainActivity : AppCompatActivity(), View.OnTouchListener {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var mediaPlayerVictoria: MediaPlayer
    private lateinit var mediaPlayerDerrota: MediaPlayer
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var gestureTextView: TextView
    private var playbackSpeed = 1.0f
    private var count = 0
    private var delay: Long = 5000

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

        val handler = Handler()
        val runnable = object : Runnable {
            override fun run() {
                val random = Random().nextInt(4)
                val gesture: String = when (random) {
                    0 -> "arriba"
                    1 -> "abajo"
                    2 -> "derecha"
                    else -> "izquierda"
                }
                gestureTextView.text = gesture
                count++
                if (delay >= 1000){
                    if (count % 3 == 0 ) {
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
                    playbackSpeed = ( playbackSpeed + 0.05f)
                    mediaPlayer.playbackParams = mediaPlayer.playbackParams.setSpeed(playbackSpeed)
                    count++
                    speedHandler.postDelayed(this, 15000)
                }
            }
        }, 10000)
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        val height: Int = v?.height ?: 0
        val touchY: Float = event.y

        if (touchY < height / 2) {
            mediaPlayerVictoria.start()
        } else {
            mediaPlayerDerrota.start()
        }

        return true
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
}












