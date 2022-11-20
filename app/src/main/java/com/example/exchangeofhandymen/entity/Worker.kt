package com.example.exchangeofhandymen.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Worker (
    val id:String,
    val name:String?,
    val phone:String,
    val email:String?,
    val rating:Double?,
    val geoPoint:@RawValue GeoPosition?,
    val skills:List<Skill>?,
    val description:String?,
    val wokerFlag:Boolean,
    val img:String,
    val distance:String,
) : Parcelable