package com.vama.android.handymen.ui.preferences

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.vama.android.data.preferences.DataStoreManager
import com.vama.android.handymen.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
// TODO Pas de texts en dur dans le code
class PreferencesFragment : Fragment() {

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_preferences, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val switchMode = view.findViewById<SwitchCompat>(R.id.switch_mode)
        switchMode.isChecked = dataStoreManager.isDatabaseMode()
        updateSwitchText(switchMode)

        switchMode.setOnCheckedChangeListener { _, isChecked ->
            dataStoreManager.setDatabaseMode(isChecked)
            updateSwitchText(switchMode)

            Toast.makeText(
                requireContext(),
                if (isChecked) "Database mode enabled" else "Memory mode enabled",
                Toast.LENGTH_SHORT
            ).show()

            Toast.makeText(
                requireContext(),
                "Please restart the application to apply changes",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun updateSwitchText(switchMode: SwitchCompat) {
        switchMode.text = if (switchMode.isChecked) "Database mode enabled" else "Memory mode enabled"
    }
}