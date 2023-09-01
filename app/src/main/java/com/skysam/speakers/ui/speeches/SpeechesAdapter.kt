package com.skysam.speakers.ui.speeches

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.skysam.speakers.R
import com.skysam.speakers.dataClasses.SpeechToView

class SpeechesAdapter: RecyclerView.Adapter<SpeechesAdapter.ViewHolder>() {
    private lateinit var context: Context
    private var speeches = listOf<SpeechToView>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpeechesAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_item_view_speeches, parent, false)
        context = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SpeechesAdapter.ViewHolder, position: Int) {
        val item = speeches[position]
        holder.title.text = item.title
        holder.sectionA.text = if (item.sectionA.isNotEmpty())
            "${context.getString(R.string.text_section_b)}: ${item.sectionA}"
        else context.getString(R.string.text_not_speech_assing)
        holder.sectionB.text = if (item.sectionB.isNotEmpty())
            "${context.getString(R.string.text_section_b)}: ${item.sectionB}"
        else context.getString(R.string.text_not_speech_assing)
    }

    override fun getItemCount(): Int = speeches.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tv_title)
        val sectionA: TextView = view.findViewById(R.id.tv_section_a)
        val sectionB: TextView = view.findViewById(R.id.tv_section_b)
    }

    fun updateList(newList: List<SpeechToView>) {
        val diffUtil = SpeecheDiffUtil(speeches, newList)
        val result = DiffUtil.calculateDiff(diffUtil)
        speeches = newList
        result.dispatchUpdatesTo(this)
    }
}