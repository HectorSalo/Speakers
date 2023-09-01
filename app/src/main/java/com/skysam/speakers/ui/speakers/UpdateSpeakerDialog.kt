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

class UpdateSpeakerDialog: DialogFragment() {
    private var _binding: DialogAddSpeakerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SpeakersViewModel by activityViewModels()
    private lateinit var speaker: Speaker

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
        binding.btnSave.setText(R.string.text_update)

        binding.btnSave.setOnClickListener { validateData() }
        binding.btnExit.setOnClickListener { getOut() }

        subscribeObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun subscribeObservers() {
        viewModel.speakerToUpdate.observe(viewLifecycleOwner) {
            if (_binding != null) {
                binding.etName.setText(it.name)
                binding.etCongregation.setText(it.congregation)
                binding.etObservations.setText(it.observations)
                if (it.isSectionA) binding.rbA.isChecked = true else binding.rbB.isChecked = true
                speaker = it
            }
        }
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
            speaker.id,
            name,
            congregation,
            binding.rbA.isChecked,
            speaker.speeches,
            binding.etObservations.text.toString(),
            speaker.isActive
        )
        viewModel.updateSpeaker(newSpeaker)
        dismiss()
    }

    private fun getOut() {
        dismiss()
    }
}