package com.skysam.speakers.ui.assign

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.skysam.speakers.R
import com.skysam.speakers.common.Constants
import com.skysam.speakers.common.Utils
import com.skysam.speakers.dataClasses.Convention
import com.skysam.speakers.dataClasses.Speaker
import com.skysam.speakers.databinding.FragmentFirstBinding


class FirstFragment : Fragment() {
    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AssignViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (activity?.intent!!.hasExtra(Constants.SPEAKER)) {
            val speaker = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                activity?.intent?.getSerializableExtra(Constants.SPEAKER, Speaker::class.java)
            } else {
                @Suppress("DEPRECATION")
                activity?.intent?.getSerializableExtra(Constants.SPEAKER) as Speaker
            }
            viewModel.selectSpeaker(speaker!!)
        }

        if (activity?.intent!!.hasExtra(Constants.CONVENTION)) {
            val convention = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                activity?.intent?.getSerializableExtra(Constants.CONVENTION, Convention::class.java)
            } else {
                @Suppress("DEPRECATION")
                activity?.intent?.getSerializableExtra(Constants.CONVENTION) as Convention
            }
            viewModel.selectConvention(convention!!)
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
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
        viewModel.conventions.observe(viewLifecycleOwner) {
            if (_binding != null) {
                if (it.isEmpty()) {
                    binding.card1.visibility = View.GONE
                    binding.card2.visibility = View.GONE
                    binding.tvTitle.text = getString(R.string.text_no_convention_to_assign)
                    return@observe
                }
                if (it.size > 1) {
                    fillConventionA(it[0])
                    fillConventionB(it[1])
                } else {
                    fillConventionA(it[0])
                    binding.card2.visibility = View.GONE
                }
            }
        }
    }

    private fun fillConventionA(convention: Convention) {
        Glide.with(requireContext())
            .load(convention.image)
            .centerCrop()
            .placeholder(R.drawable.ic_convention_24)
            .into(binding.iv1)
        binding.tvTitle1.text = convention.title
        binding.tvDates1.text = if (convention.dateA == null || convention.dateB == null)
            getString(R.string.text_no_dates)
        else "${Utils.convertDateToString(convention.dateA!!)} / ${Utils.convertDateToString(convention.dateB!!)}"

        binding.card1.setOnClickListener {
            viewModel.selectConvention(convention)
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    private fun fillConventionB(convention: Convention) {
        Glide.with(requireContext())
            .load(convention.image)
            .centerCrop()
            .placeholder(R.drawable.ic_convention_24)
            .into(binding.iv2)
        binding.tvTitle2.text = convention.title
        binding.tvDates2.text = if (convention.dateA == null || convention.dateB == null)
            getString(R.string.text_no_dates)
        else "${Utils.convertDateToString(convention.dateA!!)} / ${Utils.convertDateToString(convention.dateB!!)}"

        binding.card2.setOnClickListener {
            viewModel.selectConvention(convention)
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }
}