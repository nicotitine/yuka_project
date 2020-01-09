package fr.univpau.kayu;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ProductAdapter extends ArrayAdapter {

    private final Activity context;
    private final String[] images;
    private final String[] names;
    private final String[] descriptions;

    public ProductAdapter(Activity context, String[] names, String[] descriptions, String[] images) {
        super(context, R.layout.row_listview_product, names);

        this.context = context;
        this.names = names;
        this.descriptions = descriptions;
        this.images = images;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.row_listview_product, null, true);

        TextView name = rowView.findViewById(R.id.productNameRow);
        TextView description = rowView.findViewById(R.id.productDescriptionRow);
        ImageView image = rowView.findViewById(R.id.productImageRow);

        name.setText(names[position]);
        description.setText(descriptions[position]);


        Glide.with(getContext()).load(images[position]).into(image);
        return rowView;
    }

}