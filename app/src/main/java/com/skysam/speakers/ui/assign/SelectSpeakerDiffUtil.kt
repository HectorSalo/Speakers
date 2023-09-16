package com.skysam.speakers.ui.assign

import androidx.recyclerview.widget.DiffUtil
import com.skysam.speakers.dataClasses.SpeakerToView

class SelectSpeakerDiffUtil(private val oldList: List<SpeakerToView>, private val newList: List<SpeakerToView>):
    DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newList.contains(oldList[oldItemPosition])
    }
}