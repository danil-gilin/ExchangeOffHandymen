package com.example.exchangeofhandymen.presenter.home.profile.profileUser.profileAdapter


import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.marginLeft
import androidx.core.view.marginTop
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.exchangeofhandymen.R
import com.example.exchangeofhandymen.databinding.SkillItemBinding
import com.example.exchangeofhandymen.entity.Skill

class SkillsAdapter(
    private val clickSkill: (() -> Unit)? = null,
    private val clickClear: ((Skill) -> Unit)? = null
) : ListAdapter<Skill, SkillViewHolder>(SkillDiffutil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillViewHolder {
        return SkillViewHolder(
            SkillItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SkillViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.skillName.text = item.name

        if (clickSkill != null) {
            if (item.name == "+") {
                holder.binding.root.setOnClickListener {
                    clickSkill.invoke()
                }
            }
        }

        if (clickClear != null) {
            if (position != itemCount - 1) {
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.marginEnd = 8
                holder.binding.skillName.layoutParams = params

                holder.binding.clearBtn.visibility = View.VISIBLE
                holder.binding.clearBtn.setOnClickListener {
                    clickClear.invoke(item)
                }
            }
        }
    }
}

class SkillViewHolder(val binding: SkillItemBinding) : RecyclerView.ViewHolder(binding.root)

class SkillDiffutil : DiffUtil.ItemCallback<Skill>() {
    override fun areItemsTheSame(oldItem: Skill, newItem: Skill): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Skill, newItem: Skill): Boolean {
        return oldItem == newItem
    }

}