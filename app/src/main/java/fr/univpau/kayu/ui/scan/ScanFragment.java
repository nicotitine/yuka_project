package fr.univpau.kayu.ui.scan;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import java.io.IOException;
import fr.univpau.kayu.OFFService;
import fr.univpau.kayu.models.Product;
import fr.univpau.kayu.ui.product.ProductActivity;
import fr.univpau.kayu.R;
import fr.univpau.kayu.db.AppDatabase;
import fr.univpau.kayu.db.DatabaseTask;
import it.sephiroth.android.library.xtooltip.ClosePolicy;
import it.sephiroth.android.library.xtooltip.Tooltip;

public class ScanFragment extends Fragment implements View.OnClickListener {

    private final static int CAMERA_PERMISSION_CODE = 100;

    private Button searchBtn;
    private EditText eanInput;
    private ProgressBar progressBar;
    private SurfaceView surfaceView;
    private CameraSource cameraSource;
    private Tooltip tooltip;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String ean = intent.getStringExtra(OFFService.PRODUCT_EAN_EXTRA);
            boolean isPreview = intent.getBooleanExtra(OFFService.PRODUCT_IS_PREVIEW_EXTRA, false);
            boolean isProductFound = intent.getBooleanExtra(OFFService.PRODUCT_FOUND_EXTRA, false);
            Product product = (Product) intent.getSerializableExtra(OFFService.PRODUCT_EXTRA);

            if(isPreview) {
                if(isProductFound && product != null && getView() != null) {
                    tooltip = new Tooltip.Builder(context)
                            .anchor(getView().findViewById(R.id.bottomLayoutScan), 0, 0, true)
                            .text(product.getName())
                            .styleId(R.style.ToolTipTextStyle)
                            .overlay(false)
                            .closePolicy(ClosePolicy.Companion.getTOUCH_NONE())
                            .arrow(true)
                            .maxWidth(500)
                            .create();

                    tooltip.show(getView().findViewById(R.id.bottomLayoutScan), Tooltip.Gravity.TOP, false);
                } else {
                    if(tooltip != null)
                        tooltip.dismiss();
                }
            } else {
                launchProductActivity(ean, product);
                if(product != null && getActivity() != null) {
                    DatabaseTask.getInstance(getActivity().getApplication()).insert(product);
                }
            }
            searchBtn.setText(R.string.search);
            progressBar.setVisibility(View.GONE);
        }
    };

    @Override
    public void onResume() {
        IntentFilter intentFilter= new IntentFilter();
        intentFilter.addAction(OFFService.GET_PRODUCT_ACTION);
        if(getActivity() != null) {
            getActivity().registerReceiver(this.receiver, intentFilter);
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if(getActivity() != null) {
            getActivity().unregisterReceiver(this.receiver);
        }

        if(this.tooltip != null) {
            this.tooltip.dismiss();
        }

        super.onPause();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_scan, container, false);

        searchBtn = root.findViewById(R.id.searchBtn);
        eanInput = root.findViewById(R.id.eanInput);
        progressBar = root.findViewById(R.id.progress);
        surfaceView = root.findViewById(R.id.cameraPreview);
        searchBtn.setOnClickListener(this);

        final BarcodeDetector detector = new BarcodeDetector.Builder(getActivity()).build();

        if(getActivity() != null)
            cameraSource = new CameraSource.Builder(getActivity(), detector).setAutoFocusEnabled(true).build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if(getContext() != null && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    try {
                        cameraSource.start(surfaceView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    requestPermission();
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

                if(codes.size() != 0 && getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Vibrator vibrator = (Vibrator)getActivity().getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            if(vibrator != null) {
                                vibrator.vibrate(200);
                            }
                            String ean = codes.valueAt(0).displayValue;
                            cameraSource.stop();

                            launchSearchService(ean, false);
                        }
                    });
                }
            }
        });

        if(eanInput.getText().length() == 8 || eanInput.getText().length() == 13) {
            if(getContext() != null)
                searchBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            searchBtn.setEnabled(true);
        } else {
            if(getContext() != null)
                searchBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.colorSecondary));
            searchBtn.setEnabled(false);
        }

        eanInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(tooltip != null) {
                    tooltip.dismiss();
                }
                if((s.length() == 8 || s.length() == 13) && s.toString().matches("\\d+")) {
                    if(getContext() != null)
                        searchBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                    searchBtn.setEnabled(true);
                    launchSearchService(s.toString(), true);
                } else {
                    if(getContext() != null)
                        searchBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.colorSecondary));
                    searchBtn.setEnabled(false);
                }
            }
        });
        return root;
    }

    private void launchSearchService(final String ean, final boolean isPreview) {
        final LiveData<Product> mProduct = AppDatabase.getAppDatabase(getContext()).productDao().getByEan(ean);
        final Intent service = new Intent(getContext(), OFFService.class);

        mProduct.observe(this, new Observer<Product>() {
            @Override
            public void onChanged(Product _product) {

                if((_product == null || isPreview) && getActivity() != null) {
                    searchBtn.setText("");
                    progressBar.setVisibility(View.VISIBLE);

                    service.putExtra(OFFService.PRODUCT_EAN_EXTRA, ean);
                    service.putExtra(OFFService.PRODUCT_IS_PREVIEW_EXTRA, isPreview);
                    getActivity().startService(service);
                } else {
                    launchProductActivity(ean, _product);
                }

                /*
                 * Need to remove this observer, in case the user modifies the product.
                 * Without removing it, other ProductActivity-es would be launched in top of the current one
                 */
                mProduct.removeObserver(this);
            }
        });
    }

    private void launchProductActivity(String ean, Product product) {
        this.eanInput.setText("");
        Intent productActivity = new Intent(getContext(), ProductActivity.class);
        productActivity.putExtra(ProductActivity.PRODUCT_EAN_EXTRA, ean);
        productActivity.putExtra(ProductActivity.PRODUCT_EXTRA, product);
        startActivity(productActivity);
    }

    private void requestPermission() {
        if(getActivity() != null && ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.need_permission)
                    .setMessage(R.string.need_permission_message)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            requestPermissions(new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == CAMERA_PERMISSION_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               try {
                   cameraSource.start(surfaceView.getHolder());
               } catch (IOException e) {
                   e.printStackTrace();
               }
            } else {
                Toast.makeText(getContext(), R.string.permission_refused, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.searchBtn) {
            String ean = eanInput.getText().toString();
            if ((ean.length() == 8 || ean.length() == 13) && ean.matches("\\d+")) {
                this.launchSearchService(ean, false);
            }
        }
    }
}