package fr.univpau.kayu;

import org.json.JSONException;
import org.json.JSONObject;

public class Nutriscore {

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

    public Nutriscore(String nutriscore) {
        try {
            JSONObject json = new JSONObject(nutriscore);

            try {
                fat100g = json.getString("fat_100g")
                        + json.getString("fat_unit")
                        + "\n"
                        + json.getString("saturated-fat_100g")
                        + json.getString("saturated-fat_unit");

                fatPortion = json.getString("fat_serving")
                        + json.getString("fat_unit")
                        + "\n"
                        + json.getString("saturated-fat_serving")
                        + json.getString("saturated-fat_unit");
            } catch (JSONException e) {
                fat100g = "";
                fatPortion = "";
            }

            try {
                sugars100g = json.getString("carbohydrates_100g")
                        + json.getString("carbohydrates_unit")
                        + "\n"
                        + json.getString("sugars_100g")
                        + json.getString("sugars_unit");

                sugarsPortion = json.getString("carbohydrates_serving")
                        + json.getString("carbohydrates_unit")
                        + "\n"
                        + json.getString("sugars_serving")
                        + json.getString("sugars_unit");
            } catch (JSONException e) {
                sugars100g = "";
                sugarsPortion = "";
            }

            try {
                fiber100g = json.getString("fiber_100g") + json.getString("fiber_unit");
                fiberPortion = json.getString("fiber_serving") + json.getString("fiber_unit");
            } catch (JSONException e) {
                fiber100g = "";
                fiberPortion = "";
            }

            try {
                proteins100g = json.getString("proteins_100g") + json.getString("proteins_unit");
                proteinsPortion = json.getString("proteins_serving") + json.getString("proteins_unit");
            } catch (JSONException e) {
                proteins100g = "";
                proteinsPortion = "";
            }

            try {
                sodium100g = json.getString("salt_100g") + json.getString("salt_unit");
                sodiumPortion = json.getString("salt_serving") + json.getString("salt_unit");
            } catch (JSONException e) {
                sodium100g = "";
                sodiumPortion = "";
            }

            try {
                proteins100g = json.getString("proteins_100g") + json.getString("proteins_unit");
                proteinsPortion = json.getString("proteins_serving") + json.getString("proteins_unit");
            } catch (JSONException e) {
                proteins100g = "";
                proteinsPortion = "";
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getEnergy100g() {
        return energy100g;
    }

    public void setEnergy100g(String energy100g) {
        this.energy100g = energy100g;
    }

    public String getFat100g() {
        return fat100g;
    }

    public void setFat100g(String fat100g) {
        this.fat100g = fat100g;
    }

    public String getSugars100g() {
        return sugars100g;
    }

    public void setSugars100g(String sugars100g) {
        this.sugars100g = sugars100g;
    }

    public String getFiber100g() {
        return fiber100g;
    }

    public void setFiber100g(String fiber100g) {
        this.fiber100g = fiber100g;
    }

    public String getProteins100g() {
        return proteins100g;
    }

    public void setProteins100g(String proteins100g) {
        this.proteins100g = proteins100g;
    }

    public String getSodium100g() {
        return sodium100g;
    }

    public void setSodium100g(String sodium100g) {
        this.sodium100g = sodium100g;
    }

    public String getScore100g() {
        return score100g;
    }

    public void setScore100g(String score100g) {
        this.score100g = score100g;
    }

    public String getEnergyPortion() {
        return energyPortion;
    }

    public void setEnergyPortion(String energyPortion) {
        this.energyPortion = energyPortion;
    }

    public String getFatPortion() {
        return fatPortion;
    }

    public void setFatPortion(String fatPortion) {
        this.fatPortion = fatPortion;
    }

    public String getSugarsPortion() {
        return sugarsPortion;
    }

    public void setSugarsPortion(String sugarsPortion) {
        this.sugarsPortion = sugarsPortion;
    }

    public String getFiberPortion() {
        return fiberPortion;
    }

    public void setFiberPortion(String fiberPortion) {
        this.fiberPortion = fiberPortion;
    }

    public String getProteinsPortion() {
        return proteinsPortion;
    }

    public void setProteinsPortion(String proteinsPortion) {
        this.proteinsPortion = proteinsPortion;
    }

    public String getSodiumPortion() {
        return sodiumPortion;
    }

    public void setSodiumPortion(String sodiumPortion) {
        this.sodiumPortion = sodiumPortion;
    }

    public String getScorePortion() {
        return scorePortion;
    }

    public void setScorePortion(String scorePortion) {
        this.scorePortion = scorePortion;
    }
}
