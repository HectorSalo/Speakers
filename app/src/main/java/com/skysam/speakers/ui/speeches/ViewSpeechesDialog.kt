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

class ViewSpeechesDialog: DialogFragment(), OnClick {
    private var _binding: DialogViewSpeechesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SpeechViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogViewSpeechesBinding.inflate(layoutInflater)

        subscribeViewModel()

        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(getString(R.string.app_name))
            .setView(binding.root)
            .setPositiveButton(R.string.text_accept) {_, _ ->
                dismiss()
            }

        val dialog = builder.create()
        dialog.show()

        return dialog
    }

    private fun subscribeViewModel() {
        viewModel.convention.observe(this.requireActivity()) {
            if (_binding != null) {
                viewModel.getSpeeches(it).observe(this.requireActivity()) {speeches ->
                    if (_binding != null) {
                        val speechesToView = mutableListOf<SpeechToView>()
                        for (speech in speeches) {
                            viewModel.getSpeakers(speech).observe(this.requireActivity()) {speakers ->
                                if (_binding != null) {
                                    var nameA = ""
                                    var nameB = ""
                                    speakers.forEach {speak ->
                                        if (speak.isSectionA) nameA = speak.name else nameB = speak.name
                                    }
                                    val sectionA = if (speech.isViajante) Speakers.Speakers.getContext().getString(R.string.text_viajante)
                                    else if (speech.isRepresentante) Speakers.Speakers.getContext().getString(R.string.text_representante) else nameA
                                    val sectionB = if (speech.isViajante) Speakers.Speakers.getContext().getString(R.string.text_viajante)
                                    else if (speech.isRepresentante) Speakers.Speakers.getContext().getString(R.string.text_representante) else nameB
                                    val newSpeech = SpeechToView(
                                        speech.position,
                                        speech.title,
                                        sectionA,
                                        sectionB
                                    )
                                    speechesToView.add(newSpeech)
                                    if (speeches.size == speechesToView.size) {
                                        val speechesAdapter = SpeechesAdapter(
                                            viewSection = true,
                                            canSelect = false,
                                            onClick = this
                                        )
                                        binding.rvSpeeches.apply {
                                            setHasFixedSize(true)
                                            adapter = speechesAdapter
                                        }
                                        speechesAdapter.updateList(speechesToView.sortedBy { spe -> spe.number })
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun select(speechToView: SpeechToView) {

    }
}