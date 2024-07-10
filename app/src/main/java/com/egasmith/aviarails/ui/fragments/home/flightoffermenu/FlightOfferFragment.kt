package com.egasmith.aviarails.ui.fragments.home.flightoffermenu

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
import com.egasmith.aviarails.databinding.FragmentFlightOfferBinding
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFlightOfferBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        binding.allTicketsButton.setOnClickListener {

        }

        homeViewModel.fetchTicketsOffers()
        observeOffers()
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.flyingRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }


    private fun observeOffers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.ticketOffers.collect { state ->
                    when (state) {
                        is TicketOffersViewState.Loading -> showLoading()
                        is TicketOffersViewState.Success -> {
                            Log.d("showOffers", "${state.offerInfo.ticketsOffers}")
                            showOffers(state.offerInfo.ticketsOffers)
                        }
                        is TicketOffersViewState.Error -> showError(state.message)
                    }
                }
            }
        }
    }

    private fun showOffers(flightOffers: List<TicketOffers>) {
        binding.loadingProgressBar.visibility = View.GONE
        binding.flyingRecyclerview.visibility = View.VISIBLE
        binding.flyingRecyclerview.adapter = FlightOfferAdapter(flightOffers)
    }

    private fun showLoading() {
        binding.loadingProgressBar.visibility = View.VISIBLE
        binding.flyingRecyclerview.visibility = View.GONE
    }

    private fun showError(message: String) {
        binding.loadingProgressBar.visibility = View.GONE
        binding.flyingRecyclerview.visibility = View.GONE
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}