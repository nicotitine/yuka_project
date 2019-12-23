package fr.univpau.kayu.ui.scan;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import org.json.JSONObject;

import fr.univpau.kayu.MainActivity;
import fr.univpau.kayu.OFFIntentService;
import fr.univpau.kayu.R;

public class ScanFragment extends Fragment implements View.OnClickListener {

    private ScanViewModel scanViewModel;
    private Button searchBtn;
    private EditText gtinInput;

    private static final int JSON_REQUEST_CODE = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        scanViewModel =
                ViewModelProviders.of(this).get(ScanViewModel.class);
        View root = inflater.inflate(R.layout.fragment_scan, container, false);
        final TextView textView = root.findViewById(R.id.text_scan);
        scanViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        searchBtn = root.findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(this);

        gtinInput = root.findViewById(R.id.gtinInput);

        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchBtn:

                String gtin = gtinInput.getText().toString();
                Log.i("DEVUPPAGTIN", gtin);
                PendingIntent pendingResult = getActivity().createPendingResult(JSON_REQUEST_CODE, new Intent(), 0);
                Intent intent = new Intent(getContext(), OFFIntentService.class);
                intent.putExtra(OFFIntentService.URL_EXTRA, "https://world.openfoodfacts.org/api/v0/product/" + gtin + ".json");
                intent.putExtra(OFFIntentService.PENDING_RESULT_EXTRA, pendingResult);
                getActivity().startService(intent);

                break;
        }
    }
}