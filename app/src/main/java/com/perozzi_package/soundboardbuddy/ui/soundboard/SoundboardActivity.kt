package com.perozzi_package.soundboardbuddy.ui.soundboard

import android.annotation.SuppressLint
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.SeekBar
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.perozzi_package.soundboardbuddy.R
import com.perozzi_package.soundboardbuddy.data.db.SoundDatabase
import com.perozzi_package.soundboardbuddy.data.db.entities.SoundGridItem
import com.perozzi_package.soundboardbuddy.data.repositories.SoundRepository
import com.perozzi_package.soundboardbuddy.databinding.ActivityMainBinding
import com.perozzi_package.soundboardbuddy.databinding.DialogAddSoundBinding
import com.perozzi_package.soundboardbuddy.other.SoundItemAdapter
import kotlinx.android.synthetic.main.activity_main.*

class SoundboardActivity : AppCompatActivity() { // KodeinAware {

    // These two lines should replace the three lines in onCreate, but error is thrown:
    // android.app.Application cannot be cast to org.kodein.di.KodeinAware
    //
    // override val kodein by kodein()
    // private val factory: SoundViewModelFactory by instance()

    lateinit var audioManager: AudioManager

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val soundboardActivityViewModel = SoundboardActivityViewModel()

        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager


        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        binding.soundboardActivityViewModel = soundboardActivityViewModel
        setContentView(binding.root)
        val volumeAdjustmentBar = binding.volumeAdjustmentBar
        val volumeAdjustmentText = binding.volumeAdjustmentText
        volumeAdjustmentBar.max = /*soundboardActivityViewModel.*/maxVolume
        volumeAdjustmentBar.progress = /*soundboardActivityViewModel.*/currentVolume
        volumeAdjustmentText.text = resources.getString(
            R.string.volume_bar_text,/*soundboardActivityViewModel.*/
            currentVolume.toString(),/*soundboardActivityViewModel.*/
            maxVolume.toString()
        )
        volumeAdjustmentBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                volumeAdjustmentText.text = resources.getString(
                    R.string.volume_bar_text,
                    progress.toString(),/*soundboardActivityViewModel.*/
                    maxVolume.toString()
                )
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })


        // these need to be moved, but something went wrong when trying to implement
        // dependency injection so these are just here for now
        // https://www.youtube.com/watch?v=8Pl1EVgenkg
        val database = SoundDatabase(this)
        val repository = SoundRepository(database)
        val factory = SoundViewModelFactory(repository)

        val viewModel = ViewModelProvider(this, factory).get(SoundViewModel::class.java)

        val adapter = SoundItemAdapter(listOf(), listOf(), /*listOf(), */viewModel)

        soundboardRecyclerView.layoutManager =
            GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false)
        soundboardRecyclerView.adapter = adapter

        viewModel.getAllSoundItems().observe(this, Observer {
            adapter.items = it
            adapter.sounds = listOf(MediaPlayer.create(this, R.raw.taco_bell)) // TODO()
            //adapter.colors = listOf(6)
            adapter.notifyDataSetChanged()
        })

        floatingAddButton.setOnClickListener {
            AddSoundItemDialog(this, object : AddDialogListener {
                override fun onAddButtonClicked(item: SoundGridItem) {
                    viewModel.upsert(item)
                }
            }).show()
        }
    }
}