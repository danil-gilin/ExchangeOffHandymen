package com.example.exchangeofhandymen.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Skill(
    val id:String,
    val name:String,
    val population:Int
): Parcelable
