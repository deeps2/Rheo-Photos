package com.example.rheophotos.repository.local;

import androidx.room.Room;

import com.example.rheophotos.base.MyApplication;

public class RheoRoomDBHelper {
    private static final String RHEO_ROOM_DB_NAME = "rheo_room_db";
    private static RheoRoomDB sInstance;

    public static synchronized RheoRoomDB getInstance() {
        if (sInstance == null) {
            sInstance = Room
                    .databaseBuilder(MyApplication.getInstance(),
                            RheoRoomDB.class,
                            RHEO_ROOM_DB_NAME)
                    .build();
        }
        return sInstance;
    }

}
