package com.example.sarada.moviereviews

import android.preference.PreferenceActivity
import android.os.Bundle
import com.example.sarada.moviereviews.SettingsActivity.SettingsFragment
import android.preference.PreferenceFragment
import com.example.sarada.moviereviews.R

class SettingsActivity : PreferenceActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentManager.beginTransaction().replace(android.R.id.content, SettingsFragment())
            .commit()
    }

    class SettingsFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.preference)
        }
    }
}