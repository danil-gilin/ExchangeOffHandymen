package com.example.exchangeofhandymen.entity.worker

import com.example.exchangeofhandymen.entity.GeoPosition
import com.example.exchangeofhandymen.entity.Skill

interface WorrkerInt {
    val id:String
    val name:String?
    val phone:String
    val email:String?
    val rating:Double?
    val geoPoint: GeoPosition?
    val skills:List<Skill>?
    val description:String?
    val wokerFlag:Boolean
    val img:String
    val distance:String
}