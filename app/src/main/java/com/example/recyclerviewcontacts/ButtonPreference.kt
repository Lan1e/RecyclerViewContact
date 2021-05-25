package com.example.recyclerviewcontacts

import android.content.Context
import android.util.AttributeSet
import androidx.preference.Preference

class ButtonPreference(
    context: Context?,
    attrs: AttributeSet? = null
) : Preference(context, attrs) {
    init {
        layoutResource = R.xml.layout_button_pref
    }
}