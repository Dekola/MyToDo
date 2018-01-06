package com.kola.mytodo.database;

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
    void insertAllintoOngoingTaskTable(OngoingTaskDb task);

    @Query("SELECT * FROM ongoingTaskTable WHERE timeStamp LIKE :timeStamp")
    Cursor getAtTimeStampFromOngoingTaskTable(String timeStamp);

    @Query("DELETE FROM ongoingTaskTable WHERE timeStamp LIKE :timeStamp")
    public void deleteFromOngoingTaskTable(String timeStamp );



    @Query("SELECT * FROM completedTaskTable")
    Cursor getAllFromCompletedTaskTable();

    @Insert
    void insertAllintoCompletedTaskTable(CompletedTaskDb task);

    @Query("SELECT * FROM completedTaskTable WHERE timeStamp LIKE :timeStamp")
    Cursor getAtTimeStampFromCompletedTaskTable(String timeStamp);

    @Query("DELETE FROM completedTaskTable WHERE timeStamp LIKE :timeStamp")
    public void deleteFromcompletedTaskTable(String timeStamp );



    @Query("SELECT * FROM deletedTaskTable")
    Cursor getAllFromDeletedTaskTable();

    @Insert
    void insertAllintoDeletedTaskTable(DeletedTaskDb task);

    @Query("SELECT * FROM deletedTaskTable WHERE timeStamp LIKE :timeStamp")
    Cursor getAtTimeStampFromDeletedTaskTable(String timeStamp);

    @Query("DELETE FROM deletedTaskTable WHERE timeStamp LIKE :timeStamp")
    public void deleteFromdeletedTaskTable(String timeStamp );





//    @Delete
//    void delete(OngoingTaskDb user);
//
//    @Update
//    public void updateUsers(OngoingTaskDb... users);
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    public void insertUsers(OngoingTaskDb... users);
//
//    @Delete
//    public void deleteUsers(OngoingTaskDb... users);
//
//    @Insert
//    public void insertUsersAndFriends(OngoingTaskDb user, List<User> friends);
}
