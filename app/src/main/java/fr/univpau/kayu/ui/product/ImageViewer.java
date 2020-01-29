package fr.univpau.kayu.ui.product;

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

import fr.univpau.kayu.Constants;
import fr.univpau.kayu.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public final class ImageViewer extends PopupWindow {

    /**
     * Creates a fullscreen popup to display an image.
     * The image can be pinched to zoom.
     * This is using <code>PhotoView</code> library and the <code>Constants</code> class for the background blur.
     * Unfortunately, I don't know how to change <code>Constants</code> in order to adapt the background main color for dark mode.
     * @param context the current application context.
     * @param v the parent view.
     * @param bitmap the image we want to see.
     */
    public ImageViewer(Context context, View v, Bitmap bitmap) {
        super(
                ((LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.popup_photo_full, (ViewGroup)v.getParent(), false),
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);



        View view = this.getContentView();

        ImageButton closeButton = view.findViewById(R.id.ib_close);

        this.setFocusable(true);

        closeButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Dismiss the popup when the close button is clicked.
             * @param v the view the user clicked.
             */
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        PhotoView photoView = view.findViewById(R.id.photoView);
        ProgressBar loading = view.findViewById(R.id.loading);
        photoView.setMaximumScale(6);
        ViewGroup parent = (ViewGroup) photoView.getParent();

        loading.setIndeterminate(true);
        loading.setVisibility(View.VISIBLE);

        parent.setBackground(new BitmapDrawable(context.getResources(), Constants.fastblur(Bitmap.createScaledBitmap(bitmap, 50, 50, true))));

        photoView.setImageBitmap(bitmap);
        loading.setVisibility(View.GONE);

        showAtLocation(v, Gravity.CENTER, 0, 0);
    }
}