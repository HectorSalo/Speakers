package com.skysam.speakers.ui.conventions

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.skysam.speakers.common.Constants
import com.skysam.speakers.dataClasses.Convention
import com.skysam.speakers.databinding.FragmentConventionsBinding
import com.skysam.speakers.ui.assign.AssignActivity
import com.skysam.speakers.ui.speeches.SpeechViewModel
import com.skysam.speakers.ui.speeches.ViewSpeechesDialog

class ConventionsFragment : Fragment(), OnClick {

    private var _binding: FragmentConventionsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ConventionsViewModel by activityViewModels()
    private val viewModelSpeech: SpeechViewModel by activityViewModels()
    private lateinit var conventionsAdapter: ConventionsAdapter
    private var conventions = listOf<Convention>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConventionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        conventionsAdapter = ConventionsAdapter(this)
        binding.rvConventions.apply {
            setHasFixedSize(true)
            adapter = conventionsAdapter
        }

        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.conventions.observe(viewLifecycleOwner) {
            if (_binding != null) {
                if (it.isNotEmpty()) {
                    binding.rvConventions.visibility = View.VISIBLE
                    binding.tvListEmpty.visibility = View.GONE
                    conventionsAdapter.updateList(it)
                    conventions = it
                } else {
                    binding.rvConventions.visibility = View.GONE
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

    override fun view(convention: Convention) {
        viewModelSpeech.viewConvention(convention)
        val viewSpeechesDialog = ViewSpeechesDialog()
        viewSpeechesDialog.show(requireActivity().supportFragmentManager, tag)
    }

    override fun dates(convention: Convention) {
        viewModel.selectDates(convention)
        val dialog = SelectDatesDialog()
        dialog.show(requireActivity().supportFragmentManager, tag)
    }

    override fun speakers(convention: Convention) {
        val intent = Intent(requireContext(), AssignActivity::class.java)
        intent.putExtra(Constants.CONVENTION, convention)
        startActivity(intent)
    }
}