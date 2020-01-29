package fr.univpau.kayu.models;

import org.json.JSONException;
import org.json.JSONObject;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Nutriscore {

    private String nutriscoreGrade;

    private String energy100g;
    private String fat100g;
    private String sugars100g;
    private String fiber100g;
    private String proteins100g;
    private String sodium100g;
    private String score100g;

    private String energyPortion;
    private String fatPortion;
    private String sugarsPortion;
    private String fiberPortion;
    private String proteinsPortion;
    private String sodiumPortion;
    private String scorePortion;

    /**
     * The nutriscore constructor.
     * We have to be careful here due to all exceptions that can be raised by searching in a JSONObject in Java...
     * @param nutriscore the nutriscore descriptor. Actually, this is a <code>JSONObject</code> stored as a <code>String</code> format.
     * @param nutriscoreGrade the nutriscore grade.
     */
    public Nutriscore(String nutriscore, String nutriscoreGrade) {
        try {
            if(nutriscoreGrade == null) {
                this.nutriscoreGrade = "";
            }

            if(nutriscore == null) {
                energy100g = "";
                fat100g = "";
                sugars100g = "";
                fiber100g = "";
                proteins100g = "";
                sodium100g = "";
                score100g = "";
                energyPortion = "";
                fatPortion = "";
                sugarsPortion = "";
                fiberPortion = "";
                proteinsPortion = "";
                sodiumPortion = "";
                scorePortion = "";

                return ;
            }

            JSONObject json = new JSONObject(nutriscore);

            this.nutriscoreGrade = nutriscoreGrade;

            // Energy (kcal and kj) per 100g
            try {
                energy100g = json.getString("energy_100g") + "kj"
                        + "\n"
                        + this.kjToKcal(Integer.valueOf(json.getString("energy_100g"))) + "kcal";
            } catch (JSONException e) {
                energy100g = "";
            }

            // Energy (kcal and kj) per portion
            try {
                energyPortion = json.get("energy_serving") + "kj"
                        + "\n"
                        + this.kjToKcal(Integer.valueOf(json.getString("energy_serving"))) + "kcal";
            } catch (JSONException e) {
                energyPortion = "";
            }

            // Fat and saturated-fat per 100g
            try {
                fat100g = json.getString("fat_100g")
                        + json.getString("fat_unit")
                        + "\n"
                        + json.getString("saturated-fat_100g")
                        + json.getString("saturated-fat_unit");
            } catch (JSONException e) {
                fat100g = "";
            }

            // Fat and saturated-fat per 100g
            try {
                fatPortion = json.getString("fat_serving")
                        + json.getString("fat_unit")
                        + "\n"
                        + json.getString("saturated-fat_serving")
                        + json.getString("saturated-fat_unit");
            } catch (JSONException e) {
                fatPortion = "";
            }

            // Carbohydrates and sugars per 100g
            try {
                sugars100g = json.getString("carbohydrates_100g")
                        + json.getString("carbohydrates_unit")
                        + "\n"
                        + json.getString("sugars_100g")
                        + json.getString("sugars_unit");
            } catch (JSONException e) {
                sugars100g = "";
            }

            // Carbohydrates and sugars per portion
            try {
                sugarsPortion = json.getString("carbohydrates_serving")
                        + json.getString("carbohydrates_unit")
                        + "\n"
                        + json.getString("sugars_serving")
                        + json.getString("sugars_unit");
            } catch (JSONException e) {
                sugarsPortion = "";
            }

            // Fibers per 100g
            try {
                fiber100g = json.getString("fiber_100g") + json.getString("fiber_unit");
            } catch (JSONException e) {
                fiber100g = "";
            }

            // Fibers per portion
            try {
                fiberPortion = json.getString("fiber_serving") + json.getString("fiber_unit");
            } catch (JSONException e) {
                fiberPortion = "";
            }

            // Proteins per 100g
            try {
                proteins100g = json.getString("proteins_100g") + json.getString("proteins_unit");
            } catch (JSONException e) {
                proteins100g = "";
            }

            // Proteins per portion
            try {
                proteinsPortion = json.getString("proteins_serving") + json.getString("proteins_unit");
            } catch (JSONException e) {
                proteinsPortion = "";
            }

            // Salt and sodium per 100g
            try {
                sodium100g = json.getString("salt_100g")
                        + json.getString("salt_unit")
                        + "\n"
                        + json.getString("sodium_100g")
                        + json.getString("sodium_unit");
            } catch (JSONException e) {
                sodium100g = "";
            }

            // Salt and sodium per portion
            try {
                sodiumPortion = json.getString("salt_serving")
                        + json.getString("salt_unit")
                        + "\n"
                        + json.getString("sodium_serving")
                        + json.getString("sodium_unit");
            } catch (JSONException e) {
                sodiumPortion = "";
            }

            // Proteins per 100g
            try {
                proteins100g = json.getString("proteins_100g") + json.getString("proteins_unit");
            } catch (JSONException e) {
                proteins100g = "";
            }

            // Proteins per portion
            try {
                proteinsPortion = json.getString("proteins_serving") + json.getString("proteins_unit");
            } catch (JSONException e) {
                proteinsPortion = "";
            }

            // Score per 100g
            try {
                score100g = json.getString("nutrition-score-fr_100g")
                        + "\n"
                        + nutriscoreGrade;
            } catch (JSONException e) {
                score100g = "";
            }

            // Score per portion is the same...
            try {
                scorePortion = json.getString("nutrition-score-fr_100g")
                        + "\n"
                        + nutriscoreGrade;
            } catch (JSONException e) {
                scorePortion = "";
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Computes the value in kcal for a given kj value.
     * @param kj the value we want to convert in kcal.
     * @return the kcal value.
     */
    private int kjToKcal(int kj) {
        return BigDecimal.valueOf(kj / 4.184)
                .setScale(1, RoundingMode.HALF_UP)
                .intValue();
    }


    /*******************************************************
     *
     * All getters (setters are unused in this project)
     *
     ******************************************************/

    public String getNutriscoreGrade() {
        return nutriscoreGrade;
    }

    public String getEnergy100g() {
        return energy100g;
    }

    public String getFat100g() {
        return fat100g;
    }

    public String getSugars100g() {
        return sugars100g;
    }

    public String getFiber100g() {
        return fiber100g;
    }

    public String getProteins100g() {
        return proteins100g;
    }

    public String getSodium100g() {
        return sodium100g;
    }

    public String getScore100g() {
        return score100g;
    }

    public String getEnergyPortion() {
        return energyPortion;
    }

    public String getFatPortion() {
        return fatPortion;
    }

    public String getSugarsPortion() {
        return sugarsPortion;
    }

    public String getFiberPortion() {
        return fiberPortion;
    }

    public String getProteinsPortion() {
        return proteinsPortion;
    }

    public String getSodiumPortion() {
        return sodiumPortion;
    }

    public String getScorePortion() {
        return scorePortion;
    }
}