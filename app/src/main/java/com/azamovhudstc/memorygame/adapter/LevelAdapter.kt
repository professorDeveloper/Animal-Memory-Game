package com.azamovhudstc.memorygame.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azamovhudstc.memorygame.data.room.entity.GameEntity
import com.azamovhudstc.memorygame.databinding.ItemMiniLevelBinding


class LevelAdapter : ListAdapter<GameEntity, LevelAdapter.ViewHolder>(itemEventCallback) {

    private var itemClickListener: ((GameEntity) -> Unit)? = null

    fun setSwitchChangedListener(block: (GameEntity) -> Unit) {
        itemClickListener = block
    }


    inner class ViewHolder(private val binding: ItemMiniLevelBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                itemClickListener?.invoke(getItem(absoluteAdapterPosition))
            }
        }

        fun onBind() {
            val data = getItem(absoluteAdapterPosition)

            binding.number.text = data.number.toString()

            if (absoluteAdapterPosition != 0) {
                if (data.state) {
                    binding.lock.visibility = View.GONE
                    binding.number.visibility = View.VISIBLE
                    itemView.isClickable = true
                } else {
                    binding.lock.visibility = View.VISIBLE
                    binding.number.visibility = View.GONE
                    itemView.isClickable = false
                }
            }

            when (data.star) {
                0 -> {
                    binding.starOne.visibility = View.GONE
                    binding.starTwo.visibility = View.GONE
                    binding.starThree.visibility = View.GONE
                }
                1 -> {
                    binding.starOne.visibility = View.VISIBLE
                    binding.starTwo.visibility = View.GONE
                    binding.starThree.visibility = View.GONE
                }
                2 -> {
                    binding.starOne.visibility = View.VISIBLE
                    binding.starTwo.visibility = View.VISIBLE
                    binding.starThree.visibility = View.GONE
                }
                3 -> {
                    binding.starOne.visibility = View.VISIBLE
                    binding.starTwo.visibility = View.VISIBLE
                    binding.starThree.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemMiniLevelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.onBind()
}

private val itemEventCallback = object : DiffUtil.ItemCallback<GameEntity>() {
    override fun areItemsTheSame(oldItem: GameEntity, newItem: GameEntity) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: GameEntity, newItem: GameEntity) =
        oldItem.id == newItem.id
                && oldItem.state == newItem.state
                && oldItem.star == newItem.star
                && oldItem.number == newItem.number
                && oldItem.level == newItem.level
                && oldItem.time == newItem.time
                && oldItem.imageList == newItem.imageList

}