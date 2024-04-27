package com.example.market

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.market.databinding.ActivityDetailBinding
import java.lang.IllegalStateException

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val item by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("item", Item::class.java)
        } else {
            intent.getParcelableExtra("item")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setButtonClickListeners()
        setViewWithItem()
    }

    private fun setButtonClickListeners(){
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnLike.setOnClickListener {
            item?.let{
                it.likeCount++
            }
        }

        binding.btnChat.setOnClickListener {
            item?.let{
                it.chatCount++
            }
        }
    }

    private fun setViewWithItem(){
        item?.let{
            binding.ivItem.setImageResource(it.imageResourceId)
            binding.tvSeller.text = it.seller
            binding.tvAddress.text = it.address
            binding.tvItem.text = it.item
            binding.tvDescription.text = it.description
            binding.tvPrice.text = it.getFormattedPrice()
        } ?: throw IllegalStateException("Item이 null입니다.")
    }
}