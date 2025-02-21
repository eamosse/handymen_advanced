package com.vama.android.handymen.ui.home

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.vama.android.handymen.R
import com.vama.android.handymen.databinding.HomeFragmentBinding
import com.vama.android.handymen.model.UserModelView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import androidx.appcompat.widget.SearchView

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var _binding: HomeFragmentBinding

    private lateinit var adapter: UsersAdapter

    val viewModel: HomeViewModel by lazy {
        ViewModelProvider(requireActivity())[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)

        // Initialize adapter with favorite and delete click handlers
        adapter = UsersAdapter(
            onFavoriteClick = { user ->
                viewModel.toggleFavorite(user.id)
            },
            onDeleteClick = { user ->
                showDeleteConfirmationDialog(user)
            },
            onShareClick = { user ->
                shareUserProfile(user)
            }
        )

        _binding.list.layoutManager = LinearLayoutManager(requireContext())
        _binding.list.adapter = adapter

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSearchView()
        setupSortSpinner()
        // Observer la liste filtrée d'utilisateurs
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.filteredUsers.observe(viewLifecycleOwner) { userList ->
                adapter.submitList(userList)
            }

        }
//        viewModel.users.observe(viewLifecycleOwner) { userList ->
//                // Mettre à jour l'adapter
//                adapter.submitList(userList)
//            }
    }

    private fun setupSearchView() {
        _binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.updateSearchQuery(newText ?: "")
                return true
            }
        })
    }

    private fun setupSortSpinner() {
        // Create sort options
        val sortOptions = arrayOf(
            "Name (A-Z)",
            "Name (Z-A)",
            "Date Created (Oldest)",
            "Date Created (Newest)"
        )

        // Create an ArrayAdapter for the spinner
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            sortOptions
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set the adapter to the spinner
        _binding.sortSpinner.adapter = adapter

        // Set up selection listener
        _binding.sortSpinner.onItemSelectedListener =
            object : android.widget.AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: android.widget.AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val sortType = when (position) {
                        0 -> HomeViewModel.SortType.NAME_ASC
                        1 -> HomeViewModel.SortType.NAME_DESC
                        2 -> HomeViewModel.SortType.DATE_CREATED_ASC
                        3 -> HomeViewModel.SortType.DATE_CREATED_DESC
                        else -> HomeViewModel.SortType.NAME_ASC
                    }
                    viewModel.updateSortType(sortType)
                }

                override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
            }
    }


    private fun showDeleteConfirmationDialog(user: UserModelView) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.delete_user_title)
            .setMessage(R.string.delete_user_message)
            .setPositiveButton(R.string.delete) { _, _ ->
                // Call delete method in ViewModel
                viewModel.deleteUser(user.id)

                // Ajouter cette ligne pour mettre à jour les favoris
                viewModel.loadUsers()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun shareUserProfile(user: UserModelView) {
        val shareText = viewModel.shareUserProfile(user)

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Profil de ${user.name}")
            putExtra(Intent.EXTRA_TEXT, shareText)
        }

        startActivity(Intent.createChooser(shareIntent, "Partager le profil"))
    }
}