package fr.univpau.kayu.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import fr.univpau.kayu.Product;

@Database(entities = {Product.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;
    public abstract ProductDao productDao();

    public static AppDatabase getAppDatabase(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "product-database").fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }

    public static void DestroyInstance() {
        INSTANCE = null;
    }
}