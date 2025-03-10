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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.vama.android.handymen.R
import com.vama.android.handymen.databinding.FavoriteUsersFragmentBinding
import com.vama.android.handymen.model.UserModelView
import com.vama.android.handymen.ui.home.HomeViewModel
import com.vama.android.handymen.ui.home.UserInteractionListener
import com.vama.android.handymen.ui.home.UsersAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment(), UserInteractionListener {

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
        adapter = UsersAdapter(this) // Utiliser l'interface au lieu des lambdas

        binding.favoritesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@FavoritesFragment.adapter
        }
    }

    private fun observeFavorites() {
        viewModel.refreshFavorites()
        viewModel.favorites.observe(viewLifecycleOwner) { favoriteUsers ->
            adapter.submitList(favoriteUsers)

            // Afficher la vue vide si nécessaire
            binding.emptyView.visibility = if (favoriteUsers.isEmpty()) View.VISIBLE else View.GONE
            binding.favoritesRecyclerView.visibility = if (favoriteUsers.isEmpty()) View.GONE else View.VISIBLE
        }
    }

    // Implémentation de UserInteractionListener
    override fun onFavoriteClick(user: UserModelView) {
        viewModel.toggleFavorite(user.id)
        homeViewModel.loadUsers() // Mettre à jour également la liste principale
    }

    override fun onDeleteClick(user: UserModelView) {
        showDeleteConfirmationDialog(user)
    }

    override fun onShareClick(user: UserModelView) {
        shareUserProfile(user)
    }

    override fun onUserClick(user: UserModelView) {
        // Navigation vers le détail de l'utilisateur
        val action = FavoritesFragmentDirections.actionNavigationFavoritesToNavigationUserDetail(user.id)
        findNavController().navigate(action)
    }

    private fun shareUserProfile(user: UserModelView) {
        val shareText = homeViewModel.shareUserProfile(user)

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_profile_subject, user.name))
            putExtra(Intent.EXTRA_TEXT, shareText)
        }

        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_profile_chooser_title)))
    }

    private fun showDeleteConfirmationDialog(user: UserModelView) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.delete_user_title)
            .setMessage(R.string.delete_user_message)
            .setPositiveButton(R.string.delete) { _, _ ->
                viewModel.deleteUser(user.id)
                homeViewModel.loadUsers() // Mettre à jour également la liste principale
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}