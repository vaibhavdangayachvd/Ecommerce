package com.example.ecommerce.category;

public final class CategoryItem {
    private String name;
    private String image;
    public CategoryItem(String name,String image)
    {
        this.name=name;
        this.image=image;
    }
    public String getName()
    {
        return this.name;
    }
    public String getImage()
    {
        return this.image;
    }
}
