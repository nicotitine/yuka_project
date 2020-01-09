package fr.univpau.kayu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import com.github.chrisbanes.photoview.PhotoView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public final class ImageViewer extends PopupWindow {

    View view;
    Context context;
    PhotoView photoView;
    ProgressBar loading;
    ViewGroup parent;

    private static ImageViewer instance = null;

    public ImageViewer(Context context, View v, Bitmap bitmap) {
        super(((LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.popup_photo_full, null), ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        this.context = context;
        this.view = getContentView();

        ImageButton closeButton = this.view.findViewById(R.id.ib_close);

        setFocusable(true);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        photoView = view.findViewById(R.id.photoView);
        loading = view.findViewById(R.id.loading);
        photoView.setMaximumScale(6);
        parent = (ViewGroup) photoView.getParent();

        loading.setIndeterminate(true);
        loading.setVisibility(View.VISIBLE);

        parent.setBackground(new BitmapDrawable(context.getResources(), Constants.fastblur(Bitmap.createScaledBitmap(bitmap, 50, 50, true))));

        photoView.setImageBitmap(bitmap);
        loading.setVisibility(View.GONE);

        showAtLocation(v, Gravity.CENTER, 0, 0);
    }
}