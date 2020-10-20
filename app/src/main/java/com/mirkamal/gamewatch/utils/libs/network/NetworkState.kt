package com.mirkamal.gamewatch.utils.libs.network

/**
 * Created by Mirkamal on 18 October 2020
 */
sealed class NetworkState {
    data class Success<T>(val data: T) : NetworkState()
    object InvalidData : NetworkState()
    data class Error(val error: String?) : NetworkState()
    data class NetworkException(val error: String?) : NetworkState()
    sealed class HttpErrors(val exceptionMessage: String?) : NetworkState() {
        data class ResourceForbidden( val exception: String?) : HttpErrors(exception)
        data class ResourceNotFound(val exception: String?) : HttpErrors(exception)
        data class InternalServerError(val exception: String?) : HttpErrors(exception)
        data class BadGateWay(val exception: String?) : HttpErrors(exception)
        data class ResourceRemoved(val exception: String?) : HttpErrors(exception)
        data class RemovedResourceFound(val exception: String?) : HttpErrors(exception)
    }
}