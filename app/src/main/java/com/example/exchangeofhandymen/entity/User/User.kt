package com.example.exchangeofhandymen.entity.User

import android.os.Parcelable
import com.example.exchangeofhandymen.entity.GeoPosition
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class User(
    val name:String?,
    val phone:String,
    val email:String?,
    val rating:Double?,
    val geoPoint:@RawValue GeoPosition?,
    val skills:List<String>?,
    val description:String?,
    val wokerFlag:Boolean,
    val img:String
) : Parcelable