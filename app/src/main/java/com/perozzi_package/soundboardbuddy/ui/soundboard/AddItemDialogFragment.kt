package com.perozzi_package.soundboardbuddy.ui.soundboard

import android.app.Activity.MODE_PRIVATE
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.navigation.NavController
import com.perozzi_package.soundboardbuddy.data.db.entities.SoundboardItem
import kotlinx.android.synthetic.main.fragment_add_item_dialog.*
import soundboardbuddy.R
import soundboardbuddy.databinding.FragmentAddItemDialogBinding
import java.io.File
import java.io.IOException

class AddItemDialogFragment(context: Context, private var addDialogListener: AddDialogListener) :
    AppCompatDialogFragment() {

    private lateinit var binding: FragmentAddItemDialogBinding
    private lateinit var navController: NavController
    private lateinit var addItemDialogViewModel: AddItemDialogViewModel
    private lateinit var selectedAudioUri: Uri
    private lateinit var mp: MediaPlayer
    private lateinit var mediaPlayerForPresets: MediaPlayer
    private lateinit var mr: MediaRecorder
    private lateinit var audioPath: File
    private lateinit var presetPath: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentAddItemDialogBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addItemDialogViewModel = AddItemDialogViewModel()
        binding.addItemDialogViewModel = addItemDialogViewModel

        val sdCardPath = (requireActivity()).filesDir
        val pathForAllAudioFiles = sdCardPath.absolutePath + "/.soundboard_buddy_audio_files"

        // val pathForAllAudioFiles = requireContext().getExternalFilesDir("")?.absolutePath + "/.soundboard_buddy_audio_files"
        val fileForAllAudioFiles = File(pathForAllAudioFiles)

        // val recordingPath = context?.getExternalFilesDir(null)?.absolutePath + "latest_recording.mp3"
        val latestRecordingPath =
            requireContext().getExternalFilesDir("")?.absolutePath + "/latest_recording.mp3"
        val pathUri = Uri.parse(latestRecordingPath)
        mp = MediaPlayer()
        mr = MediaRecorder()
        initializeMediaRecorder(mr)
        mr.setOutputFile(latestRecordingPath)

        uploadRecordRadioGroup.setOnCheckedChangeListener { _, id ->
            addItemDialogViewModel.buttonColor.observe(viewLifecycleOwner, { buttonColor ->

            })
            when (id) {
                R.id.uploadRadioButton -> {
                    mp.reset()
                    confirmButton.isEnabled = false
                    addItemDialogViewModel.buttonColor.value = R.drawable.play_button_blue
                    addItemDialogViewModel.typeOfAudioSelected.value = "Upload"
                    Toast.makeText(context, "Upload time", Toast.LENGTH_SHORT).show()
                }
                R.id.recordRadioButton -> {
                    mp.reset()
                    confirmButton.isEnabled = false
                    addItemDialogViewModel.buttonColor.value = R.drawable.play_button_red
                    addItemDialogViewModel.typeOfAudioSelected.value = "Record"
                    Toast.makeText(context, "Recording time", Toast.LENGTH_SHORT).show()
                }
                R.id.presetRadioButton -> {
                    mp.reset()
                    confirmButton.isEnabled = true
                    addItemDialogViewModel.buttonColor.value = R.drawable.play_button_black
                    addItemDialogViewModel.typeOfAudioSelected.value = "Preset"
                    Toast.makeText(context, "Preset time", Toast.LENGTH_SHORT).show()
                }
                else -> {
                }
            }
        }

        uploadAudioButton.setOnClickListener {
            startFileChooser()
        }


        // val recording = mediaPlayer.setDataSource(recordingPath)

        // Starts the recording
        startRecordAudioButton.setOnClickListener {
            mr.prepare()
            mr.start()
            startRecordAudioButton.isEnabled = false
            stopRecordAudioButton.isEnabled = true
            previewRecordAudioButton.isEnabled = false
            resetRecordAudioButton.isEnabled = false
        }

        // Stops the recording
        stopRecordAudioButton.setOnClickListener {
            mr.stop()
            startRecordAudioButton.isEnabled = false
            stopRecordAudioButton.isEnabled = false
            previewRecordAudioButton.isEnabled = true
            resetRecordAudioButton.isEnabled = true
            confirmButton.isEnabled = true
        }

        // Plays back the recording
        previewRecordAudioButton.setOnClickListener {
            mp.reset()
            mp.setDataSource(latestRecordingPath)
            mp.prepare()
            mp.start()
        }

        // Resets the recording
        resetRecordAudioButton.setOnClickListener {
            mp.reset()
            mr.setAudioSource(MediaRecorder.AudioSource.MIC)
            mr.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            mr.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB)
            mr.setOutputFile(latestRecordingPath)
            startRecordAudioButton.isEnabled = true
            stopRecordAudioButton.isEnabled = false
            previewRecordAudioButton.isEnabled = false
            resetRecordAudioButton.isEnabled = false
            confirmButton.isEnabled = false
        }

        presetSoundsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                // Currently doing by index; may do by string later
                addItemDialogViewModel.selectedPresetAudioIndex.value =
                        /*addItemDialogViewModel.presetSpinnerOptions[]*/position
                // presetPath = "app/src/main/res/raw/" + addItemDialogViewModel.selectedPresetAudio
                addItemDialogViewModel.selectedPresetAudio.observe(
                    viewLifecycleOwner,
                    { selection ->
                        mediaPlayerForPresets = MediaPlayer.create(requireContext(), selection)
                    })
                // mediaPlayer.setDataSource(presetPath)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        presetPreviewButton.setOnClickListener {

            mediaPlayerForPresets.start()
        }


        confirmButton.setOnClickListener {
            val name = editTextSoundName.text.toString()
            if (name.isEmpty()) {
                Toast.makeText(context, "Please give your sound a name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // var mydir = requireContext().getDir("media", MODE_PRIVATE)
            if (!fileForAllAudioFiles.exists()) {
                fileForAllAudioFiles.mkdir()
            }

            var confirmedAudioPath = pathForAllAudioFiles + "/$name"
            if (File(confirmedAudioPath).exists()) {
                Toast.makeText(
                    context,
                    "A sound with this name exists. Please give your sound a unique name.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            var confirmedAudioFile = File(confirmedAudioPath)

            val colorAddress = addItemDialogViewModel.buttonColor.value!! as Int

            val item = SoundboardItem(name, confirmedAudioPath, colorAddress)
            addDialogListener.onAddButtonClicked(item)
            dismiss()
        }

        cancelButton.setOnClickListener {
            dismiss()
        }
    }

    fun initializeMediaRecorder(mediaRecorder: MediaRecorder): Boolean {
        mediaRecorder.reset()
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB)
        return true
    }

    fun saveAudioToInternalStorage(filename: String, audio: /*TODO()*/Any ): Boolean {
        return try {
            requireActivity().openFileOutput("${editTextSoundName.text}", MODE_PRIVATE)
            true
        } catch(e: IOException) {
            e.printStackTrace()
            false
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            startRecordAudioButton.isEnabled = true
    }

    private fun startFileChooser() {
        val intent = Intent()
        intent.type = "audio/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            selectedAudioUri = data?.data!!
        }
        // addItemDialogViewModel.uploadedFileName.value = selectedAudioUri.queryParameterNames
    }
}
