package com.kola.mytodo;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.database.Cursor;

/**
 * Created by Akano on 1/3/2018.
 */
@Dao
public interface TaskDao  {

    @Query("SELECT * FROM ongoingTaskTable")
    Cursor getAllFromOngoingTaskTable();

    @Insert
    void insertAllintoOngoingTaskTable(TaskDb task);

    @Query("SELECT * FROM ongoingTaskTable WHERE timeStamp LIKE :timeStamp")
    Cursor getAtTimeStampFromOngoingTaskTable(String timeStamp);




    @Query("SELECT * FROM CompletedDb")
    Cursor getAllFromCompletedTaskTable();

    @Insert
    void insertAllintoCompletedTaskTable(TaskDb task);

    @Query("SELECT * FROM CompletedDb WHERE timeStamp LIKE :timeStamp")
    Cursor getAtTimeStampFromCompletedTaskTable(String timeStamp);




    @Query("SELECT * FROM deletedTaskTable")
    Cursor getAllFromDeletedTaskTable();

    @Insert
    void insertAllintoDeletedTaskTable(TaskDb task);

    @Query("SELECT * FROM deletedTaskTable WHERE timeStamp LIKE :timeStamp")
    Cursor getAtTimeStampFromDeletedTaskTable(String timeStamp);



//    @Query("DELETE FROM ongoingTaskTable WHERE id LIKE :id")
//    public void deleteAll(String id );


//    @Delete
//    void delete(TaskDb user);
//
//    @Update
//    public void updateUsers(TaskDb... users);
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    public void insertUsers(TaskDb... users);
//
//    @Delete
//    public void deleteUsers(TaskDb... users);
//
//    @Insert
//    public void insertUsersAndFriends(TaskDb user, List<User> friends);
}
