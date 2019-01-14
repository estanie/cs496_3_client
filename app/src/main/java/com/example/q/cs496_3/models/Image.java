package com.example.q.cs496_3.models;

public class Image {
    private String path;
    private boolean isSelected = false;

    public Image(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void toggle() {
        isSelected =  !isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
