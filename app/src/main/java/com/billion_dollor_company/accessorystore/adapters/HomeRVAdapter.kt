package com.billion_dollor_company.accessorystore.adapters

import android.content.Context
import android.graphics.Paint
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.billion_dollor_company.accessorystore.R
import com.billion_dollor_company.accessorystore.models.WatchInfo
import com.bumptech.glide.Glide

class HomeRVAdapter(
    private val context: Context,
    private val list: List<WatchInfo>,
    private val onClickListener: OnClickListener
) : RecyclerView.Adapter<HomeRVAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_watch_item, parent, false)
        return ViewHolder(view,onClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info: WatchInfo = list[position]
        val company:TextView = holder.company
        val name: TextView = holder.name
        val origPrice: TextView = holder.origPrice
        val offerPrice: TextView = holder.offerPrice
        val discount: TextView = holder.discount

        val fav:ImageView = holder.favorite
        val image: ImageView = holder.image
        val container: ConstraintLayout = holder.container
        val clickListener:OnClickListener = holder.clickListener

        company.text = info.company
        name.text = info.name
        origPrice.text = "₹${info.price}"
        Log.d("WATCH_INFO", info.toString())
        Log.d("WATCH_INFO","For ${info.name} the price is ${info.price} and after discount "+getOfferPrice(info.price,info.discount).toString())
        offerPrice.text = "₹"+getOfferPrice(info.price,info.discount)
        origPrice.paintFlags = origPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        discount.text = "${info.discount}% OFF"


        //to load the image from the url into the imageview
        Glide.with(context).load(Uri.parse(info.imageURL)).into(image)

        container.setOnClickListener{
            clickListener.onNoteClick(position)
        }

        fav.setOnClickListener{
            fav.setImageResource(R.drawable.baseline_favorite_24)
        }
    }

    private fun getOfferPrice(price: Int, discount: Int): Int {
        return price - (price * discount) / 100
    }

    override fun getItemCount(): Int {
        return list.size
    }


    class ViewHolder(itemView: View,onNoteClickListener: OnClickListener) :
        RecyclerView.ViewHolder(itemView){
        var company: TextView = itemView.findViewById(R.id.tv_company)
        var name: TextView = itemView.findViewById(R.id.tv_name)
        var offerPrice: TextView = itemView.findViewById(R.id.tv_offerPrice)
        var origPrice: TextView = itemView.findViewById(R.id.tv_origPrice)
        var discount: TextView = itemView.findViewById(R.id.tv_discount)

        var image: ImageView = itemView.findViewById(R.id.iv_image)
        var favorite: ImageView = itemView.findViewById(R.id.iv_favorite)

        var container:ConstraintLayout = itemView.findViewById(R.id.cv_container)
        var clickListener = onNoteClickListener
    }

    interface OnClickListener {
        fun onNoteClick(pos:Int)
    }
}