package com.cashley.app.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cashley.app.R
import com.cashley.app.activity.OfferDetailActivity
import com.cashley.app.model.AllOfferData
import com.cashley.app.model.HomeData
import com.squareup.picasso.Picasso

class HighPayingAdapter(var allOfferList:ArrayList<HomeData.Data.HighestPaying>, var aContext:Context):RecyclerView.Adapter<HighPayingAdapter.ViewHolder>() {

    class ViewHolder(v:View):RecyclerView.ViewHolder(v)
    {
        var offerType:TextView=v.findViewById(R.id.offerType)
        var earnExtra:TextView=v.findViewById(R.id.earnExtra)
        var offerImage:ImageView=v.findViewById(R.id.offerImage)
        var totalAmount:TextView=v.findViewById(R.id.amount)
        var offerName:TextView=v.findViewById(R.id.offerName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.all_offer_list_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        holder.offerType.text=allOfferList[position].offerCategory
        holder.earnExtra.text="Per Refer Coins "+allOfferList[position].perRefer
        Picasso.get().load(allOfferList[position].offerImage).placeholder(R.drawable.baseline_image_24).error(R.drawable.baseline_image_24).into(holder.offerImage)
        holder.offerName.text=allOfferList[position].offerName
        holder.totalAmount.text=allOfferList[position].offerAmount

        holder.itemView.setOnClickListener {
            val intent= Intent(aContext, OfferDetailActivity::class.java)
            intent.putExtra("offerId",allOfferList[position].offerId.toString())
            aContext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return allOfferList.size
    }
}