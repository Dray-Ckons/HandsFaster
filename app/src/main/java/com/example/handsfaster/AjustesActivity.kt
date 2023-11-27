package com.example.handsfaster

import android.content.Context
import android.content.SharedPreferences
import android.health.connect.datatypes.units.Velocity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AjustesActivity : AppCompatActivity() {

    companion object {
        const val PREFS_NAME = "HandsFasterPrefs"
        const val MUSIC_VOLUME_KEY = "musicVolume"
        const val EFFECTS_VOLUME_KEY = "effectsVolume"
        const val DIFFICULTY_KEY = "selectedDifficulty"
        const val VELOCITY_KEY = "velocity"
        const val IMPRESION_KEY = "impresion"
    }

    private var musicVolume = 50
    private var effectsVolume = 50
    private var selectedDifficulty = "Gradual"
    private var velocity = 1.0f
    private var impresion = 5000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajustes)

        val musicVolumeSeekBar: SeekBar = findViewById(R.id.musicVolumeSeekBar)
        val effectsVolumeSeekBar: SeekBar = findViewById(R.id.effectsVolumeSeekBar)
        val difficultyRadioGroup: RadioGroup = findViewById(R.id.difficultyRadioGroup)

        val preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        musicVolume = preferences.getInt(MUSIC_VOLUME_KEY, 50)
        effectsVolume = preferences.getInt(EFFECTS_VOLUME_KEY, 50)
        selectedDifficulty = preferences.getString(DIFFICULTY_KEY, "Gradual") ?: "Gradual"

        // Configurar los valores iniciales de las preferencias
        musicVolumeSeekBar.progress = musicVolume
        effectsVolumeSeekBar.progress = effectsVolume

        when (selectedDifficulty) {
            "Gradual" -> {
                difficultyRadioGroup.check(R.id.radioButtonGradual)
                impresion = 5000
                velocity = 1.0f
            }
            "Fácil" -> {
                difficultyRadioGroup.check(R.id.radioButtonEasy)
                impresion = 5000
                velocity = 0.8f
            }
            "Media" -> {
                difficultyRadioGroup.check(R.id.radioButtonMedium)
                impresion = 3500
                velocity = 1.2f
            }
            "Difícil" -> {
                difficultyRadioGroup.check(R.id.radioButtonHard)
                impresion = 2500
                velocity = 1.4f
            }
        }

        val textViewMusicVolumeNum: TextView = findViewById(R.id.textViewMusicVolumeNum)
        val textViewEffectsVolumeNum: TextView = findViewById(R.id.textViewEfectVolumeNum)

        // Configurar los valores iniciales de los TextViews
        textViewMusicVolumeNum.text = musicVolume.toString()
        textViewEffectsVolumeNum.text = effectsVolume.toString()

        musicVolumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                musicVolume = progress
                textViewMusicVolumeNum.text = progress.toString() // Actualizar el TextView
                savePreferences()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // No es necesario implementar en este caso
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // No es necesario implementar en este caso
            }
        })

        effectsVolumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                effectsVolume = progress
                textViewEffectsVolumeNum.text = progress.toString() // Actualizar el TextView
                savePreferences()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // No es necesario implementar en este caso
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // No es necesario implementar en este caso
            }
        })

        difficultyRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            val radioButton: RadioButton = findViewById(checkedId)
            selectedDifficulty = radioButton.text.toString()
            savePreferences()
        }

        // Resto del código...
    }

    private fun savePreferences() {
        val preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putInt(MUSIC_VOLUME_KEY, musicVolume)
        editor.putInt(EFFECTS_VOLUME_KEY, effectsVolume)
        editor.putString(DIFFICULTY_KEY, selectedDifficulty)
        editor.putFloat(VELOCITY_KEY, velocity)
        editor.putLong(IMPRESION_KEY, impresion.toLong())
        editor.apply()
    }
}
