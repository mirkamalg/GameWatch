package com.mirkamal.gamewatch.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mirkamal.gamewatch.local.GamesDataBase
import com.mirkamal.gamewatch.model.entity.Game
import com.mirkamal.gamewatch.model.entity.GameEntity
import com.mirkamal.gamewatch.model.pojo.ScreenshotPOJO
import com.mirkamal.gamewatch.repositories.GamesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by Mirkamal on 18 October 2020
 */
class GamesViewModel(application: Application) : AndroidViewModel(application) {

    private val gamesRepository =
        GamesRepository(GamesDataBase.getInstance(application.applicationContext).gameDao())

    private val _resultGames = MutableLiveData<List<Game>>()
    val resultGames: LiveData<List<Game>>
        get() = _resultGames

    private val _wantToPlayGames = MutableLiveData<List<Game>>()
    val wantToPlayGames: LiveData<List<Game>>
        get() = _wantToPlayGames

    private val _screenshots = MutableLiveData<List<ScreenshotPOJO>>()
    val screenshots: LiveData<List<ScreenshotPOJO>>
        get() = _screenshots

    @Suppress("UNCHECKED_CAST")
    fun onSearch(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = gamesRepository.searchForGames(name)

            withContext(Dispatchers.Main) {
                if (response != null) {
                    _resultGames.value = response
                } else {
                    _resultGames.value = emptyList()
                }
            }
        }
    }

    fun onLoadUserGames(ids: List<Long>) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = gamesRepository.fetchGamesByIDs(ids)

            withContext(Dispatchers.Main) {
                if (response != null) {
                    _wantToPlayGames.value = response
                } else {
                    _wantToPlayGames.value = emptyList()
                }
            }
        }
    }

    fun fetchGameFromLocalDatabase(ID: Long): GameEntity {
        return gamesRepository.fetchGame(ID)
    }

    fun loadScreenShots(gameID: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = gamesRepository.fetchScreenshots(gameID)

            withContext(Dispatchers.Main) {
                _screenshots.value = response
            }
        }
    }
}