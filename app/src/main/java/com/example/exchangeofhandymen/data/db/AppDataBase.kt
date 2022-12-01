package com.example.exchangeofhandymen.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.exchangeofhandymen.entity.User.UserDB
import com.example.exchangeofhandymen.entity.worker.WorkerDB

@Database(entities =[WorkerDB::class,UserDB::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase(){
    abstract fun workerDao():WorkerDao
    abstract fun userDao():UserDao
}