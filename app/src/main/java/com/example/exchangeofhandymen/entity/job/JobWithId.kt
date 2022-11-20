package com.example.exchangeofhandymen.entity.job

import android.os.Parcelable
import com.example.exchangeofhandymen.entity.GeoPosition
import kotlinx.android.parcel.Parcelize

@Parcelize
data class JobWithId (
    val id:String,
    val employer:String,
    val workers:List<String>,
    val geopoint: GeoPosition?,
    val img:List<String>,
    val money:String,
    val countWorkers:String,
    val info:String,
    val name:String,
    val phone:String,
    val workerAccept: List<String>,
):Parcelable