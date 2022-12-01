package com.example.exchangeofhandymen.entity.worker

import android.os.Parcelable
import com.example.exchangeofhandymen.entity.GeoPosition
import com.example.exchangeofhandymen.entity.Skill
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Worker (
   override val id:String,
   override val name:String?,
   override val phone:String,
   override val email:String?,
   override val rating:Double?,
   override val geoPoint:GeoPosition?,
   override val skills:List<Skill>?,
   override val description:String?,
   override val wokerFlag:Boolean,
   override val img:String,
   override val distance:String,
) : Parcelable,WorrkerInt