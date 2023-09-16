package com.skysam.speakers.ui.assign

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.skysam.speakers.R
import com.skysam.speakers.common.Speakers
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
    private val speechesToView = mutableListOf<SpeechToView>()
    private var speaker: Speaker? = null

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
                viewModel.getSpeeches(it).observe(viewLifecycleOwner) {list ->
                    speeches = list
                    binding.tvTitle.text = it.title

                    for (speech in speeches) {
                        viewModel.getSpeakers(speech).observe(viewLifecycleOwner) {speakers ->
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
                                val listToView: List<SpeechToView> = speechesToView.sortedBy { spe -> spe.number }
                                speechesAdapter.updateList(listToView)
                            }
                        }
                    }
                }
            }
        }
        viewModel.speaker.observe(viewLifecycleOwner) {
            if (_binding != null) {
                speaker = it
            }
        }
    }

    override fun select(speechToView: SpeechToView) {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(R.string.text_select)
            .setItems(R.array.options_select) { _, which ->
                when(which) {
                    0 -> {

                    }
                    1 -> {}
                    2 -> {}
                    3 -> {}
                }
            }
        builder.create()
        builder.show()
    }
}