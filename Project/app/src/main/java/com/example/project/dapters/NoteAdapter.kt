package com.example.project.dapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.project.R
import com.example.project.models.NoteKey

class NoteAdapter(context: Context, private val notes: List<NoteKey>) : BaseAdapter() {

    override fun getCount(): Int {
        return notes.size
    }

    override fun getItem(position: Int): Any {
        return notes[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView: View? = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(parent?.context).inflate(R.layout.note_item, parent, false)
        }

        val note = notes[position]
        val title = convertView!!.findViewById<TextView>(R.id.nr_title)
        val detail = convertView.findViewById<TextView>(R.id.nr_detail)
        val date = convertView.findViewById<TextView>(R.id.nr_date)

        title.text = note.value?.tittle
        detail.text = note.value?.detail
        date.text = note.value?.date.toString()

        return convertView
    }

}