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

    private lateinit var binding: HomeFragmentBinding


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
       // binding = HomeFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(requireContext())
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // We observe the users list, when the list is updated, we submit the new list to the adapter
        viewModel.users.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}