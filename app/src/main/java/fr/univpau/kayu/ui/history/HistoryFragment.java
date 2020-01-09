package fr.univpau.kayu.ui.history;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import java.util.List;
import fr.univpau.kayu.Product;
import fr.univpau.kayu.ProductActivity;
import fr.univpau.kayu.ProductAdapter;
import fr.univpau.kayu.R;
import fr.univpau.kayu.db.AppDatabase;

public class HistoryFragment extends Fragment {
    private HistoryViewModel historyViewModel;
    private ListView list;
    private String[] productsName;
    private String[] productsDescription;
    private String[] productsImage;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        historyViewModel =
                ViewModelProviders.of(this).get(HistoryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_history, container, false);
        list = root.findViewById(R.id.productsListView);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("DEVUPPAITEMCLICK", new Integer(position).toString());
                AppDatabase.getAppDatabase(getContext()).productDao().getByGtin(productsDescription[position]).observe(getViewLifecycleOwner(), new Observer<Product>() {
                    @Override
                    public void onChanged(Product product) {

                        Intent productActivity = new Intent(getContext(), ProductActivity.class);

                        if(product != null) {
                            productActivity.putExtra(ProductActivity.PRODUCT_EXTRA_PARAM, product);
                            productActivity.putExtra(ProductActivity.PRODUCT_FOUND, true);
                        } else {
                            productActivity.putExtra(ProductActivity.PRODUCT_FOUND, false);
                            productActivity.putExtra(ProductActivity.PRODUCT_GTIN, "");
                        }

                        startActivity(productActivity);
                    }
                });
            }
        });

        AppDatabase.getAppDatabase(getContext()).productDao().getAll().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                productsName = new String[products.size()];
                productsDescription = new String[products.size()];
                productsImage = new String[products.size()];

                for(int i = 0; i < products.size(); i++) {
                    productsName[i] = products.get(i).getName();
                    productsDescription[i] = products.get(i).getGtin();
                    String[] images = products.get(i).getImages().split(", ");
                    productsImage[i] = images[0];
                }

                ProductAdapter adapter = new ProductAdapter(getActivity(), productsName, productsDescription, productsImage);
                list.setAdapter(adapter);
                Log.i("DEVUPPA", ((Integer)products.size()).toString() + " produits danss la db");
            }
        });

        return root;
    }
}