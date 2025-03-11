package com.vama.android.handymen.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.vama.android.data.utils.DataSource
import com.vama.android.handymen.R
import com.vama.android.handymen.databinding.AddUserFragmentBinding
import com.vama.android.handymen.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddUserFragment : Fragment() {

    private lateinit var _binding: AddUserFragmentBinding

    // Le ViewModel AddUser injecté par Hilt
    private val viewModel: AddUserViewModel by viewModels()

    // Le ViewModel partagé (HomeViewModel), si besoin
    private val sharedViewModel: HomeViewModel by lazy {
        ViewModelProvider(requireActivity())[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddUserFragmentBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSubmitButton()
        observeAddSuccess()
    }

    private fun setupSubmitButton() {
        _binding.submitButton.setOnClickListener {
            val name = _binding.nameInput.text.toString()
            val address = _binding.addressInput.text.toString()
            val phone = _binding.phoneInput.text.toString()
            val aboutMe = _binding.aboutMeInput.text.toString()
            val website = _binding.websiteInput.text.toString()

            if (validateInputs(name, address, phone)) {
                // Activer l'indicateur de chargement si disponible
                _binding.submitButton.isEnabled = false
                _binding.submitButton.text = getString(R.string.adding_user)

                viewModel.addUser(name, address, phone, aboutMe, website)
            }
        }
    }

    private fun validateInputs(name: String, address: String, phone: String): Boolean {
        var isValid = true

        if (name.isBlank()) {
            _binding.nameInput.error = getString(R.string.field_required)
            isValid = false
        }
        if (address.isBlank()) {
            _binding.addressInput.error = getString(R.string.field_required)
            isValid = false
        }
        if (phone.isBlank()) {
            _binding.phoneInput.error = getString(R.string.field_required)
            isValid = false
        }

        return isValid
    }

    private fun observeAddSuccess() {
        viewModel.addSuccess.observe(viewLifecycleOwner) { event ->
            // Réactiver le bouton quel que soit le résultat
            _binding.submitButton.isEnabled = true
            _binding.submitButton.text = getString(R.string.add_user)

            event.getContentIfNotHandled()?.let { success ->
                if (success) {
                    clearInputs()
                    Toast.makeText(context, R.string.user_added_success, Toast.LENGTH_SHORT).show()

                    // Appel explicit au HomeViewModel pour rafraîchir la liste
                    sharedViewModel.loadUsers()
                } else {
                    Toast.makeText(context, R.string.user_added_error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun clearInputs() {
        _binding.nameInput.text?.clear()
        _binding.addressInput.text?.clear()
        _binding.phoneInput.text?.clear()
        _binding.aboutMeInput.text?.clear()
        _binding.websiteInput.text?.clear()
    }
}