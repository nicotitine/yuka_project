package fr.univpau.kayu.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import fr.univpau.kayu.models.Product;

@Database(entities = {Product.class}, version = 9, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;
    public abstract ProductDao productDao();

    /**
     * Singleton to create the database instance.
     * @param context the current application context.
     * @return the database instance.
     */
    public static AppDatabase getAppDatabase(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "product-database").fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }
}