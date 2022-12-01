package com.example.exchangeofhandymen.entity.User

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.exchangeofhandymen.entity.GeoPosition
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Entity(tableName = "User")
@Parcelize
data class UserDB(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id:String,
    @ColumnInfo(name = "name")
    val name:String?,
    @ColumnInfo(name = "phone")
    val phone:String,
    @ColumnInfo(name = "email")
    val email:String?,
    @ColumnInfo(name = "rating")
    val rating:Double?,
    @ColumnInfo(name = "geoPoint")
    val geoPoint:@RawValue GeoPosition?,
    @ColumnInfo(name = "skills")
    val skills:List<String>?,
    @ColumnInfo(name = "description")
    val description:String?,
    @ColumnInfo(name = "wokerFlag")
    val wokerFlag:Boolean,
    @ColumnInfo(name = "img")
    val img:String
):Parcelable
