package fr.univpau.kayu.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import fr.univpau.kayu.ui.product.ImageViewer;

/**
 * Image carousel adapter. Used in <code>ProductActivity</code>.
 */
public class ImageCarouselAdapter extends PagerAdapter {

    private Context context;
    private String[] imagesUrls;
    private Bitmap[] resources;
    private View view;

    /**
     * Constructor of this adapter.
     * @param context the current application context.
     * @param imagesUrls the image url list.
     * @param v the parent view.
     */
    public ImageCarouselAdapter(Context context, String[] imagesUrls, View v) {
        this.context = context;
        this.imagesUrls = imagesUrls;
        this.resources = new Bitmap[imagesUrls.length];
        this.view = v;
    }

    /**
     * Returns the total number of images in the image url array.
     * @return the images count.
     */
    @Override
    public int getCount() {
        return imagesUrls.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    /**
     * Creates the view for each image :
     *  - creates an <code>ImageView</code>.
     *  - load the image at <code>position</code> into the created <code>ImageView</code> using Glide.
     * @param container the view container.
     * @param position the current position in the image array.
     * @return the created view for each image.
     */
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        final ImageView imageView = new ImageView(context);


        imageView.setOnClickListener(new View.OnClickListener() {
            /**
             * We want to add a listener here in order to start the ImageViewer when the user
             * clicks on the displayed image.
             * @param v the view clicked on.
             */
            @Override
            public void onClick(View v) {
                new ImageViewer(context, view, resources[position]);
            }
        });

        Glide.with(context)
                .asBitmap()
                .load(imagesUrls[position])
                .override(1500, 1500)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        imageView.setImageBitmap(resource);
                        resources[position] = resource;
                        return false;
                    }
                })
                .skipMemoryCache(true)
                .into(imageView);

        container.addView(imageView);

        return imageView;
    }

    /**
     *
     * @param container the view container.
     * @param position the current position in the image array.
     * @param object the object we want to remove (casted as a <code>View</code>)
     */
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
        resources[position] = null;
        Glide.with(context).clear(container);
    }
}
