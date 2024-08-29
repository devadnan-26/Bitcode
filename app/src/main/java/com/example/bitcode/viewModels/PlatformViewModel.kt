package com.example.bitcode.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bitcode.Items.Items
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject

class PlatformViewModel: ViewModel() {
    val aChosen = MutableLiveData(false)

    val bChosen = MutableLiveData(false)

    val cChosen = MutableLiveData(false)

    val whichNavItemChosen = MutableLiveData(Items.Home)

    val chosenDate = MutableLiveData("")


    private val _searchValue = MutableLiveData("")
    val searchValue get() = _searchValue

    val numberOfTries = MutableLiveData(0)

    private val _array = MutableLiveData(JSONArray())
    val array get() = _array

    private fun changeAValue(value: Boolean) { aChosen.value = value }
    fun changeSearchValue(value: String) { _searchValue.value = value }
    private fun changeBValue(value: Boolean) { bChosen.value = value }
    private fun changeCValue(value: Boolean) { cChosen.value = value }
    fun changeDate(value: String) { chosenDate.value = value; numberOfTries.value = numberOfTries.value?.plus(
        1
    )
    }

    fun put(value: JSONObject) {
        _array.value?.put(value)
    }

    fun resetArray() {
        _array.value = JSONArray()
    }

    fun whichChosen(): Int? {
        return when {
            aChosen.value == true -> 0
            bChosen.value == true -> 1
            cChosen.value == true -> 2
            else -> null
        }
    }

    fun changeItem(item: Items) {
        whichNavItemChosen.value = item
    }
    fun change(letter: String, value: Boolean) {
        when (letter) {
            "A" ->  {
                changeAValue(value)
                if (value) {
                    changeBValue(false)
                    changeCValue(false)
                }
            }
            "B" -> {
                changeBValue(value)
                if (value) {
                    changeAValue(false)
                    changeCValue(false)
                }
            }
            "C" -> {
                changeCValue(value)
                if (value) {
                    changeAValue(false)
                    changeBValue(false)
                }
            }
        }
    }
    fun reset() {
        changeAValue(false)
        changeBValue(false)
        changeCValue(false)
    }
}