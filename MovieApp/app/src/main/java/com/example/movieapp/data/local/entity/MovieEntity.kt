package com.example.movieapp.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "movies")
class MovieEntity(
    @field:ColumnInfo(name = "id")
    @field:PrimaryKey
    val id: Long,

    @field:ColumnInfo(name = "title")
    val title: String,

    @field:ColumnInfo(name = "urlToImage")
    val urlToImage: String,

    @field:ColumnInfo(name = "releasedAt")
    val releasedAt: String

) : Parcelable