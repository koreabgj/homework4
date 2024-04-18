package com.example.market

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.market.databinding.ItemLayoutBinding

class ItemAdapter(private val itemList: List<Item>, private val onClick: (Item) -> Unit) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {

            binding.ivItem.setImageResource(item.imageResourceId)
            binding.tvItem.text = item.item
            binding.tvAddress.text = item.address
            binding.tvPrice.text = item.price.toString()
            binding.tvChatCount.text = item.chatCount.toString()
            binding.tvLikeCount.text = item.likeCount.toString()

            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onClick(itemList[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}