package com.example.sarada.moviereviews.models.sealedc

sealed class NetworkStatus {
    object Available : NetworkStatus()
    object Unavailable : NetworkStatus()
}