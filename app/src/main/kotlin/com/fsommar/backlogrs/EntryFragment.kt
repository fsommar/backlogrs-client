package com.fsommar.backlogrs

import android.app.Fragment
import com.fsommar.backlogrs.dummy.DummyContent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.TextView
import com.fsommar.backlogrs.model.Library
import com.fsommar.backlogrs.model.Entry
import android.app.DialogFragment
import android.content.Intent
import android.app.Activity
import android.widget.Toast

/**
 * A fragment representing a single Entry detail screen.
 * This fragment is either contained in a {@link EntryListActivity}
 * in two-pane mode (on tablets) or a {@link EntryDetailActivity}
 * on handsets.
 */
public class EntryFragment
/**
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */
: Fragment() {

    /**
     * The dummy content this fragment is presenting.
     */
    private var mEntry: Entry? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mEntry = Library.entries.get(getArguments().getInt(ARG_ITEM_ID))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_entry, container, false)

        populateFromEntry(view)

        view.findViewById(R.id.fab_edit).setOnClickListener {
            val editDialog = EditDialogFragment.newInstance(mEntry)
            editDialog.setTargetFragment(this, Library.EDIT_DIALOG)
            showDialog(editDialog, "edit_dialog")
        }

        return view
    }

    private fun populateFromEntry(view: View) {
        (view.findViewById(R.id.game_title) as TextView)
                .setText(mEntry?.game?.name)
        (view.findViewById(R.id.game_description) as TextView)
                .setText(mEntry?.game?.description)
        (view.findViewById(R.id.entry_status) as TextView)
                .setText(mEntry?.status.toString())
        (view.findViewById(R.id.entry_time_played) as TextView)
                .setText(mEntry?.time_played.toString() + " hrs")
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == Library.EDIT_DIALOG && resultCode == Activity.RESULT_OK) {
            val entry = data.getSerializableExtra("entry") as Entry
            mEntry = entry
            populateFromEntry(getView())
        }
    }

    class object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        public val ARG_ITEM_ID: String = "item_id"
    }
}