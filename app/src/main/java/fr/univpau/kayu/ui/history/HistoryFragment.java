package fr.univpau.kayu.ui.history;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import java.util.ArrayList;
import java.util.List;
import fr.univpau.kayu.models.Product;
import fr.univpau.kayu.ui.product.ProductActivity;
import fr.univpau.kayu.adapters.ProductAdapter;
import fr.univpau.kayu.R;
import fr.univpau.kayu.db.AppDatabase;

public class HistoryFragment extends Fragment {

    private ListView list;
    private List<Product> products;

    /**
     * Creates the history view by retrieving all products from the database and using the <code>ProductAdapter</code>.
     * @param inflater the LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container if non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState if non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return return the View for the fragment's UI
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_history, container, false);

        list = root.findViewById(R.id.productsListView);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * When a product is clicked, we want to launch the <code>ProductActivity</code> to get more details about this specific product.
             * @param parent the AdapterView where the click happened.
             * @param view the view within the AdapterView that was clicked
             * @param position the position of the view in the adapter.
             * @param id the row id of the product that was clicked.
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent productActivity = new Intent(getContext(), ProductActivity.class);
                productActivity.putExtra(ProductActivity.PRODUCT_EXTRA, products.get(position));
                startActivity(productActivity);
            }
        });

        AppDatabase.getAppDatabase(getContext()).productDao().getAll().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> _products) {
                products = _products;

                ProductAdapter adapter = new ProductAdapter(getActivity(), new ArrayList<>(products));
                list.setAdapter(adapter);

                if(products.size() == 0) {
                    root.findViewById(R.id.noProductYetTextView).setVisibility(View.VISIBLE);
                }
            }
        });
        return root;
    }
}