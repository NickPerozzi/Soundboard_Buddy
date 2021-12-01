package com.perozzi_package.soundboardbuddy.ui.soundboard

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.graphics.Bitmap
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.perozzi_package.soundboardbuddy.data.db.SoundDatabase
import com.perozzi_package.soundboardbuddy.data.db.entities.SoundboardItem
import com.perozzi_package.soundboardbuddy.data.repositories.SoundRepository
import com.perozzi_package.soundboardbuddy.other.SoundboardAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_soundboard.*
import soundboardbuddy.R
import soundboardbuddy.databinding.FragmentSoundboardBinding
import java.io.IOException

class SoundboardFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var binding: FragmentSoundboardBinding
    private lateinit var soundboardViewModel: SoundboardViewModel

    // These two lines should replace the three lines in onCreate, but error is thrown:
    // android.app.Application cannot be cast to org.kodein.di.KodeinAware
    //
    // override val kodein by kodein()
    // private val factory: SoundViewModelFactory by instance()

    lateinit var audioManager: AudioManager

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSoundboardBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()


        soundboardViewModel = SoundboardViewModel()
        binding.soundboardViewModel = soundboardViewModel

        audioManager = (requireActivity() as AppCompatActivity).getSystemService(AppCompatActivity.AUDIO_SERVICE) as AudioManager

        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

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
        val database = context?.let { SoundDatabase(it) } as SoundDatabase
        val repository = SoundRepository(database)
        val factory = SoundViewModelFactory(repository)

        val viewModel = ViewModelProvider(this, factory).get(SoundViewModel::class.java)

        val listAdapter = SoundboardAdapter()
        soundboardRecyclerView.adapter = listAdapter
        soundboardRecyclerView.layoutManager =
            GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)

/*
        soundboardViewModel.arrayForSoundboard.observe(viewLifecycleOwner, {data ->
            listAdapter.submitList(data)
        })
*/

        viewModel.getAllSoundItems().observe(viewLifecycleOwner, { listOfSoundboardItems ->
            var x = R.raw.taco_bell
/*
            listAdapter.currentList = listOfSoundboardItems
            listAdapter.sounds = listOf(MediaPlayer.create(context, R.raw.taco_bell)) // TODO()
            listAdapter.colors =
            listAdapter.notifyDataSetChanged()
*/
        })

        floatingAddButton.setOnClickListener{
            navController.navigate(R.id.action_soundboardFragment_to_addItemDialogFragment)
        }

        val fragmentManager = (requireActivity() as AppCompatActivity).supportFragmentManager

        floatingAddButton.setOnClickListener {
            context?.let { context ->
                AddItemDialogFragment(context, object : AddDialogListener {
                    override fun onAddButtonClicked(item: SoundboardItem) {
                        viewModel.upsert(item)
                    }
                }).show(fragmentManager,"My fragment")
            }
        }

/*
        private fun saveAudioToInternalStorage(filename: String, mediaPlayer: MediaPlayer): Boolean {
            return try {
                requireActivity().openFileOutput("$filename", MODE_PRIVATE).use { stream ->
                    if(!mediaPlayer.)
                }
            } catch(e: IOException) {
                e.printStackTrace()
                false
            }
        }
*/
    }
}