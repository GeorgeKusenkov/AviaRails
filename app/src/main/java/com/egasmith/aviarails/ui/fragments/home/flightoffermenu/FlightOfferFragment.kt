package com.egasmith.aviarails.ui.fragments.home.flightoffermenu

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.egasmith.aviarails.R
import com.egasmith.aviarails.databinding.FragmentFlightOfferBinding
import com.egasmith.aviarails.ui.features.ViewStateHandler
import com.egasmith.aviarails.ui.fragments.home.HomeViewModel
import com.egasmith.aviarails.ui.fragments.home.TicketOffersViewState
import com.egasmith.domain.models.ticketoffers.TicketOffers
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FlightOfferFragment : Fragment() {
    private var _binding: FragmentFlightOfferBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var viewStateHandler: ViewStateHandler<TicketOffers>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFlightOfferBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.fetchTicketsOffers()
        setupRecyclerView()
        setOnClickListeners()
        setupViewStateHandler()
        observeOffers()
    }

    private fun setOnClickListeners() {
        binding.allTicketsButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_allTicketsFragment)
        }
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.flyingRecyclerview
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun setupViewStateHandler() {
        viewStateHandler = ViewStateHandler(
            binding.loadingProgressBar,
            binding.flyingRecyclerview,
            requireContext()
        )
    }

    private fun observeOffers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.ticketOffers.collect { state ->
                    when (state) {
                        is TicketOffersViewState.Loading -> viewStateHandler.showLoading()
                        is TicketOffersViewState.Success -> {
                            Log.d("showOffers", "${state.ticketOffersInfo.ticketsOffers}")
                            viewStateHandler.showData(
                                state.ticketOffersInfo.ticketsOffers,
                                FlightOfferAdapter(state.ticketOffersInfo.ticketsOffers)
                            )
                        }
                        is TicketOffersViewState.Error -> viewStateHandler.showError(state.message)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}