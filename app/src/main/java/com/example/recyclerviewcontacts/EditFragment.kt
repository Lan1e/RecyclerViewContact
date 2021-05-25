package com.example.recyclerviewcontacts

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditFragment : PreferenceFragmentCompat() {
    val entityId get() = activity?.intent?.getIntExtra("id", -1) ?: -1
    val readOnly get() = activity?.intent?.getBooleanExtra("readOnly", false) == true
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.owo)
        findPreference<PreferenceCategory>("pref_info")?.apply {
            addPreference(createPref("pref_name", "名字"))
            addPreference(createPref("pref_phone", "電話").apply {

            })
            addPreference(createPref("pref_email", "電子郵件"))
            addPreference(createPref("pref_office_number", "辦公室地點編號"))
            addPreference(createPref("pref_major", "分屬領域"))
            addPreference(createPref("pref_take_courses", "多筆修課名稱"))
            addPreference(createPref("pref_memo", "備註"))
        }
        if (entityId != -1) {
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    context?.let {
                        val dao = ContactDatabase(it).getDao()
                        val entities = dao.read(entityId)
                        if (!entities.isNullOrEmpty()) {
                            entities.first().let {
                                withContext(Dispatchers.Main) {
                                    findPreference<Preference>("pref_name")?.summary = it.name
                                    findPreference<Preference>("pref_phone")?.summary = it.phone
                                    findPreference<Preference>("pref_email")?.summary = it.email
                                    findPreference<Preference>("pref_office_number")?.summary =
                                        it.officeNumber
                                    findPreference<Preference>("pref_major")?.summary = it.major
                                    findPreference<Preference>("pref_take_courses")?.summary =
                                        it.takeCourses
                                    findPreference<Preference>("pref_memo")?.summary = it.memo
                                }
                            }
                        }
                    }
                }
            }
        }

        findPreference<Preference>("pref_confirm")?.let {
            it.setOnPreferenceClickListener {
                context?.let {
                    GlobalScope.launch {
                        withContext(Dispatchers.IO) {
                            if (findPreference<Preference>("pref_name")?.summary.toString() == "" && findPreference<Preference>(
                                    "pref_major"
                                )?.summary.toString() == ""
                            ) {
                                return@withContext
                            }
                            Entity(
                                findPreference<Preference>("pref_name")?.summary.toString(),
                                findPreference<Preference>("pref_phone")?.summary.toString(),
                                findPreference<Preference>("pref_email")?.summary.toString(),
                                findPreference<Preference>("pref_office_number")?.summary.toString(),
                                findPreference<Preference>("pref_major")?.summary.toString(),
                                findPreference<Preference>("pref_take_courses")?.summary.toString(),
                                findPreference<Preference>("pref_memo")?.summary.toString()
                            ).let { entity ->
                                val dao = ContactDatabase(it).getDao()
                                if (entityId == -1) {
                                    dao.insert(entity)
                                } else {
                                    dao.update(entity.apply { id = entityId })
                                }
                            }
                        }
                    }
                }
                activity?.finish()
                true
            }
        }
    }

    private fun createPref(key: String, title: String): Imagable =
        (if (readOnly) Preference(context) else MyEditText(context)).apply {
            this.key = key
            this.title = title
            isPersistent = false
            isIconSpaceReserved = false
        }
}