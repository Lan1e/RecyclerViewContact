package com.example.recyclerviewcontacts

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.recyclerviewcontacts.Entity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
class Entity(
    var name: String = DEFAULT_VAL,
    var phone: String = DEFAULT_VAL,
    var email: String = DEFAULT_VAL,
    var officeNumber: String = DEFAULT_VAL,
    var major: String = DEFAULT_VAL,
    var takeCourses: String = DEFAULT_VAL,
    var memo: String = DEFAULT_VAL
) {
    companion object {
        const val TABLE_NAME = "ouo"
        const val DEFAULT_VAL = " - "
    }

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

}