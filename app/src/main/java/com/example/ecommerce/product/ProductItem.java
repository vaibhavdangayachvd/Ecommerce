package com.example.ecommerce.product;

public final class ProductItem{
    private String id;
    private String name;
    private String description;
    private String price;
    private String image;
    public ProductItem(String id,String name,String description,String price,String image)
    {
        this.name=name;
        this.description=description;
        this.price=price;
        this.image=image;
        this.id=id;
    }
    public String getName()
    {
        return this.name;
    }
    public String getDescription()
    {
        return this.description;
    }
    public String getImage(){
        return this.image;
    }
    public String getPrice()
    {
        return this.price;
    }
    public String getId(){
        return this.id;
    }
}
