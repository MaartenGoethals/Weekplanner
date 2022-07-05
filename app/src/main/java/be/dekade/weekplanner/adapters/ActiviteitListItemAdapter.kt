package be.dekade.weekplanner.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import be.dekade.weekplanner.R
import be.dekade.weekplanner.data.Activiteit
import be.dekade.weekplanner.databinding.ListItemActiviteitBinding

class ActiviteitListItemAdapter : ListAdapter<Activiteit, ActiviteitListItemAdapter.ActiviteitViewHolder>(ActiviteitDiffCallback()){

    class ActiviteitViewHolder(
        private val binding: ListItemActiviteitBinding
    ): RecyclerView.ViewHolder(binding.root) {
        init {
            //binding.setClickListener TODO
        }

        fun bind(item: Activiteit) {
            binding.apply {
                activiteit = item
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiviteitViewHolder {
        return ActiviteitViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_activiteit,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ActiviteitViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private class ActiviteitDiffCallback : DiffUtil.ItemCallback<Activiteit>(){
    override fun areItemsTheSame(oldItem: Activiteit, newItem: Activiteit): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Activiteit, newItem: Activiteit): Boolean {
        return oldItem == newItem
    }
}
