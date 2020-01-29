package fr.univpau.kayu.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;

@Entity(tableName = "Products")
public class Product implements Serializable {

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "quantity")
    private String quantity;

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "ean")
    private String ean = "";

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

    @ColumnInfo(name = "nutriscore")
    private String nutriscore;

    @ColumnInfo(name = "nutriscore_grade")
    private String nutriscoreGrade;

    /**
     * Unused constructor. But must be present for the DAO.
     */
    public Product() {
    }

    /**
     * The product constructor.
     * We have to be careful here due to all exceptions that can be raised by searching in a JSONObject in Java...
     * @param from the JSONObject we got from <code>OFFService</code>.
     */
    public Product(JSONObject from) {
        try {
            try {
                this.name = from.getString("product_name_fr");
            } catch (JSONException e) {
                this.name = from.getString("product_name");
            }

            try {
                this.quantity = from.getString("quantity");
            } catch (JSONException e) {
                this.quantity = "";
            }
            this.quantity = from.getString("quantity");
            this.ean = from.getString("code");

            try {
                this.genericName = from.getString("generic_name_fr");
            } catch (JSONException e) {
                this.genericName = from.getString("generic_name");
            }

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
                e.printStackTrace();
            }

            try {
                this.images += selectedImages.getJSONObject("front").getJSONObject("display").getString("fr");
                this.images += ", ";
                this.images += selectedImages.getJSONObject("ingredients").getJSONObject("display").getString("fr");
                this.images += ", ";
                this.images += selectedImages.getJSONObject("nutrition").getJSONObject("display").getString("fr");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                ingredients = from.getString("ingredients_text_fr").replaceAll("_", "");
            } catch (JSONException e){
                JSONArray ingredientsArray = from.getJSONArray("ingredients");
                StringBuilder ingredientsBuilder = new StringBuilder();
                for(int i = 0; i < ingredientsArray.length(); i++) {
                    JSONObject ingredient = ingredientsArray.getJSONObject(i);

                    ingredientsBuilder.append(ingredient.getString("text").replaceAll("_", ""));

                    try {
                        String percent = ingredient.getString("percent");
                        ingredientsBuilder.append(" (")
                                .append(percent)
                                .append("%)");
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                    ingredientsBuilder.append(", ");
                }
                ingredients = ingredientsBuilder.toString();
            }

            try {
                this.nutriscore = from.getJSONObject("nutriments").toString();
            } catch (JSONException e) {
                this.nutriscore = "";
            }

            try {
                this.nutriscoreGrade = from.getString("nutriscore_grade").toUpperCase();
            } catch (JSONException e) {
                this.nutriscoreGrade = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the printable product description.
     */
    @NonNull
    @Override
    public String toString() {
        String result = "\n";

        result += "Product " + this.ean + ":\n";
        result += "\tName: " + this.name + "\n";
        result += "\tQuantity: "  + this.quantity + "\n";
        result += "\tGeneric name: " + this.genericName + "\n";
        result += "\tImages: " + this.images + "\n";

        return result;
    }


    /*******************************************************
     *
     * All getters and setters (mainly used by the product DAO).
     *
     ******************************************************/



    @NonNull
    public String getEan() {
        return this.ean;
    }

    public String getName() {
        return this.name;
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

    public String getNutriscore() { return this.nutriscore; }

    public String getNutriscoreGrade() { return this.nutriscoreGrade; }

    public void setEan(@NonNull String ean) {
        this.ean = ean;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setNutriscore(String nutriscore) { this.nutriscore = nutriscore; }

    public void setNutriscoreGrade(String nutriscoreGrade) { this.nutriscoreGrade = nutriscoreGrade; }
}