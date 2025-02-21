package com.vama.android.handymen.ui.preferences

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.preference.PreferenceFragmentCompat
import com.vama.android.handymen.R

class PreferencesFragment : PreferenceFragmentCompat() {

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
        // Utilisation du layout personnalisé
        return inflater.inflate(R.layout.fragment_preferences, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Configure la toolbar
        val toolbar = view.findViewById<Toolbar>(R.id.preferences_toolbar)
        toolbar?.let {
            it.setTitle("Préférences")
            // Pour que le menu soit affiché dans ce fragment
            setHasOptionsMenu(true)
        }

        // Charger les préférences partagées
        prefs = requireContext().getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        isDatabaseMode = prefs.getBoolean(PREF_MODE_KEY, false)

        // Charge les préférences depuis le fichier XML (si vous en avez)
        setPreferencesFromResource(R.xml.preferences, null)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_preferences, menu)
        val menuItem = menu.findItem(R.id.action_toggle_mode)
        val modeSwitch = menuItem.actionView as SwitchCompat

        // Initialise l'état du switch selon la préférence sauvegardée
        modeSwitch.isChecked = isDatabaseMode

        // Définir le listener pour enregistrer le changement
        modeSwitch.setOnCheckedChangeListener { _, isChecked ->
            isDatabaseMode = isChecked
            prefs.edit().putBoolean(PREF_MODE_KEY, isChecked).apply()
            Toast.makeText(
                requireContext(),
                if (isChecked) "Mode Database activé" else "Mode Memory activé",
                Toast.LENGTH_SHORT
            ).show()
            // Ajoutez ici la logique pour basculer réellement entre les modes, si nécessaire
        }
        super.onCreateOptionsMenu(menu, inflater)
    }
}
