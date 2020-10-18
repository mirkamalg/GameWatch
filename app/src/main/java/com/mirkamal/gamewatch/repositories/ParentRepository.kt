package com.mirkamal.gamewatch.repositories

import com.mirkamal.gamewatch.utils.libs.network.NetworkState
import com.mirkamal.gamewatch.utils.libs.network.ResponseParentData
import retrofit2.Response
import java.io.IOException

/**
 * Created by Mirkamal on 18 October 2020
 */
open class ParentRepository {

    fun <T> getNetworkState(response: Response<ResponseParentData<T>>?): NetworkState {

        return try {
            if (response?.isSuccessful == true) {
                if (response.body() != null) {

                    val data = response.body()
                    when ((data as ResponseParentData<*>).status) {
                        200 -> NetworkState.Success(data)
                        402->NetworkState.Error("Wrong Header")
                        403->NetworkState.Error("Wrong Credentials")
                        405->NetworkState.Error("Expired")
                        else -> NetworkState.Error("System Error")
                    }

                } else {
                    NetworkState.InvalidData
                }
            } else {
                when (response?.code()) {
                    403 -> NetworkState.HttpErrors.ResourceForbidden(response.message())
                    404 -> NetworkState.HttpErrors.ResourceNotFound(response.message())
                    500 -> NetworkState.HttpErrors.InternalServerError(response.message())
                    502 -> NetworkState.HttpErrors.BadGateWay(response.message())
                    301 -> NetworkState.HttpErrors.ResourceRemoved(response.message())
                    302 -> NetworkState.HttpErrors.RemovedResourceFound(response.message())
                    else -> NetworkState.Error(response?.message())
                }
            }
        } catch (e: IOException) {
            NetworkState.NetworkException(e.message!!)
        }
    }

}