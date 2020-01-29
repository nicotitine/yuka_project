package fr.univpau.kayu.ui.product;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.res.ResourcesCompat;
import fr.univpau.kayu.R;
import fr.univpau.kayu.models.Nutriscore;

public class NutriscoreLayout extends LinearLayout {

    private Nutriscore nutriscore;

    /**
     * Creates the <code>Nutriscore</code> object using <code>nutriscore</code> and <code>nutriscoreGrade</code> params.
     * Then, initializes the nutriscore layout.
     * @param context the current application context.
     * @param nutriscore the nutriscore <code>String</code> value.
     * @param nutriscoreGrade the nutriscore grade <code>String</code> value.
     */
    public NutriscoreLayout(Context context, String nutriscore, String nutriscoreGrade) {
        super(context);

        this.nutriscore = new Nutriscore(nutriscore, nutriscoreGrade);
        this.init(context);
    }

    /**
     * Initializes the nutriscore layout using the <code>Nutriscore</code> created before.
     * @param context the current application context
     * @param attrs this value may be null.
     */
    public NutriscoreLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
    }

    /**
     * Initializes the nutriscore layout using <code>Nutriscore</code> attribute created before.
     * @param context the current application context.
     */
    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.nutriscore_layout, (ViewGroup)getParent(), false);

        TextView nutriscoreTitleTextView = v.findViewById(R.id.newNutriscore);
        TextView energy100g = v.findViewById(R.id.energy_100g);
        TextView fat100g = v.findViewById(R.id.fat_100g);
        TextView sugars100g = v.findViewById(R.id.sugars_100g);
        TextView fiber100g = v.findViewById(R.id.fiber_100g);
        TextView proteins100g = v.findViewById(R.id.proteins_100g);
        TextView sodium100g = v.findViewById(R.id.sodium_100g);
        TextView score100g = v.findViewById(R.id.score_100g);

        TextView energyPortion= v.findViewById(R.id.energy_portion);
        TextView fatPortion = v.findViewById(R.id.fat_portion);
        TextView sugarsPortion = v.findViewById(R.id.sugars_portion);
        TextView fiberPortion = v.findViewById(R.id.fiber_portion);
        TextView proteinsPortion = v.findViewById(R.id.proteins_portion);
        TextView sodiumPortion = v.findViewById(R.id.sodium_portion);
        TextView scorePortion = v.findViewById(R.id.score_portion);

        nutriscoreTitleTextView.setCompoundDrawablePadding(20);
        if(nutriscore.getNutriscoreGrade() != null) {
            switch (nutriscore.getNutriscoreGrade()) {
                case "A":
                    nutriscoreTitleTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, ResourcesCompat.getDrawable(getResources(), R.drawable.nutriscore_a, null), null);
                    break;
                case "B":
                    nutriscoreTitleTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, ResourcesCompat.getDrawable(getResources(), R.drawable.nutriscore_b, null), null);
                    break;
                case "C":
                    nutriscoreTitleTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, ResourcesCompat.getDrawable(getResources(), R.drawable.nutriscore_c, null), null);
                    break;
                case "D":
                    nutriscoreTitleTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, ResourcesCompat.getDrawable(getResources(), R.drawable.nutriscore_d, null), null);
                    break;
                case "E":
                    nutriscoreTitleTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, ResourcesCompat.getDrawable(getResources(), R.drawable.nutriscore_e, null), null);
                    break;
            }
        }

        energy100g.setText(this.nutriscore.getEnergy100g());
        fat100g.setText(this.nutriscore.getFat100g());
        sugars100g.setText(this.nutriscore.getSugars100g());
        fiber100g.setText(this.nutriscore.getFiber100g());
        proteins100g.setText(this.nutriscore.getProteins100g());
        sodium100g.setText(this.nutriscore.getSodium100g());
        score100g.setText(this.nutriscore.getScore100g());

        energyPortion.setText(this.nutriscore.getEnergyPortion());
        fatPortion.setText(this.nutriscore.getFatPortion());
        sugarsPortion.setText(this.nutriscore.getSugarsPortion());
        fiberPortion.setText(this.nutriscore.getFiberPortion());
        proteinsPortion.setText(this.nutriscore.getProteinsPortion());
        sodiumPortion.setText(this.nutriscore.getSodiumPortion());
        scorePortion.setText(this.nutriscore.getScorePortion());

        this.addView(v);
    }
}