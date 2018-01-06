package com.kola.mytodo.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import static com.kola.mytodo.other.Constants.DATE;
import static com.kola.mytodo.other.Constants.ONGOING_TASK_TABLE;
import static com.kola.mytodo.other.Constants.NOTE;
import static com.kola.mytodo.other.Constants.TASK;
import static com.kola.mytodo.other.Constants.TIME;
import static com.kola.mytodo.other.Constants.TIMESTAMP;
/**
 * Created by Akano on 1/3/2018.
 */

@Entity (tableName = ONGOING_TASK_TABLE)
public class OngoingTaskDb {

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

