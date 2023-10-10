package com.example.githubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.githubuser.data.local.entity.FavoriteUser

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: FavoriteUser)
    @Delete
    fun delete(user: FavoriteUser)
    @Update
    fun updateUser(user: FavoriteUser)
    @Query("SELECT * FROM FavoriteUser ORDER BY username ASC")
    fun getFavoriteUserByUsername(): LiveData<List<FavoriteUser>>

    @Query("SELECT count(*) FROM favoriteUser WHERE username = :username")
    fun checkUser(username: String?): Int
    @Query("DELETE FROM FavoriteUser WHERE username = :username")
    fun deleteNonFavoriteUserByUsername(username: String?): Int

}