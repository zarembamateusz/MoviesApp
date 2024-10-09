package com.shmz.core_database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_info")
data class MovieInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val isFavorite: Boolean = false
)