package com.perozzi_package.soundboardbuddy.ui.soundboard

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.app.ActivityCompat.getDrawable
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.getDrawable
import com.perozzi_package.soundboardbuddy.R
import com.perozzi_package.soundboardbuddy.data.db.entities.SoundGridItem
import com.perozzi_package.soundboardbuddy.databinding.DialogAddSoundBinding
import kotlinx.android.synthetic.main.dialog_add_sound.*

class AddSoundItemDialog(context: Context, private var addDialogListener: AddDialogListener) : AppCompatDialog(context) {

    var parentActivity = ownerActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val addSoundItemDialogViewModel = AddSoundItemDialogViewModel()
        val binding = DialogAddSoundBinding.inflate(layoutInflater)
        binding.addSoundItemDialogViewModel = addSoundItemDialogViewModel
        setContentView(binding.root)
        val recordButton = binding.recordAudioButton
        val rerecordButton = binding.rerecordAudioButton
        val uploadRadioButton = binding.uploadRadioButton
        uploadRadioButton.isSelected = true
        val recordRadioButton = binding.recordRadioButton
        recordRadioButton.isSelected = false
        val redRadio = binding.redRadio
        val blueRadio = binding.blueRadio
        val blackRadio = binding.blackRadio
        val whiteRadio = binding.whiteRadio

        setContentView(R.layout.dialog_add_sound)

        uploadRecordRadioGroup.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.uploadRadioButton -> {
                    addSoundItemDialogViewModel.recordSelectedNotUpload.value = false
                    addSoundItemDialogViewModel.recordSelectedNotUpload.postValue(false)
                    Toast.makeText(context, "Upload time", Toast.LENGTH_SHORT).show()
                    uploadAudioButton.visibility = View.VISIBLE
                    recordAudioButton.visibility = View.GONE
                    rerecordAudioButton.visibility = View.GONE
                }
                R.id.recordRadioButton -> {
                    addSoundItemDialogViewModel.recordSelectedNotUpload.value = true
                    Toast.makeText(context, "Recording time", Toast.LENGTH_SHORT).show()
                    uploadAudioButton.visibility = View.GONE
                    recordAudioButton.visibility = View.VISIBLE
                    rerecordAudioButton.visibility = View.VISIBLE
                }
                else -> {
                }
            }
        }

        var selectedColor = 69420
        colorSelectRadioGroup.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.redRadio -> {
                    Toast.makeText(context, "Upload time", Toast.LENGTH_SHORT).show()
                    selectedColor = R.drawable.diy_button_unpressed_red
                }
                R.id.blueRadio -> {
                    selectedColor = R.drawable.diy_button_unpressed_red
                }
                R.id.whiteRadio -> {
                    selectedColor = R.drawable.diy_button_unpressed_red
                }
                R.id.blackRadio -> {
                    selectedColor = R.drawable.diy_button_unpressed_red
                }

            }
        }

        uploadAudioButton.setOnClickListener {
            startFileChooser()
        }

        confirmButton.setOnClickListener {
            val name = editTextSoundName.text.toString()
            val mp3 = 6 // upload sounds comes later date
            val color = selectedColor
            if (name.isEmpty()) {
                Toast.makeText(context, "Please give your sound a name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (selectedColor == 69420) {
                Toast.makeText(context, "Please choose a button color", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val item = SoundGridItem(name, mp3/*, color*/)
            addDialogListener.onAddButtonClicked(item)
            dismiss()
        }

        cancelButton.setOnClickListener {
            cancel()
        }
    }


    private fun startFileChooser() {
        val intent = Intent()
        intent.setType("audio/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        ownerActivity?.let {
            startActivityForResult(
                it,
                Intent.createChooser(intent, "Select audio"),
                111,
                null
            )
        }
    }

    /*override fun OnActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 111 && resultCode == Activity.RESULT_OK && data != null) {

        }

    }*/
}