package com.mirkamal.gamewatch.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mirkamal.gamewatch.model.pojo.GamePOJO
import com.mirkamal.gamewatch.repositories.DiscoverGamesRepository
import com.mirkamal.gamewatch.utils.libs.network.NetworkState
import com.mirkamal.gamewatch.utils.libs.network.ResponseParentData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by Mirkamal on 18 October 2020
 */
class DiscoverGamesViewModel : ViewModel() {

    private val discoverGamesRepository = DiscoverGamesRepository()

    val resultGames = MutableLiveData<List<GamePOJO>>()

    @Suppress("UNCHECKED_CAST")
    fun onSearch(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = discoverGamesRepository.searchForGames(name)
            if (response is NetworkState.Success<*>) {
                withContext(Dispatchers.Main) {
                    resultGames.value = (response.data as? ResponseParentData<List<GamePOJO>>)?.data
                }
            }
        }
    }
}