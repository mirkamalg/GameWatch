package com.mirkamal.gamewatch.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mirkamal.gamewatch.model.entity.Game
import com.mirkamal.gamewatch.repositories.DiscoverGamesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by Mirkamal on 18 October 2020
 */
class DiscoverGamesViewModel : ViewModel() {

    private val discoverGamesRepository = DiscoverGamesRepository()

    private val _resultGames = MutableLiveData<List<Game>>()
    val resultGames: LiveData<List<Game>>
        get() = _resultGames

    @Suppress("UNCHECKED_CAST")
    fun onSearch(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = discoverGamesRepository.searchForGames(name)

            withContext(Dispatchers.Main) {
                if (response != null) {
                    _resultGames.value = response
                } else {
                    _resultGames.value = emptyList()
                }
            }
        }
    }
}