package com.example.recyclerviewcontacts

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditFragment : PreferenceFragmentCompat() {
    private val entityId get() = activity?.intent?.getIntExtra("id", -1) ?: -1
    private val readOnly get() = activity?.intent?.getBooleanExtra("readOnly", false) == true
    private val dao by lazy { ContactDatabase(requireContext()).getDao() }
    private val entities by lazy { dao.read(entityId) }
    private val entity by lazy { if (entities.isNullOrEmpty()) Entity() else entities.first() }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_fragment_edit)
        findPreference<PreferenceCategory>("pref_info")?.apply {
            addPreference(createPref("pref_name", "名字"))
            addPreference(
                createPref("pref_phone", "電話", android.R.drawable.stat_sys_vp_phone_call) {
                    Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel: ${entity.phone}")
                    }.let {
                        startActivity(it)
                    }
                })
            addPreference(createPref("pref_email", "電子郵件", android.R.drawable.sym_action_email) {
                Intent(Intent.ACTION_SEND).apply {
                    data = Uri.parse("mailto: ")
                    type = "text/plain"
                    putExtra(Intent.EXTRA_EMAIL, entity.email)
                    putExtra(Intent.EXTRA_SUBJECT, "寫信給教授")
                    putExtra(Intent.EXTRA_TEXT, "老師好，")
                }.let {
                    startActivity(Intent.createChooser(it, "Send Email by using: "))
                }
            })
            addPreference(createPref("pref_office_number", "辦公室地點編號"))
            addPreference(createPref("pref_major", "分屬領域"))
            addPreference(createPref("pref_take_courses", "多筆修課名稱"))
            addPreference(createPref("pref_memo", "備註"))
        }
        if (entityId != -1) {
            if (!entities.isNullOrEmpty()) {
                entities.first().let {
                    findPreference<Preference>("pref_name")?.summary = it.name
                    findPreference<Preference>("pref_phone")?.summary = it.phone
                    findPreference<Preference>("pref_email")?.summary = it.email
                    findPreference<Preference>("pref_office_number")?.summary = it.officeNumber
                    findPreference<Preference>("pref_major")?.summary = it.major
                    findPreference<Preference>("pref_take_courses")?.summary = it.takeCourses
                    findPreference<Preference>("pref_memo")?.summary = it.memo
                }
            }
        }

        findPreference<Preference>("pref_confirm")?.let {
            it.setOnPreferenceClickListener {
                context?.let {
                    GlobalScope.launch {
                        withContext(Dispatchers.IO) {
                            if (findPreference<Preference>("pref_name")?.summary.toString() == Entity.DEFAULT_VAL
                                && findPreference<Preference>("pref_major")?.summary.toString() == Entity.DEFAULT_VAL
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

    private fun createPref(
        key: String,
        title: String,
        imageId: Int? = null,
        listener: ((View) -> Unit)? = null
    ): Preference =
        (if (readOnly) MyPreference(context) else MyEditText(context)).apply {
            this.key = key
            this.title = title
            if (readOnly) {
                this.listener = listener
                imageId?.let { this.imageId = it }
            }
            isPersistent = false
            isIconSpaceReserved = false
        }
}