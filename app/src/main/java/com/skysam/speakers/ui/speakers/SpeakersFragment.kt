package com.skysam.speakers.ui.speakers

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.skysam.speakers.common.Constants
import com.skysam.speakers.dataClasses.Speaker
import com.skysam.speakers.databinding.FragmentSpeakersBinding
import com.skysam.speakers.ui.assign.AssignActivity

class SpeakersFragment : Fragment(), OnClick {

    private var _binding: FragmentSpeakersBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SpeakersViewModel by activityViewModels()
    private lateinit var speakersAdapter: SpeakersAdapter
    private var speakers = listOf<Speaker>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSpeakersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        speakersAdapter = SpeakersAdapter(this)
        binding.rvSpeakers.apply {
            setHasFixedSize(true)
            adapter = speakersAdapter
        }

        binding.fab.setOnClickListener {
            val newSpeakerDialog = NewSpeakerDialog()
            newSpeakerDialog.show(requireActivity().supportFragmentManager, tag)
        }

        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.speakers.observe(viewLifecycleOwner) {
            if (_binding != null) {
                if (it.isNotEmpty()) {
                    speakers = it
                    binding.rvSpeakers.visibility = View.VISIBLE
                    binding.tvListEmpty.visibility = View.GONE
                    speakersAdapter.updateList(speakers)
                } else {
                    binding.rvSpeakers.visibility = View.GONE
                    binding.tvListEmpty.visibility = View.VISIBLE
                }
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun view(speaker: Speaker) {
        viewModel.speakerToView(speaker)
        val viewDetailsDialog = ViewDetailsDialog()
        viewDetailsDialog.show(requireActivity().supportFragmentManager, tag)
    }

    override fun assign(speaker: Speaker) {
        val intent = Intent(requireContext(), AssignActivity::class.java)
        intent.putExtra(Constants.SPEAKER, speaker)
        startActivity(intent)
    }

    override fun update(speaker: Speaker) {
        viewModel.speakerToUpdate(speaker)
        val updateSpeakerDialog = UpdateSpeakerDialog()
        updateSpeakerDialog.show(requireActivity().supportFragmentManager, tag)
    }

    override fun enable(speaker: Speaker) {
        viewModel.enableSpeaker(speaker)
    }

    override fun delete(speaker: Speaker) {
        viewModel.deleteSpeaker(speaker)
    }
}