package com.fsommar.backlogrs.model

import java.util.ArrayList
import com.fsommar.backlogrs.controller.RestController
import com.google.gson.GsonBuilder
import retrofit.RestAdapter
import retrofit.converter.GsonConverter
import org.w3c.dom.UserDataHandler
import kotlin.properties.Delegates
import com.fsommar.backlogrs.EntryArrayAdapter

/**
 * Created by Fredrik Sommar on 2015-02-28.
 */
public class Library private () {

    private var entries: MutableList<Entry> = ArrayList()
    private var games: MutableList<Game> = ArrayList()
    private var user: User = User(1, "Fred");
    private var entryAdapter: EntryArrayAdapter? = null
    private val service: RestController by Delegates.lazy {
        val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        val restAdapter = RestAdapter.Builder()
                .setEndpoint("http://fsommar.com:3000/api")
                .setConverter(GsonConverter(gson))
                .build()

        restAdapter.create(javaClass<RestController>())
    }

    class object {
        public val CREATE_DIALOG: Int = 1
        public val EDIT_DIALOG: Int = 2

        public val entries: MutableList<Entry>
        get() = this.getInstance().entries

        public val games: MutableList<Game>
        get() = this.getInstance().games

        public val user: User
        get() = this.getInstance().user

        public val service: RestController
        get() = this.getInstance().service

        public var entryAdapter: EntryArrayAdapter?
        get() = this.getInstance().entryAdapter
        set(x) { this.getInstance().entryAdapter = x }

        private val _instance: Library by Delegates.lazy {
            Library()
        }
        public fun getInstance(): Library {
            return _instance
        }

    }
}