package com.example.poch5;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SupportFactory;


/**
 * @author ex-zhusiyu003
 */
@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(String password) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    byte[] passphrase = SQLiteDatabase.getBytes(password.toCharArray());
                    SupportFactory factory = new SupportFactory(passphrase);
                    INSTANCE = Room.databaseBuilder(AppUtil.mContext, AppDatabase.class, "database-name")
                            .openHelperFactory(factory)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
