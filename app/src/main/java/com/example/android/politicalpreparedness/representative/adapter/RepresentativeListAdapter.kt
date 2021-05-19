package com.example.android.politicalpreparedness.representative.adapter

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.RepresentativeItemBinding
import com.example.android.politicalpreparedness.network.models.Channel
import com.example.android.politicalpreparedness.representative.model.Representative

class RepresentativeListAdapter:
        ListAdapter<Representative, RepresentativeListAdapter.RepresentativeViewHolder>(RepresentativeDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepresentativeViewHolder {
        return RepresentativeViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RepresentativeViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class RepresentativeViewHolder(val binding: RepresentativeItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Representative) {
            binding.representative = item
            binding.representativePhoto.setImageResource(R.drawable.ic_profile)

            item.official.channels?.let { showSocialLinks(it) }
            item.official.urls?.let { showWWWLinks(it) }

            binding.executePendingBindings()
        }


        companion object {
            fun from(parent: ViewGroup): RepresentativeViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RepresentativeItemBinding.inflate(layoutInflater, parent, false)
                return RepresentativeViewHolder(binding)
            }
        }

        private fun showSocialLinks(channels: List<Channel>) {
            val facebookUrl = getFacebookUrl(channels)
            if (!facebookUrl.isNullOrBlank()) {
                enableLink(binding.facebookIcon, facebookUrl)
            }else{
                binding.facebookIcon.visibility= View.GONE
            }

            val twitterUrl = getTwitterUrl(channels)
            if (!twitterUrl.isNullOrBlank()) {
                enableLink(binding.twitterIcon, twitterUrl)
            }else{
                binding.twitterIcon.visibility = View.GONE
            }
        }

        private fun showWWWLinks(urls: List<String>) {
            if (urls.first().isNotEmpty()) {
                enableLink(binding.wwwIcon, urls.first())
            }else{
                binding.wwwIcon.visibility = View.GONE
            }
        }

        private fun getFacebookUrl(channels: List<Channel>): String? {
            return channels.filter { channel -> channel.type == "Facebook" }
                    .map { channel -> "https://www.facebook.com/${channel.id}" }
                    .firstOrNull()
        }

        private fun getTwitterUrl(channels: List<Channel>): String? {
            return channels.filter { channel -> channel.type == "Twitter" }
                    .map { channel -> "https://www.twitter.com/${channel.id}" }
                    .firstOrNull()
        }

        private fun enableLink(view: ImageView, url: String) {
            view.visibility = View.VISIBLE
            view.setOnClickListener { setIntent(url) }
        }

        private fun setIntent(url: String) {
            val uri = Uri.parse(url)
            val intent = Intent(ACTION_VIEW, uri)
            itemView.context.startActivity(intent)
        }


    }

    companion object RepresentativeDiffCallback : DiffUtil.ItemCallback<Representative>() {
        override fun areItemsTheSame(oldItem: Representative, newItem: Representative): Boolean {
            return oldItem.office === newItem.office
        }

        override fun areContentsTheSame(oldItem: Representative, newItem: Representative): Boolean {
            return oldItem == newItem
        }
    }

    class RepresentativeListener(val clickListener: (representative: Representative) -> Unit) {
        fun onClick(representative: Representative) = clickListener(representative)
    }
}