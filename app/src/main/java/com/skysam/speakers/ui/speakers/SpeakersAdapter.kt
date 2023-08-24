package com.skysam.speakers.ui.speakers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.skysam.speakers.R
import com.skysam.speakers.common.Utils
import com.skysam.speakers.dataClasses.Speaker

/**
 * Created by Hector Chirinos on 23/08/2023.
 */

class SpeakersAdapter: RecyclerView.Adapter<SpeakersAdapter.ViewHolder>() {
 private lateinit var context: Context
 private var speakers = listOf<Speaker>()

 override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpeakersAdapter.ViewHolder {
  val view = LayoutInflater.from(parent.context)
   .inflate(R.layout.layout_item_speaker, parent, false)
  context = parent.context
  return ViewHolder(view)
 }

 override fun onBindViewHolder(holder: SpeakersAdapter.ViewHolder, position: Int) {
  val item = speakers[position]
  holder.name.text = item.name
  holder.congregation.text = item.congregation
  holder.date.text = if (item.lastTime != null) Utils.convertDateToString(item.lastTime!!)
  else context.getString(R.string.text_speaker_not_speech)

  /*holder.card.setOnClickListener {
   val popMenu = PopupMenu(context, holder.card)
   popMenu.inflate(R.menu.menu_convention_item)

   popMenu.setOnMenuItemClickListener {
    when (it.itemId) {
     R.id.menu_assign_speakers -> onClick.speakers(item)
     R.id.menu_assign_dates -> onClick.dates(item)
     R.id.menu_view -> onClick.view(item)
    }
    false
   }
   popMenu.show()
  }*/
 }

 override fun getItemCount(): Int = speakers.size

 inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
  val name: TextView = view.findViewById(R.id.tv_name)
  val congregation: TextView = view.findViewById(R.id.tv_congregation)
  val date: TextView = view.findViewById(R.id.tv_date)
  val card: MaterialCardView = view.findViewById(R.id.card)
 }

 fun updateList(newList: List<Speaker>) {
  val diffUtil = SpeakerDiffUtil(speakers, newList)
  val result = DiffUtil.calculateDiff(diffUtil)
  speakers = newList
  result.dispatchUpdatesTo(this)
 }
}