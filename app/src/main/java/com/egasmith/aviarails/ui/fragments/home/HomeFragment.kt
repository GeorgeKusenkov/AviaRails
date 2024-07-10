package com.egasmith.aviarails.ui.fragments.home

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.egasmith.aviarails.OffersAdapter
import com.egasmith.aviarails.R
import com.egasmith.aviarails.databinding.FragmentHomeBinding
import com.egasmith.aviarails.ui.features.CardAnimator
import com.egasmith.aviarails.ui.features.TextInputManager
import com.egasmith.aviarails.ui.features.ViewStateHandler
import com.egasmith.aviarails.ui.fragments.home.flightoffermenu.FlightOfferFragment
import com.egasmith.aviarails.ui.fragments.home.searchmenu.SearchMenuFragment
import com.egasmith.domain.models.offer.Offer
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by activityViewModels()
    private var isSearchMenuVisible = false

    private lateinit var viewStateHandler: ViewStateHandler<Offer>

    private val cardAnimator by lazy {
        CardAnimator(binding.cardView, binding.constraintLayout)
    }

    private val textInputManager by lazy {
        TextInputManager(binding.startCity, binding.endCity)
    }

    private val searchMenuFragment by lazy { SearchMenuFragment() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()

        binding.icX.setOnClickListener {
            homeViewModel.cleanEndCity()
        }

        setupRecyclerView()

        setupViewStateHandler()
        observeOffers()
        observeRecommendedCity()
        homeViewModel.fetchOffers()



    }

    private fun setupViews() = with(binding) {
        startCity.setup { textInputManager.onStartCityTextChanged(it) }
        endCity.setup { textInputManager.onEndCityTextChanged(it) }

        startCity.setOnFocusChangeListener { _, _ -> updateViewsOnFocus() }
        endCity.setOnFocusChangeListener { _, hasFocus -> updateViewsOnFocus(hasFocus) }

        endCity.setOnEditorActionListener { _, actionId, _ ->
            if (actionId.isSubmitAction() && textInputManager.areFieldsFilled()) {
                submitCities()
                true
            } else false
        }
    }

    private fun TextInputEditText.setup(onTextChanged: (String) -> Unit) {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = onTextChanged(s.toString())
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun updateViewsOnFocus(hasFocus: Boolean = false) {
        if (hasFocus || !binding.endCity.text.isNullOrEmpty()) {
            cardAnimator.expandCard()
            showSearchMenu()
            updateImageViewAttributes()
        } else {
            cardAnimator.collapseCard()
            hideSearchMenu()
        }
        textInputManager.updateImeOptions()
    }

    private fun updateImageViewAttributes() {
        // Update attributes for ic_search
        val icSearchLayoutParams = binding.icSearch.layoutParams as ConstraintLayout.LayoutParams
        icSearchLayoutParams.bottomMargin = 10.dpToPx()
        icSearchLayoutParams.topToTop = ConstraintLayout.LayoutParams.UNSET
        binding.icSearch.layoutParams = icSearchLayoutParams

        // Set ic_planer visibility to VISIBLE
        binding.icPlaner.visibility = View.VISIBLE

        // Set ic_x visibility to VISIBLE
        binding.icX.visibility = View.VISIBLE
    }

    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }

    private fun submitCities() {
        hideKeyboard()
        cardAnimator.collapseCard()
        hideSearchMenu()
        showFlightOfferFragment()
        binding.flying.visibility = View.GONE
        binding.offersRecyclerview.visibility = View.GONE
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun showSearchMenu() {
        if (!isSearchMenuVisible) {
            isSearchMenuVisible = true
            if (childFragmentManager.findFragmentById(R.id.search_menu_container) == null) {
                childFragmentManager.beginTransaction()
                    .add(R.id.search_menu_container, searchMenuFragment)
                    .commit()
            }
            binding.searchMenuContainer.visibility = View.VISIBLE
        }
    }

    private fun hideSearchMenu() {
        if (isSearchMenuVisible) {
            isSearchMenuVisible = false
            binding.searchMenuContainer.visibility = View.GONE
        }
    }

    private fun showFlightOfferFragment() {
        val flightOfferFragment = FlightOfferFragment()
        childFragmentManager.beginTransaction()
            .add(R.id.flight_offer_container, flightOfferFragment)
            .addToBackStack(null)
            .commit()
        binding.flightOfferContainer.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        binding.offersRecyclerview.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(SpaceItemDecoration(140))
        }
    }

    private fun setupViewStateHandler() {
        viewStateHandler = ViewStateHandler(
            binding.loadingProgressBar,
            binding.offersRecyclerview,
            requireContext()
        )
    }

    private fun observeOffers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.offers.collect { state ->
                    when (state) {
                        is OffersViewState.Loading -> viewStateHandler.showLoading()
                        is OffersViewState.Success -> viewStateHandler.showData(state.offerInfo.offers, OffersAdapter(state.offerInfo.offers))
                        is OffersViewState.Error -> viewStateHandler.showError(state.message)
                    }
                }
            }
        }
    }

    private fun observeRecommendedCity() {
        homeViewModel.recommendedCity.observe(viewLifecycleOwner) { city ->
            binding.endCity.setText(city)
        }
    }

    class SpaceItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.right = space
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}

fun Int.isSubmitAction() = this in listOf(EditorInfo.IME_ACTION_DONE, EditorInfo.IME_ACTION_GO, EditorInfo.IME_ACTION_NEXT, EditorInfo.IME_ACTION_SEND)