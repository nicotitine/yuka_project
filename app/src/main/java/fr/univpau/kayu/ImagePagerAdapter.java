package fr.univpau.kayu;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
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

public class ImagePagerAdapter extends PagerAdapter {

    private Context context;
    private String[] imagesUrls;
    private Bitmap[] resources;
    private View view;

    ImagePagerAdapter(Context context, String[] imagesUrls, View v) {
        this.context = context;
        this.imagesUrls = imagesUrls;
        this.resources = new Bitmap[imagesUrls.length];
        this.view = v;
    }

    @Override
    public int getCount() {
        return imagesUrls.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        final ImageView imageView = new ImageView(context);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("DEVUPPA", "image clicked : " + imagesUrls[position]);
                new ImageViewer(context, view, resources[position]);
            }
        });

        Glide.with(context)
                .asBitmap()
                .load(imagesUrls[position])

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

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
        resources[position] = null;
        Glide.with(context).clear(container);
    }
}