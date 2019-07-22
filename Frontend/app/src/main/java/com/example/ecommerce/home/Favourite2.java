package com.example.ecommerce.home;

public class Favourite2{

    String title;
    boolean isSelected = true;


    public Favourite2(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
