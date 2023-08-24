package com.skysam.speakers.ui.speakers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.skysam.speakers.dataClasses.Convention
import com.skysam.speakers.dataClasses.Speaker
import com.skysam.speakers.databinding.FragmentSpeakersBinding

class SpeakersFragment : Fragment() {

    private var _binding: FragmentSpeakersBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SpeakersViewModel by activityViewModels()
    private lateinit var speakersAdapter: SpeakersAdapter
    private var speakers = listOf<Speaker>()
    private var conventions = listOf<Convention>()

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
        speakersAdapter = SpeakersAdapter()
        binding.rvSpeakers.apply {
            setHasFixedSize(true)
            adapter = speakersAdapter
        }

        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.conventions.observe(viewLifecycleOwner) {
            if (_binding != null) {
                conventions = it
            }
        }
        viewModel.speakers.observe(viewLifecycleOwner) {
            if (_binding != null) {
                if (speakers.isNotEmpty()) {
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
}