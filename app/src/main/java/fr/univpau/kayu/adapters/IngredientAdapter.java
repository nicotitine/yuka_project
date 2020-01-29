package fr.univpau.kayu.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import fr.univpau.kayu.R;

/**
 * Ingredient list adapter. Used in ProductActivity.
 */
public class IngredientAdapter extends  RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    /**
     * The ingredient array.
     */
    private final String[] ingredients;

    /**
     * Ingredient's view holder.
     */
    static class IngredientViewHolder extends RecyclerView.ViewHolder {

        private  TextView textView;

        IngredientViewHolder(TextView v) {
            super(v);
            this.textView = v;
        }
    }

    /**
     * Constructor of this adapter.
     * @param ingredients all the ingredients to display.
     */
    public IngredientAdapter(String[] ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an ingredient.
     * @param parent the main view.
     * @param viewType the view type.
     * @return the ingredient view.
     */
    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.ingredient_item, parent, false);

        return new IngredientViewHolder(v);
    }

    /**
     * Called by RecyclerView to display the ingredient at the specified position.
     * This method should update the contents of the IngredientViewHolder to reflect the item at the given position.
     * @param holder the main view holder.
     * @param position the position in the ingredient array.
     */
    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        if(this.ingredients[position].length() > 0) {
            StringBuilder sb = new StringBuilder(ingredients[position]);
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            holder.textView.setText(sb.toString());
        } else {
            holder.textView.setText("");
        }
    }

    /**
     * Returns the total number of ingredient in the ingredients array.
     * @return the ingredients count.
     */
    @Override
    public int getItemCount() {
        return this.ingredients.length;
    }
}