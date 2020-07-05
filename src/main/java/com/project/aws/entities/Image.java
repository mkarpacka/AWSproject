package com.project.aws.entities;

public class Image {


    public Image() {}

    public Image(String name) {
        this.name = name;
    }

    public Image(int id, String name ) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
    private int id;

}
