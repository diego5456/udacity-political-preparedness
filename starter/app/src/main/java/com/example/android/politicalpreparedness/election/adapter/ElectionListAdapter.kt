package com.example.android.politicalpreparedness.election.adapter

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.ElectionItemBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.AdministrationBody
import com.example.android.politicalpreparedness.network.models.Election
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ElectionListAdapter(private val clickListener: ElectionListener) :
    ListAdapter<Election, ElectionViewHolder>(ElectionDiffCallback()) {

        var elections: List<Election> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        return ElectionViewHolder.from(parent)
    }


    // TODO: Bind ViewHolder

    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        val election = getItem(position)
        holder.bind(clickListener, election)
    }

    // TODO: Add companion object to inflate ViewHolder (from)

}

// TODO: Create ElectionViewHolder
//
class ElectionViewHolder(val binding: ElectionItemBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(clickListener: ElectionListener, election: Election){
        binding.election = election
        binding.clickListener = clickListener
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): ElectionViewHolder {
            val binding =
                ElectionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ElectionViewHolder(binding)
        }
    }
}

// TODO: Create ElectionDiffCallback
class ElectionDiffCallback : DiffUtil.ItemCallback<Election>() {
    override fun areItemsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem.id == newItem.id
    }

}

// TODO: Create ElectionListener
class ElectionListener(val clickListener: (election: Election) -> Unit) {
    fun onClick(election: Election) = clickListener(election)
}

@BindingAdapter("upcomingElectionsList")
fun setUpcomingElectionsList(recyclerView: RecyclerView, elections: List<Election>?) {
    val adapter = recyclerView.adapter as? ElectionListAdapter
    Log.i("ElectionListAdapter", "Setting elections list ${elections?.size}")
    adapter?.submitList(elections)
}

@BindingAdapter("formattedDate")
fun bindFormattedDate(textView: TextView, date: Date?) {
    date?.let {
        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        textView.text = sdf.format(date)
    }
}

@BindingAdapter("openUrl")
fun bindOpenUrl(textView: TextView, url: String?) {
    textView.setOnClickListener {
        url?.let {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
            textView.context.startActivity(intent)
        }
    }
}
@BindingAdapter("addressText")
fun setAddressText(textView: TextView, electionAdministrationBody: AdministrationBody?) {
    var address: Address? = null
    electionAdministrationBody?.let {
        address = it.correspondenceAddress ?: it.physicalAddress
    }
    if (address != null) {
        // Address exists, so set the formatted text
        val formattedAddress = if (address?.line2.isNullOrEmpty()) {
            "${address?.line1}\n${address?.city}, ${address?.state}, ${address?.zip}"
        } else {
            "${address?.line1}\n${address?.line2}\n${address?.city}, ${address?.state}, ${address?.zip}"
        }
        textView.text = formattedAddress
        textView.visibility = View.VISIBLE
    } else {
        // No address, so hide the TextView
        textView.visibility = View.GONE
    }
}


@BindingAdapter("isVisibleIfNotNull")
fun isVisibleIfNotNull(view: View, url: String?) {
    if (url.isNullOrEmpty()) {
        Log.i("ElectionListAdapter", "URL is null or empty")
        view.visibility = View.GONE
    } else {
        Log.i("ElectionListAdapter", "URL:${url} ")
        view.visibility = View.VISIBLE
    }
}

@BindingAdapter("isLoadingVisible")
fun setIsVisible(view: View, isVisible: Boolean) {
    view.visibility = if (isVisible) View.VISIBLE else View.GONE
}

@BindingAdapter("isVisibleIfNotLoading")
fun isVisibleIfNotLoading(view: View, isLoading: Boolean) {
    view.visibility = if (!isLoading) View.VISIBLE else View.GONE
}

