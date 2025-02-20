package com.vama.android.handymen.ui.favorites

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.vama.android.handymen.R
import com.vama.android.handymen.databinding.FavoriteUsersFragmentBinding
import com.vama.android.handymen.model.UserModelView
import com.vama.android.handymen.ui.home.HomeViewModel
import com.vama.android.handymen.ui.home.UsersAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var _binding: FavoriteUsersFragmentBinding? = null
    private val binding get() = _binding!!

    // Récupération du FavoritesViewModel via Hilt
    private val viewModel: FavoritesViewModel by viewModels()

    private val homeViewModel: HomeViewModel by activityViewModels()

    private lateinit var adapter: UsersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FavoriteUsersFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeFavorites()
    }

    private fun setupRecyclerView() {
        adapter = UsersAdapter(
            onFavoriteClick = { user ->
                // Toggle favorite
                viewModel.toggleFavorite(user.id)

                // Si tu veux aussi forcer la mise à jour côté HomeViewModel :
                homeViewModel.loadUsers()
            },
            onDeleteClick = { user ->
                showDeleteConfirmationDialog(user)
            }
        )

        binding.favoritesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@FavoritesFragment.adapter
        }
    }

    private fun observeFavorites() {
        viewModel.favorites.observe(viewLifecycleOwner) { favorites ->
            val favoriteModelViews = favorites.map { user ->
                com.vama.android.handymen.model.UserModelView(
                    id = user.id,
                    name = user.name,
                    address = user.address,
                    phoneNumber = user.phoneNumber,
                    webSite = user.webSite,
                    aboutMe = user.aboutMe,
                    favorite = true, // ou selon ta logique
                    avatarUrl = ""
                )
            }
            adapter.submitList(favoriteModelViews)

            binding.emptyView.visibility = if (favorites.isEmpty()) View.VISIBLE else View.GONE
            binding.favoritesRecyclerView.visibility = if (favorites.isEmpty()) View.GONE else View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showDeleteConfirmationDialog(user: UserModelView) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.delete_user_title)
            .setMessage(R.string.delete_user_message)
            .setPositiveButton(R.string.delete) { _, _ ->
                viewModel.deleteUser(user.id)
                homeViewModel.loadUsers() // si besoin
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }
}
