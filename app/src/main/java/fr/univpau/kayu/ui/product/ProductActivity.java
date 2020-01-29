package fr.univpau.kayu.ui.product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URISyntaxException;
import fr.univpau.kayu.adapters.ImageCarouselAdapter;
import fr.univpau.kayu.models.Product;
import fr.univpau.kayu.adapters.IngredientAdapter;
import fr.univpau.kayu.R;
import fr.univpau.kayu.db.DatabaseTask;
import me.relex.circleindicator.CircleIndicator;

public class ProductActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String PRODUCT_EAN_EXTRA = "product_ean";
    public static final String PRODUCT_EXTRA = "product";

    private boolean isAutomaticSearchOn;
    private ScrollView scrollView;
    private LinearLayout productNotFoundLayout;
    private LinearLayout loadingLayout;
    private TextView productNameTextView;
    private TextView productEanTextView;
    private TextView productQuantityTextView;
    private TextView searchOtherImagesTextView;
    private TextView carrefourPriceTextView;
    private TextView imagesTitleTextField;
    private ProgressBar carrefourPriceLoader;
    private Button savePriceButton;
    private ProgressBar imagesLoader;
    private Button displayImagesButton;
    private Button hideOtherImagesInfoButton;
    private Button cancelOtherImagesButton;
    private Button saveNewImagesButton;
    private RecyclerView ingredientsView;

    private ViewPager viewPager;
    private CircleIndicator indicator;
    private LinearLayout nutriscoreLinearLayout;
    private TextView noProductTextView;

    private String[] images;
    private String[] newImages;
    private String[] previousImages;

    // WebSocket related
    private Socket socket;

    private Product product;
    private String productEan;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        /*
         * Preference handling
         */
        SharedPreferences prefs = getSharedPreferences("preferences", 0);
        this.isAutomaticSearchOn = prefs.getBoolean("isAutomaticSearchOn", true);

        /*
         * Init all UI elements
         */
        this.scrollView = findViewById(R.id.scrollView);
        this.productNotFoundLayout = findViewById(R.id.productNotFoundLayout);
        this.loadingLayout = findViewById(R.id.loadingLayout);
        this.productNameTextView = findViewById(R.id.productName);
        this.productEanTextView = findViewById(R.id.productEan);
        this.productQuantityTextView = findViewById(R.id.productQuantity);
        this.imagesTitleTextField = findViewById(R.id.imagesTitle);
        this.searchOtherImagesTextView = findViewById(R.id.searchOtherImagesText);
        this.carrefourPriceTextView = findViewById(R.id.carrefourPrice);
        this.carrefourPriceLoader = findViewById(R.id.carrefourPriceLoader);
        this.savePriceButton = findViewById(R.id.savePriceBtn);
        this.imagesLoader = findViewById(R.id.imagesLoader);
        this.displayImagesButton = findViewById(R.id.displayImagesBtn);
        this.hideOtherImagesInfoButton = findViewById(R.id.hideOtherImagesInfoBtn);
        this.cancelOtherImagesButton = findViewById(R.id.cancelOtherImagesBtn);
        this.saveNewImagesButton = findViewById(R.id.saveNewImagesBtn);
        this.ingredientsView = findViewById(R.id.ingredients);
        Button openOnOffButton = findViewById(R.id.openOnOffBtn);
        this.viewPager = findViewById(R.id.viewPager);
        this.indicator = findViewById(R.id.indicator);
        this.nutriscoreLinearLayout = findViewById(R.id.nutriscoreLayout);
        this.noProductTextView = findViewById(R.id.noProductTitle);
        Button addProductOFFButton = findViewById(R.id.addProductOFF);

        /*
         * Init button on click listener
         */
        this.savePriceButton.setOnClickListener(this);
        this.displayImagesButton.setOnClickListener(this);
        this.hideOtherImagesInfoButton.setOnClickListener(this);
        this.cancelOtherImagesButton.setOnClickListener(this);
        this.saveNewImagesButton.setOnClickListener(this);
        openOnOffButton.setOnClickListener(this);
        addProductOFFButton.setOnClickListener(this);

        /*
         * Display back button at the top left corner.
         */
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        /*
         * Intent params handling
         */
        Intent intent = getIntent();
        this.product = (Product)intent.getSerializableExtra(PRODUCT_EXTRA);
        this.productEan = intent.getStringExtra(PRODUCT_EAN_EXTRA);

        this.init();
    }

    /**
     * Initialise all the UI elements.
     */
    private void init() {

        /*
         * If the product is found, then we display it, unwise, we display the 'product not found' layout.
         */
        if(this.product != null) {
            /*
             * Hide loading and 'product not found' layout.
             */
            this.loadingLayout.setVisibility(View.GONE);
            this.productNotFoundLayout.setVisibility(View.GONE);
            this.scrollView.setVisibility(View.VISIBLE);

            /*
             * Set text for basic product information.
             */
            this.productNameTextView.setText(this.product.getName());
            this.productEanTextView.setText(this.product.getEan());
            this.productQuantityTextView.setText(this.product.getQuantity());
            this.carrefourPriceTextView.setText(getString(R.string.product_price_carrefour, this.product.getPrice()));

            /*
             * Transform "image1, image2" to ["image1", "image2]
             */
            try {
                this.images = product.getImages().split(", ");
            } catch (Exception e) {
                this.images = new String[0];
            }
            this.imagesTitleTextField.setText(getResources().getQuantityString(R.plurals.product_images, this.images.length, this.images.length));

            /*
             *  Carousel init
             */
            this.initCarousel();


            /*
             * Ingredients list init
             */
            String[] ingredientsArray;
            try {
                ingredientsArray = this.product.getIngredients().split(", ");
            } catch (Exception e) {
                ingredientsArray = new String[0];
            }

            /*
             * Ingredients layout init
             */
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            this.ingredientsView.setLayoutManager(layoutManager);
            IngredientAdapter adapter = new IngredientAdapter(ingredientsArray);
            this.ingredientsView.setAdapter(adapter);

            /*
             * Nutriscore layout init
             */
            NutriscoreLayout nutriscoreLayout = new NutriscoreLayout(this, this.product.getNutriscore(), this.product.getNutriscoreGrade());
            this.nutriscoreLinearLayout.removeAllViews();
            this.nutriscoreLinearLayout.addView(nutriscoreLayout.getRootView());


            /*
             * Socket related
             * It uses websockets (socket.io) to connect to an external server.
             * We send two requests to this server:
             *      - Get the product price (only available for Carrefour)
             *      - Get better images for this product
             */
            if(this.isAutomaticSearchOn) {
                try {
                    this.socket = IO.socket("http://51.83.46.109:9091");
                    this.socket.connect();

                    JSONObject data = new JSONObject("{data: {gtin: " + this.product.getEan() + ", id: " + this.socket.id() + "}}");

                    if (!this.product.getPriceAlreadyRetrieved()) {
                        this.socket.emit("getPriceCarrefour", data);
                    } else {
                        this.carrefourPriceLoader.setVisibility(View.GONE);
                        this.carrefourPriceTextView.setText(getString(R.string.product_price_carrefour, product.getPrice()));
                    }


                    if(!this.product.getImagesAlreadyRetrieved()) {
                        this.socket.emit("getImages", data);
                    } else {
                        this.imagesLoader.setVisibility(View.GONE);
                        this.searchOtherImagesTextView.setVisibility(View.GONE);
                    }

                    this.socket.on("getImagesResponse", this.getImagesResponse);
                    this.socket.on("getPriceCarrefourResponse", this.getPriceCarrefourResponse);
                } catch (JSONException | URISyntaxException e) {
                    e.printStackTrace();
                }
            } else {
                /*
                 * Is the automatic search is off, we don't connect to the socket and we hide loaders.
                 */
                this.carrefourPriceLoader.setVisibility(View.GONE);
                this.imagesLoader.setVisibility(View.GONE);
                this.searchOtherImagesTextView.setVisibility(View.GONE);
            }
        } else {
            /*
             * If the product has not been found, we display the 'product not found' layout.
             */
            this.scrollView.setVisibility(View.GONE);
            this.loadingLayout.setVisibility(View.GONE);
            this.productNotFoundLayout.setVisibility(View.VISIBLE);
            this.noProductTextView.setText(String.format(getString(R.string.productNotFound), this.productEan));
        }
    }

    /**
     * Carousel and indicators init. This function is called every times the product images change.
     */
    private void initCarousel() {
        ImageCarouselAdapter imagePagerAdapter = new ImageCarouselAdapter(this, this.images, this.viewPager);
        this.viewPager.setAdapter(imagePagerAdapter);
        this.indicator.setViewPager(this.viewPager);
        imagePagerAdapter.registerDataSetObserver(this.indicator.getDataSetObserver());

        this.imagesTitleTextField.setText(getResources().getQuantityString(R.plurals.product_images, this.images.length, this.images.length));
    }

    /**
     * Socket related.
     * Response listener for 'getImages' request.
     */
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
                        hideOtherImagesInfoButton.setVisibility(View.VISIBLE);

                        if(images.length() > 0) {
                            searchOtherImagesTextView.setText(getResources().getQuantityString(R.plurals.betterImagesFound, images.length(), images.length()));
                            searchOtherImagesTextView.setPadding(0, 0, 32, 0);

                            displayImagesButton.setVisibility(View.VISIBLE);

                            newImages = new String[images.length()];
                            for(int i = 0; i < images.length(); i++) {
                                newImages[i] = images.get(i).toString();
                            }
                        } else {
                            searchOtherImagesTextView.setText(getString(R.string.noAdditionalImages));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };


    /**
     * Socket related.
     * Response listener for 'getPriceCarrefour' request.
     */
    private Emitter.Listener getPriceCarrefourResponse = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                    try {
                        JSONObject price = data.getJSONObject("data");

                        if(!price.getString("price").equals("null")) {
                            StringBuilder priceBuilder = new StringBuilder();
                            priceBuilder.append(price.getString("price")).append(getString(R.string.currency));

                            carrefourPriceTextView.setText(getString(R.string.product_price_carrefour, priceBuilder.toString()));
                            product.setPrice(priceBuilder.toString());
                            savePriceButton.setVisibility(View.VISIBLE);
                        } else {
                            carrefourPriceTextView.setText(getString(R.string.priceNotFound));
                        }
                        carrefourPriceLoader.setVisibility(View.GONE);
                    } catch (JSONException e) {
                       e.printStackTrace();
                    }
                }
            });
        }
    };


    /**
     * Must disconnect the socket.
     */
    @Override
    public void onBackPressed() {
        // Check because socket can be null if the user doesn't want automatic search.
        if(this.socket != null) {
            this.socket.disconnect();
            this.socket.off("getPriceCarrefourResponse", this.getPriceCarrefourResponse);
            this.socket.off("getImagesResponse", this.getImagesResponse);
        }
        super.onBackPressed();
    }

    /**
     * Must disconnect the socket
     */
    @Override
    public void onDestroy() {
        // Check because socket can be null if the user doesn't want automatic search.
        if(this.socket != null) {
            this.socket.disconnect();
            this.socket.off("getPriceCarrefourResponse", this.getPriceCarrefourResponse);
            this.socket.off("getImagesResponse", this.getImagesResponse);
        }
        super.onDestroy();
    }

    /**
     * Implement View.OnClickListener.
     * Basically only for buttons in that Activity.
     * @param v the view the user clicked on
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.displayImagesBtn:
                /*
                 * Context: New images have been found. The user clicks on 'Display'.
                 * We must save the current images if the user doesn't want to save the new ones.
                 * Then we initialize the carousel to update images.
                 * Finally, we hide previous buttons to show new ones :
                 *  - 'Save' if the user likes the new images
                 *  - 'Cancel' in the other case
                 */
                this.previousImages = this.images;
                this.images = this.newImages;
                this.displayImagesButton.setVisibility(View.GONE);
                this.searchOtherImagesTextView.setVisibility(View.GONE);
                this.hideOtherImagesInfoButton.setVisibility(View.GONE);
                this.cancelOtherImagesButton.setVisibility(View.VISIBLE);
                this.saveNewImagesButton.setVisibility(View.VISIBLE);
                this.initCarousel();
                break;
            case R.id.hideOtherImagesInfoBtn:
                /*
                 * Context: New images have been found. The user has no interest in them.
                 * We hide the popup.
                 */
                this.searchOtherImagesTextView.setVisibility(View.GONE);
                this.displayImagesButton.setVisibility(View.GONE);
                this.hideOtherImagesInfoButton.setVisibility(View.GONE);
                break;
            case R.id.cancelOtherImagesBtn:
                /*
                 * Context: New images have been found and the user don't want to save them.
                 * We rollback the previous images, update the carousel and hide the button.
                 */
                this.images = this.previousImages;
                this.cancelOtherImagesButton.setVisibility(View.GONE);
                this.saveNewImagesButton.setVisibility(View.GONE);
                this.initCarousel();
                break;
            case R.id.saveNewImagesBtn:
                /*
                 * Context: New images have been found and the user want to save them.
                 */
                this.cancelOtherImagesButton.setVisibility(View.GONE);
                this.saveNewImagesButton.setVisibility(View.GONE);

                /*
                 * We must transform String[] of images to a simple String of this type : * "image1, image2, ..."
                 */
                StringBuilder newImagesProduct = new StringBuilder();

                for(int i = 0; i < this.images.length; i++) {
                    if(i == this.images.length - 1) {
                        newImagesProduct.append(this.images[i]);
                    } else {
                        newImagesProduct.append(this.images[i]).append(", ");
                    }
                }

                /*
                 * Update the product in the database
                 */
                this.product.setImages(newImagesProduct.toString());
                this.product.setImagesAlreadyRetrieved(true);
                DatabaseTask.getInstance(getApplication()).update(this.product);
                Toast.makeText(this, getResources().getQuantityString(R.plurals.saveImages, this.images.length, this.images.length), Toast.LENGTH_SHORT).show();
                break;
            case R.id.savePriceBtn:
                /*
                 * Context: the product price has been found and the user want to save it.
                 */
                this.product.setPriceAlreadyRetrieved(true);
                DatabaseTask.getInstance(getApplication()).update(this.product);
                this.savePriceButton.setVisibility(View.GONE);
                Toast.makeText(this, R.string.priceSaved, Toast.LENGTH_SHORT).show();
                break;
            case R.id.openOnOffBtn:
                /*
                 * Context: the product has been found. The user want more details on this product on OpenFoodFacts.com
                 */
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://fr.openfoodfacts.org/produit/" + this.product.getEan()));
                startActivity(browserIntent);
                break;
            case R.id.addProductOFF:
                /*
                 * Context: the product has not been found. The user want to add the product on OpenFoodFacts.com
                 */
                Intent browserIntentNew = new Intent(Intent.ACTION_VIEW, Uri.parse("https://fr.openfoodfacts.org/produit/" + this.productEan));
                startActivity(browserIntentNew);
                break;
        }
    }
}