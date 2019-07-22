package com.example.ecommerce.address;

public final class AddressItem {
    private String address;
    private String id;
    public AddressItem(String id,String address)
    {
        this.address=address;
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }
}
