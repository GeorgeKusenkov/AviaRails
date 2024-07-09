package com.egasmith.aviarails.ui.fragments.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egasmith.aviarails.R
import com.egasmith.domain.models.offer.Offer
import com.egasmith.domain.models.offer.OfferResponse
import com.egasmith.domain.models.ticketoffers.TicketOffers
import com.egasmith.domain.models.ticketoffers.TicketOffersResponse
import com.egasmith.domain.usecases.GetOffersUseCase
import com.egasmith.domain.usecases.GetTicketOffersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getOffersUseCase: GetOffersUseCase,
    private val getTicketOffers: GetTicketOffersUseCase
): ViewModel() {

    private val _ticketOffers = MutableStateFlow<TicketOffersViewState>(value = TicketOffersViewState.Loading)
    val ticketOffers: StateFlow<TicketOffersViewState> = _ticketOffers

    private val _offers = MutableStateFlow<TicketsViewState>(value = TicketsViewState.Loading)
    val offers: StateFlow<TicketsViewState> = _offers

    private val _recommendedCity = MutableLiveData<String>()
    val recommendedCity: LiveData<String> = _recommendedCity

    init {
        fetchTicketsOffers()
        fetchTickets()
    }

    private var _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text


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

    fun fetchTicketsOffers() = viewModelScope.launch {
        _ticketOffers.value = TicketOffersViewState.Loading

        Log.d("showOffers3", "1: ${_ticketOffers.value}")

        getTicketOffers().collect { result ->
            _ticketOffers.value = result.fold(
                onSuccess = { response ->
                    Log.d("showOffers3", "2: $response")
                    val offersWithImages = addCirclesToOffersTickets(response.ticketsOffers)
                    Log.d("showOffers3", "3: $offersWithImages")
                    TicketOffersViewState.Success(TicketOffersResponse(offersWithImages))
                },
                onFailure = { TicketOffersViewState.Error(it.message ?: "Unknown error") }
            )
        }
    }

    fun setRecommendedCity(city: String) {
        _recommendedCity.value = city
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

private fun addCirclesToOffersTickets(ticketOffers: List<TicketOffers>): List<TicketOffers> {
    return ticketOffers.mapIndexed { index, offer ->
        offer.copy(
            color = when (index) {
                0 -> R.color.red
                1 -> R.color.blue
                2 -> R.color.gray6
                else -> R.color.green
            }
        )
    }
}

sealed interface TicketsViewState {
    data object Loading : TicketsViewState
    data class Error(val message: String) : TicketsViewState
    data class Success(val offerInfo: OfferResponse) : TicketsViewState
}

sealed interface TicketOffersViewState {
    data object Loading : TicketOffersViewState
    data class Error(val message: String) : TicketOffersViewState
    data class Success(val offerInfo: TicketOffersResponse) : TicketOffersViewState
}