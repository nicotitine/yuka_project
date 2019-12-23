package fr.univpau.kayu;

import androidx.appcompat.app.AppCompatActivity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import org.json.JSONException;
import org.json.JSONObject;



public class TestToDelete extends AppCompatActivity {

    private static final int RSS_DOWNLOAD_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_to_delete);

    }
}
