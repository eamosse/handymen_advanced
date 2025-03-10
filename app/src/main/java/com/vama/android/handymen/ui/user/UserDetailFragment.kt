package com.vama.android.handymen.ui.user

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.vama.android.handymen.R
import com.vama.android.handymen.databinding.FragmentUserDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserDetailFragment : Fragment() {

    private var _binding: FragmentUserDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserDetailViewModel by lazy {
        ViewModelProvider(this)[UserDetailViewModel::class.java]
    }

    private val args: UserDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMenu()
        loadUserDetails()
        setupClickListeners()
    }

    private fun setupMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.user_detail_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_edit -> {
                        // Navigation vers l'écran d'édition
                        val action = UserDetailFragmentDirections
                            .actionNavigationUserDetailToNavigationEditUser(args.userId)
                        findNavController().navigate(action)
                        true
                    }
                    R.id.action_share -> {
                        viewModel.userDetails.value?.let { user ->
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_profile_subject, user.name))
                                putExtra(Intent.EXTRA_TEXT, viewModel.getShareText(user))
                            }
                            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_profile_chooser_title)))
                        }
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun loadUserDetails() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loadUserDetails(args.userId)

            viewModel.userDetails.observe(viewLifecycleOwner) { user ->
                with(binding) {
                    userName.text = user.name
                    userAddress.text = user.address
                    userPhone.text = user.phoneNumber
                    userWebsite.text = user.webSite
                    userAbout.text = user.aboutMe

                    favoriteButton.isChecked = user.isFavorite

                    // Afficher/masquer les sections vides
                    userAboutSection.visibility = if (user.aboutMe.isNotBlank()) View.VISIBLE else View.GONE
                    userWebsiteSection.visibility = if (user.webSite.isNotBlank()) View.VISIBLE else View.GONE
                }
            }
        }
    }

    private fun setupClickListeners() {
        with(binding) {
            // Appel téléphonique
            phoneCallButton.setOnClickListener {
                viewModel.userDetails.value?.let { user ->
                    if (user.phoneNumber.isNotBlank()) {
                        val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${user.phoneNumber}"))
                        startActivity(dialIntent)
                    }
                }
            }

            // Ouvrir le site web
            websiteButton.setOnClickListener {
                viewModel.userDetails.value?.let { user ->
                    if (user.webSite.isNotBlank()) {
                        val webIntent = Intent(Intent.ACTION_VIEW)
                        val url = if (!user.webSite.startsWith("http")) "http://${user.webSite}" else user.webSite
                        webIntent.data = Uri.parse(url)
                        startActivity(webIntent)
                    }
                }
            }

            // Gérer le favori
            favoriteButton.setOnClickListener {
                viewModel.toggleFavorite()
            }

            // Ouvrir dans Maps
            addressButton.setOnClickListener {
                viewModel.userDetails.value?.let { user ->
                    val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=${Uri.encode(user.address)}"))
                    startActivity(mapIntent)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}