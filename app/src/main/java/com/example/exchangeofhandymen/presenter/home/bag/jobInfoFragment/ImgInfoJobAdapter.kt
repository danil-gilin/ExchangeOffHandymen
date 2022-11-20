package com.example.exchangeofhandymen.presenter.home.bag.jobInfoFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.exchangeofhandymen.databinding.ImgJobItemBinding


class ImgInfoJobAdapter (private val imgItems: List<String>): RecyclerView.Adapter<ImgInfoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImgInfoViewHolder {
        val binding=ImgJobItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ImgInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImgInfoViewHolder, position: Int) {
        val img=imgItems[position]
        Glide.with(holder.binding.root).load(img).centerInside().into(holder.binding.imageView2)
    }

    override fun getItemCount(): Int {
         return imgItems.size
    }

}

class ImgInfoViewHolder(val binding: ImgJobItemBinding):RecyclerView.ViewHolder(binding.root)