package com.egasmith.aviarails.ui.fragments.home

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
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


        if (homeViewModel.backState.value == "AllTickets") {
            submitCities()
            setupViews()
            setOnClickListeners()
        } else {
            setupViews()
            setupRecyclerView()
            setupViewStateHandler()
            setOnClickListeners()
            observeOffers()
            observeRecommendedCity()
            homeViewModel.fetchOffers()
        }
    }

    private fun showSearchMenu() {
        isSearchMenuVisible = true
        binding.searchMenuContainer.visibility = View.VISIBLE
        if (childFragmentManager.findFragmentById(R.id.search_menu_container) == null) {
            childFragmentManager.beginTransaction()
                .add(R.id.search_menu_container, searchMenuFragment)
                .commit()
        }
    }

    private fun setOnClickListeners() {
        binding.icX.setOnClickListener {
            homeViewModel.cleanEndCity()
        }
        binding.icReplace.setOnClickListener {
            updateCitiesState()
            swapCities()
        }
    }

    private fun hideSearchMenu() {
        isSearchMenuVisible = false
        binding.searchMenuContainer.visibility = View.GONE
    }

    private fun swapCities() {
        homeViewModel.startCityText.observe(viewLifecycleOwner) { city ->
            binding.endCity.setText(city)
        }
        homeViewModel.endCityText.observe(viewLifecycleOwner) { city ->
            binding.startCity.setText(city)
        }
    }

    private fun updateCitiesState() {
        val startCity = binding.startCity.text.toString()
        val endCity = binding.endCity.text.toString()
        homeViewModel.updateCityTexts(startCity, endCity)
    }

    private fun setupViews() = with(binding) {
        startCity.setup { textInputManager.onStartCityTextChanged(it) }
        endCity.setup { textInputManager.onEndCityTextChanged(it) }

        startCity.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) handleStartCityFocus()
        }

        endCity.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) handleEndCityFocus()
        }

        startCity.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE && textInputManager.areFieldsFilled()) {
                updateCitiesState()
                submitCities()
                true
            } else false
        }

        endCity.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE && textInputManager.areFieldsFilled()) {
                updateCitiesState()
                submitCities()
                true
            } else false
        }

        startCity.setOnClickListener {
            handleCityFieldFocus()
        }
        endCity.setOnClickListener {
            handleCityFieldFocus()
        }
    }

    private fun handleStartCityFocus() {
        if (binding.flightOfferContainer.visibility == View.VISIBLE) {
            binding.flightOfferContainer.visibility = View.GONE
            showSearchMenu()
            cardAnimator.expandCard()
        }
    }

    private fun handleEndCityFocus() {
        binding.flightOfferContainer.visibility = View.GONE
        setSearchIconsPosition()
        showSearchMenu()
        cardAnimator.expandCard()
    }

    private fun setSearchIconsPosition() {
        binding.icX.visibility = View.VISIBLE
        binding.icPlaner.visibility = View.VISIBLE
        val icSearchLayoutParams = binding.icSearch.layoutParams as ConstraintLayout.LayoutParams
        icSearchLayoutParams.bottomMargin = 10.dpToPx()
        icSearchLayoutParams.topToTop = ConstraintLayout.LayoutParams.UNSET
        binding.icSearch.layoutParams = icSearchLayoutParams
    }

    private fun TextInputEditText.setup(onTextChanged: (String) -> Unit) {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = onTextChanged(s.toString())
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun updateSearchingIconsAttributes() {
        setSearchIconsPosition()
        binding.offersRecyclerview.visibility = View.GONE
        binding.flying.visibility = View.GONE
        binding.icReplace.visibility = View.VISIBLE
        binding.loadingProgressBar.visibility = View.GONE
        setOnClickListeners()
    }

    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }

    private fun submitCities() {
        homeViewModel.startCityText.observe(viewLifecycleOwner) { startCity ->
            binding.startCity.setText(startCity)
        }
        homeViewModel.endCityText.observe(viewLifecycleOwner) { endCity ->
            binding.endCity.setText(endCity)
        }

        hideSearchMenu()
        hideKeyboard()
        cardAnimator.collapseCard()
        showFlightOfferFragment()
        updateSearchingIconsAttributes()
    }

    private fun handleCityFieldFocus() {
        binding.flightOfferContainer.visibility = View.GONE
        if (binding.searchMenuContainer.visibility != View.VISIBLE) {
            showSearchMenu()
        }

        cardAnimator.expandCard()
        updateSearchingIconsAttributes()
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun showFlightOfferFragment() {
        val flightOfferFragment = FlightOfferFragment()
        childFragmentManager.beginTransaction()
            .replace(R.id.flight_offer_container, flightOfferFragment)
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
                        is OffersViewState.Success -> viewStateHandler.showData(
                            state.offerInfo.offers,
                            OffersAdapter(state.offerInfo.offers)
                        )

                        is OffersViewState.Error -> viewStateHandler.showError(state.message)
                    }
                }
            }
        }
    }

    fun updateEndCity(city: String) {
        binding.endCity.setText(city)
        homeViewModel.updateCityTexts(binding.startCity.text.toString(), city)
    }

    private fun observeRecommendedCity() {
        homeViewModel.recommendedCity.observe(viewLifecycleOwner) { city ->
            binding.endCity.setText(city)
        }
    }

    class SpaceItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.right = space
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        isSearchMenuVisible = false
    }
}

fun Int.isSubmitAction() = this in listOf(
    EditorInfo.IME_ACTION_DONE,
    EditorInfo.IME_ACTION_GO,
    EditorInfo.IME_ACTION_NEXT,
    EditorInfo.IME_ACTION_SEND
)