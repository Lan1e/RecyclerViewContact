package com.example.recyclerviewcontacts

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.preference.EditTextPreference
import androidx.preference.Preference.OnPreferenceChangeListener
import androidx.preference.PreferenceViewHolder

class MyEditText(context: Context?, attributes: AttributeSet? = null) :
    EditTextPreference(context, attributes), Imagable {
    override var image: Int = -1

    init {
        summary = ""
        onPreferenceChangeListener = OnPreferenceChangeListener { _, newValue ->
            true.also {
                summary = newValue?.toString() ?: ""
            }
        }
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder?) {
        super.onBindViewHolder(holder)
        holder?.itemView?.findViewById<LinearLayout>(android.R.id.widget_frame)?.let {
            if (image != -1) {
                it.addView(ImageView(context).apply {
                    setImageResource(image)
                })
            }
        }
    }
}