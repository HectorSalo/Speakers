package com.skysam.speakers.ui.conventions

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.datepicker.MaterialDatePicker
import com.skysam.speakers.R
import com.skysam.speakers.common.Utils
import com.skysam.speakers.dataClasses.Convention
import com.skysam.speakers.databinding.DialogSelectDatesBinding
import java.text.DateFormat
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

/**
 * Created by Hector Chirinos on 23/08/2023.
 */

class SelectDatesDialog: DialogFragment() {
 private var _binding: DialogSelectDatesBinding? = null
 private val binding get() = _binding!!
 private val viewModel: ConventionsViewModel by activityViewModels()
 private lateinit var btnPositive: Button
 private lateinit var btnNegative: Button
 private lateinit var dateSelectedA: Date
 private lateinit var dateSelectedB: Date
 private lateinit var convention: Convention

 override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
  _binding = DialogSelectDatesBinding.inflate(layoutInflater)

  viewModel.convention.observe(this.requireActivity()) {
   if (_binding != null) {
    convention = it
    dateSelectedA = if (convention.dateA != null) convention.dateA!! else Date()
    dateSelectedB = if (convention.dateB != null) convention.dateB!! else Date()

    binding.etDateA.setText(Utils.convertDateToString(dateSelectedA))
    binding.etDateB.setText(Utils.convertDateToString(dateSelectedB))
   }
  }

  binding.etDateA.setOnClickListener { selecDate(true) }
  binding.etDateB.setOnClickListener { selecDate(false) }

  val builder = AlertDialog.Builder(requireActivity())
  builder.setTitle(getString(R.string.title_dialog_select_dates))
   .setView(binding.root)
   .setPositiveButton(R.string.text_save, null)
   .setNegativeButton(R.string.text_cancel, null)

  val dialog = builder.create()
  dialog.show()

  btnNegative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
  btnNegative.setOnClickListener { dialog.dismiss() }
  btnPositive = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
  btnPositive.setOnClickListener { sendDates() }

  return dialog
 }

 private fun sendDates() {
  val conventionSend = Convention(
   convention.id,
   convention.title,
   convention.image,
   dateSelectedA,
   dateSelectedB,
   convention.speeches
  )
  viewModel.setDates(conventionSend)
  dismiss()
 }

 private fun selecDate(sectionA: Boolean) {
  val builder = MaterialDatePicker.Builder.datePicker()
  val calendar = Calendar.getInstance()
  val picker = builder.build()
  picker.addOnPositiveButtonClickListener { selection: Long? ->
   calendar.timeInMillis = selection!!
   val timeZone = TimeZone.getDefault()
   val offset = timeZone.getOffset(Date().time) * -1
   calendar.timeInMillis = calendar.timeInMillis + offset
   if (sectionA) {
    dateSelectedA = calendar.time
    binding.etDateA.setText(DateFormat.getDateInstance().format(dateSelectedA))
   } else {
    dateSelectedB = calendar.time
    binding.etDateB.setText(DateFormat.getDateInstance().format(dateSelectedB))
   }
  }
  picker.show(requireActivity().supportFragmentManager, picker.toString())
 }
}