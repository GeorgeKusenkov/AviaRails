package com.egasmith.aviarails.ui.fragments.home.alltickets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.egasmith.aviarails.R
import com.egasmith.aviarails.databinding.FragmentAllTicketsBinding
import com.egasmith.aviarails.ui.features.ViewStateHandler
import com.egasmith.aviarails.ui.fragments.home.HomeViewModel
import com.egasmith.aviarails.ui.fragments.home.TicketsViewState
import com.egasmith.domain.models.uiticket.UiTicket
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllTicketsFragment : Fragment() {
    private var _binding: FragmentAllTicketsBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by activityViewModels()

    private lateinit var viewStateHandler: ViewStateHandler<UiTicket>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllTicketsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupViewStateHandler()
        observeViewModel()
        setupOnClickListener()
    }

    private fun setupRecyclerView() {
        binding.allTicketsRecyclerview.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun observeViewModel() {
        homeViewModel.fetchTickets()
        homeViewModel.fetchTicketsOffers()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.tickets.collect { state ->
                    when (state) {
                        is TicketsViewState.Loading -> viewStateHandler.showLoading()
                        is TicketsViewState.Success -> {
                            viewStateHandler.showData(
                                state.ticketInfo.ticket,
                                AllTicketsAdapter(state.ticketInfo.ticket)
                            )
                        }
                        is TicketsViewState.Error -> viewStateHandler.showError(state.message)
                    }
                }
            }
        }
        observeCityNames()
        observeSelectedCounter()
    }

    private fun observeCityNames() {
        homeViewModel.startCityText.observe(viewLifecycleOwner) { startCity ->
            homeViewModel.endCityText.observe(viewLifecycleOwner) { endCity ->
                val fromToCity = "$startCity-$endCity"
                binding.fromToCity.text = fromToCity
            }
        }
    }

    private fun observeSelectedCounter() {
        val combinedLiveData = MediatorLiveData<Pair<String, String>>().apply {
            addSource(homeViewModel.selectedCounter) { count ->
                value = homeViewModel.getFormattedCounter() to homeViewModel.getFormattedDate()
            }
            addSource(homeViewModel.selectedDate) { date ->
                value = homeViewModel.getFormattedCounter() to homeViewModel.getFormattedDate()
            }
        }

        combinedLiveData.observe(viewLifecycleOwner) { (text1, text2) ->
            binding.flyInfo.text = "$text1, $text2"
        }
    }

    private fun setupViewStateHandler() {
        viewStateHandler = ViewStateHandler(
            binding.loadingProgressBar,
            binding.allTicketsRecyclerview,
            requireContext()
        )
    }

    private fun setupOnClickListener() {
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_allTicketsFragment_to_navigation_home)
            homeViewModel.setBackState("AllTickets")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}