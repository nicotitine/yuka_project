package fr.univpau.kayu.db;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.widget.Toast;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fr.univpau.kayu.MainActivity;
import fr.univpau.kayu.Product;

public class DatabaseTask {
    private static DatabaseTask instance;
    private AppDatabase myDatabase;
    private Executor mExecutor = Executors.newSingleThreadExecutor();
    private Context context;

    private DatabaseTask(Application app) {
        myDatabase = AppDatabase.getAppDatabase(app.getApplicationContext());
        context = app.getApplicationContext();
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
                try {
                    myDatabase.productDao().insertAll(product);
                } catch (SQLiteConstraintException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void update(final Product product) {

        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                myDatabase.productDao().update(product);
            }
        });

    }

    public void deleteAll() {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                myDatabase.productDao().deleteAll();
            }
        });
    }
}