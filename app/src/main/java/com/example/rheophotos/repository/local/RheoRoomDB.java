package com.example.rheophotos.repository.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {SearchTable.class}, version = 1)
public abstract class RheoRoomDB extends RoomDatabase {
    public abstract SearchDao searchDao();
}
