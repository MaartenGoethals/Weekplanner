package be.dekade.weekplanner.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import be.dekade.weekplanner.R
import be.dekade.weekplanner.data.ActiviteitEnDagGegevensDag
import be.dekade.weekplanner.databinding.ListItemActiviteitBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class ActiviteitListItemAdapter(val uitstelClickListener: UitstelClickListener, val afgewerktClickListener: AfgewerktClickListener, val detailClickListener: DetailClickListener) :
    ListAdapter<ActiviteitEnDagGegevensDag, RecyclerView.ViewHolder>(ActiviteitDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiviteitViewHolder {
        val viewHolder = ActiviteitViewHolder(

            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_activiteit,
                parent,
                false
            )
        )
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ActiviteitViewHolder).bind(getItem(position))
        holder.setUitstelListener {
            uitstelClickListener.onClick(getItem(position))
        }
        holder.setAfgewerktListener {
            afgewerktClickListener.onClick(getItem(position))
        }
        holder.setDetailListener {
            detailClickListener.onClick(getItem(position))
        }
    }

    class AfgewerktClickListener(val clickListener: (gegevens: ActiviteitEnDagGegevensDag) -> Unit){
        fun onClick(gegevens: ActiviteitEnDagGegevensDag) = clickListener(gegevens)
    }

    class UitstelClickListener(val clickListener: (gegevens: ActiviteitEnDagGegevensDag) -> Unit){
        fun onClick(gegevens: ActiviteitEnDagGegevensDag) = clickListener(gegevens)
    }

    class DetailClickListener(val clickListener: (gegevens: ActiviteitEnDagGegevensDag) -> Unit){
        fun onClick(gegevens: ActiviteitEnDagGegevensDag) = clickListener(gegevens)
    }

    class ActiviteitViewHolder(
        private val binding: ListItemActiviteitBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.isExpanded = false
            binding.setExpandClickListener {
                binding.apply {
                    isExpanded = isExpanded?.not()
                }
            }
        }

        fun setUitstelListener(listener: () -> Unit) {
            binding.setUitstellenClickListener {
                listener()
            }
        }

        fun setAfgewerktListener(listener: () -> Unit) {
            binding.setAfgewerktClickListener {
                listener()
            }
        }

        fun setDetailListener(listener: () -> Unit) {
            binding.setAanpassenClickListener {
                listener()
            }
        }

        fun bind(item: ActiviteitEnDagGegevensDag) {
            binding.apply {
                data = item
                executePendingBindings()
            }
        }
    }

}

private class ActiviteitDiffCallback : DiffUtil.ItemCallback<ActiviteitEnDagGegevensDag>() {
    override fun areItemsTheSame(
        oldItem: ActiviteitEnDagGegevensDag,
        newItem: ActiviteitEnDagGegevensDag
    ): Boolean {
        return oldItem.activiteit.activiteitId == newItem.activiteit.activiteitId && oldItem.dagGegevens.gegevensId == newItem.dagGegevens.gegevensId
    }

    override fun areContentsTheSame(
        oldItem: ActiviteitEnDagGegevensDag,
        newItem: ActiviteitEnDagGegevensDag
    ): Boolean {
        return oldItem == newItem
    }
}
