package com.kola.mytodo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import static com.kola.mytodo.CompletedDb.DATE;
import static com.kola.mytodo.CompletedDb.NOTE;
import static com.kola.mytodo.CompletedDb.TASK;
import static com.kola.mytodo.CompletedDb.TIME;
import static com.kola.mytodo.CompletedDb.TIMESTAMP;
import static com.kola.mytodo.DeletedDb.DELETED_TASK_TABLE;

@Entity(tableName = DELETED_TASK_TABLE)
public class DeletedDb{

    public static final String DELETED_TASK_TABLE = "deletedTaskTable" ;
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
