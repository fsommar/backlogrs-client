package com.fsommar.backlogrs

import android.app.DialogFragment
import android.os.Bundle
import android.app.Dialog
import android.app.AlertDialog
import android.content.DialogInterface
import android.widget.Spinner
import com.fsommar.backlogrs.model.Library
import android.widget.ArrayAdapter
import android.widget.Toast
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

/**
 * Created by Fredrik Sommar on 2015-02-28.
 */
public class CreateDialogFragment() : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(getActivity())

        // Get the layout inflater
        val inflater = getActivity().getLayoutInflater()
        // Inflate the layout
        val view = inflater.inflate(R.layout.dialog_create_entry, null)

        // Populate fields with games etc
        val games = view.findViewById(R.id.games) as Spinner
        val adapter = GameArrayAdapter(getActivity())
        games.setAdapter(adapter)

        builder.setTitle("Choose game for new entry")
        builder.setView(view)
                .setPositiveButton(android.R.string.ok) {
                    (dialog: DialogInterface, id: Int) ->
                    Library.service
                            .updateEntry(Library.user.id!!,
                                    Entry(game_id = (games.getSelectedItem() as Game?)?.id))
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .map { entry ->
                                Library.service.getGame(entry.game_id!!).map {
                                    entry.copy(game = it)
                                }.toBlocking().first()
                            }
                            .subscribe({
                                Library.games.remove(it.game!!)
                                Log.d("TAG", it.toString())
                                val intent = Intent().putExtra("entry", it)
                                getTargetFragment().onActivityResult(
                                        getTargetRequestCode(), Activity.RESULT_OK, intent)
                            }, {
                                // On error
                                Log.e("TAG", "Error while creating; " + it.toString())
                            })
                }
                .setNegativeButton(android.R.string.cancel) {
                    (dialog: DialogInterface, id: Int) ->
                    dialog.cancel();
                }

        val dialog = builder.create()

        if (Library.games.isEmpty()) {
            Library.service.getGames().subscribe { (games: List<Game>) ->
                val gameIds = Library.entries.map { it.game_id }
                // Filter on already available games
                val filteredGames = games.filter { !gameIds.contains(it.id) }
                adapter.addAll(filteredGames)
            }
        }

        return dialog
    }

    class object {
        public fun newInstance(): CreateDialogFragment {
            return CreateDialogFragment()
        }
    }

}