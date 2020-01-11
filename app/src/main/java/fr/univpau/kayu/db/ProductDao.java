package fr.univpau.kayu.db;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

import fr.univpau.kayu.Product;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM Products")
    LiveData<List<Product>> getAll();

    @Query("SELECT * FROM Products WHERE gtin = :gtin LIMIT 1")
    LiveData<Product> getByGtin(String gtin);

    @Insert
    void insertAll(Product... products);

    @Update
    int update(Product product);

    @Query("DELETE FROM Products")
    void deleteAll();
}
