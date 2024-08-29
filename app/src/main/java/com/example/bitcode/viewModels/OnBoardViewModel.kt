package com.example.bitcode.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OnBoardViewModel : ViewModel() {

    private var _onBoardFirstState = MutableLiveData(false)
    val onBoardFirstState get() = _onBoardFirstState

    private var _onBoardSecondState = MutableLiveData(false)
    val onBoardSecondState get() = _onBoardSecondState

    private var _onBoardThirdState = MutableLiveData(false)
    val onBoardThirdState get() = _onBoardThirdState

    private var _onBoardFouthState = MutableLiveData(false)
    val onBoardFourthState get() = _onBoardFouthState

    fun changeFirstState(value: Boolean) {
        _onBoardFirstState.value = value
    }

    fun changeSecondState(value: Boolean) {
        _onBoardSecondState.value = value
    }

    fun changeThirdState(value: Boolean) {
        _onBoardThirdState.value = value
    }

    fun changeFourthState(value: Boolean) {
        _onBoardFouthState.value = value
    }

}