package com.vama.android.handymen.ui.preferences

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.vama.android.data.preferences.DataStoreManager
import com.vama.android.data.repositories.UserRepository
import com.vama.android.handymen.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PreferencesFragment : Fragment() {

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    @Inject
    lateinit var userRepository: UserRepository

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
            // Désactiver le switch pendant le changement
            switchMode.isEnabled = false

            // Mettre à jour le mode
            viewLifecycleOwner.lifecycleScope.launch {
                if (isChecked) {
                    userRepository.switchToDatabaseMode()
                } else {
                    userRepository.switchToInMemoryMode()
                }

                // Afficher un message de confirmation
                Toast.makeText(
                    requireContext(),
                    if (isChecked) R.string.database_mode_enabled else R.string.memory_mode_enabled,
                    Toast.LENGTH_SHORT
                ).show()

                // Réactiver le switch une fois terminé
                switchMode.isEnabled = true
            }

            // Mettre à jour le texte du switch
            updateSwitchText(switchMode)
        }
    }

    private fun updateSwitchText(switchMode: SwitchCompat) {
        switchMode.text = getString(
            if (switchMode.isChecked) R.string.database_mode_enabled
            else R.string.memory_mode_enabled
        )
    }
}