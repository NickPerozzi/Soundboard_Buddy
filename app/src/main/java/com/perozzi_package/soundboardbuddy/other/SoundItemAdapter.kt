package com.perozzi_package.soundboardbuddy.other

import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import soundboardbuddy.R
import com.perozzi_package.soundboardbuddy.data.db.entities.SoundboardItem

class SoundboardAdapter :
    ListAdapter<SoundboardItem, SoundboardAdapter.ItemHolder>(SoundboardDiffUtil()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemHolder {
        val itemHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.sound_item, parent, false)
        return ItemHolder(itemHolder)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val soundboardItem: SoundboardItem = getItem(position)
        holder.soundName.text = soundboardItem.name
        holder.button.setImageResource(soundboardItem.color)
        holder.button.setOnClickListener {
            MediaPlayer().setDataSource(soundboardItem.audioPath)
            MediaPlayer().start()
        }
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var soundName: TextView = itemView.findViewById(R.id.soundName)
        var button: ImageView = itemView.findViewById(R.id.play_button)
    }
}

class SoundboardDiffUtil : DiffUtil.ItemCallback<SoundboardItem>() {
    override fun areItemsTheSame(oldItem: SoundboardItem, newItem: SoundboardItem): Boolean {
        val sameColor = oldItem.color == newItem.color
        // val sameAudio = oldItem.audio == newItem.audio
        val sameTitle = oldItem.name == newItem.name
        return sameColor && /*sameAudio && */sameTitle
    }

    override fun areContentsTheSame(oldItem: SoundboardItem, newItem: SoundboardItem): Boolean {
        return oldItem == newItem
    }
}