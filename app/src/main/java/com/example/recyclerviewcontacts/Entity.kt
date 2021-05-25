package com.example.recyclerviewcontacts

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.recyclerviewcontacts.Entity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
class Entity(
    var name: String = "",
    var phone: String = "",
    var email: String = "",
    var officeNumber: String = "",
    var major: String = "",
    var takeCourses: String = "",
    var memo: String = ""
) {
    companion object {
        const val TABLE_NAME = "ouo"
    }

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

}