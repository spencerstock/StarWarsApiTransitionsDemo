package com.lambdaschool.starwarsapitransitionsdemo;

import java.io.Serializable;

// S04M03-3 Add Model Object
public class SwApiObject implements Serializable {
    private int id;
    protected String category, name;

    public SwApiObject(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }
}
