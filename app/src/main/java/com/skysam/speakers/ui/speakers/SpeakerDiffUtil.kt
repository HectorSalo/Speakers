package com.skysam.speakers.ui.speakers

import androidx.recyclerview.widget.DiffUtil
import com.skysam.speakers.dataClasses.Speaker

/**
 * Created by Hector Chirinos on 23/08/2023.
 */

class SpeakerDiffUtil(private val oldList: List<Speaker>, private val newList: List<Speaker>):
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