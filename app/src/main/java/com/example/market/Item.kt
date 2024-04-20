package com.example.market

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.NumberFormat
import java.util.Locale

@Parcelize
data class Item(
    val imageResourceId: Int,
    val seller: String,
    val address: String,
    val item: String,
    val description: String,
    val price: Int,
    var likeCount: Int,
    var chatCount: Int,
) : Parcelable

// 확장 함수
fun Item.getFormattedPrice(): String {
    return NumberFormat.getNumberInstance(Locale.US).format(price)
}