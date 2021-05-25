package com.example.recyclerviewcontacts

import android.view.View

interface Imageable {
    var imageId: Int
    var listener: ((View) -> Unit)?
    var validator: ((String) -> Boolean)?
}