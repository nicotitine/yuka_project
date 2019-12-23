package fr.univpau.kayu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import fr.univpau.kayu.db.AppDatabase;
import fr.univpau.kayu.db.DatabaseTask;

public class ProductActivity extends AppCompatActivity {

    public static final String PRODUCT_EXTRA_PARAM = "product";

    private TextView productName;
    private TextView productGtin;
    private TextView productQuantity;
    private TextView searchOtherImagesText;
    private TextView carrefourPrice;
    private ProgressBar carrefourPriceLoader;
    private ProgressBar imagesLoader;

    // WebSocket related
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        productName = findViewById(R.id.productName);
        productGtin = findViewById(R.id.productGtin);
        productQuantity = findViewById(R.id.productQuantity);
        searchOtherImagesText = findViewById(R.id.searchOtherImagesText);
        carrefourPrice = findViewById(R.id.carrefourPrice);
        carrefourPriceLoader = findViewById(R.id.carrefourPriceLoader);
        imagesLoader = findViewById(R.id.imagesLoader);

        Intent intent = getIntent();
        String productString = intent.getStringExtra(PRODUCT_EXTRA_PARAM);


        try {
            JSONObject productJson = new JSONObject(productString);
            Product product = new Product(productJson);

            productName.setText(product.getName());
            productGtin.setText(product.getGtin());
            productQuantity.setText(product.getQuantity());



            socket = IO.socket("http://51.83.46.109:9091");
            socket.connect();

            JSONObject data = new JSONObject("{data: {gtin: " + product.getGtin() + ", id: " + socket.id() + "}}");

            socket.emit("getPriceCarrefour", data);
            socket.emit("getImages", data);


            socket.on("getImagesResponse", getImagesResponse);
            socket.on("getPriceCarrefourResponse", getPriceCarrefourResponse);


//            Thread t = new Thread(new DatabaseTask(product, getApplicationContext()));
//            t.start();

            Log.i("DEVUPPA", product.toString());
        } catch (JSONException e) {
            Log.i("DEVUPPA", e.getMessage());
        } catch (URISyntaxException e) {
            Log.i("DEVUPPA", e.getMessage());
        }
    }

    private void displayImages(JSONArray images) {
        searchOtherImagesText.setText(images.length() + " images pouvant être de meilleure qualité ont été trouvées !");
        searchOtherImagesText.setPadding(16, 0, 8, 0);
        imagesLoader.setVisibility(View.GONE);
    }

    private void displayPrice(JSONObject price) throws JSONException {
        carrefourPrice.setText("Carrefour: " + price.getString("price") + "€");
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

                        if(images.length() > 0) {
                            displayImages(images);
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

                        if(result.get("price") != null) {
                            displayPrice(result);
                        }

                        Log.i("DEVUPPASOCKET", data.toString());
                    } catch (JSONException e) {
                        Log.i("DEVUPPASOCKET", e.getMessage());
                    }
                }
            });
        }
    };
}
