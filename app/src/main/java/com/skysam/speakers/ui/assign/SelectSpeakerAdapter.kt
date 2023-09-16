package com.skysam.speakers.ui.assign

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
import com.skysam.speakers.dataClasses.SpeakerToView

class SelectSpeakerAdapter(private val onClick: OnClick): RecyclerView.Adapter<SelectSpeakerAdapter.ViewHolder>() {
    private lateinit var context: Context
    private var speakers = listOf<SpeakerToView>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectSpeakerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_item_speaker, parent, false)
        context = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectSpeakerAdapter.ViewHolder, position: Int) {
        val item = speakers[position]
        holder.name.text = item.name
        holder.congregation.text = if (item.lastDate != null) Utils.convertDateToString(item.lastDate!!)
        else context.getString(R.string.text_not_speech_assing)
        holder.speeches.text = item.observations

        holder.card.setOnClickListener { onClick.select(item) }
    }

    override fun getItemCount(): Int = speakers.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tv_name)
        val congregation: TextView = view.findViewById(R.id.tv_congregation)
        val speeches: TextView = view.findViewById(R.id.tv_speeches)
        val card: MaterialCardView = view.findViewById(R.id.card)
    }

    fun updateList(newList: List<SpeakerToView>) {
        val diffUtil = SelectSpeakerDiffUtil(speakers, newList)
        val result = DiffUtil.calculateDiff(diffUtil)
        speakers = newList
        result.dispatchUpdatesTo(this)
    }
}