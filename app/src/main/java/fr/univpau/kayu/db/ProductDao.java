package fr.univpau.kayu.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import fr.univpau.kayu.models.Product;

@Dao
public interface ProductDao {
    /**
     * Retrieve all products from the database.
     * @return a <code>LiveData</code> <code>List</code> which contains all products.
     */
    @Query("SELECT * FROM Products")
    LiveData<List<Product>> getAll();

    /**
     * Retrieve a specific product described by its EAN.
     * @param ean the product unique identifier.
     * @return the product described by the identifier.
     */
    @Query("SELECT * FROM Products WHERE ean = :ean LIMIT 1")
    LiveData<Product> getByEan(String ean);

    /**
     * Insert new products in the database.
     * @param products the product list we want to insert. Size from 1 to ...
     */
    @Insert
    void insertAll(Product... products);

    /**
     * Update a product already in the database.
     * @param product the product we want to update, containing all modifications.
     */
    @Update
    void update(Product product);

    /**
     * Delete every entry in the database.
     * Used in order to clear the history.
     */
    @Query("DELETE FROM Products")
    void deleteAll();
}
