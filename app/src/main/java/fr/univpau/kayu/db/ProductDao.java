package fr.univpau.kayu.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

import fr.univpau.kayu.Product;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM product")
    LiveData<List<Product>> getAll();

    @Query("SELECT * FROM product WHERE gtin = :gtin LIMIT 1")
    LiveData<Product> getByGtin(String gtin);

    @Insert
    void insertAll(Product... products);

    @Update
    int update(Product product);
}
