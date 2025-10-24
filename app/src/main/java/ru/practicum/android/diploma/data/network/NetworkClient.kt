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
                    in 200..299 -> {
                        val body = response.body()
                        if (body != null) {
                            NetworkResult.Success(body)
                        } else {
                            NetworkResult.Error(
                                code = response.code(),
                                message = "Response body is null"
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
            } catch (e: Exception) {
                NetworkResult.Error(
                    code = -1,
                    message = e.message
                )
            }
        }
    }
}
