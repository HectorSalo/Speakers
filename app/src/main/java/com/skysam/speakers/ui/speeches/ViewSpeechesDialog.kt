package com.skysam.speakers.ui.speeches

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.skysam.speakers.R
import com.skysam.speakers.common.Speakers
import com.skysam.speakers.dataClasses.SpeechToView
import com.skysam.speakers.databinding.DialogViewSpeechesBinding

class ViewSpeechesDialog: DialogFragment() {
    private var _binding: DialogViewSpeechesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SpeechViewModel by activityViewModels()
    private lateinit var speechesAdapter: SpeechesAdapter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogViewSpeechesBinding.inflate(layoutInflater)

        speechesAdapter = SpeechesAdapter()
        binding.rvSpeeches.apply {
            setHasFixedSize(true)
            adapter = speechesAdapter
        }

        subscribeViewModel()

        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(getString(R.string.app_name))
            .setView(binding.root)
            .setPositiveButton(R.string.text_accept, null)

        val dialog = builder.create()
        dialog.show()

        return dialog
    }

    private fun subscribeViewModel() {
        viewModel.convention.observe(this.requireActivity()) {
            if (_binding != null) {
                viewModel.getSpeeches(it).observe(this.requireActivity()) {speeches ->
                    val speechesToView = mutableListOf<SpeechToView>()
                    speeches.forEach {speech ->
                        val sectionA = if (speech.isViajante) Speakers.Speakers.getContext().getString(R.string.text_viajante)
                        else if (speech.isRepresentante) Speakers.Speakers.getContext().getString(R.string.text_viajante) else ""
                        val sectionB = if (speech.isViajante) Speakers.Speakers.getContext().getString(R.string.text_viajante)
                        else if (speech.isRepresentante) Speakers.Speakers.getContext().getString(R.string.text_viajante) else ""
                        val newSpeech = SpeechToView(
                            speech.title,
                            sectionA,
                            sectionB
                        )
                        speechesToView.add(newSpeech)
                        /*viewModel.getSpeakers(speech).observe(this.requireActivity()) {speakers ->

                        }*/
                    }
                    speechesAdapter.updateList(speechesToView)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}