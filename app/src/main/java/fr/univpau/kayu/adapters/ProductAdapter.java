package fr.univpau.kayu.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import fr.univpau.kayu.R;
import fr.univpau.kayu.models.Product;

/**
 * Product list adapter. Used in <code>HistoryFragment</code>.
 */
public class ProductAdapter extends ArrayAdapter<Product> {

    /**
     * Constructor of this adapter.
     * @param context the application context.
     * @param products the product list.
     */
    public ProductAdapter(Activity context, ArrayList<Product> products) {
        super(context, R.layout.row_listview_product, products);
    }

    /**
     * Create the view for each product and returns it.
     * The view created contains two TextViews (product name and product ean) ans one ImageView (first image from product as a thumbnail).
     * @param position the current position in the product list.
     * @param convertView the previous created view. Inflating is resources expensive so we re-use the previous one.
     * @param parent the parent view that the returned view will be the children.
     * @return the created view for one product in the list.
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        Product product = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_listview_product, parent, false);
        }

        TextView name = convertView.findViewById(R.id.productNameRow);
        TextView description = convertView.findViewById(R.id.productDescriptionRow);
        ImageView image = convertView.findViewById(R.id.productImageRow);

        if(product != null) {
            name.setText(product.getName());
            description.setText(product.getEan());

            String[] images = product.getImages().split(", ");
            Glide.with(getContext()).load(images[0]).into(image);
        }
        return convertView;
    }
}