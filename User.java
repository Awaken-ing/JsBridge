package com.example.poch5;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "user_name")
    public String userName;

    @ColumnInfo(name = "age")
    public int age;

    @ColumnInfo(name = "gender")
    public String gender;

    @ColumnInfo(name = "income")
    public double income;

    @ColumnInfo(name = "height")
    public double height;
}
