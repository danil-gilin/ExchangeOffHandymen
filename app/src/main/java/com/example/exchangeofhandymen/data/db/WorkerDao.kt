package com.example.exchangeofhandymen.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import com.example.exchangeofhandymen.entity.worker.Worker
import com.example.exchangeofhandymen.entity.worker.WorkerDB

@Dao
interface WorkerDao {
    @Insert(onConflict =IGNORE )
    suspend fun addWorker(workerDB: WorkerDB)

    @Query("SELECT * FROM Worker")
    suspend fun getListWorker():List<WorkerDB>

    @Query("DELETE FROM Worker WHERE Id=:id")
    suspend fun deleteListWorker(id:String)

    @Query("DELETE FROM Worker ")
    suspend fun deleteAll()
}