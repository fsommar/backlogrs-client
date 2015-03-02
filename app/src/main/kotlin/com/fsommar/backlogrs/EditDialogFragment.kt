package com.fsommar.backlogrs

import android.app.DialogFragment
import android.os.Bundle
import android.app.Dialog
import android.app.AlertDialog
import android.content.DialogInterface
import android.widget.Spinner
import com.fsommar.backlogrs.model.Library
import android.widget.ArrayAdapter
import com.fsommar.backlogrs.model.Game
import com.fsommar.backlogrs.model.Entry
import android.util.Log
import rx.schedulers.Schedulers
import rx.android.schedulers.AndroidSchedulers
import java.io.Serializable
import android.app.Activity
import retrofit.RetrofitError
import retrofit.mime.TypedByteArray
import android.content.Intent
import com.fsommar.backlogrs.model.Status
import android.widget.EditText
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.Toast

/**
 * Created by Fredrik Sommar on 2015-03-01.
 */
public class EditDialogFragment(val entry: Entry? = null) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(getActivity())

        // Get the layout inflater
        val inflater = getActivity().getLayoutInflater()
        // Inflate the layout
        val view = inflater.inflate(R.layout.dialog_edit_entry, null)

        // Populate fields with games etc
        val statuses = view.findViewById(R.id.statuses) as Spinner
        val statusAdapter = StatusArrayAdapter(getActivity())
        statuses.setAdapter(statusAdapter)
        val timePlayed = view.findViewById(R.id.time_played) as EditText

        if (entry == null) {
            // This shouldn't happen
            return builder.create()
        }

        statuses.setSelection(entry.status?.ordinal() ?: 0)
        timePlayed.setText(entry.time_played?.toString())

        builder.setTitle("Edit entry for ${entry.game?.name}")
        builder.setView(view)
                .setPositiveButton(android.R.string.ok) {
                    (dialog: DialogInterface, id: Int) ->
                    val newEntry = entry.copy(
                            time_played = timePlayed.getText().toString().toFloat(),
                            status = statuses.getSelectedItem() as Status)

                    Library.service
                            .updateEntry(Library.user.id!!, newEntry)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                val iter = Library.entries.listIterator()
                                while (iter.hasNext()) {
                                    val e = iter.next()
                                    if (e == entry) {
                                        iter.set(newEntry)
                                        break
                                    }
                                }
                                Library.entryAdapter?.notifyDataSetChanged()
                                val intent = Intent().putExtra("entry", it)
                                getTargetFragment().onActivityResult(
                                        getTargetRequestCode(), Activity.RESULT_OK, intent)
                            }, {
                                // On Error
                                Log.e("TAG", it.toString())
                            })
                }
                .setNegativeButton(android.R.string.cancel) {
                    (dialog: DialogInterface, id: Int) ->
                    dialog.cancel();
                }

        val dialog = builder.create()

        return dialog
    }

    class object {
        public fun newInstance(entry: Entry? = null): EditDialogFragment {
            return EditDialogFragment(entry)
        }
    }

}