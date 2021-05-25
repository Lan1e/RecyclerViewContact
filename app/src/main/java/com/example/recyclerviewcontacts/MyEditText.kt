package com.example.recyclerviewcontacts

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.preference.EditTextPreference
import androidx.preference.Preference.OnPreferenceChangeListener
import androidx.preference.PreferenceViewHolder
import java.lang.Exception

class MyEditText(
    context: Context?,
    attributes: AttributeSet? = null
) : EditTextPreference(context, attributes), Imageable {
    init {
        summary = Entity.DEFAULT_VAL
        onPreferenceChangeListener = OnPreferenceChangeListener { _, newValue ->
            true.also {
                summary = newValue?.toString() ?: Entity.DEFAULT_VAL
            }
        }
        layoutResource = R.layout.layout_my_pref
    }

    override var imageId: Int = -1
        set(value) {
            field = value
            setIcon(value)
        }

    override var listener: ((View) -> Unit)? = null
    override var validator: ((String) -> Boolean)? = null

    override fun onBindViewHolder(holder: PreferenceViewHolder?) {
        super.onBindViewHolder(holder)
        holder?.itemView?.findViewById<ImageView>(android.R.id.icon)?.setOnClickListener(listener)
    }
}