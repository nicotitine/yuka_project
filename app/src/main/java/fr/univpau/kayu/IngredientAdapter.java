package fr.univpau.kayu;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public class IngredientAdapter extends  RecyclerView.Adapter<IngredientAdapter.MyViewHolder> {

    private final String[] ingredients;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public MyViewHolder(TextView v) {
            super(v);
            textView = v;
        }
    }

    public IngredientAdapter(String[] ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public IngredientAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_item, parent, false);


        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textView.setText(ingredients[position].substring(0, 1).toUpperCase() + ingredients[position].substring(1));
    }

    @Override
    public int getItemCount() {
        return ingredients.length;
    }
}