package com.fsommar.backlogrs

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import android.content.Intent

/**
 * An activity representing a single Entry detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link EntryListActivity}.
 * <p/>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link EntryDetailFragment}.
 */
public class EntryActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry)

        // Show the Up button in the action bar.
        getActionBar()!!.setDisplayHomeAsUpEnabled(true)

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            val arguments = Bundle()
            if (getIntent().hasExtra(EntryFragment.ARG_ITEM_ID)) {
                arguments.putInt(EntryFragment.ARG_ITEM_ID,
                        getIntent().getIntExtra(EntryFragment.ARG_ITEM_ID, 0))
            }
            val fragment = EntryFragment()
            fragment.setArguments(arguments)
            getFragmentManager().beginTransaction().add(R.id.entry_container, fragment).commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(Intent(this, javaClass<LibraryActivity>()))
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}