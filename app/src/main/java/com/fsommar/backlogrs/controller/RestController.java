package com.fsommar.backlogrs.controller;

import com.fsommar.backlogrs.model.Entry;
import com.fsommar.backlogrs.model.Game;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Fredrik Sommar on 2015-02-27.
 */
public interface RestController {
    @GET("/user/{user_id}/library")
    public Observable<List<Entry>> getLibrary(@Path("user_id") int id);

    @GET("/user/{user_id}/library")
    public Observable<List<Entry>> getLibrary(@Path("user_id") int id,
                                              @Query("status") String status);

    @GET("/user/{user_id}/library/{entry_id}")
    public Observable<Entry> getEntry(
            @Path("user_id") int userId,
            @Path("entry_id") int entryId);

    @POST("/user/{user_id}/library")
    public Observable<Entry> updateEntry(@Path("user_id") int id,
                                         @Body Entry entry);

    @GET("/game")
    public Observable<List<Game>> getGames();

    @GET("/game/{game_id}")
    public Observable<Game> getGame(@Path("game_id") int id);
}
