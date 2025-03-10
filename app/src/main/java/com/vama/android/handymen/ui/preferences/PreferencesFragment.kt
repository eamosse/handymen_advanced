package com.vama.android.handymen.ui.preferences

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.vama.android.data.preferences.DataSource
import com.vama.android.data.preferences.DataStoreManager
import com.vama.android.data.repositories.UserRepository
import com.vama.android.handymen.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PreferencesFragment : Fragment() {

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    private lateinit var radioGroup: RadioGroup
    private lateinit var radioMemory: RadioButton
    private lateinit var radioDatabase: RadioButton
    private lateinit var radioOnline: RadioButton
    private lateinit var switchSync: SwitchCompat
    private lateinit var btnSyncNow: Button
    private lateinit var textRestartInfo: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_preferences, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        radioGroup = view.findViewById(R.id.radio_data_source)
        radioMemory = view.findViewById(R.id.radio_memory)
        radioDatabase = view.findViewById(R.id.radio_database)
        radioOnline = view.findViewById(R.id.radio_online)
        switchSync = view.findViewById(R.id.switch_sync)
        btnSyncNow = view.findViewById(R.id.btn_sync_now)
        textRestartInfo = view.findViewById(R.id.text_restart_info)

        // Setup initial state
        setupInitialState()

        // Setup listeners
        setupListeners()
    }

    private fun setupInitialState() {
        val currentSource = userRepository.getCurrentDataSource()
        when (currentSource) {
            DataSource.MEMORY -> radioMemory.isChecked = true
            DataSource.DATABASE -> radioDatabase.isChecked = true
            DataSource.ONLINE -> radioOnline.isChecked = true
        }

        switchSync.isChecked = userRepository.isSyncEnabled()
        btnSyncNow.isEnabled = userRepository.isSyncEnabled()
    }

    private fun setupListeners() {
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val previousSource = userRepository.getCurrentDataSource()
            val newSource = when (checkedId) {
                R.id.radio_memory -> DataSource.MEMORY
                R.id.radio_database -> DataSource.DATABASE
                R.id.radio_online -> DataSource.ONLINE
                else -> DataSource.MEMORY
            }

            if (previousSource != newSource) {
                userRepository.setDataSource(newSource)
                textRestartInfo.visibility = View.VISIBLE
                Toast.makeText(
                    requireContext(),
                    getString(R.string.restart_required),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        switchSync.setOnCheckedChangeListener { _, isChecked ->
            userRepository.setSyncEnabled(isChecked)
            btnSyncNow.isEnabled = isChecked
        }

        btnSyncNow.setOnClickListener {
            performSync()
        }
    }

    private fun performSync() {
        Toast.makeText(
            requireContext(),
            getString(R.string.sync_started),
            Toast.LENGTH_SHORT
        ).show()

        lifecycleScope.launch {
            try {
                btnSyncNow.isEnabled = false
                userRepository.syncData()
                Toast.makeText(
                    requireContext(),
                    getString(R.string.sync_completed),
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.sync_failed),
                    Toast.LENGTH_SHORT
                ).show()
                e.printStackTrace()
            } finally {
                btnSyncNow.isEnabled = true
            }
        }
    }
}