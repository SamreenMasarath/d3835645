package com.example.alertguard.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.alertguard.db.models.UserModel

@Dao
interface UserModelDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userModel: UserModel)

    @Query("Select * from UserTable where email = :email")
    fun getUser(email: String): UserModel?

    @Query("Select * from UserTable where id = :id")
    fun getUserById(id: Int): UserModel?

}