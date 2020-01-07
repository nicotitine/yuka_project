package fr.univpau.kayu.ui.scan;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONObject;
import fr.univpau.kayu.OFFIntentService;
import fr.univpau.kayu.R;
import fr.univpau.kayu.ShowCamera;

public class ScanFragment extends Fragment implements View.OnClickListener {

    private ScanViewModel scanViewModel;
    private Button searchBtn;
    private EditText gtinInput;

    private FrameLayout frameLayout;
    private ShowCamera showCamera;

    public static final int JSON_REQUEST_CODE = 0;
    public static final int PREVIEW_REQUEST_CODE = 1;
    public static final String RESULT_OFF_ARG = "off_arg";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        scanViewModel =
                ViewModelProviders.of(this).get(ScanViewModel.class);
        View root = inflater.inflate(R.layout.fragment_scan, container, false);

        searchBtn = root.findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(this);
        gtinInput = root.findViewById(R.id.gtinInput);
        frameLayout = root.findViewById(R.id.frameLayout);
        showCamera = new ShowCamera(getContext());
        frameLayout.addView(showCamera);





        gtinInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 8 || s.length() == 12 || s.length() == 13) {
                    String gtin = gtinInput.getText().toString();
                    launchGtinIntent(gtin, PREVIEW_REQUEST_CODE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return root;
    }

    public void launchGtinIntent(String gtin, int requestCode) {
        PendingIntent pendingResult = getActivity().createPendingResult(requestCode, new Intent(), 0);
        Intent intent = new Intent(getContext(), OFFIntentService.class);
        intent.putExtra(OFFIntentService.URL_EXTRA, "https://world.openfoodfacts.org/api/v0/product/" + gtin + ".json");
        intent.putExtra(OFFIntentService.PENDING_RESULT_EXTRA, pendingResult);
        getActivity().startService(intent);
    }

    @Override
    public void onActivityResult(int a, int b, Intent c) {
        Log.i("DEVUPPARES", "RESINTEJTBNEJTBEZEGJTH");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchBtn:

                String gtin = gtinInput.getText().toString();
                Log.i("DEVUPPAGTIN", gtin);
                launchGtinIntent(gtin, JSON_REQUEST_CODE);

                showCamera = null;
                break;
        }
    }

    public void update(JSONObject product) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}