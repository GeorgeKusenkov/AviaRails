package com.egasmith.aviarails.ui.fragments.home.searchmenu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.egasmith.aviarails.R
import com.egasmith.aviarails.databinding.FragmentSearchMenuBinding
import com.egasmith.aviarails.ui.fragments.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchMenuFragment : Fragment() {
    private var _binding: FragmentSearchMenuBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sochiImg.setOnClickListener{
            homeViewModel.setRecommendedCity("Сочи")
        }

        binding.phuketImg.setOnClickListener{
            homeViewModel.setRecommendedCity("Пхукет")
        }

        binding.stambulImage.setOnClickListener{
            homeViewModel.setRecommendedCity("Стамбул")
        }


        binding.iconDifficultRoute.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_difficultRoutesFragment)
        }

        binding.iconAnywhere.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_anywhereFragment)
        }

        binding.iconWeekends.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_weekendsFragment)
        }

        binding.iconHotTickets.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_hotTicketsFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}