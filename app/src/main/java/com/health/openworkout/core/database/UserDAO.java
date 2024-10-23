package com.health.openworkout.core.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.health.openworkout.core.datatypes.User;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert
    long insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM User WHERE userId=:userId")
    User getSome(long userId);

    @Query("SELECT * FROM User")
    List<User> getAll();
}
