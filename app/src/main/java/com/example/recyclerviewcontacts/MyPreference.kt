package com.example.recyclerviewcontacts

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.example.recyclerviewcontacts.Entity.Companion.DEFAULT_VAL

class MyPreference(
    context: Context?,
    attrs: AttributeSet? = null
) : Preference(context, attrs), Imageable {
    init {
        layoutResource = R.layout.layout_my_pref
        summary = DEFAULT_VAL
    }
    override var imageId: Int = -1
        set(value) {
            field = value
            setIcon(value)
        }

    override var listener: ((View) -> Unit)? = null

    override fun onBindViewHolder(holder: PreferenceViewHolder?) {
        super.onBindViewHolder(holder)

        holder?.itemView?.findViewById<ImageView>(android.R.id.icon)?.setOnClickListener(listener)
    }
}