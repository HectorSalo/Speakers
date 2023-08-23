package com.skysam.speakers.ui.conventions

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.skysam.speakers.R
import com.skysam.speakers.common.Utils
import com.skysam.speakers.dataClasses.Convention

/**
 * Created by Hector Chirinos on 22/08/2023.
 */

class ConventionsAdapter: RecyclerView.Adapter<ConventionsAdapter.ViewHolder>() {
    lateinit var context: Context
    private var conventions = listOf<Convention>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConventionsAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_item_convention, parent, false)
        context = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConventionsAdapter.ViewHolder, position: Int) {
        val item = conventions[position]
        holder.title.text = item.title
        holder.date.text = if (item.dateA != null && item.dateB != null) context.getString(R.string.text_two_variable,
            Utils.convertDateToString(item.dateA!!), Utils.convertDateToString(item.dateB!!))
        else context.getString(R.string.text_no_dates)

        /*holder.menu.setOnClickListener {
            val popMenu = PopupMenu(context, holder.menu)
            popMenu.inflate(R.menu.menu_bookings_item)

            popMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_edit -> onClick.edit(item)
                    R.id.menu_delete -> onClick.delete(item)
                }
                false
            }
            popMenu.show()
        }

        holder.card.setOnClickListener { onClick.view(item) }*/
    }

    override fun getItemCount(): Int = conventions.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tv_title)
        val date: TextView = view.findViewById(R.id.tv_date)
        val card: MaterialCardView = view.findViewById(R.id.card)
    }

    fun updateList(newList: List<Convention>) {
        val diffUtil = ConventionsDiffUtil(conventions, newList)
        val result = DiffUtil.calculateDiff(diffUtil)
        conventions = newList
        result.dispatchUpdatesTo(this)
    }
}