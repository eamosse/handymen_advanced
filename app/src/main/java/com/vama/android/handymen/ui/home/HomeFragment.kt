package com.vama.android.handymen.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.vama.android.handymen.databinding.HomeFragmentBinding

class HomeFragment : Fragment() {

    // Généré grâce à home_fragment.xml (ou fragment_home.xml)
    private lateinit var _binding: HomeFragmentBinding

    // Instancie l'adapter
    private val adapter: UsersAdapter by lazy { UsersAdapter() }

    // The old fashion way to get the view model
    // We ask the view model provider to give us the view model
    // Lazy is used to initialize the view model only when it is needed
    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this)[HomeViewModel::class.java]
    }

    // The new way to get the view model
    // TODO ("Make it work")
    // private val _viewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflater le layout et initialiser binding
        _binding = HomeFragmentBinding.inflate(inflater, container, false)

        // Configurer le RecyclerView
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
    override fun onDestroyView() {
        super.onDestroyView()
    }
}