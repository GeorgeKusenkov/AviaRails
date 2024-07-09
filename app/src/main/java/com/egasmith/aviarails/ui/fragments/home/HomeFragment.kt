package com.egasmith.aviarails.ui.fragments.home

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.egasmith.aviarails.OffersAdapter
import com.egasmith.aviarails.R
import com.egasmith.aviarails.databinding.FragmentHomeBinding
import com.egasmith.domain.models.offer.Offer
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var cardView: CardView
    private lateinit var endCity: TextInputEditText
    private lateinit var startCity: TextInputEditText
    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var searchMenu: ConstraintLayout
    private var originalWidth: Int = 0
    private var originalHeight: Int = 0
    private lateinit var innerCardView: CardView
    private var originalInnerHeight: Int = 0
    private var originalInnerMargin: Int = 0
    private var isCardExpanded = false

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

        startCity = binding.startCity
        endCity = binding.endCity
        cardView = binding.cardView
        constraintLayout = binding.constraintLayout
        searchMenu = binding.searchMenuViewGroup
        innerCardView = binding.innerCardView

        cardView.post {
            originalWidth = cardView.width
            originalHeight = cardView.height
            originalInnerHeight = innerCardView.height
            originalInnerMargin = (innerCardView.layoutParams as ViewGroup.MarginLayoutParams).topMargin
        }

        endCity.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                expandCardView()
            } else {
                collapseCardView()
            }
        }

        val cyrillicTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()
                if (input.isNotEmpty() && !containsOnlyCyrillic(input)) {
                    s?.clear()
                    Toast.makeText(context, "Пожалуйста, используйте только кириллицу", Toast.LENGTH_SHORT).show()
                }
            }
        }

        startCity.addTextChangedListener(cyrillicTextWatcher)
        endCity.addTextChangedListener(cyrillicTextWatcher)
    }

    private fun expandCardView() {
        if (isCardExpanded) return
        isCardExpanded = true
        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 300
            addUpdateListener { animation ->
                val value = animation.animatedValue as Float
                val params = cardView.layoutParams as ConstraintLayout.LayoutParams
                params.width = (originalWidth + (constraintLayout.width - originalWidth) * value).toInt()
                params.height = (originalHeight + (constraintLayout.height - originalHeight) * value).toInt()
                cardView.layoutParams = params
                searchMenu.visibility = View.VISIBLE
                // Оставляем innerCardView без изменений
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    val params = cardView.layoutParams as ConstraintLayout.LayoutParams
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT
                    params.height = ViewGroup.LayoutParams.MATCH_PARENT
                    cardView.layoutParams = params
                    cardView.bringToFront()

                    // Не меняем положение innerCardView
                }
            })
            start()
        }
    }

    private fun collapseCardView() {
        if (!isCardExpanded) return
        isCardExpanded = false
        ValueAnimator.ofFloat(1f, 0f).apply {
            duration = 300
            addUpdateListener { animation ->
                val value = animation.animatedValue as Float
                val params = cardView.layoutParams as ConstraintLayout.LayoutParams
                params.width = (originalWidth + (constraintLayout.width - originalWidth) * value).toInt()
                params.height = (originalHeight + (constraintLayout.height - originalHeight) * value).toInt()
                cardView.layoutParams = params
                searchMenu.visibility = View.GONE
                // Оставляем innerCardView без изменений
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    val params = cardView.layoutParams as ConstraintLayout.LayoutParams
                    params.width = originalWidth
                    params.height = originalHeight
                    cardView.layoutParams = params

                    // Не меняем положение innerCardView
                }
            })
            start()
        }
    }

    private fun containsOnlyCyrillic(input: String): Boolean {
        return input.matches(Regex("[а-яА-ЯёЁ]+"))
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