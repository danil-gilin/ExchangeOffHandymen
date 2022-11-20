package com.example.exchangeofhandymen.presenter.home.bag.listJob.adapterJob


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.exchangeofhandymen.databinding.JobsItemBinding
import com.example.exchangeofhandymen.entity.job.JobWithId

class AdapterJob(val clickdelte:((JobWithId)->Unit)?=null,val clickInfo:((JobWithId)->Unit)?=null,val clickselect:((JobWithId)->Unit)?=null) :ListAdapter<JobWithId,JobViewHolder>(JobDiffutil()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        return JobViewHolder(JobsItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val item=getItem(position)
        with(holder.binding){
            nameJobList.text="${nameJobList.text} ${item.name}"
            countWorkerJobList.text="${countWorkerJobList.text} ${item.workerAccept.size}/${item.countWorkers}"
            moneyJobList.text="${moneyJobList.text} ${item.money}"
            if(item.img.isNotEmpty()){
                Glide.with(jobImg.context).load(item.img[0]).centerCrop().into(jobImg)
            }
            if(clickdelte!=null){
                Log.d("DeleteJob", item.id.toString())
                btnDelete.text = "Удалить"
                btnDelete.setOnClickListener {
                    clickdelte.invoke(item)
                }
            }else{

            }

            if(clickInfo!=null){
                root.setOnClickListener {
                    clickInfo.invoke(item)
                }
            }

            if(clickselect!=null){
                btnDelete.text = "Выбрать"
                btnDelete.setOnClickListener {
                    clickselect.invoke(item)
                }
            }

            if(clickselect==null&&clickdelte==null) {
                btnDelete.isVisible = false
            }

        }
    }
}

class JobViewHolder(val binding: JobsItemBinding):RecyclerView.ViewHolder(binding.root)

class JobDiffutil:DiffUtil.ItemCallback<JobWithId>(){
    override fun areItemsTheSame(oldItem: JobWithId, newItem: JobWithId): Boolean {
      return oldItem.id==newItem.id
    }

    override fun areContentsTheSame(oldItem: JobWithId, newItem: JobWithId): Boolean {
        return oldItem==newItem
    }
}
