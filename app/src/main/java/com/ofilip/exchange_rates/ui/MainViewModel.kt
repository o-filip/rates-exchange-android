package com.ofilip.exchange_rates.ui

import androidx.lifecycle.ViewModel
import com.ofilip.exchange_rates.domain.useCase.GetInternetConnectionStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getInternetConnectionStatusUseCase: GetInternetConnectionStatusUseCase
) : ViewModel() {

    val internetConnectionStatus = getInternetConnectionStatusUseCase.execute()
}
