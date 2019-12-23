package fr.univpau.kayu;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final int RSS_DOWNLOAD_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == RSS_DOWNLOAD_REQUEST_CODE) {
            switch (resultCode) {
                case OFFIntentService.RESULT_CODE:
                    handleResponse(data);
                    break;
                default:
                    Log.i("DEVUPPA", new Integer(resultCode).toString());
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleResponse(Intent data) {


        String result = data.getExtras().get(OFFIntentService.JSON_RESULT_EXTRA).toString();

        try {
            JSONObject json = new JSONObject(result);
            int status = json.getInt("status");

            if(status == 1) {
                JSONObject product = json.getJSONObject("product");

                Intent productActivity = new Intent(this, ProductActivity.class);
                productActivity.putExtra(ProductActivity.PRODUCT_EXTRA_PARAM, product.toString());
                startActivity(productActivity);
            }
        } catch (JSONException e) {
            Log.i("DEVUPPA", e.getMessage());
        }
    }

}
