package com.eamosse.android.handymen.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.eamosse.android.handymen.databinding.UserItemBinding
import com.eamosse.android.handymen.model.UserModelView

class UsersAdapter : RecyclerView.Adapter<UserViewHolder>() {
    private val mDiffer: AsyncListDiffer<UserModelView> = AsyncListDiffer(this, DiffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: UserItemBinding = UserItemBinding.inflate(inflater, parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(mDiffer.currentList[position])
    }

    override fun getItemCount(): Int {
        return mDiffer.currentList.size
    }

    fun submitList(it: List<UserModelView>) {
        return mDiffer.submitList(it)
    }

    private object DiffCallback : DiffUtil.ItemCallback<UserModelView>() {
        override fun areItemsTheSame(
            oldItem: UserModelView,
            newItem: UserModelView
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: UserModelView,
            newItem: UserModelView
        ): Boolean {
            return oldItem == newItem
        }

    }
}

class UserViewHolder(private val binding: UserItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(user: UserModelView) {
        binding.name.text = user.name
    }
}