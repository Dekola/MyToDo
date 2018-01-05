package com.kola.mytodo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import static com.kola.mytodo.TaskDb.DATABASE;

/**
 * Created by Akano on 1/3/2018.
 */


@Entity (tableName = DATABASE)
public class TaskDb {
    public static final String DATABASE = "Tasks" ;
    public static final String TASK = "task" ;
    public static final String NOTE = "note" ;
    public static final String DATE = "date" ;
    public static final String TIME = "time" ;
    public static final String TIMESTAMP = "timeStamp" ;


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

