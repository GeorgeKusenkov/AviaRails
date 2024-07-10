package com.egasmith.aviarails.ui.fragments.home.alltickets

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.egasmith.aviarails.databinding.FragmentAllTicketsBinding
import com.egasmith.aviarails.ui.features.ViewStateHandler
import com.egasmith.aviarails.ui.fragments.home.HomeViewModel
import com.egasmith.aviarails.ui.fragments.home.TicketsViewState
import com.egasmith.domain.models.offer.Offer
import com.egasmith.domain.models.ticket.Ticket
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
        homeViewModel.fetchTicketsOffers()
        observeTickets()
        setupViewStateHandler()
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.allTicketsRecyclerview
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun observeTickets() {
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
    }

    private fun setupViewStateHandler() {
        viewStateHandler = ViewStateHandler(
            binding.loadingProgressBar,
            binding.allTicketsRecyclerview,
            requireContext()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}