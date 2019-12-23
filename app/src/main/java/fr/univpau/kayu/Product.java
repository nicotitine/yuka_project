package fr.univpau.kayu;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

@Entity
public class Product {

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "quantity")
    private String quantity;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "gtin")
    private String gtin;

    @ColumnInfo(name = "generic_name")
    private String genericName;

    public Product() {

    }

    public Product(JSONObject from) {
        try {
            this.name = from.getString("product_name");
            this.quantity = from.getString("quantity");
            this.gtin = from.getString("code");
            this.genericName = from.getString("generic_name");
        } catch (JSONException e) {
            Log.i("DEVUPPA", e.getMessage());
        }
    }

    public String toString() {
        String result = "\n";

        result += "Product " + this.gtin + ":\n";
        result += "\tName: " + this.name + "\n";
        result += "\tQuantity: "  + this.quantity + "\n";
        result += "\tGeneric name: " + this.genericName + "\n";

        return result;
    }

    public String getName() {
        return this.name;
    }

    public String getGtin() {
        return this.gtin;
    }

    public String getQuantity() {
        return this.quantity;
    }

    public String getGenericName() {
        return this.genericName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGtin(String gtin) {
        this.gtin = gtin;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }
}
