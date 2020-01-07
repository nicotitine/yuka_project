package fr.univpau.kayu.db;

import android.app.Application;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import fr.univpau.kayu.Product;

public class DatabaseTask {
    private static DatabaseTask instance;
    private AppDatabase myDatabase;
    private Executor mExecutor = Executors.newSingleThreadExecutor();

    private DatabaseTask(Application app) {
        myDatabase = AppDatabase.getAppDatabase(app.getApplicationContext());
    }

    public static DatabaseTask getInstance(Application app) {
        if(instance == null) {
            instance = new DatabaseTask(app);
        }
        return instance;
    }

    public void insert(final Product product) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                myDatabase.productDao().insertAll(product);
            }
        });

        try {
            finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public void update(final Product product) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                myDatabase.productDao().update(product);

            }
        });
        try {
            finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}