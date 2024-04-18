package com.example.market

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.market.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.scrollView

        val item: Item? = intent.getParcelableExtra("item")
        if (item != null) {

            binding.ivItem.setImageResource(item.imageResourceId)
            binding.tvSeller.text = item.seller
            binding.tvAddress.text = item.address
            binding.tvItem.text = item.item
            binding.tvDescription.text = item.description
            binding.tvPrice.text = item.getFormattedPrice()

            binding.btnLike.setOnClickListener {
                item.likeCount++
                binding.btnLike.contentDescription = item.likeCount.toString()
            }

            binding.btnChat.setOnClickListener {
                item.chatCount++
                binding.btnChat.contentDescription = item.chatCount.toString()
            }
        }
    }
}