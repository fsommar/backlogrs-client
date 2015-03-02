package com.fsommar.backlogrs

import android.app.Activity
import android.os.Bundle
import android.content.Intent

/**
 * An activity representing a list of Entries. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link EntryDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link EntryListFragment} and the item details
 * (if present) is a {@link EntryDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link EntryListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class LibraryActivity : Activity(), LibraryFragment.Callbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super<Activity>.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)

        val fragment = LibraryFragment()
        getFragmentManager().beginTransaction().add(R.id.library_container, fragment).commit()
    }

    /**
     * Callback method from {@link EntryListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    override fun onItemSelected(id: Int) {
        // In single-pane mode, simply start the detail activity
        // for the selected item ID.
        val detailIntent = Intent(this, javaClass<EntryActivity>())
        detailIntent.putExtra(EntryFragment.ARG_ITEM_ID, id)
        startActivity(detailIntent)
    }
}