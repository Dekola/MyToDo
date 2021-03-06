package com.kola.mytodo.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by Akano on 1/3/2018.
 */

@Database (entities = {OngoingTaskDb.class, CompletedTaskDb.class, DeletedTaskDb.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase{

    public abstract TaskDao taskDao();

}
