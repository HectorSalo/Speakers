package com.skysam.speakers.ui.speeches

import androidx.recyclerview.widget.DiffUtil
import com.skysam.speakers.dataClasses.Speech
import com.skysam.speakers.dataClasses.SpeechToView

class SpeecheDiffUtil(private val oldList: List<SpeechToView>, private val newList: List<SpeechToView>):
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