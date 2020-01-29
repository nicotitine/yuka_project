package fr.univpau.kayu;

import android.app.IntentService;
import android.content.Intent;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import fr.univpau.kayu.models.Product;

public class OFFService extends IntentService {

    private static final String TAG = OFFService.class.getSimpleName();
    public static final String PRODUCT_FOUND_EXTRA = "product_found";
    public static final String PRODUCT_EXTRA = "product";
    public static final String PRODUCT_EAN_EXTRA = "product_ean";
    public static final String PRODUCT_IS_PREVIEW_EXTRA = "product_is_preview";
    public static final String GET_PRODUCT_ACTION = "get_product";

    public OFFService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String ean = intent.getStringExtra(PRODUCT_EAN_EXTRA);
        boolean isPreview = intent.getBooleanExtra(PRODUCT_IS_PREVIEW_EXTRA, false);

        URL url = null;
        HttpURLConnection connection;
        StringBuilder total = null;
        Intent reply = new Intent();
        boolean productFound = false;
        Product product = null;

        reply.setAction(GET_PRODUCT_ACTION);
        reply.putExtra(PRODUCT_IS_PREVIEW_EXTRA, isPreview);
        reply.putExtra(PRODUCT_EAN_EXTRA, ean);

        try {
            url = new URL("https://world.openfoodfacts.org/api/v0/product/" + ean + ".json");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if(url == null) {
            reply.putExtra(PRODUCT_FOUND_EXTRA, false);
            sendBroadcast(reply);
            return;
        }

        try {
            connection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(connection.getInputStream());
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            total = new StringBuilder();
            for(String line; (line = r.readLine()) != null; ) {
                total.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(total == null) {
            reply.putExtra(PRODUCT_FOUND_EXTRA, false);
            sendBroadcast(reply);
            return;
        }

        try {
            JSONObject response = new JSONObject(total.toString());
            int status =  response.getInt("status");

            if(status == 1) {
                productFound = true;
                product = new Product(response.getJSONObject("product"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(productFound) {
            reply.putExtra(PRODUCT_EXTRA, product);
            reply.putExtra(PRODUCT_FOUND_EXTRA, true);
        } else {
            reply.putExtra(PRODUCT_FOUND_EXTRA, false);
        }

        sendBroadcast(reply);
    }
}