package com.example.recyclerviewcontacts

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.contact_row.view.*
import kotlinx.coroutines.*
import java.util.concurrent.CopyOnWriteArrayList


class MainActivity : AppCompatActivity() {
    private val ioScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val dao get() = ContactDatabase(this).getDao()
    private val contacts = arrayListOf(
        Contact("柯健全", "影像處理 圖形識別 電腦圖學", -1),
        Contact("章定遠", "視訊處理 資料壓縮與傳輸 立體視訊處理", -1)
//        Contact("陳宗和", "05-2717723", "thchen@mail.ncyu.edu.tw"),
//        Contact("陳耀輝", "05-2717737", "ychen@mail.ncyu.edu.tw"),
//        Contact("賴泳伶", "05-2717735", "yllai@mail.ncyu.edu.tw"),
//        Contact("林楚迪", "05-2717227", "chutilin@mail.ncyu.edu.tw"),
//        Contact("郭煌政", "05-2717731", "hckuo@mail.ncyu.edu.tw"),
//        Contact("洪燕竹", "05-2717728", "andrew@mail.ncyu.edu.tw"),
//        Contact("葉瑞峰", "05-2717709", "ralph@mail.ncyu.edu.tw"),
//        Contact("盧天麒", "05-2717730", "tclu@mail.ncyu.edu.tw"),
//        Contact("邱志義", "05-2717228", "cychiu@mail.ncyu.edu.tw"),
//        Contact("方文杰", "05-2717739", "ncyu.deep@mail.ncyu.edu.tw"),
//        Contact("許政穆", "05-2717742", "hsujm@mail.ncyu.edu.tw"),
//        Contact("王智弘", "05-2717736", "wangch@mail.ncyu.edu.tw"),
//        Contact("李龍盛", "05-2717733", "sheng@mail.ncyu.edu.tw"),
//        Contact("王皓立", "05-2717724", "haoli@mail.ncyu.edu.tw")
    ).let {
        CopyOnWriteArrayList(it)
    }

    private val adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            layoutInflater.inflate(R.layout.contact_row, parent, false).let {
                object : RecyclerView.ViewHolder(it) {}
            }

        override fun getItemCount() = contacts.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            holder.itemView.apply {
                contact_name.text = contacts[position].name
                contact_major.text = contacts[position].major
                setOnClickListener {
                    showPopup(this, contacts[position].id)
                }
            }
        }
    }

    private fun initDatabase(contacts: List<Contact>) {
        contacts.apply {
            this@MainActivity.getSharedPreferences("minlee", Context.MODE_PRIVATE).run {
                if (getBoolean("db_initialized", false)) {
                    Log.e("Lanie", "return")
                    return@run
                }
                edit().putBoolean("db_initialized", true).apply()
                this@apply.forEach {
                    dao.insert(
                        Entity(
                            name = it.name,
                            major = it.major
                        )
                    )
                    Log.e("Lanie", "inserted")
                }
            }
        }
    }

    private fun updateView(contacts: CopyOnWriteArrayList<Contact>) {
        contacts.clear()
        contacts.addAll(dao.getAll().map {
            Contact(it.name, it.major, it.id)
        })
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            startActivity(Intent(this, EditActivity::class.java))
        }

        ioScope.launch {
            initDatabase(contacts)
        }

        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        ioScope.launch {
            updateView(contacts)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        MenuCompat.setGroupDividerEnabled(menu, true)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_contacts -> {
            }
            R.id.action_help -> {
                Toast.makeText(this, "關於選單被點選", Toast.LENGTH_LONG).show()
                AlertDialog.Builder(this)
                    .setMessage("資工系老師聯絡資訊\n1.0.0\nDesigned by goodhelper.tw")
                    .setTitle("RecyclerViewContacts")
                    .setPositiveButton("確定", null)
                    .show()
            }
            R.id.action_contacts_add -> {
                Toast.makeText(this, "聯絡人_新增聯絡人_選單被點選", Toast.LENGTH_LONG).show()
            }
            R.id.action_contacts_delete -> {
                Toast.makeText(this, "聯絡人_刪除聯絡人_選單被點選", Toast.LENGTH_LONG).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showPopup(view: View, id: Int) {
        val popup = PopupMenu(this, view)
        popup.inflate(R.menu.popup_menu)
        MenuCompat.setGroupDividerEnabled(popup.menu, true)
        popup.setOnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.data_update -> {
                    val intent = Intent(this, EditActivity::class.java).putExtra("id", id)
                    startActivity(intent)
                }
                R.id.data_delete -> {
                    ioScope.launch {
                        dao.delete(Entity().apply { this.id = id })
                        contacts.clear()
                        contacts.addAll(dao.getAll().map {
                            Contact(it.name, it.major, it.id)
                        })

                        withContext(Dispatchers.Main) {
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
                R.id.data_show -> {
                    val intent = Intent(this, EditActivity::class.java)
                        .putExtra("id", id)
                        .putExtra("readOnly", true)
                    startActivity(intent)
                }
            }
            true
        }
        popup.show()
    }
}