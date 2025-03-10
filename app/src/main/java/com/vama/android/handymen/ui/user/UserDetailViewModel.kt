package com.vama.android.handymen.ui.user

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vama.android.data.repositories.UserRepository
import com.vama.android.handymen.R
import com.vama.android.handymen.model.UserModelView
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val userRepository: UserRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _userDetails = MutableLiveData<UserModelView>()
    val userDetails: LiveData<UserModelView> = _userDetails

    private var userId: Long = 0

    suspend fun loadUserDetails(id: Long) {
        userId = id
        userRepository.getById(id)?.let { user ->
            _userDetails.value = UserModelView(
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
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            userRepository.toggleFavorite(userId)

            _userDetails.value?.let { user ->
                _userDetails.value = user.copy(favorite = !user.favorite)
            }
        }
    }

    fun getShareText(user: UserModelView): String {
        return context.getString(
            R.string.share_user_profile_template,
            user.name,
            user.address,
            user.phoneNumber,
            user.webSite,
            user.aboutMe
        )
    }
}