package com.example.android.politicalpreparedness

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.network.models.Election

@BindingAdapter("electionsList")
fun bindRecyclerViewAdapter(recyclerView: RecyclerView, data: List<Election>?) {
    val adapter = recyclerView.adapter as ElectionListAdapter
    adapter.submitList(data)
}

@BindingAdapter("navigate")
fun bindNavigateButton(button: Button, url: String?) {
    button.isEnabled = false
    url?.let {
        button.isEnabled = true
        button.setOnClickListener {
            val context = button.context
            if (url.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        }
    }

}
