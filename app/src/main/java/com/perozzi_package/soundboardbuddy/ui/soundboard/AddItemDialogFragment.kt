package com.perozzi_package.soundboardbuddy.ui.soundboard

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.perozzi_package.soundboardbuddy.data.db.entities.SoundGridItem
import kotlinx.android.synthetic.main.fragment_add_item_dialog.*
import soundboardbuddy.R
import soundboardbuddy.databinding.FragmentAddItemDialogBinding
import java.io.File

class AddItemDialogFragment(context: Context, private var addDialogListener: AddDialogListener) :
    AppCompatDialogFragment() {

    private lateinit var navController: NavController
    private lateinit var binding: FragmentAddItemDialogBinding
    private lateinit var addItemDialogViewModel: AddItemDialogViewModel
    private lateinit var mediaRecorder: MediaRecorder

    private lateinit var audioUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
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

        startRecordAudioButton.isEnabled = true /*false*/
        stopRecordAudioButton.isEnabled = false
        previewRecordAudioButton.isEnabled = false

        if (ActivityCompat.checkSelfPermission(
                (requireActivity() as AppCompatActivity),
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                (requireActivity() as AppCompatActivity),
                arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), 111
            )
            startRecordAudioButton.isEnabled = true
        }

        // val uploadRadioButton = binding.uploadRadioButton
        // val recordRadioButton = binding.recordRadioButton
        uploadRadioButton.isSelected = true
        recordRadioButton.isSelected = false
        // val recordButton = binding.recordAudioButton
        // val rerecordButton = binding.rerecordAudioButton

        uploadRecordRadioGroup.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.uploadRadioButton -> {
                    addItemDialogViewModel.recordSelectedNotUpload.value = false
                    addItemDialogViewModel.recordSelectedNotUpload.postValue(false)
                    Toast.makeText(context, "Upload time", Toast.LENGTH_SHORT).show()
                    uploadAudioButton.visibility = View.VISIBLE
                    startRecordAudioButton.visibility = View.GONE
                    stopRecordAudioButton.visibility = View.GONE
                }
                R.id.recordRadioButton -> {
                    addItemDialogViewModel.recordSelectedNotUpload.value = true
                    Toast.makeText(context, "Recording time", Toast.LENGTH_SHORT).show()
                    uploadAudioButton.visibility = View.GONE
                    startRecordAudioButton.visibility = View.VISIBLE
                    stopRecordAudioButton.visibility = View.VISIBLE
                }
                else -> {
                }
            }
        }

        uploadAudioButton.setOnClickListener {
            startFileChooser()
        }

        // val recordingPath = context?.getExternalFilesDir(null)?.absolutePath + "latest_recording.mp3"
        val recordingPath = requireContext().getExternalFilesDir("")?.absolutePath + "latest_recording.mp3"
        val mediaPlayer = MediaPlayer()
        mediaRecorder = MediaRecorder()
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB)
        mediaRecorder.setOutputFile(recordingPath)

        // val recording = mediaPlayer.setDataSource(recordingPath)

        // Starts the recording
        startRecordAudioButton.setOnClickListener {
            mediaRecorder.prepare()
            mediaRecorder.start()
            startRecordAudioButton.isEnabled = false
            stopRecordAudioButton.isEnabled = true
            previewRecordAudioButton.isEnabled = false
            resetRecordAudioButton.isEnabled = false
        }

        // Stops the recording
        stopRecordAudioButton.setOnClickListener {
            mediaRecorder.stop()
            startRecordAudioButton.isEnabled = false
            stopRecordAudioButton.isEnabled = false
            previewRecordAudioButton.isEnabled = true
            resetRecordAudioButton.isEnabled = true

        }

        // Plays back the recording
        previewRecordAudioButton.setOnClickListener {
            mediaPlayer.setDataSource(recordingPath)
            mediaPlayer.prepare()
            mediaPlayer.start()
        }

        resetRecordAudioButton.setOnClickListener {
            mediaPlayer.reset()
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB)
            mediaRecorder.setOutputFile(recordingPath)
            startRecordAudioButton.isEnabled = true
            stopRecordAudioButton.isEnabled = false
            previewRecordAudioButton.isEnabled = false
            resetRecordAudioButton.isEnabled = false
        }


        confirmButton.setOnClickListener {
            val name = editTextSoundName.text.toString()
            val mp3 = 6 // upload sounds comes later date
            if (name.isEmpty()) {
                Toast.makeText(context, "Please give your sound a name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val item = SoundGridItem(name, mp3/*, color*/)
            addDialogListener.onAddButtonClicked(item)
            dismiss()
        }

        cancelButton.setOnClickListener {
            dismiss()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==111 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            startRecordAudioButton.isEnabled = true
    }

    private fun startFileChooser() {
        val intent = Intent()
        intent.type = "audio/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
    }

    fun OnActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            audioUri = data?.data!!
        }

    }
}
