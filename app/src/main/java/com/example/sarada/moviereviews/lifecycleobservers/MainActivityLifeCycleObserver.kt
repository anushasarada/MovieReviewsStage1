package com.example.sarada.moviereviews.lifecycleobservers

import androidx.lifecycle.*

class MainActivityLifeCycleObserver(lifecycle: Lifecycle) : DefaultLifecycleObserver {
    init {
        lifecycle.addObserver(this)
    }
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        // your code
    }
}