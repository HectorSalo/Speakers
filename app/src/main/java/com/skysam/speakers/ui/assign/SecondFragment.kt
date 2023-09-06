package com.skysam.speakers.ui.assign

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.skysam.speakers.R
import com.skysam.speakers.dataClasses.Speaker
import com.skysam.speakers.dataClasses.Speech
import com.skysam.speakers.databinding.FragmentSecondBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AssignViewModel by activityViewModels()
    private var speeches = listOf<Speech>()
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
                }
            }
        }
        viewModel.speaker.observe(viewLifecycleOwner) {
            if (_binding != null) {
                speaker = it
            }
        }
    }
}