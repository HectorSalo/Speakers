package com.skysam.speakers.ui.speakers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.skysam.speakers.R
import com.skysam.speakers.dataClasses.Speaker
import com.skysam.speakers.databinding.DialogAddSpeakerBinding

/**
 * Created by Hector Chirinos on 24/08/2023.
 */

class NewSpeakerDialog: DialogFragment() {
 private var _binding: DialogAddSpeakerBinding? = null
 private val binding get() = _binding!!
 private val viewModel: SpeakersViewModel by activityViewModels()

 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  setStyle(
   STYLE_NORMAL,
   com.google.android.material.R.style.ShapeAppearanceOverlay_MaterialComponents_MaterialCalendar_Window_Fullscreen
  )
 }

 override fun onCreateView(
  inflater: LayoutInflater, container: ViewGroup?,
  savedInstanceState: Bundle?
 ): View {
  _binding = DialogAddSpeakerBinding.inflate(inflater, container, false)
  return binding.root
 }

 override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
  super.onViewCreated(view, savedInstanceState)
  val callback = object : OnBackPressedCallback(true) {
   override fun handleOnBackPressed() {
    getOut()
   }
  }
  requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

  binding.etName.doAfterTextChanged { binding.tfName.error = null }
  binding.etCongregation.doAfterTextChanged { binding.tfCongregation.error = null }

  binding.btnSave.setOnClickListener { validateData() }
  binding.btnExit.setOnClickListener { getOut() }
 }

 override fun onDestroyView() {
  super.onDestroyView()
  _binding = null
 }

 private fun validateData() {
  val name = binding.etName.text.toString()
  if (name.isEmpty()) {
   binding.tfName.error = getString(R.string.error_field_empty)
   binding.etName.requestFocus()
   return
  }
  val congregation = binding.etCongregation.text.toString()
  if (congregation.isEmpty()) {
   binding.tfCongregation.error = getString(R.string.error_field_empty)
   binding.etCongregation.requestFocus()
   return
  }

  val newSpeaker = Speaker(
   "",
   name,
   congregation,
   binding.rbA.isChecked,
   listOf(),
   binding.etObservations.text.toString(),
   true
  )
  viewModel.saveSpeaker(newSpeaker)
  dismiss()
 }

 private fun getOut() {
  dismiss()
 }
}