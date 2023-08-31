package com.rewardpay.app.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.rewardpay.app.R
import com.rewardpay.app.activity.OfferDetailActivity
import com.rewardpay.app.model.HomeData
import com.squareup.picasso.Picasso

class ViewPagerAdapter(var bannerList:ArrayList<HomeData.Data.SliderList>) :PagerAdapter(){

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return bannerList.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val myImageLayout = LayoutInflater.from(view.context).inflate(R.layout.view_pager_layout, view, false)
        val myImage = myImageLayout.findViewById<View>(R.id.banner) as ImageView
        Picasso.get().load(bannerList[position].offerImage).placeholder(R.drawable.baseline_image_24).error(R.drawable.baseline_image_24)
            .into(myImage)
        view.addView(myImageLayout, 0)

        myImageLayout.setOnClickListener {
            val intent= Intent(view.context, OfferDetailActivity::class.java)
            intent.putExtra("offerId",bannerList[position].offerId.toString())
            view.context.startActivity(intent)
        }
        return myImageLayout
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }
}