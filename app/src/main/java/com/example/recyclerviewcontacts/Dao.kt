package com.example.recyclerviewcontacts

import androidx.room.*
import androidx.room.Dao
import com.example.recyclerviewcontacts.Entity.Companion.TABLE_NAME

@Dao
interface Dao {
    @Insert
    fun insert(entity: Entity):Long

    @Update
    fun update(entity: Entity)

    @Delete
    fun delete(entity: Entity)

    @Query("SELECT * FROM $TABLE_NAME WHERE id = :id")
    fun read(id: Int):List<Entity>

    @Query("SELECT * FROM $TABLE_NAME")
    fun getAll():List<Entity>
}