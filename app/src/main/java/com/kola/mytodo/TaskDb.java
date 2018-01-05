package com.kola.mytodo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import static com.kola.mytodo.CompletedDb.DATE;
import static com.kola.mytodo.CompletedDb.NOTE;
import static com.kola.mytodo.CompletedDb.TASK;
import static com.kola.mytodo.CompletedDb.TIME;
import static com.kola.mytodo.CompletedDb.TIMESTAMP;

import static com.kola.mytodo.TaskDb.ONGOING_TASK_TABLE;

/**
 * Created by Akano on 1/3/2018.
 */

@Entity (tableName = ONGOING_TASK_TABLE)
public class TaskDb {
    public static final String ONGOING_TASK_TABLE = "ongoingTaskTable" ;

    @PrimaryKey (autoGenerate = true)
    int id;

    @ColumnInfo (name = TIMESTAMP)
    public String timeStamp;

    @ColumnInfo (name = TASK)
    public String task;

    @ColumnInfo (name = NOTE)
    public String note;

    @ColumnInfo (name = DATE)
    public String date;

    @ColumnInfo (name = TIME)
    public String time;
}

