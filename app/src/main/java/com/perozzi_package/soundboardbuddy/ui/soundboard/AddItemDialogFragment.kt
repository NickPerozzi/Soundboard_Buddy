package com.perozzi_package.soundboardbuddy.ui.soundboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.navigation.NavController
import com.perozzi_package.soundboardbuddy.data.db.entities.SoundGridItem
import kotlinx.android.synthetic.main.fragment_add_item_dialog.*
import soundboardbuddy.R
import soundboardbuddy.databinding.FragmentAddItemDialogBinding

class AddItemDialogFragment(context: Context, private var addDialogListener: AddDialogListener) :
    AppCompatDialogFragment() {

    private lateinit var navController: NavController
    private lateinit var binding: FragmentAddItemDialogBinding
    private lateinit var addItemDialogViewModel: AddItemDialogViewModel

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
                    recordAudioButton.visibility = View.GONE
                    rerecordAudioButton.visibility = View.GONE
                }
                R.id.recordRadioButton -> {
                    addItemDialogViewModel.recordSelectedNotUpload.value = true
                    Toast.makeText(context, "Recording time", Toast.LENGTH_SHORT).show()
                    uploadAudioButton.visibility = View.GONE
                    recordAudioButton.visibility = View.VISIBLE
                    rerecordAudioButton.visibility = View.VISIBLE
                }
                else -> {
                }
            }
        }

        uploadAudioButton.setOnClickListener {
/*
            startFileChooser()
*/
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
}


/*private fun startFileChooser() {
    val intent = Intent()
    intent.setType("audio/*")
    intent.setAction(Intent.ACTION_GET_CONTENT)
    startActivityForResult(this, Intent.createChooser(intent, "Select audio"), 111, null)
}*/

/*override fun OnActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == 111 && resultCode == Activity.RESULT_OK && data != null) {

    }

}*/