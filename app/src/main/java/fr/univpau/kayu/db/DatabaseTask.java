package fr.univpau.kayu.db;

import android.content.Context;
import android.util.Log;

import fr.univpau.kayu.Product;

public class DatabaseTask implements Runnable {
    private Product product;
    private Context context;

    public DatabaseTask(Product product, Context context) {
        this.product = product;
        this.context = context;
    }

    @Override
    public void run() {
        Log.i("DEVUPPA", "PRODUCT SAVED");
        AppDatabase.getAppDatabase(context).productDao().insertAll(product);
    }
}
