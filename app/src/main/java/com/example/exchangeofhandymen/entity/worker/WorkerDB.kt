package com.example.exchangeofhandymen.entity.worker

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.exchangeofhandymen.entity.GeoPosition
import com.example.exchangeofhandymen.entity.Skill
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Entity(tableName = "Worker")
@Parcelize
data class WorkerDB (
    @PrimaryKey
    @ColumnInfo(name = "Id")
    override val id:String,
    @ColumnInfo(name = "name")
    override val name:String?,
    @ColumnInfo(name = "phone")
    override val phone:String,
    @ColumnInfo(name = "email")
    override val email:String?,
    @ColumnInfo(name = "rating")
    override val rating:Double?,
    @ColumnInfo(name = "geoPoint")
    override val geoPoint: GeoPosition?,
    @ColumnInfo(name = "skills")
    override val skills:List<Skill>?,
    @ColumnInfo(name = "description")
    override val description:String?,
    @ColumnInfo(name = "wokerFlag")
    override  val wokerFlag:Boolean,
    @ColumnInfo(name = "img")
    override val img:String,
    @ColumnInfo(name = "distance")
    override val distance:String,
): Parcelable,WorrkerInt
