package com.skysam.speakers.ui.conventions

import androidx.recyclerview.widget.DiffUtil
import com.skysam.speakers.dataClasses.Convention

/**
 * Created by Hector Chirinos on 22/08/2023.
 */

class ConventionsDiffUtil(private val oldList: List<Convention>, private val newList: List<Convention>):
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