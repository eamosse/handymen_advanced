package com.vama.android.handymen.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.vama.android.handymen.databinding.AddUserFragmentBinding

class AddUserFragment : Fragment() {

    private lateinit var binding: AddUserFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val addUserViewModel =
            ViewModelProvider(this).get(AddUserViewModel::class.java)

        binding = AddUserFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        addUserViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }
}