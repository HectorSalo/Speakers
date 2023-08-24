package com.skysam.speakers.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.skysam.speakers.R
import com.skysam.speakers.common.Utils
import com.skysam.speakers.dataClasses.Convention
import com.skysam.speakers.dataClasses.Speaker
import com.skysam.speakers.dataClasses.Speech
import com.skysam.speakers.databinding.FragmentHomeBinding
import java.util.Date

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()
    private var speeches = listOf<Speech>()
    private val speakers = listOf<Speaker>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun subscribeObservers() {
        viewModel.speeches.observe(viewLifecycleOwner) {
            if (_binding != null) {
                speeches = it
            }
        }
        viewModel.conventions.observe(viewLifecycleOwner) {
            if (_binding != null) {
                fillConventionA(it[0])
                fillConventionB(it[1])
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
        val speechesFromConvention = mutableListOf<Speech>()
        for (speech in speeches) {
            if (speech.conventionId == convention.id) {
                speechesFromConvention.add(speech)
            }
        }
        var totalSpeakers = speakers.size

        val totalSpeeches = speechesFromConvention.size * 2

        if (totalSpeeches == totalSpeakers) {
            binding.tvSpeakers1.text = getString(R.string.text_assing_complete)
            if (convention.dateB != null && convention.dateB!!.before(Date())) {
                binding.btnAssign1.visibility = View.GONE
            }
        }
        if (totalSpeeches > totalSpeakers) {
            binding.tvSpeakers1.text = getString(R.string.text_assing_incomplete,
                (totalSpeeches - totalSpeakers).toString())
            binding.btnAssign1.text = getString(R.string.text_assign_speakers)
            if (totalSpeakers == 0) binding.btnView1.visibility = View.GONE
            else binding.btnView1.visibility = View.VISIBLE
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
        val speechesFromConvention = mutableListOf<Speech>()
        for (speech in speeches) {
            if (speech.conventionId == convention.id) {
                speechesFromConvention.add(speech)
            }
        }
        var totalSpeakers = speakers.size
        val totalSpeeches = speechesFromConvention.size * 2

        if (totalSpeeches == totalSpeakers) {
            binding.tvSpeakers2.text = getString(R.string.text_assing_complete)
            if (convention.dateB != null && convention.dateB!!.before(Date())) {
                binding.btnAssign2.visibility = View.GONE
            }
        }
        if (totalSpeeches > totalSpeakers) {
            binding.tvSpeakers2.text = getString(R.string.text_assing_incomplete,
                (totalSpeeches - totalSpeakers).toString())
            binding.btnAssign2.text = getString(R.string.text_assign_speakers)
            if (totalSpeakers == 0) binding.btnView2.visibility = View.GONE
            else binding.btnView2.visibility = View.VISIBLE
        }
    }
}