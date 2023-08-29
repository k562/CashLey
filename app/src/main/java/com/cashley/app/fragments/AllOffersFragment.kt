package com.cashley.app.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cashley.app.adapter.AllOfferAdapter
import com.cashley.app.databinding.FragmentAllOffersBinding
import com.cashley.app.model.AllOfferData
import com.cashley.app.model.HomeData

class AllOffersFragment(allOffers: ArrayList<HomeData.Data.AllOffer>) : Fragment() {

    private lateinit var binding:FragmentAllOffersBinding
    lateinit var aContext:Context
    private var allOffers1=allOffers

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is FragmentActivity)
        {
            aContext=context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding=FragmentAllOffersBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (allOffers1!=null) {
            binding.allOfferRecyclerView.visibility=View.VISIBLE
            binding.noListFound.visibility=View.GONE
            binding.allOfferRecyclerView.layoutManager = LinearLayoutManager(aContext)
            binding.allOfferRecyclerView.adapter = AllOfferAdapter(allOffers1, aContext)
        }
        else
        {
            binding.allOfferRecyclerView.visibility=View.GONE
            binding.noListFound.visibility=View.VISIBLE
        }
    }

}