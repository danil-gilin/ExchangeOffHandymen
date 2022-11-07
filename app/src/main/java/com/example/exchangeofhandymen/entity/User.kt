package com.example.exchangeofhandymen.entity

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class User(
    val name:String?,
    val phone:String,
    val email:String?,
    val rating:Double?,
    val geoPoint:@RawValue GeoPoint?,
    val skills:List<String>?,
    val description:String?
) : Parcelable
