package com.example.market

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.market.databinding.ItemLayoutBinding

// 생성자에 itemList를 넘기지 말고 아래 "submitList" 함수를 public으로 만들어서 외부에서 호출하도록 설정
class ItemAdapter(private val onClick: (Item) -> Unit) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    private var itemList: List<Item> = emptyList()
    @SuppressLint("NotifyDataSetChanged")
    fun submitList(inputList: List<Item>) {
        itemList = inputList
        notifyDataSetChanged()
    }

    // inner class로 작성하면 부모 클라스인 ItemAdapter 내부 프로퍼티나 함수에 참조가 이루어지지만 메모리 누수로 이어질 수 있기에
    // inner class가 아닌 static 형태의 class로 작성할 것
    // 생성자(constructor)에 필요한 변수(예: onClick)를 주입하여 사용할 수 있음.
    class ViewHolder(private val binding: ItemLayoutBinding, private val onClick: (Item) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        private var item: Item? = null

        init {
            binding.root.setOnClickListener {
                item?.let {
                    onClick(it)
                }
            }
        }

        fun bind(item: Item) {
            this.item = item
            binding.ivItem.setImageResource(item.imageResourceId)
            binding.tvItem.text = item.item
            binding.tvAddress.text = item.address
            binding.tvPrice.text = item.getFormattedPrice()
            binding.tvChatCount.text = item.chatCount.toString()
            binding.tvLikeCount.text = item.likeCount.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.bind(item = currentItem)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}