package fr.univpau.kayu.db;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import fr.univpau.kayu.models.Product;

/**
 * In order to execute any database query, we need to use a different thread than the UIThread.
 */
public class DatabaseTask {
    private static DatabaseTask instance;
    private AppDatabase myDatabase;
    private Executor mExecutor = Executors.newSingleThreadExecutor();

    /**
     * Retrieve the database instance.
     * @param app the application.
     */
    private DatabaseTask(Application app) {
        myDatabase = AppDatabase.getAppDatabase(app.getApplicationContext());
    }

    /**
     * Singleton to create the DatabaseTask instance.
     * @param app the application.
     * @return the DatabaseTask instance
     */
    public static DatabaseTask getInstance(Application app) {
        if(instance == null) {
            instance = new DatabaseTask(app);
        }
        return instance;
    }

    /**
     * Insert a product in the database.
     * @param product the product we want to insert.
     */
    public void insert(final Product product) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    myDatabase.productDao().insertAll(product);
                } catch (SQLiteConstraintException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Update a product in the database.
     * @param product the product we want to update.
     */
    public void update(final Product product) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                myDatabase.productDao().update(product);
            }
        });
    }

    /**
     * Delete every product entry in the database.
     * Used to clear the history.
     */
    public void deleteAll() {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                myDatabase.productDao().deleteAll();
            }
        });
    }
}