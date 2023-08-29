package com.cashley.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cashley.app.R
import com.cashley.app.model.HistoryData
import com.squareup.picasso.Picasso

class OfferHistoryAdapter(var list: ArrayList<HistoryData.Data.OfferHistory>, var context: Context) :
    RecyclerView.Adapter<OfferHistoryAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var offerImage: ImageView = v.findViewById(R.id.offerImage)
        var title: TextView = v.findViewById(R.id.title)
        var date: TextView = v.findViewById(R.id.date)
        var amount: TextView = v.findViewById(R.id.amount)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.history_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = list[position].offerName
        holder.date.text = list[position].date
        holder.amount.text = list[position].amount
        Picasso.get().load(list[position].offerImageUrl).placeholder(R.drawable.baseline_image_24)
            .error(
                R.drawable.baseline_image_24
            ).into(holder.offerImage)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}