package com.kola.mytodo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import static com.kola.mytodo.CompletedDb.COMPLETED_TASK_TABLE;

@Entity(tableName = COMPLETED_TASK_TABLE)
public class CompletedDb {

    public static final String COMPLETED_TASK_TABLE = "completedTaskTable" ;
    public static final String TASK = "task" ;
    public static final String NOTE = "note" ;
    public static final String DATE = "date" ;
    public static final String TIME = "time" ;
    public static final String TIMESTAMP = "timeStamp" ;
    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = TIMESTAMP)
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
