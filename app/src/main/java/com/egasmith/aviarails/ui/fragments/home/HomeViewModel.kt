package com.egasmith.aviarails.ui.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egasmith.domain.models.offer.OfferResponse
import com.egasmith.domain.usecases.GetOffersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val getOffersUseCase: GetOffersUseCase): ViewModel() {

    private var _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text


    private val _tickets = MutableStateFlow<TicketsViewState>(value = TicketsViewState.Loading)
    val tickets: StateFlow<TicketsViewState> = _tickets

    fun fetchTickets() = viewModelScope.launch {
        _tickets.value = TicketsViewState.Loading
        getOffersUseCase().collect { result ->
            _tickets.value = result.fold(
                onSuccess = { TicketsViewState.Success(it) },
                onFailure = { TicketsViewState.Error(it.message ?: "Unknown error") }
            )
        }
    }
}

sealed interface TicketsViewState {
    data object Loading: TicketsViewState
    data class  Error(val message: String): TicketsViewState
    data class Success(val offerInfo: OfferResponse): TicketsViewState
}