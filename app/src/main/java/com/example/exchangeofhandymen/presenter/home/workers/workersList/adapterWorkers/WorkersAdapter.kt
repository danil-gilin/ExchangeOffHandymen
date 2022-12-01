package com.example.exchangeofhandymen.presenter.home.workers.workersList.adapterWorkers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.exchangeofhandymen.R
import com.example.exchangeofhandymen.databinding.WorkersItemBinding
import com.example.exchangeofhandymen.entity.worker.Worker
import com.example.exchangeofhandymen.entity.worker.WorrkerInt
import com.example.exchangeofhandymen.presenter.home.profile.profileUser.profileAdapter.SkillsAdapter
import com.google.android.flexbox.FlexboxLayoutManager
import kotlin.math.roundToInt

class WorkersAdapter(private val clickWorker:(WorrkerInt)->Unit) :
    ListAdapter<WorrkerInt, SkillViewHolder>(SkillDiffutil()) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillViewHolder {
        return SkillViewHolder(
            WorkersItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SkillViewHolder, position: Int) {
        val item = getItem(position)
        val adapterSkillsWorker= SkillsAdapter()
        with(holder.binding) {
            if(item.img.isNotEmpty()) {
                Glide.with(workerImg.context).load(item.img).circleCrop().into(workerImg)
            }else{
                Glide.with(workerImg.context).load(R.drawable.worker_icon).circleCrop().into(workerImg)
            }
            nameWorker.text = item.name
            geoWoker.text = item.geoPoint?.getTownName(this.root.context) ?: ""
            workersSkill.adapter=adapterSkillsWorker
            workersSkill.layoutManager=FlexboxLayoutManager(root.context)
            val distanceFromat=(item.distance.toDouble() * 100).roundToInt() / 100.0
            distance.text="$distanceFromat км"
            if(item.skills!=null){
                adapterSkillsWorker.submitList(item.skills)
            }

            root.setOnClickListener {
                clickWorker(item)
            }

        }
    }
}

class SkillViewHolder(val binding: WorkersItemBinding) : RecyclerView.ViewHolder(binding.root)

class SkillDiffutil : DiffUtil.ItemCallback<WorrkerInt>() {
    override fun areItemsTheSame(oldItem: WorrkerInt, newItem: WorrkerInt): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: WorrkerInt, newItem: WorrkerInt): Boolean {
        return oldItem.equals(newItem)
    }

}