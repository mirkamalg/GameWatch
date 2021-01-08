package com.mirkamal.gamewatch.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mirkamal.gamewatch.model.parcel.Game
import com.mirkamal.gamewatch.model.pojo.GameDealPOJO
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
        GamesRepository()

    private val _resultGames = MutableLiveData<List<Game>>()
    val resultGames: LiveData<List<Game>>
        get() = _resultGames

    private val _wantToPlayGames = MutableLiveData<List<Game>>()
    val wantToPlayGames: LiveData<List<Game>>
        get() = _wantToPlayGames

    private val _screenshots = MutableLiveData<List<ScreenshotPOJO>>()
    val screenshots: LiveData<List<ScreenshotPOJO>>
        get() = _screenshots

    private val _coverUrl = MutableLiveData<String>()
    val coverUrl: LiveData<String>
        get() = _coverUrl

    private val _genreName = MutableLiveData<String>()
    val genreName: LiveData<String>
        get() = _genreName

    private val _gameDeals = MutableLiveData<List<GameDealPOJO>>()
    val gameDeals: LiveData<List<GameDealPOJO>>
        get() = _gameDeals

    private val _similarGames = MutableLiveData<List<Game>>()
    val similarGames: LiveData<List<Game>>
        get() = _similarGames

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
                _wantToPlayGames.value = response
            }
        }
    }

    fun loadScreenShots(gameID: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = gamesRepository.fetchScreenshots(gameID)

            withContext(Dispatchers.Main) {
                _screenshots.value = response
            }
        }
    }

    fun loadCoverPhoto(gameID: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            var url = gamesRepository.fetchCoverByGameID(gameID)
            if (url.isNotEmpty()) {
                url = "https:$url".replace("t_thumb", "t_screenshot_med")
                withContext(Dispatchers.Main) {
                    _coverUrl.value = url
                }
            }
        }
    }

    fun fetchGenre(genreIDs: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val genre = gamesRepository.fetchGenreByIDs(genreIDs)
            if (genre.isNotEmpty()) {
                withContext(Dispatchers.Main) {
                    _genreName.value = genre
                }
            }
        }
    }

    fun fetchDeals(title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val deals = gamesRepository.fetchGameDeals(title)
                .sortedWith(compareBy { it.salePrice?.toDouble() })
            if (deals.isNotEmpty()) {
                withContext(Dispatchers.Main) {
                    _gameDeals.value = deals
                }
            }
        }
    }

    fun fetchSimilarGames(ids: List<Long>) {
        viewModelScope.launch(Dispatchers.IO) {
            val games = gamesRepository.fetchGamesByIDs(ids)
            withContext(Dispatchers.Main) {
                _similarGames.value = games
            }
        }
    }
}