package com.example.exchangeofhandymen.entity.job

import com.example.exchangeofhandymen.entity.GeoPosition

data class Job(
    val employer:String,
    val workers:List<String>,
    val geopoint:GeoPosition?,
    val img:List<String>,
    val money:String,
    val countWorkers:String,
    val info:String,
    val name:String,
    val phone:String,
    val workerAccept: List<String>,
)
