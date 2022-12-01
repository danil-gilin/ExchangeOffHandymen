package com.example.exchangeofhandymen.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.exchangeofhandymen.entity.User.UserDB

@Dao
interface UserDao {
    @Insert(onConflict = REPLACE)
    suspend fun addUser(userDB: UserDB)

    @Query("DELETE FROM User")
    suspend fun deleteUser()

    @Query("SELECT * FROM User WHERE id=:uid")
    suspend fun getUser(uid:String):UserDB?
}