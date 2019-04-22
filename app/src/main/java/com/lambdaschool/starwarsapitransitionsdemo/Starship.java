package com.lambdaschool.starwarsapitransitionsdemo;

import android.support.annotation.NonNull;

public class Starship extends SwApiObject{

    private String model, manufacturer, costInCredits, length;

    public Starship(int id, String name, String model, String manufacturer, String costInCredits, String length) {
        super(id, name);
        this.model = model;
        this.manufacturer = manufacturer;
        this.costInCredits = costInCredits;
        this.length = length;
    }

    public String getModel() {
        return model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getCostInCredits() {
        return costInCredits;
    }

    public String getLength() {
        return length;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("The %s model %s is manufactured by %s,  is %s meters in length, at a cost of %s credits", name, model, manufacturer, length, costInCredits);
    }
}
