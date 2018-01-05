package com.kola.mytodo;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import java.util.List;

/**
 * Created by Akano on 1/3/2018.
 */
@Dao
public interface TaskDao  {

//    @Query("SELECT * FROM Tasks")
//    Cursor loadAllUsers;

    @Query("SELECT * FROM Tasks")
    Cursor getAll();

    @Insert
    void insertAll(TaskDb task);

    @Query("SELECT * FROM Tasks WHERE timeStamp LIKE :timeStamp")
    Cursor getAtTimeStamp(String timeStamp);

//    @Query("SELECT * FROM Tasks WHERE id LIKE :position")
//    Cursor getAtPosition(int position);



    //    @Query("SELECT * FROM Tasks WHERE first_name LIKE :first AND "
//            + "last_name LIKE :last LIMIT 1")
//    TaskDb findByName(String first, String last);
//
//
//
//    @Insert
//    void insertAll(TaskDb... users);
//
@Query("DELETE FROM Tasks WHERE id LIKE :id")
public void deleteAll(String id );


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
