package com.example.githubuser.data.local.entity


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoriteUser")
data class FavoriteUser(
    @PrimaryKey
    @ColumnInfo(name = "username")
    var username: String = "",

    @ColumnInfo(name = "avatar")
    var avatarUrl: String? = null,
)