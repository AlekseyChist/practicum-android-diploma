package ru.practicum.android.diploma.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException

class NetworkClient(private val context: Context) {

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val capabilities = connectivityManager.getActiveNetwork()?.let {
            connectivityManager.getNetworkCapabilities(it)
        }

        return capabilities?.let {
            it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                it.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        } ?: false
    }

    suspend fun <T> executeRequest(
        request: suspend () -> Response<T>
    ): NetworkResult<T> {
        return withContext(Dispatchers.IO) {
            if (!isConnected()) {
                return@withContext NetworkResult.NoConnection
            }

            try {
                val response = request()

                when (response.code()) {
                    in HTTP_SUCCESS_START..HTTP_SUCCESS_END -> {
                        val body = response.body()
                        if (body != null) {
                            NetworkResult.Success(body)
                        } else {
                            NetworkResult.Error(
                                code = response.code(),
                                message = RESPONSE_BODY_NULL_MESSAGE
                            )
                        }
                    }
                    else -> {
                        NetworkResult.Error(
                            code = response.code(),
                            message = response.message()
                        )
                    }
                }
            } catch (e: IOException) {
                NetworkResult.NoConnection
            }
        }
    }

    companion object {
        private const val HTTP_SUCCESS_START = 200
        private const val HTTP_SUCCESS_END = 299
        private const val RESPONSE_BODY_NULL_MESSAGE = "Response body is null"
    }
}
