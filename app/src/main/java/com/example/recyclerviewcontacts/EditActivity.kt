package com.example.recyclerviewcontacts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class EditActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        supportFragmentManager.beginTransaction().replace(R.id.container, EditFragment()).commit()
    }
}