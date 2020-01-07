package fr.univpau.kayu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import org.json.JSONException;
import org.json.JSONObject;
import fr.univpau.kayu.db.DatabaseTask;
import fr.univpau.kayu.ui.scan.ScanFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_scan, R.id.navigation_history, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == ScanFragment.JSON_REQUEST_CODE) {
            switch (resultCode) {
                case OFFIntentService.RESULT_CODE:
                    handleResponse(data);
                    break;
                default:
                    Log.i("DEVUPPA", new Integer(resultCode).toString());
            }
        }

        if(requestCode == ScanFragment.PREVIEW_REQUEST_CODE && data.getExtras() != null) {
            try {
                JSONObject json = new JSONObject(data.getExtras().get(OFFIntentService.JSON_RESULT_EXTRA).toString());
                int status = json.getInt("status");

                if(status == 1) {
                    JSONObject product = json.getJSONObject("product");
                    ScanFragment frag = (ScanFragment)getSupportFragmentManager().findFragmentById(R.id.navigation_scan);
                    Log.i("DEVUPPA", "RESPONSE");

                    if(frag != null) {
                        frag.update(product);
                    } else {
//                        ScanFragment newFrag = new ScanFragment();
//                        Bundle args = new Bundle();
//
//                        args.putString(ScanFragment.RESULT_OFF_ARG, product.toString());
//                        newFrag.setArguments(args);
//
//                        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
//                        trans.replace(R.id.navigation_scan, newFrag);
//                        trans.addToBackStack(null);
//
//                        trans.commit();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
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
                Product pro = new Product(product);
                DatabaseTask.getInstance(getApplication()).insert(pro);
                Intent productActivity = new Intent(this, ProductActivity.class);
                productActivity.putExtra(ProductActivity.PRODUCT_EXTRA_PARAM, pro);
                startActivity(productActivity);
            }
        } catch (JSONException e) {
            Log.i("DEVUPPA", e.getMessage());
        }
    }

}