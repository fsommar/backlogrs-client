package com.fsommar.backlogrs

import android.content.Context
import com.fsommar.backlogrs.model.Entry
import android.widget.ArrayAdapter
import android.view.ViewGroup
import android.view.View
import android.view.LayoutInflater
import android.widget.TextView
import com.fsommar.backlogrs.model.Library
import android.util.Log

/**
 * Created by Fredrik Sommar on 2015-02-27.
 */
public class EntryArrayAdapter(context: Context)
    : ArrayAdapter<Entry>(context, R.layout.library_list_item, Library.entries) {

    private fun getCustomView(pos: Int, parent: ViewGroup?): View? {
        val inflater = getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater;
        val rowView = inflater.inflate(R.layout.library_list_item, parent, false);

        val entry = Library.entries.get(pos)
        (rowView.findViewById(R.id.list_game_title) as TextView)
                .setText(entry.game?.name);
        (rowView.findViewById(R.id.list_entry_status) as TextView)
                .setText("(" + entry.status + ")");
        (rowView.findViewById(R.id.list_entry_time_played) as TextView)
                .setText(entry.time_played.toString() + " hrs");

        return rowView;
    }

    override fun getView(pos: Int, convertView: View?, parent: ViewGroup?): View? {
        return getCustomView(pos, parent)
    }
}