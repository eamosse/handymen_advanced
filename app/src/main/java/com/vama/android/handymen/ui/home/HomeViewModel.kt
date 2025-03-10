package com.vama.android.handymen.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vama.android.data.api.SortCriteria
import com.vama.android.data.repositories.UserRepository
import com.vama.android.handymen.R
import com.vama.android.handymen.model.UserModelView
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    enum class SortType {
        NAME_ASC,
        NAME_DESC,
        DATE_CREATED_ASC,
        DATE_CREATED_DESC
    }

    private val _filteredUsers = MediatorLiveData<List<UserModelView>>()
    val filteredUsers: LiveData<List<UserModelView>> = _filteredUsers

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            val users = userRepository.getAll().value?.map { user ->
                UserModelView(
                    id = user.id,
                    name = user.name,
                    address = user.address,
                    phoneNumber = user.phoneNumber,
                    webSite = user.webSite,
                    aboutMe = user.aboutMe,
                    favorite = user.favorite,
                    avatarUrl = ""
                )
            } ?: emptyList()

            _filteredUsers.value = users
        }
    }

    fun toggleFavorite(userId: Long) {
        viewModelScope.launch {
            userRepository.toggleFavorite(userId)
            loadUsers()
        }
    }

    fun deleteUser(userId: Long) {
        viewModelScope.launch {
            userRepository.delete(userId)
            loadUsers()
        }
    }

    fun updateSearchQuery(query: String) {
        viewModelScope.launch {
            val users = userRepository.search(query).map { user ->
                UserModelView(
                    id = user.id,
                    name = user.name,
                    address = user.address,
                    phoneNumber = user.phoneNumber,
                    webSite = user.webSite,
                    aboutMe = user.aboutMe,
                    favorite = user.favorite,
                    avatarUrl = ""
                )
            }
            _filteredUsers.value = users
        }
    }

    fun updateSortType(sortType: SortType) {
        viewModelScope.launch {
            val criteria = when (sortType) {
                SortType.NAME_ASC -> SortCriteria.NAME_ASC
                SortType.NAME_DESC -> SortCriteria.NAME_DESC
                SortType.DATE_CREATED_ASC -> SortCriteria.DATE_ASC
                SortType.DATE_CREATED_DESC -> SortCriteria.DATE_DESC
            }

            val users = userRepository.sortBy(criteria).map { user ->
                UserModelView(
                    id = user.id,
                    name = user.name,
                    address = user.address,
                    phoneNumber = user.phoneNumber,
                    webSite = user.webSite,
                    aboutMe = user.aboutMe,
                    favorite = user.favorite,
                    avatarUrl = ""
                )
            }
            _filteredUsers.value = users
        }
    }

    fun shareUserProfile(user: UserModelView): String {
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