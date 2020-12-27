package com.mirkamal.gamewatch.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mirkamal.gamewatch.utils.*
import com.mirkamal.gamewatch.utils.libs.livedata.ValidationResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by Mirkamal on 27 December 2020
 */
class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val db = Firebase.firestore
    private val email = Firebase.auth.currentUser?.email ?: ""

    private val _userNameValidationResult = MutableLiveData<ValidationResult>()
    val userNameValidationResult: LiveData<ValidationResult>
        get() = _userNameValidationResult

    private val _steamDisplayNameValidationResult = MutableLiveData<ValidationResult>()
    val steamDisplayNameValidationResult: LiveData<ValidationResult>
        get() = _steamDisplayNameValidationResult

    private val _epicGamesDisplayNameValidationResult = MutableLiveData<ValidationResult>()
    val epicGamesDisplayNameValidationResult: LiveData<ValidationResult>
        get() = _epicGamesDisplayNameValidationResult

    private val _uplayDisplayNameValidationResult = MutableLiveData<ValidationResult>()
    val uplayDisplayNameValidationResult: LiveData<ValidationResult>
        get() = _uplayDisplayNameValidationResult

    private val _discordDisplayNameValidationResult = MutableLiveData<ValidationResult>()
    val discordDisplayNameValidationResult: LiveData<ValidationResult>
        get() = _discordDisplayNameValidationResult

    private val _steamURLValidationResult = MutableLiveData<ValidationResult>()
    val steamURLValidationResult: LiveData<ValidationResult>
        get() = _steamURLValidationResult

    private lateinit var username: String
    private lateinit var bio: String

    init {
        db.collection(USER_DATA_COLLECTION_KEY).document(email).get().addOnSuccessListener {
            username = it[USERNAME_KEY] as String
            bio = it[BIO_KEY] as String
        }
    }

    fun validateUsername(username: String) {
        val validationResult = ValidationResult()
        viewModelScope.launch(Dispatchers.IO) {
            when {
                username == this@ProfileViewModel.username -> {
                    validationResult.isValid = false
                    validationResult.message = "Username is same as the current."
                    withContext(Dispatchers.Main) {
                        _userNameValidationResult.value = validationResult
                    }
                }
                username.isNotBlank() -> {
                    db.collection(USER_DATA_COLLECTION_KEY).get()
                        .addOnSuccessListener { querySnapshot ->
                            querySnapshot.documents.forEach {
                                if (it[USERNAME_KEY] == username) {
                                    validationResult.isValid = false
                                    validationResult.message = "Username is already in use."
                                    _userNameValidationResult.value = validationResult
                                }
                            }
                        }
                }
                else -> {
                    validationResult.isValid = false
                    validationResult.message = "Username cannot be blank."
                    withContext(Dispatchers.Main) {
                        _userNameValidationResult.value = validationResult
                    }
                }
            }
        }
    }

    fun validateDisplayName(displayName: String, account: String) {
        val validationResult = ValidationResult()
        viewModelScope.launch(Dispatchers.IO) {
            if (displayName.isBlank()) {
                validationResult.isValid = false
                validationResult.message = "Display name cannot be blank."
                withContext(Dispatchers.Main) {
                    when (account) {
                        ACCOUNT_STEAM -> _steamDisplayNameValidationResult.value = validationResult
                        ACCOUNT_EPIC_GAMES -> _epicGamesDisplayNameValidationResult.value =
                            validationResult
                        ACCOUNT_UPLAY -> _uplayDisplayNameValidationResult.value = validationResult
                        ACCOUNT_DISCORD -> _discordDisplayNameValidationResult.value =
                            validationResult
                    }
                }
            }
        }
    }

    fun validateSteamURL(URL: String) {
        val validationResult = ValidationResult()
        viewModelScope.launch(Dispatchers.IO) {
            if (URL.isNotBlank()) {
                val regex = REGEX_STEAM_PROFILE_URL.toRegex()
                if (!regex.matches(URL.dropLast(1))) {
                    validationResult.isValid = false
                    validationResult.message = "Please enter a valid Steam profile URL."
                }
            } else {
                validationResult.isValid = false
                validationResult.message = "URL cannot be blank."
            }

            withContext(Dispatchers.Main) {
                _steamURLValidationResult.value = validationResult
            }
        }
    }
}