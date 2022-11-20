package com.example.exchangeofhandymen.presenter.home.bag.listJob.adapterJob

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.exchangeofhandymen.databinding.JobsWorkerItemBinding
import com.example.exchangeofhandymen.entity.job.JobWithId

class AdapterWorkerJob(val clickdelte:((JobWithId)->Unit)?=null,val clickInfo:((JobWithId)->Unit)?=null, val clickselect:((JobWithId)->Unit)?=null) :
    ListAdapter<JobWithId, WorkerJobViewHolder>(WorkerJobDiffutil()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkerJobViewHolder {
        return WorkerJobViewHolder(JobsWorkerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: WorkerJobViewHolder, position: Int) {
        val item=getItem(position)
        with(holder.binding){
            nameJobList.text="${nameJobList.text} ${item.name}"
            countWorkerJobList.text="${countWorkerJobList.text} ${item.workerAccept.size}/${item.countWorkers}"
            moneyJobList.text="${moneyJobList.text} ${item.money}"
            if(item.img.isNotEmpty()){
                Glide.with(jobImg.context).load(item.img[0]).centerCrop().into(jobImg)
            }

            if(clickInfo!=null){
                root.setOnClickListener {
                    clickInfo.invoke(item)
                }
            }

            if(clickdelte!=null){
                btnDelete.setOnClickListener {
                    clickdelte.invoke(item)
                }
            }
            if(clickselect!=null){
                btnSelect.setOnClickListener {
                    clickselect.invoke(item)
                }
            }
        }
    }
}

class WorkerJobViewHolder(val binding: JobsWorkerItemBinding): RecyclerView.ViewHolder(binding.root)

class WorkerJobDiffutil: DiffUtil.ItemCallback<JobWithId>(){
    override fun areItemsTheSame(oldItem: JobWithId, newItem: JobWithId): Boolean {
        return oldItem.id==newItem.id
    }

    override fun areContentsTheSame(oldItem: JobWithId, newItem: JobWithId): Boolean {
        return oldItem==newItem
    }
}