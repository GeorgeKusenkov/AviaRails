package com.egasmith.aviarails.ui.fragments.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egasmith.aviarails.R
import com.egasmith.domain.models.offer.Offer
import com.egasmith.domain.models.offer.OfferResponse
import com.egasmith.domain.models.ticket.TicketResponse
import com.egasmith.domain.models.ticketoffers.TicketOffers
import com.egasmith.domain.models.ticketoffers.TicketOffersResponse
import com.egasmith.domain.models.uiticket.UITicketResponse
import com.egasmith.domain.usecases.GetOffersUseCase
import com.egasmith.domain.usecases.GetTicketOffersUseCase
import com.egasmith.domain.usecases.GetTicketsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getOffersUseCase: GetOffersUseCase,
    private val getTicketOffersUseCase: GetTicketOffersUseCase,
    private val getTicketUseCase: GetTicketsUseCase
): ViewModel() {

    private val _ticketOffers = MutableStateFlow<TicketOffersViewState>(value = TicketOffersViewState.Loading)
    val ticketOffers: StateFlow<TicketOffersViewState> = _ticketOffers

    private val _offers = MutableStateFlow<OffersViewState>(value = OffersViewState.Loading)
    val offers: StateFlow<OffersViewState> = _offers

    private val _tickets = MutableStateFlow<TicketsViewState>(value = TicketsViewState.Loading)
    val tickets: StateFlow<TicketsViewState> = _tickets

    private val _recommendedCity = MutableLiveData<String>()
    val recommendedCity: LiveData<String> = _recommendedCity

    init {
        fetchTickets()
        fetchTicketsOffers()
        fetchOffers()
    }

    private var _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text


    fun  fetchTickets() = viewModelScope.launch {
        _tickets.value = TicketsViewState.Loading
        getTicketUseCase().collect { result ->
            _tickets.value = result.fold(
                onSuccess = {response ->
                    TicketsViewState.Success(UITicketResponse(response))
                },
                onFailure = { TicketsViewState.Error(it.message ?: "Unknown error") }
            )
        }
    }

    fun fetchOffers() = viewModelScope.launch {
        _offers.value = OffersViewState.Loading
        getOffersUseCase().collect { result ->
            _offers.value = result.fold(
                onSuccess = { response ->
                    val offersWithImages = addImagesToOffers(response.offers)
                    OffersViewState.Success(OfferResponse(offersWithImages))
                },
                onFailure = { OffersViewState.Error(it.message ?: "Unknown error") }
            )
        }
    }

    fun fetchTicketsOffers() = viewModelScope.launch {
        _ticketOffers.value = TicketOffersViewState.Loading
        getTicketOffersUseCase().collect { result ->
            _ticketOffers.value = result.fold(
                onSuccess = { response ->
                    val offersWithImages = addCirclesToOffersTickets(response.ticketsOffers)
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
    data class Success(val ticketInfo: UITicketResponse) : TicketsViewState
}

sealed interface OffersViewState {
    data object Loading : OffersViewState
    data class Error(val message: String) : OffersViewState
    data class Success(val offerInfo: OfferResponse) : OffersViewState
}

sealed interface TicketOffersViewState {
    data object Loading : TicketOffersViewState
    data class Error(val message: String) : TicketOffersViewState
    data class Success(val ticketOffersInfo: TicketOffersResponse) : TicketOffersViewState
}