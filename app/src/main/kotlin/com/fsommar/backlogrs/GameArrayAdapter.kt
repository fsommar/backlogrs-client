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

/**
 * Created by Fredrik Sommar on 2015-02-27.
 */
public class GameArrayAdapter(context: Context)
    : ArrayAdapter<Game>(context, android.R.layout.simple_list_item_1, Library.games) {

    private fun getCustomView(pos: Int, parent: ViewGroup?): View? {
        val inflater = getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater;
        val rowView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);

        val game = Library.games.get(pos)
        (rowView.findViewById(android.R.id.text1) as TextView)
                .setText(game.name);

        return rowView;
    }

    override fun getView(pos: Int, convertView: View?, parent: ViewGroup?): View? {
        return getCustomView(pos, parent)
    }

    override fun getDropDownView(pos: Int, convertView: View?, parent: ViewGroup?): View? {
        return getCustomView(pos, parent)
    }
}