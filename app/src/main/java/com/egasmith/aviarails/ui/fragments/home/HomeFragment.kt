package com.egasmith.aviarails.ui.fragments.home

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.egasmith.aviarails.OffersAdapter
import com.egasmith.aviarails.databinding.FragmentHomeBinding
import com.egasmith.domain.models.offer.Offer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeOffers()
        homeViewModel.fetchTickets()
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.offersRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.addItemDecoration(SpaceItemDecoration(140))
    }

    class SpaceItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.right = space
        }
    }

    private fun observeOffers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.offers.collect { state ->
                    when (state) {
                        is TicketsViewState.Loading -> showLoading()
                        is TicketsViewState.Success -> showOffers(state.offerInfo.offers)
                        is TicketsViewState.Error -> showError(state.message)
                    }
                }
            }
        }
    }

    private fun showLoading() {
        binding.loadingProgressBar.visibility = View.VISIBLE
        binding.offersRecyclerview.visibility = View.GONE
    }

    private fun showOffers(offers: List<Offer>) {
        binding.loadingProgressBar.visibility = View.GONE
        binding.offersRecyclerview.visibility = View.VISIBLE
        binding.offersRecyclerview.adapter = OffersAdapter(offers)
    }

    private fun showError(message: String) {
        binding.loadingProgressBar.visibility = View.GONE
        binding.offersRecyclerview.visibility = View.GONE
        // Здесь вы можете показать сообщение об ошибке, например, используя Toast или Snackbar
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}