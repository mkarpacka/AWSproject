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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
