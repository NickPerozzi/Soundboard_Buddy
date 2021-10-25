package com.perozzi_package.soundboardbuddy.other

import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.perozzi_package.soundboardbuddy.R
import com.perozzi_package.soundboardbuddy.ui.soundboard.SoundViewModel
import com.perozzi_package.soundboardbuddy.data.db.entities.SoundGridItem
import kotlinx.android.synthetic.main.sound_item.view.*


// chapter 1's from every book (bad)
class SoundItemAdapter(
    var items: List<SoundGridItem>,
    var sounds: List<MediaPlayer>,
    //var colors: List<Int>,
    private val viewModel: SoundViewModel
): RecyclerView.Adapter<SoundItemAdapter.SoundViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sound_item,parent,false)
        return SoundViewHolder(view)
    }

    inner class SoundViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var items: TextView = itemView.findViewById(R.id.soundName)
        // var sounds: ?? = itemView.findViewById(?) Don't think I need this one
        var colors: Button = itemView.findViewById(R.id.play_button)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SoundViewHolder, position: Int) {
        val currentSoundItem = items[position]
        holder.itemView.soundName.text = currentSoundItem.name

        val currentSound = sounds[0] // val currentSound = sounds[position]
        holder.itemView.delete_button.setOnClickListener {
            viewModel.delete(currentSoundItem)
            // viewModel.delete(currentSound)
        }
        holder.itemView.play_button.setOnClickListener {
            currentSound.start()
        }

        //val currentColor = colors[position]
        //holder.
    }
}