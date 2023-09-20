package com.skysam.speakers.ui.assign

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.skysam.speakers.R
import com.skysam.speakers.common.Speakers
import com.skysam.speakers.dataClasses.Convention
import com.skysam.speakers.dataClasses.Speaker
import com.skysam.speakers.dataClasses.Speech
import com.skysam.speakers.dataClasses.SpeechToView
import com.skysam.speakers.databinding.FragmentSecondBinding
import com.skysam.speakers.ui.speeches.OnClick
import com.skysam.speakers.ui.speeches.SpeechesAdapter

class SecondFragment : Fragment(), OnClick {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AssignViewModel by activityViewModels()
    private lateinit var speechesAdapter: SpeechesAdapter
    private var speeches = listOf<Speech>()
    private var speakers = listOf<Speaker>()
    private var speaker: Speaker? = null
    private var convention: Convention? = null
    private var speechSelected: SpeechToView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (speaker != null) findNavController()
                    .navigate(R.id.action_SecondFragment_to_FirstFragment)
                else requireActivity().finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        speechesAdapter = SpeechesAdapter(true, canSelect = true, onClick = this)
        binding.rvSpeeches.apply {
            setHasFixedSize(true)
            adapter = speechesAdapter
        }

        subscribeObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun subscribeObservers() {
        viewModel.convention.observe(viewLifecycleOwner) {
            if (_binding != null) {
                convention = it
                fillData()
            }
        }
        viewModel.speaker.observe(viewLifecycleOwner) {
            if (_binding != null) {
                speaker = it
            }
        }
        viewModel.speeches.observe(viewLifecycleOwner) {
            if (_binding != null) {
                speeches = it
                fillData()
            }
        }
        viewModel.speakers.observe(viewLifecycleOwner) {
            if (_binding != null) {
                speakers = it
                fillData()
            }
        }
        viewModel.speakerSelectedDialog.observe(viewLifecycleOwner) {
            if (_binding != null) {
                if (it != null) {
                    var isSectionA = false
                    for (spea in speakers) {
                        if (spea.id == it.id) {
                            isSectionA = spea.isSectionA
                        }
                    }
                    val speakers = speakers.filter { speakerF -> speakerF.isSectionA == isSectionA }
                    val speeches = speeches.filter { speech -> speech.conventionId == convention?.id }
                    for (sp in speeches) {
                        if (sp.position == speechSelected?.number) {
                            viewModel.assignSpeaker(speakers, sp.id, it.id)
                            viewModel.selectSpeakerDialog(null)
                            break
                        }
                    }
                }
            }
        }
    }

    private fun fillData() {
        if (convention == null || speakers.isEmpty() || speeches.isEmpty()) return

        val speechesToView = mutableListOf<SpeechToView>()
        val speeches = speeches.filter { speech -> speech.conventionId == convention?.id }
        for (speech in speeches) {
            val speakers = speakers.filter { speaker -> speaker.speeches.contains(speech.id) }
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
                binding.tvTitle.text = convention?.title
                val listToView: List<SpeechToView> = speechesToView.sortedBy { spe -> spe.number }
                speechesAdapter.updateList(listToView)
            }
        }
    }

    override fun select(speechToView: SpeechToView) {
        if (speaker == null) {
            speechSelected = speechToView
            val builder = AlertDialog.Builder(requireActivity())
            builder.setTitle(R.string.text_select)
                .setItems(R.array.options_select) { _, which ->
                    when(which) {
                        0 -> {
                            val speeches = speeches.filter { speech -> speech.conventionId == convention?.id }
                            for (sp in speeches) {
                                if (sp.position == speechToView.number) {
                                    viewModel.assignViajante(sp)
                                    break
                                }
                            }
                        }
                        1 -> {
                            val speeches = speeches.filter { speech -> speech.conventionId == convention?.id }
                            for (sp in speeches) {
                                if (sp.position == speechToView.number) {
                                    viewModel.assignRepresentante(sp)
                                    break
                                }
                            }
                        }
                        2 -> {
                            viewModel.setSection(true)
                            //findNavController().navigate(R.id.action_SecondFragment_to_selectSpeakerFragment)
                            val selectSpeakerDialog = SelectSpeakerDialog()
                            selectSpeakerDialog.show(requireActivity().supportFragmentManager, tag)
                        }
                        3 -> {
                            viewModel.setSection(false)
                            //findNavController().navigate(R.id.action_SecondFragment_to_selectSpeakerFragment)
                            val selectSpeakerDialog = SelectSpeakerDialog()
                            selectSpeakerDialog.show(requireActivity().supportFragmentManager, tag)
                        }
                    }
                }
            builder.create()
            builder.show()
        } else {
            val builder = AlertDialog.Builder(requireActivity())
            builder.setTitle(getString(R.string.text_confirm))
                .setMessage(getString(R.string.message_dialog_assign, speechToView.title, speaker?.name))
                .setPositiveButton(R.string.text_assign_speech) { _, _ ->
                    val speakers = speakers.filter { speakerF -> speakerF.isSectionA == speaker?.isSectionA }
                    val speeches = speeches.filter { speech -> speech.conventionId == convention?.id }
                    for (sp in speeches) {
                        if (sp.position == speechToView.number) {
                            viewModel.assignSpeaker(speakers, sp.id, speaker!!.id)
                            break
                        }
                    }
                    requireActivity().finish()
                }
                .setNegativeButton(R.string.text_cancel, null)

            val dialog = builder.create()
            dialog.show()
        }
    }
}