package com.egasmith.aviarails.ui.fragments.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egasmith.aviarails.R
import com.egasmith.domain.models.offer.Offer
import com.egasmith.domain.models.offer.OfferResponse
import com.egasmith.domain.usecases.GetOffersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getOffersUseCase: GetOffersUseCase
): ViewModel() {

    private val _recommendedCity = MutableLiveData<String>()
    val recommendedCity: LiveData<String> = _recommendedCity

    val str = "123"

    private var _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    private val _offers = MutableStateFlow<TicketsViewState>(value = TicketsViewState.Loading)
    val offers: StateFlow<TicketsViewState> = _offers

    fun fetchTickets() = viewModelScope.launch {
        _offers.value = TicketsViewState.Loading
        getOffersUseCase().collect { result ->
            _offers.value = result.fold(
                onSuccess = { response ->
                    val offersWithImages = addImagesToOffers(response.offers)
                    TicketsViewState.Success(OfferResponse(offersWithImages))
                },
                onFailure = { TicketsViewState.Error(it.message ?: "Unknown error") }
            )
        }
    }

    fun setRecommendedCity(city: String) {
        Log.d("setRecommendedCity", "CITY: START")
        Log.d("setRecommendedCity", "CITY: ${city}")
        _recommendedCity.value = city
        Log.d("setRecommendedCity", "CITY: ${_recommendedCity.value}")
    }
}


private fun addImagesToOffers(offers: List<Offer>): List<Offer> {
    return offers.mapIndexed { index, offer ->
        offer.copy(
            image = when (index) {
                0 -> R.drawable.img_offer1
                1 -> R.drawable.img_offer2
                2 -> R.drawable.img_offer3
                else -> R.drawable.img_offer_default
            }
        )
    }
}

sealed interface TicketsViewState {
    data object Loading : TicketsViewState
    data class Error(val message: String) : TicketsViewState
    data class Success(val offerInfo: OfferResponse) : TicketsViewState
}