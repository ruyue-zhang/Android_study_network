package com.ruyue.network;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PersonDao {

    @Query("SELECT * FROM person")
    List<Person> getPerson();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPerson(Person person);
}
