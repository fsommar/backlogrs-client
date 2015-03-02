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
import com.fsommar.backlogrs.model.Game
import com.fsommar.backlogrs.model.Status

/**
 * Created by Fredrik Sommar on 2015-03-01.
 */
public class StatusArrayAdapter(context: Context)
    : ArrayAdapter<Status>(context, android.R.layout.simple_list_item_1, Status.values()) {

    private fun getCustomView(pos: Int, parent: ViewGroup?): View? {
        val inflater = getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater;
        val rowView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);

        val status = this.getItem(pos)
        (rowView.findViewById(android.R.id.text1) as TextView)
                .setText(status.toString());

        return rowView;
    }

    override fun getView(pos: Int, convertView: View?, parent: ViewGroup?): View? {
        return getCustomView(pos, parent)
    }

    override fun getDropDownView(pos: Int, convertView: View?, parent: ViewGroup?): View? {
        return getCustomView(pos, parent)
    }
}