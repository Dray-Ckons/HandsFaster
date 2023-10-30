package com.example.handsfaster

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity

class PreferencesActivity : AppCompatActivity() {
    private lateinit var musicVolumeSeekBar: SeekBar
    private lateinit var effectsVolumeSeekBar: SeekBar
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        musicVolumeSeekBar = findViewById(R.id.musicVolumeSeekBar)
        effectsVolumeSeekBar = findViewById(R.id.effectsVolumeSeekBar)
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        musicVolumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                saveMusicVolume(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        effectsVolumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                saveEffectsVolume(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    private fun saveMusicVolume(volume: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("music_volume", volume)
        editor.apply()
    }

    private fun saveEffectsVolume(volume: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("effects_volume", volume)
        editor.apply()
    }
}

