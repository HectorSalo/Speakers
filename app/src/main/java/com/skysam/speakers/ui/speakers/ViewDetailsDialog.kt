package com.skysam.speakers.ui.speakers

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.skysam.speakers.R
import com.skysam.speakers.common.Speakers
import com.skysam.speakers.dataClasses.SpeechToView
import com.skysam.speakers.databinding.DialogViewDetailsSpeakerBinding
import com.skysam.speakers.ui.speeches.SpeechesAdapter

class ViewDetailsDialog: DialogFragment() {
    private var _binding: DialogViewDetailsSpeakerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SpeakersViewModel by activityViewModels()
    private lateinit var speechesAdapter: SpeechesAdapter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogViewDetailsSpeakerBinding.inflate(layoutInflater)

        speechesAdapter = SpeechesAdapter()
        binding.rvSpeeches.apply {
            setHasFixedSize(true)
            adapter = speechesAdapter
        }

        subscribeViewModel()

        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(getString(R.string.text_details))
            .setView(binding.root)
            .setPositiveButton(R.string.text_accept, null)

        val dialog = builder.create()
        dialog.show()

        return dialog
    }

    private fun subscribeViewModel() {
        viewModel.speakerToView.observe(this.requireActivity()) {
            if (_binding != null) {
                if (it.speeches.isEmpty()) {
                    binding.rvSpeeches.visibility = View.GONE
                    binding.tvListEmpty.visibility = View.VISIBLE
                } else {
                    viewModel.getSpeechesBySpeaker(it).observe(this.requireActivity()) {speeches ->
                        val speechesToView = mutableListOf<SpeechToView>()
                        speeches.forEach { speech ->
                            val speechToView = SpeechToView(
                                speech.title,
                                "",
                                ""
                            )
                            speechesToView.add(speechToView)
                        }
                        speechesAdapter.updateList(speechesToView)
                        binding.rvSpeeches.visibility = View.VISIBLE
                        binding.tvListEmpty.visibility = View.GONE
                    }
                }

                binding.etName.setText(it.name)
                binding.etCongregation.setText(it.congregation)
                binding.etObservations.setText(it.observations)
                binding.tvSection.text = if (it.isSectionA) Speakers.Speakers.getContext()
                    .getString(R.string.text_section_a)
                else Speakers.Speakers.getContext().getString(R.string.text_section_b)


            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}