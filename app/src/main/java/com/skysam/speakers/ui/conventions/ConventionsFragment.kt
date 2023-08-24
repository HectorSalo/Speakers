package com.skysam.speakers.ui.conventions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.skysam.speakers.dataClasses.Convention
import com.skysam.speakers.databinding.FragmentConventionsBinding

class ConventionsFragment : Fragment(), OnClick {

    private var _binding: FragmentConventionsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ConventionsViewModel by activityViewModels()
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

    }

    override fun dates(convention: Convention) {
        viewModel.selectDates(convention)
        val dialog = SelectDatesDialog()
        dialog.show(requireActivity().supportFragmentManager, tag)
    }

    override fun speakers(convention: Convention) {

    }
}