package fr.univpau.kayu.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import fr.univpau.kayu.Product;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM product")
    List<Product> getAll();

    @Insert
    void insertAll(Product... products);
}
