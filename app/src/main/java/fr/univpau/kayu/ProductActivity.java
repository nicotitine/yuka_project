package fr.univpau.kayu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URISyntaxException;
import fr.univpau.kayu.db.DatabaseTask;
import me.relex.circleindicator.CircleIndicator;

public class ProductActivity extends AppCompatActivity {

    public static final String PRODUCT_EXTRA_PARAM = "product";
    public static final String PRODUCT_FOUND = "product_found";
    public static final String PRODUCT_GTIN =  "product_gtin";

    private boolean isProductFound;


    private ScrollView scrollView;
    private ConstraintLayout mainLayout;
    private TextView productName;
    private TextView productGtin;
    private TextView productQuantity;
    private TextView searchOtherImagesText;
    private TextView carrefourPrice;
    private TextView imagesTitle;
    private ProgressBar carrefourPriceLoader;
    private Button savePriceBtn;
    private ProgressBar imagesLoader;
    private Button displayImagesBtn;
    private Button hideOtherImagesInfoBtn;
    private Button cancelOtherImagesBtn;
    private Button saveNewImagesBtn;
    private RecyclerView ingredients;
    private Button openOnOffBtn;
    private ViewPager viewPager;
    private CircleIndicator indicator;
    private TextView noProductTitle;
    private Button addProductOFF;
    private String[] images;
    private String[] newImages;
    private String[] previousImages;

    // WebSocket related
    private Socket socket;

    private Product product;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        // Preference handling
        SharedPreferences prefs = getSharedPreferences("preferences", 0);
        boolean isAutomaticSearchOn = prefs.getBoolean("isAutomaticSearchOn", true);

        // All UI elements
        scrollView = findViewById(R.id.scrollView);
        mainLayout = findViewById(R.id.mainLayout);
        productName = findViewById(R.id.productName);
        productGtin = findViewById(R.id.productGtin);
        productQuantity = findViewById(R.id.productQuantity);
        imagesTitle = findViewById(R.id.imagesTitle);
        searchOtherImagesText = findViewById(R.id.searchOtherImagesText);
        carrefourPrice = findViewById(R.id.carrefourPrice);
        carrefourPriceLoader = findViewById(R.id.carrefourPriceLoader);
        savePriceBtn = findViewById(R.id.savePriceBtn);
        imagesLoader = findViewById(R.id.imagesLoader);
        displayImagesBtn = findViewById(R.id.displayImagesBtn);
        hideOtherImagesInfoBtn = findViewById(R.id.hideOtherImagesInfoBtn);
        cancelOtherImagesBtn = findViewById(R.id.cancelOtherImagesBtn);
        saveNewImagesBtn = findViewById(R.id.saveNewImagesBtn);
        ingredients = findViewById(R.id.ingredients);
        openOnOffBtn = findViewById(R.id.openOnOffBtn);
        viewPager = findViewById(R.id.viewPager);
        indicator = findViewById(R.id.indicator);
        noProductTitle = findViewById(R.id.noProductTitle);
        addProductOFF = findViewById(R.id.addProductOFF);


        // Display back button at the top left corner.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Intent params handling
        Intent intent = getIntent();
        isProductFound = intent.getBooleanExtra(PRODUCT_FOUND, false);

        if(isProductFound) {
            // Start of init if product have been found
            product = (Product)intent.getSerializableExtra(PRODUCT_EXTRA_PARAM);

            // Set UI elements text
            productName.setText(product.getName());
            productGtin.setText(product.getGtin());
            productQuantity.setText(product.getQuantity());
            carrefourPrice.setText("Carrefour: " + product.getPrice());

            // Transform "image1, image2" to ["image1", "image2]
            try {
                images = product.getImages().split(", ");
            } catch (Exception e) {
                images = new String[0];
            }

            imagesTitle.setText(getString(R.string.product_images) + " (" + images.length + ")");

            // Carousel init
            ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(this, images, viewPager);
            viewPager.setAdapter(imagePagerAdapter);
            indicator.setViewPager(viewPager);
            imagePagerAdapter.registerDataSetObserver(indicator.getDataSetObserver());

            displayImagesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    previousImages = images;
                    images = newImages;
                    displayImagesBtn.setVisibility(View.GONE);
                    searchOtherImagesText.setVisibility(View.GONE);
                    hideOtherImagesInfoBtn.setVisibility(View.GONE);
                    cancelOtherImagesBtn.setVisibility(View.VISIBLE);
                    saveNewImagesBtn.setVisibility(View.VISIBLE);
                }
            });

            hideOtherImagesInfoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchOtherImagesText.setVisibility(View.GONE);
                    displayImagesBtn.setVisibility(View.GONE);
                    hideOtherImagesInfoBtn.setVisibility(View.GONE);
                }
            });

            cancelOtherImagesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    images = previousImages;
                    cancelOtherImagesBtn.setVisibility(View.GONE);
                }
            });

            saveNewImagesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelOtherImagesBtn.setVisibility(View.GONE);
                    saveNewImagesBtn.setVisibility(View.GONE);

                    String newImagesProduct = "";

                    for(int i = 0; i < images.length; i++) {
                        if(i == images.length - 1) {
                            newImagesProduct += images[i];
                        } else {
                            newImagesProduct += images[i] + ", ";
                        }
                    }

                    product.setImages(newImagesProduct);
                    product.setImagesAlreadyRetrieved(true);

                    Log.i("DEVUPPA", "Updating product images");

                    DatabaseTask.getInstance(getApplication()).update(product);
                }
            });

            savePriceBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    product.setPriceAlreadyRetrieved(true);
                    DatabaseTask.getInstance(getApplication()).update(product);
                    savePriceBtn.setVisibility(View.GONE);
                }
            });

            // Ingredients list init
            String[] ingredientsArray;
            try {
                ingredientsArray = product.getIngredients().split(", ");
            } catch (Exception e) {
                ingredientsArray = new String[0];
            }

            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            ingredients.setLayoutManager(layoutManager);
            IngredientAdapter adapter = new IngredientAdapter(ingredientsArray);
            ingredients.setAdapter(adapter);

            openOnOffBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://fr.openfoodfacts.org/produit/" + product.getGtin()));
                    startActivity(browserIntent);
                }
            });



            /**
             * Socket related
             * It uses websockets (socket.io) to connect to an external server.
             * We send two requests to this server:
             *      - Get the product price (only available at Carrefour)
             *      - Get better images for this product
             */
            if(isAutomaticSearchOn) {

                Log.i("DEVUPPAPREFS", "DARK MODE ON");

                try {
                    socket = IO.socket("http://51.83.46.109:9091");
                    socket.connect();

                    JSONObject data = new JSONObject("{data: {gtin: " + product.getGtin() + ", id: " + socket.id() + "}}");

                    if (!product.getPriceAlreadyRetrieved()) {
                        socket.emit("getPriceCarrefour", data);
                    } else {
                        carrefourPriceLoader.setVisibility(View.GONE);
                        carrefourPrice.setText("Carrefour: " + product.getPrice());
                    }

                    if(!product.getImagesAlreadyRetrieved()) {
                        socket.emit("getImages", data);
                    } else {
                        imagesLoader.setVisibility(View.GONE);
                        searchOtherImagesText.setVisibility(View.GONE);
                    }


                    socket.on("getImagesResponse", getImagesResponse);
                    socket.on("getPriceCarrefourResponse", getPriceCarrefourResponse);
                } catch (JSONException e) {
                    Log.i("DEVUPPA", e.getMessage());
                } catch (URISyntaxException e) {
                    Log.i("DEVUPPA", e.getMessage());
                }
            } else {
                carrefourPriceLoader.setVisibility(View.GONE);
                imagesLoader.setVisibility(View.GONE);
                searchOtherImagesText.setVisibility(View.GONE);
            }

        } else {
            final String gtin = intent.getStringExtra(PRODUCT_GTIN);
            for(int i = 0; i < mainLayout.getChildCount(); i++) {
                if(mainLayout.getChildAt(i).getId() != R.id.noProductTitle) {
                    mainLayout.getChildAt(i).setVisibility(View.GONE);
                }
            }

            noProductTitle.setVisibility(View.VISIBLE);
            noProductTitle.setText("Le produit " + gtin + " n'a pas été trouvé sur Open Food Facts.");

            addProductOFF.setVisibility(View.VISIBLE);
            addProductOFF.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://fr.openfoodfacts.org/produit/" + gtin));
                    startActivity(browserIntent);
                }
            });
        }
    }


    private void displayImages(JSONArray images) throws JSONException{
        searchOtherImagesText.setText(images.length() + " images pouvant être de meilleure qualité ont été trouvées !");
        searchOtherImagesText.setPadding(0, 0, 32, 0);

        displayImagesBtn.setVisibility(View.VISIBLE);

        newImages = new String[images.length()];
        for(int i = 0; i < images.length(); i++) {
            newImages[i] = images.get(i).toString();
        }
    }

    private void displayPrice(JSONObject price) throws JSONException {
        if(price.getString("price") != "null") {
            carrefourPrice.setText("Carrefour: " + price.getString("price") + "€");
            product.setPrice(price.getString("price") + "€");
            savePriceBtn.setVisibility(View.VISIBLE);
        } else {
            carrefourPrice.setText("Prix introuvable.");
        }

        carrefourPriceLoader.setVisibility(View.GONE);
    }

    private Emitter.Listener getImagesResponse = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                    try {
                        JSONArray images = data.getJSONObject("data").getJSONObject("data").getJSONArray("images");

                        imagesLoader.setVisibility(View.GONE);
                        hideOtherImagesInfoBtn.setVisibility(View.VISIBLE);

                        if(images.length() > 0) {
                            displayImages(images);
                        } else {
                            searchOtherImagesText.setText("Aucune image additionnelle n'a été trouvée.");
                        }

                        Log.i("DEVUPPASOCKET", data.toString());
                    } catch (JSONException e) {
                        Log.i("DEVUPPASOCKET", e.getMessage());
                    }
                }
            });
        }
    };


    private Emitter.Listener getPriceCarrefourResponse = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                    try {
                        JSONObject result = data.getJSONObject("data");

                        displayPrice(result);

                        Log.i("DEVUPPASOCKET", data.toString());
                    } catch (JSONException e) {
                        Log.i("DEVUPPASOCKET", e.getMessage());
                    }
                }
            });
        }
    };

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }


    @Override
    public void onBackPressed() {
        Glide.with(this).clear(viewPager);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Glide.with(this).clear(viewPager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        finish();
    }
}