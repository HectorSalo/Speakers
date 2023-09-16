package com.skysam.speakers.ui.assign

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.skysam.speakers.dataClasses.SpeakerToView
import com.skysam.speakers.databinding.DialogSelectSpeakerBinding

class SelectSpeakerDialog: DialogFragment(), OnClick {
    private var _binding: DialogSelectSpeakerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AssignViewModel by activityViewModels()
    private lateinit var selectSpeakerAdapter: SelectSpeakerAdapter

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
        _binding = DialogSelectSpeakerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                dismiss()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        selectSpeakerAdapter = SelectSpeakerAdapter(this)
        binding.rvSpeakers.apply {
            setHasFixedSize(true)
            adapter = selectSpeakerAdapter
        }

        subscribeObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun subscribeObservers() {
        viewModel.isSectionA.observe(this.requireActivity()) {isSectionAFrom ->
            if (_binding != null) {
                viewModel.getSpeakersBySection(isSectionAFrom).observe(this.requireActivity()) {speakers ->
                    if (speakers.isEmpty()) {
                        binding.rvSpeakers.visibility = View.GONE
                        binding.tvListEmpty.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                    } else {
                        val speakersToView = mutableListOf<SpeakerToView>()
                        for (speaker in speakers) {
                            if (speaker.speeches.isEmpty()) {
                                val newSp = SpeakerToView(
                                    speaker.id,
                                    speaker.name,
                                    speaker.congregation,
                                    null,
                                    speaker.observations
                                )
                                speakersToView.add(newSp)
                                if (speakersToView.size == speakers.size) {
                                    selectSpeakerAdapter.updateList(speakersToView.sortedBy { speakerL-> speakerL.lastDate })
                                    binding.rvSpeakers.visibility = View.VISIBLE
                                    binding.tvListEmpty.visibility = View.GONE
                                    binding.progressBar.visibility = View.GONE
                                }
                            } else {
                                viewModel.getLastConventionFromSpeaker(speaker).observe(this.requireActivity()) { convention ->
                                    val newSp = SpeakerToView(
                                        speaker.id,
                                        speaker.name,
                                        speaker.congregation,
                                        if (isSectionAFrom) convention.dateA else convention.dateB,
                                        speaker.observations
                                    )
                                    speakersToView.add(newSp)
                                    if (speakersToView.size == speakers.size) {
                                        selectSpeakerAdapter.updateList(speakersToView.sortedBy { speakerL-> speakerL.lastDate })
                                        binding.rvSpeakers.visibility = View.VISIBLE
                                        binding.tvListEmpty.visibility = View.GONE
                                        binding.progressBar.visibility = View.GONE
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun select(speakerToView: SpeakerToView) {

    }
}