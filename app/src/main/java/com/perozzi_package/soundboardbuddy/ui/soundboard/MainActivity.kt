package com.perozzi_package.soundboardbuddy.ui.soundboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import soundboardbuddy.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() { // KodeinAware {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}