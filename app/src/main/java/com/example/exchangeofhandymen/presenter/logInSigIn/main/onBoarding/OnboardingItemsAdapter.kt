package com.example.exchangeofhandymen.presenter.logInSigIn.main.onBoarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.exchangeofhandymen.R
import com.example.exchangeofhandymen.entity.OnboardingItem

class OnboardingItemsAdapter(private val onboardingItems: List<OnboardingItem>):
    RecyclerView.Adapter<OnboardingItemsAdapter.OnboardingItemViewHolder>() {

    inner class OnboardingItemViewHolder(view:View):RecyclerView.ViewHolder(view){
        private val imageOnboarding=view.findViewById<ImageView>(R.id.imageOnboarding)
        private val textTitle=view.findViewById<TextView>(R.id.textTitle)
        private val textDescription=view.findViewById<TextView>(R.id.textDescription)

        fun bind(onboardingItem: OnboardingItem){
            Glide.with(imageOnboarding.context).load(onboardingItem.onboardingImage).centerInside().into(imageOnboarding)
            textTitle.text=onboardingItem.title
            textDescription.text=onboardingItem.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingItemViewHolder {
        return OnboardingItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.onboarding_item_container,parent,false))
    }

    override fun onBindViewHolder(holder: OnboardingItemViewHolder, position: Int) {
        holder.bind(onboardingItems[position] )
    }

    override fun getItemCount(): Int {
        return onboardingItems.size
    }
}