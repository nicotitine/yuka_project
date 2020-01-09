package fr.univpau.kayu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class NutriscoreLayout extends LinearLayout {

    private TextView energy100g;
    private TextView fat100g;
    private TextView sugars100g;
    private TextView fiber100g;
    private TextView proteins100g;
    private TextView sodium100g;
    private TextView score100g;

    private TextView energyPortion;
    private TextView fatPortion;
    private TextView sugarsPortion;
    private TextView fiberPortion;
    private TextView proteinsPortion;
    private TextView sodiumPortion;
    private TextView scorePortion;

    public NutriscoreLayout(Context context, String nutriscore) {
        super(context);
        this.initComponent(context);

    }

    public NutriscoreLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initComponent(context);
    }


    private void initComponent(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.nutriscore_layout, null, false);


        energy100g = v.findViewById(R.id.energy_100g);
        fat100g = v.findViewById(R.id.fat_100g);
        sugars100g = v.findViewById(R.id.sugars_100g);
        fiber100g = v.findViewById(R.id.fiber_100g);
        proteins100g = v.findViewById(R.id.proteins_100g);
        sodium100g = v.findViewById(R.id.sodium_100g);
        score100g = v.findViewById(R.id.score_100g);

        energyPortion= v.findViewById(R.id.energy_portion);
        fatPortion = v.findViewById(R.id.fat_portion);
        sugarsPortion = v.findViewById(R.id.sugars_portion);
        fiberPortion = v.findViewById(R.id.fiber_portion);
        proteinsPortion = v.findViewById(R.id.proteins_portion);
        sodiumPortion = v.findViewById(R.id.sodium_portion);
        scorePortion = v.findViewById(R.id.score_portion);



        this.addView(v);
    }
}