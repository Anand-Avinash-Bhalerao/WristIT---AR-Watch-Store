
package com.billion_dollor_company.accessorystore.models
import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable


data class WatchInfo(
    val company: String,
    val name:String, val description:String,
    val price:Int,
    val discount:Int,
    val imageURL:String,
    val type: String)