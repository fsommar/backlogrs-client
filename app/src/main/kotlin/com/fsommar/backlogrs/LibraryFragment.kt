package com.fsommar.backlogrs

import android.app.ListFragment
import com.fsommar.backlogrs.model.Entry
import rx.Observable
import android.os.Bundle
import com.fsommar.backlogrs.controller.RestController
import android.view.View
import android.app.Activity
import android.widget.ListView
import android.widget.AdapterView
import com.google.gson.GsonBuilder
import retrofit.converter.GsonConverter
import retrofit.RestAdapter
import java.util.ArrayList
import android.os.Looper
import android.os.Handler
import android.util.Log
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import com.fsommar.backlogrs.dummy.DummyContent
import android.widget.AbsListView
import com.fsommar.backlogrs.model.Library
import android.app.DialogFragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.content.Intent
import android.widget.Toast

/**
 * A list fragment representing a list of Entries. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link EntryDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class LibraryFragment
/**
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */
: ListFragment() {

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private var mCallbacks: Callbacks = NoopCallback

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public trait Callbacks {
        public fun onItemSelected(id: Int)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Library.entryAdapter = EntryArrayAdapter(getActivity())
        setListAdapter(Library.entryAdapter)

        if (Library.entries.isEmpty()) {
            Library.service.getLibrary(Library.user.id!!)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap { Observable.from(it) }
                    .map { entry ->
                        // Using blocking operations now because of either a misunderstanding
                        // on my part or a bug in the implementation (?)
                        // When not using a blocking operation all game fetches aren't collected
                        Library.service.getGame(entry.game_id!!).map {
                            entry.copy(game = it)
                        }.toBlocking().first()
                    }
                    .subscribe({ Library.entryAdapter!!.add(it) }, {
                        // On error
                        Log.e("TAG", it.toString())
                    })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == CREATE_DIALOG && resultCode == Activity.RESULT_OK) {
                val entry = data.getSerializableExtra("entry") as Entry
                Library.entryAdapter?.add(entry)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_library, container, false)

        view.findViewById(R.id.fab_create).setOnClickListener {
            val createDialog = CreateDialogFragment.newInstance()
            createDialog.setTargetFragment(this, CREATE_DIALOG)
            showDialog(createDialog, "create_dialog")
        }

        return view
    }

    private fun showDialog(dialog: DialogFragment, tag: String) {
        val manager = getFragmentManager()
        val ft = manager.beginTransaction();
        val prev = manager.findFragmentByTag(tag);
        if (prev != null) {
            ft.remove(prev);
        }

        // Create and show the dialog.
        dialog.show(ft, tag);
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)

        // Activities containing this fragment must implement its callbacks.
        if (activity !is Callbacks) {
            throw IllegalStateException("Activity must implement fragment's callbacks.")
        }
        // The type of activity is inferred to be Callbacks here
        mCallbacks = activity
    }

    override fun onDetach() {
        super.onDetach()
        mCallbacks = NoopCallback
    }

    override fun onListItemClick(listView: ListView, view: View, position: Int, id: Long) {
        super.onListItemClick(listView, view, position, id)

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mCallbacks.onItemSelected(position)
    }

    class object {
        public val CREATE_DIALOG: Int = 1
        /**
         * A noop implementation of the {@link Callbacks} interface that does
         * nothing. Used only when this fragment is not attached to an activity.
         */
        private val NoopCallback = object : Callbacks {
            override fun onItemSelected(id: Int) {
            }
        }
    }
}