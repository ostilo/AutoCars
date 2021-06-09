package com.example.autocars.core

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.autocars.db.entities.Favorites


@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: Favorites)

    @Query("delete from favorites where id = :id")
    fun delete( id: String)

    @Query("select * from favorites order by id desc")
    fun getAllFavorites(): LiveData<List<Favorites>>
}