package com.example.exchangeofhandymen.presenter.home.bag.jobInfoFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.exchangeofhandymen.R
import com.example.exchangeofhandymen.databinding.WorkersItemBinding
import com.example.exchangeofhandymen.entity.worker.Worker
import com.example.exchangeofhandymen.presenter.home.profile.profileUser.profileAdapter.SkillsAdapter
import com.google.android.flexbox.FlexboxLayoutManager
import kotlin.math.roundToInt

class WorkerJobInfoAdater () :
    ListAdapter<Worker, WorkerJobInfoViewHolder>(WorkerJobInfoDiffutil()) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkerJobInfoViewHolder {
        return WorkerJobInfoViewHolder(
            WorkersItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WorkerJobInfoViewHolder, position: Int) {
        val item = getItem(position)
        val adapterSkillsWorker= SkillsAdapter()
        with(holder.binding) {
            if(item.img.isNotEmpty()) {
                Glide.with(workerImg.context).load(item.img).circleCrop().into(workerImg)
            }else{
                Glide.with(workerImg.context).load(R.drawable.worker_icon).circleCrop().into(workerImg)
            }
            nameWorker.text = item.name
            geoWoker.text =item.phone
            workersSkill.adapter=adapterSkillsWorker
            workersSkill.layoutManager= FlexboxLayoutManager(root.context)
            val distanceFromat=(item.distance.toDouble() * 100).roundToInt() / 100.0
            distance.text="$distanceFromat км"
            if(item.skills!=null){
                adapterSkillsWorker.submitList(item.skills)
            }

        }
    }
}

class WorkerJobInfoViewHolder(val binding: WorkersItemBinding) : RecyclerView.ViewHolder(binding.root)

class WorkerJobInfoDiffutil : DiffUtil.ItemCallback<Worker>() {
    override fun areItemsTheSame(oldItem: Worker, newItem: Worker): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Worker, newItem: Worker): Boolean {
        return oldItem == newItem
    }

}