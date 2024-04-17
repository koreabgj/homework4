package com.example.market

import android.os.Parcel
import android.os.Parcelable
import java.text.NumberFormat
import java.util.Locale

data class Item(
    val imageResourceId: Int,
    val seller: String,
    val address: String,
    val item: String,
    val description: String,
    val price: Int,
    var likeCount: Int,
    var chatCount: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(imageResourceId)
        parcel.writeString(seller)
        parcel.writeString(address)
        parcel.writeString(item)
        parcel.writeString(description)
        parcel.writeInt(price)
        parcel.writeInt(likeCount)
        parcel.writeInt(chatCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun getFormattedPrice(): String {
        return NumberFormat.getNumberInstance(Locale.US).format(price)
    }

    companion object CREATOR : Parcelable.Creator<Item> {
        override fun createFromParcel(parcel: Parcel): Item {
            return Item(parcel)
        }

        override fun newArray(size: Int): Array<Item?> {
            return arrayOfNulls(size)
        }
    }
}