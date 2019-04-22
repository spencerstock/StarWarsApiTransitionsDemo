package com.lambdaschool.starwarsapitransitionsdemo;

public class Person extends SwApiObject {

    private String height, mass, hairColor, skinColor, eyeColor;


    public Person(int id, String name) {
        super(id, name);
    }

    public Person(int id, String name, String height, String mass, String hairColor, String skinColor, String eyeColor) {
        super(id, name);
        this.height = height;
        this.mass = mass;
        this.hairColor = hairColor;
        this.skinColor = skinColor;
        this.eyeColor = eyeColor;
    }
}
