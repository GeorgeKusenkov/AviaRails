package com.egasmith.aviarails.ui.fragments.home.searchmenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.egasmith.aviarails.R
import com.egasmith.aviarails.databinding.FragmentAllTicketsBinding
import com.egasmith.aviarails.databinding.FragmentDifficultRoutesBinding
import com.egasmith.aviarails.ui.fragments.home.HomeViewModel

class DifficultRoutesFragment : Fragment() {
    private var _binding: FragmentDifficultRoutesBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDifficultRoutesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_difficultRoutesFragment_to_navigation_home)
            homeViewModel.setBackState("AllTickets")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}