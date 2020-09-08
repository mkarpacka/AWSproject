package com.project.aws.entities;

public class Image {

    private String name;
    private int id;
    private boolean checked;

    public Image(int id, String name ) {
        this.name = name;
        this.id = id;
        this.checked = false;
    }
}
