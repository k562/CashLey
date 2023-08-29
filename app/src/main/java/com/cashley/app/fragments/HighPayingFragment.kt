package com.cashley.app.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cashley.app.adapter.AllOfferAdapter
import com.cashley.app.adapter.HighPayingAdapter
import com.cashley.app.databinding.FragmentHighPayingBinding
import com.cashley.app.model.AllOfferData
import com.cashley.app.model.HomeData

class HighPayingFragment(highestPaying: ArrayList<HomeData.Data.HighestPaying>) : Fragment() {

    private lateinit var binding:FragmentHighPayingBinding
    private var allOfferList=ArrayList<AllOfferData>()
    lateinit var aContext:Context
    private var highestPaying1=highestPaying

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
        binding=FragmentHighPayingBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (highestPaying1!=null) {
            binding.allOfferRecyclerView.visibility=View.VISIBLE
            binding.noListFound.visibility=View.GONE
            binding.allOfferRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.allOfferRecyclerView.adapter = HighPayingAdapter(highestPaying1, aContext)
        }
        else
        {
            binding.allOfferRecyclerView.visibility=View.GONE
            binding.noListFound.visibility=View.VISIBLE
        }
    }

}