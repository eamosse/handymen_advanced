package com.vama.android.handymen.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.vama.android.handymen.R
import com.vama.android.handymen.databinding.HomeFragmentBinding
import com.vama.android.handymen.model.UserModelView

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
                // Toggle favorite status
                // Assuming you have a method in HomeViewModel to toggle favorite
                viewModel.toggleFavorite(user.id)
            },
            onDeleteClick = { user ->
                // Show confirmation dialog before deleting
                showDeleteConfirmationDialog(user)
            }
        )

        _binding.list.layoutManager = LinearLayoutManager(requireContext())
        _binding.list.adapter = adapter

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observer la liste d'utilisateurs exposée par le ViewModel
        viewModel.users.observe(viewLifecycleOwner) { userList ->
            // Mettre à jour l'adapter
            adapter.submitList(userList)
        }
    }

    private fun showDeleteConfirmationDialog(user: UserModelView) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.delete_user_title)
            .setMessage(R.string.delete_user_message)
            .setPositiveButton(R.string.delete) { _, _ ->
                // Call delete method in ViewModel
                viewModel.deleteUser(user.id)
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }
}