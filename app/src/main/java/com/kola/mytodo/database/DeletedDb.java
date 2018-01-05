package com.kola.mytodo.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import static com.kola.mytodo.other.Constants.DATE;
import static com.kola.mytodo.other.Constants.DELETED_TASK_TABLE;
import static com.kola.mytodo.other.Constants.NOTE;
import static com.kola.mytodo.other.Constants.TASK;
import static com.kola.mytodo.other.Constants.TIME;
import static com.kola.mytodo.other.Constants.TIMESTAMP;

@Entity(tableName = DELETED_TASK_TABLE)
public class DeletedDb{

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
