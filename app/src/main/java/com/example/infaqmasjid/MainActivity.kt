package com.example.infaqmasjid

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private var listInfaq = ArrayList<Infaq>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            var intent = Intent(this, InfaqActivity::class.java)
            startActivity(intent)
        }

        loadData()

    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        var dbAdapter = DBAdapter(this)
        var cursor = dbAdapter.allQuery()

        listInfaq.clear()
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("Id"))
                val nama = cursor.getString(cursor.getColumnIndex("Nama"))
                val jumlah = cursor.getString(cursor.getColumnIndex("Jumlah"))
                val alamat = cursor.getString(cursor.getColumnIndex("Alamat"))

                listInfaq.add(Infaq(id, nama, jumlah, alamat))
            } while (cursor.moveToNext())
        }

        var infaqAdapter = InfaqAdapter(this, listInfaq)
        lvInfaq.adapter = infaqAdapter
    }

    inner class InfaqAdapter: BaseAdapter {

        private var infaqList = ArrayList<Infaq>()
        private var context: Context? = null

        constructor(context: Context, infaqList: ArrayList<Infaq>) : super() {
            this.infaqList = infaqList
            this.context = context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val view: View?
            val vh: ViewHolder

            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.infaq, parent, false)
                vh = ViewHolder(view)
                view.tag = vh
                Log.i("db", "set tag for ViewHolder, position: " + position)
            } else {
                view = convertView
                vh = view.tag as ViewHolder
            }

            var mInfaq = infaqList[position]

            vh.tvNama.text = mInfaq.name
            vh.tvJumlah.text = "Rp." + mInfaq.jumlah
            vh.tvAlamat.text = mInfaq.alamat

            lvInfaq.onItemClickListener = AdapterView.OnItemClickListener {adapterView, view, position, id ->
                updateInfaq(mInfaq)
            }

            return view

        }

        override fun getItem(position: Int): Any {
            return infaqList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return infaqList.size
        }

    }

    private fun updateInfaq(infaq: Infaq) {
        var intent = Intent(this, InfaqActivity::class.java)
        intent.putExtra("MainActId", infaq.id)
        intent.putExtra("MainActNama", infaq.name)
        intent.putExtra("MainActJumlah", infaq.jumlah)
        intent.putExtra("MainActAlamat", infaq.alamat)
        startActivity(intent)
    }

    private class ViewHolder(view: View?) {
        val tvNama: TextView
        val tvJumlah: TextView
        val tvAlamat: TextView

        init {
            this.tvNama = view?.findViewById(R.id.tvNama) as TextView
            this.tvJumlah = view?.findViewById(R.id.tvJumlah) as TextView
            this.tvAlamat = view?.findViewById(R.id.tvAlamat) as TextView
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}