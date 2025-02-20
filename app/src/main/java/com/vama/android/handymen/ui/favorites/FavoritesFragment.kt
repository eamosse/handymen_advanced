package com.vama.android.handymen.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.vama.android.handymen.databinding.FavoriteUsersFragmentBinding
import com.vama.android.handymen.ui.home.HomeViewModel

class FavoritesFragment: Fragment() {

    private var _binding: FavoriteUsersFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FavoritesViewModel


    private val sharedViewModel: HomeViewModel by lazy {
        ViewModelProvider(requireActivity())[HomeViewModel::class.java]
    }
    private lateinit var adapter: FavoritesAdapter

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
        adapter = FavoritesAdapter { user ->
            // Handler pour le clic sur un favori
            viewModel.toggleFavorite(user.id)
        }

        binding.favoritesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@FavoritesFragment.adapter
        }
    }

    private fun observeFavorites() {
        viewModel.favorites.observe(viewLifecycleOwner) { favorites ->
            adapter.updateFavorites(favorites)
            binding.emptyView.visibility = if (favorites.isEmpty()) View.VISIBLE else View.GONE
            binding.favoritesRecyclerView.visibility = if (favorites.isEmpty()) View.GONE else View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
}