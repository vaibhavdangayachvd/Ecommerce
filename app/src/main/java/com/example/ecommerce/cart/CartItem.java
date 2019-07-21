package com.example.ecommerce.cart;

public final class CartItem {
    private String id;
    private String name;
    private String price;
    private String quantity;
    private String image;
    private String category;

    public CartItem(String id,String category, String name, String price, String quantity, String image) {
        this.id=id;
        this.category = category;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public String getQuantity() {
        return quantity;
    }
    public void updateQuantity(int quantity)
    {
        this.quantity=String.valueOf(Integer.parseInt(this.quantity)+quantity);
    }
}
