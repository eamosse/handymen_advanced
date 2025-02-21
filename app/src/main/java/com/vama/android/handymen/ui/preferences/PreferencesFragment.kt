package com.vama.android.handymen.ui.preferences

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.vama.android.handymen.R

class PreferencesFragment : Fragment() {

    companion object {
        private const val PREF_MODE_KEY = "isDatabaseMode"
    }

    private lateinit var prefs: SharedPreferences
    private var isDatabaseMode: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_preferences, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = requireContext().getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        isDatabaseMode = prefs.getBoolean(PREF_MODE_KEY, false)

        val switchMode = view.findViewById<SwitchCompat>(R.id.switch_mode)
        switchMode.isChecked = isDatabaseMode
        switchMode.text = if (isDatabaseMode) "Mode Database activé" else "Mode Memory activé"

        switchMode.setOnCheckedChangeListener { _, isChecked ->
            isDatabaseMode = isChecked
            prefs.edit().putBoolean(PREF_MODE_KEY, isChecked).apply()

            Toast.makeText(
                requireContext(),
                if (isChecked) "Mode Database activé" else "Mode Memory activé",
                Toast.LENGTH_SHORT
            ).show()

            if (isChecked) {
                activateDatabaseMode()
                switchMode.text = "Mode Database activé"
            } else {
                activateMemoryMode()
                switchMode.text = "Mode Memory activé"
            }
        }
    }

    private fun activateDatabaseMode() {
    }

    private fun activateMemoryMode() {
    }
}
