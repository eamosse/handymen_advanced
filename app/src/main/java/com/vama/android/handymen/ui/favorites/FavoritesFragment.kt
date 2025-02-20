package com.vama.android.handymen.ui.favorites

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.vama.android.handymen.R
import com.vama.android.handymen.databinding.FavoriteUsersFragmentBinding
import com.vama.android.handymen.model.UserModelView
import com.vama.android.handymen.ui.home.HomeViewModel
import com.vama.android.handymen.ui.home.UsersAdapter

class FavoritesFragment: Fragment() {

    private var _binding: FavoriteUsersFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FavoritesViewModel

    private val sharedViewModel: HomeViewModel by lazy {
        ViewModelProvider(requireActivity())[HomeViewModel::class.java]
    }
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
        viewModel = ViewModelProvider(this,
            FavoritesViewModelFactory(sharedViewModel)
        )[FavoritesViewModel::class.java]

        setupRecyclerView()
        observeFavorites()
    }

    private fun setupRecyclerView() {
        adapter = UsersAdapter(
            onFavoriteClick = { user ->
                // Toggle favorite
                viewModel.toggleFavorite(user.id)
            },
            onDeleteClick = { user ->
                // Show confirmation dialog before deleting
                showDeleteConfirmationDialog(user)
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

    private fun observeFavorites() {
        viewModel.favorites.observe(viewLifecycleOwner) { favorites ->
            // Convertir les utilisateurs favoris en UserModelView avec l'URL de l'avatar
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
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }
    class FavoritesViewModelFactory(private val sharedViewModel: HomeViewModel) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
                return FavoritesViewModel(sharedViewModel = sharedViewModel) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
    private fun shareUserProfile(user: UserModelView) {
        val shareText = sharedViewModel.shareUserProfile(user)

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Profil de ${user.name}")
            putExtra(Intent.EXTRA_TEXT, shareText)
        }

        startActivity(Intent.createChooser(shareIntent, "Partager le profil"))
    }
}