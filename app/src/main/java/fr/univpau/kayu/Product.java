package fr.univpau.kayu;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;

@Entity
public class Product implements Serializable {

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

    @ColumnInfo(name = "images")
    private String images;

    @ColumnInfo(name = "images_already_retrieved")
    private boolean imagesAlreadyRetrieved;

    @ColumnInfo(name = "price")
    private String price;

    @ColumnInfo(name = "price_already_retrieved")
    private boolean priceAlreadyRetrieved;

    @ColumnInfo(name = "ingredients")
    private String ingredients;


    public Product() {

    }

    public Product(JSONObject from) {
        try {
            this.name = from.getString("product_name");
            this.quantity = from.getString("quantity");
            this.gtin = from.getString("code");
            this.genericName = from.getString("generic_name");
            this.images = "";
            this.imagesAlreadyRetrieved = false;
            this.price = "";
            this.priceAlreadyRetrieved = false;
            this.ingredients = "";

            JSONObject selectedImages = from.getJSONObject("selected_images");

            try {
                this.images += selectedImages.getJSONObject("front").getJSONObject("display").getString("en");
                this.images += ", ";
                this.images += selectedImages.getJSONObject("ingredients").getJSONObject("display").getString("en");
                this.images += ", ";
                this.images += selectedImages.getJSONObject("nutrition").getJSONObject("display").getString("en");
            } catch (JSONException e) {

            }

            try {
                this.images += selectedImages.getJSONObject("front").getJSONObject("display").getString("fr");
                this.images += ", ";
                this.images += selectedImages.getJSONObject("ingredients").getJSONObject("display").getString("fr");
                this.images += ", ";
                this.images += selectedImages.getJSONObject("nutrition").getJSONObject("display").getString("fr");
            } catch (JSONException e) {

            }

            JSONArray ingredientsArray = from.getJSONArray("ingredients");
            for(int i = 0; i < ingredientsArray.length(); i++) {
                JSONObject ingredient = ingredientsArray.getJSONObject(i);
                ingredients += ingredient.getString("text").replaceAll("_", "");

                try {
                    String percent = ingredient.getString("percent");
                    ingredients += " (";
                    ingredients += percent;
                    ingredients += "%)";
                } catch (JSONException e) {

                }

                ingredients += ", ";
            }

            Log.i("DEVUPPAINGREDIENTS", ingredients);

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
        result += "\tImages: " + this.images + "\n";

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

    public String getImages() { return this.images; }

    public boolean getImagesAlreadyRetrieved() { return this.imagesAlreadyRetrieved; }

    public String getPrice() { return this.price; }

    public boolean getPriceAlreadyRetrieved() { return this.priceAlreadyRetrieved; }

    public String getIngredients() { return this.ingredients; }

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

    public void setImages(String images) { this.images = images; }

    public void setImagesAlreadyRetrieved(boolean imagesAlreadyRetrieved) { this.imagesAlreadyRetrieved = imagesAlreadyRetrieved; }

    public void setPrice(String price) { this.price = price; }

    public void setPriceAlreadyRetrieved(boolean priceAlreadyRetrieved) { this.priceAlreadyRetrieved = priceAlreadyRetrieved; }

    public void setIngredients(String ingredients) { this.ingredients = ingredients; }
}