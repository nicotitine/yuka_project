package fr.univpau.kayu.ui.scan;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONObject;

import java.io.IOException;

import fr.univpau.kayu.OFFIntentService;
import fr.univpau.kayu.ProductActivity;
import fr.univpau.kayu.R;

public class ScanFragment extends Fragment implements View.OnClickListener {

    private ScanViewModel scanViewModel;
    private Button searchBtn;
    private EditText gtinInput;
    private ProgressBar progressBar;

    private SurfaceView surfaceView;
    private CameraSource cameraSource;
    private BarcodeDetector detector;




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

        progressBar = root.findViewById(R.id.progress);

        surfaceView = root.findViewById(R.id.cameraPreview);

        detector = new BarcodeDetector.Builder(getActivity()).build();

        cameraSource = new CameraSource.Builder(getActivity(), detector).setAutoFocusEnabled(true).setRequestedPreviewSize(640, 480).build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    cameraSource.start(surfaceView.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        detector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> codes = detections.getDetectedItems();

                if(codes.size() != 0) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("DEVUPPA", codes.valueAt(0).displayValue);
                            Vibrator vibrator = (Vibrator)getActivity().getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(200);
                            String gtin = codes.valueAt(0).displayValue;
                            cameraSource.stop();
                            searchBtn.setText(R.string.empty);
                            progressBar.setVisibility(View.VISIBLE);
                            launchProductActivity(gtin);
                        }
                    });
                }
            }
        });

        return root;
    }

    public void launchProductActivity(String gtin) {
        Intent productActivity = new Intent(getContext(), ProductActivity.class);
        productActivity.putExtra(ProductActivity.PRODUCT_GTIN, gtin);
        startActivity(productActivity);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchBtn:

                String gtin = gtinInput.getText().toString();
                Log.i("DEVUPPAGTIN", gtin);
                launchProductActivity(gtin);
                break;
        }
    }
}