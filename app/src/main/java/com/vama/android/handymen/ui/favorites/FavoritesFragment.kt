package com.vama.android.handymen.ui.favorites

import android.app.AlertDialog
import android.content.Intent
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

                homeViewModel.loadUsers()
            },
            onDeleteClick = { user ->
                showDeleteConfirmationDialog(user)
                homeViewModel.loadUsers()
            },
            onShareClick = { user ->
                shareUserProfile(user)
            }
        )

        binding.favoritesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@FavoritesFragment.adapter
        }
    }

    private fun shareUserProfile(user: UserModelView) {
        val shareText = homeViewModel.shareUserProfile(user)

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Profil de ${user.name}")
            putExtra(Intent.EXTRA_TEXT, shareText)
        }

        startActivity(Intent.createChooser(shareIntent, "Partager le profil"))
    }

    private fun observeFavorites() {
        viewModel.refreshFavorites()
        viewModel.favorites.observe(viewLifecycleOwner) { favorites ->
            // Convert User to UserModelView if necessary
            val favoriteModelViews = favorites.map { user ->
                com.vama.android.handymen.model.UserModelView(
                    id = user.id,
                    name = user.name,
                    address = user.address,
                    phoneNumber = user.phoneNumber,
                    webSite = user.webSite,
                    aboutMe = user.aboutMe,
                    favorite = user.favorite,
                    avatarUrl = user.avatarUrl
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
                // Call delete method in ViewModel
                viewModel.deleteUser(user.id)
                homeViewModel.loadUsers() // si besoin
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }
}