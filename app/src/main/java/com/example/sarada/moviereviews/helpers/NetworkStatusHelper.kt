import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.lifecycle.LiveData
import com.example.sarada.moviereviews.models.sealedc.NetworkStatus
import com.example.sarada.moviereviews.models.singletonc.InternetAvailability
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import java.net.Socket

class NetworkStatusHelper(private val context: Context) : LiveData<NetworkStatus>() {

    val valideNetworkConnections: ArrayList<Network> = ArrayList()
    var connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private lateinit var connectivityManagerCallback: ConnectivityManager.NetworkCallback

    // Update LiveData NetworkStatus
    fun announceStatus() {
        if (valideNetworkConnections.isNotEmpty()) {
            postValue(NetworkStatus.Available)
        } else {
            postValue(NetworkStatus.Unavailable)
        }
    }

    // Establish a connection to system through ConnectivityManager
    private fun getConnectivityManagerCallback() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            object : ConnectivityManager.NetworkCallback() {

                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    val networkCapability = connectivityManager.getNetworkCapabilities(network)
                    val hasNetworkConnection =
                        networkCapability?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                            ?: false
                    if (hasNetworkConnection) {
                        determineInternetAccess(network)
                    }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    valideNetworkConnections.remove(network)
                    announceStatus()
                }

                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    super.onCapabilitiesChanged(network, networkCapabilities)
                    if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                        determineInternetAccess(network)
                    } else {
                        valideNetworkConnections.remove(network)
                    }
                    announceStatus()
                }
            }
        } else {
            TODO("VERSION.SDK_INT < LOLLIPOP")
        }

    private fun determineInternetAccess(network: Network) {
        CoroutineScope(Dispatchers.IO).launch {
            if (InternetAvailability.check()) {
                withContext(Dispatchers.Main) {
                    // Check if the network already exits in valideNetworkConnections.
                    // If it already exists, then don't add it to list
                    // This will avoid duplications and abnormal functionality.
                    if(!valideNetworkConnections.contains(network))
                        valideNetworkConnections.add(network)
                    announceStatus()
                }
            }
        }
    }

    override fun onActive() {
        super.onActive()
        connectivityManagerCallback = getConnectivityManagerCallback()
        val networkRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            NetworkRequest
                .Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
        } else {
            TODO("VERSION.SDK_INT < LOLLIPOP")
        }
        connectivityManager.registerNetworkCallback(networkRequest, connectivityManagerCallback)
    }

    override fun onInactive() {
        super.onInactive()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            connectivityManager.unregisterNetworkCallback(connectivityManagerCallback)
        }
    }

}