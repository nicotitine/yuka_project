package fr.univpau.kayu;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class OFFIntentService extends IntentService {
    private static final String TAG = OFFIntentService.class.getSimpleName();

    public static final String PENDING_RESULT_EXTRA = "pending_result";
    public static final String URL_EXTRA = "url";
    public static final String JSON_RESULT_EXTRA = "result";

    public static final int RESULT_CODE = 0;
    public static final int INVALID_URL_CODE = 1;
    public static final int ERROR_CODE = 2;

    public OFFIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        PendingIntent reply = intent.getParcelableExtra(PENDING_RESULT_EXTRA);
        InputStream in;
        try {
            try {
                URL url = new URL(intent.getStringExtra(URL_EXTRA));
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                in = new BufferedInputStream(connection.getInputStream());

                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                for (String line; (line = r.readLine()) != null; ) {
                    total.append(line).append('\n');
                }
                String string = total.toString();

                Intent result = new Intent();
                result.putExtra(JSON_RESULT_EXTRA, string);

                Log.i("DEVUPPA", "Service finishing");

                reply.send(this, RESULT_CODE, result);

            } catch (MalformedURLException e) {
                reply.send(INVALID_URL_CODE);
            } catch (Exception e) {
                Log.i("DEVUPPA", e.getMessage());
                reply.send(ERROR_CODE);
            }
        } catch(PendingIntent.CanceledException e) {
            Log.i("DEVUPPA", "reply cancelled");
        }
    }
}